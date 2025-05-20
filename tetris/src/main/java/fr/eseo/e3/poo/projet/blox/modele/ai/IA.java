package fr.eseo.e3.poo.projet.blox.modele.ai;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Jeu;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.ai.actions.Action;
import fr.eseo.e3.poo.projet.blox.modele.ai.actions.MoveDown;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Tas;

public class IA implements PropertyChangeListener {
    //
    // Constante de classe
    //
    public static final String EVT_CHANGEMENT_JEU = "JEU";
    public static final String EVT_CALC_STATS = "FEEDBACK";

    private static final Logger LOGGER = Logger.getLogger(IA.class.getName());

    private static final int BATCH_SIZE = 128;

    //
    // Variables d'instance
    //

    private final Hyperparametres hp;

    // Puits
    private final int largeurPuits;
    private final int profondeurPuits;
    private final int modeUsine;

    // Flags
    private boolean defaite;
    private boolean pose;

    // Modèle
    private final MultiLayerNetwork model;

    // Pour le changement de jeu lors de l'entrainement
    private final PropertyChangeSupport pcs;

    // Pour l'aléatoires
    private final Random random = new Random();

    /// Mémoire du modèle
    // L'état actuel de l'entrainement
    private Etat etatActuel;
    private INDArray qValuesActuelles;

    private int batchIndex = 0;
    private final INDArray etats;
    private final INDArray targets;

    /// Stats
    private final Feedback feedback;

    //
    // Constructeurs
    //
    public IA(int largeurPuits, int profondeurPuits, int modeUsine) {
        this.hp = new Hyperparametres();

        this.largeurPuits = largeurPuits;
        this.profondeurPuits = profondeurPuits;
        this.modeUsine = modeUsine;

        this.defaite = false;
        this.pose = false;

        this.pcs = new PropertyChangeSupport(this);

        // Initialisation des actions
        Action.init();

        this.etats = Nd4j.create(IA.BATCH_SIZE, Etat.getNumberOfInput(largeurPuits, profondeurPuits));
        this.targets = Nd4j.create(IA.BATCH_SIZE, Action.getNbActions());

        // Initialisation du modèle
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(this.hp.getAdam())
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(Etat.getNumberOfInput(largeurPuits, profondeurPuits))
                        .nOut(64)
                        .activation(Activation.RELU)
                        .build())
                .layer(new DenseLayer.Builder()
                        .nIn(64)
                        .nOut(32)
                        .activation(Activation.RELU)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .nIn(32)
                        .nOut(Action.getNbActions())
                        .activation(Activation.IDENTITY)
                        .build())
                .build();

        this.model = new MultiLayerNetwork(conf);
        this.model.init();

        this.feedback = new Feedback(this.model, this.hp);
        this.addPropertyChangeListener(this.feedback);
    }

    //
    // Méthodes
    //

    /// Q LEARNING (Entrainement)

    private Etat getEtat(Puits puits, Tas tas) {
        Etat etat = new Etat(puits.getLargueur(), puits.getProfondeur());

        // Met des un dans le tableau de l'etat aux endroits où il y a des élements pour
        // le TAS
        tas.getElements().stream()
                .map(Element::getCoord)
                .forEach(c -> etat.setTas(c.getAbscisse(), c.getOrdonnee()));

        // Pour la PIECE SUIVANTE
        puits.getPieceSuivante().getElements().stream()
                .map(Element::getCoord)
                .forEach(c -> etat.setPieceSuivante(c.getAbscisse(), c.getOrdonnee()));

        // Pour la PIECE ACTUELLE
        puits.getPieceActuelle().getElements().stream()
                .map(Element::getCoord)
                .forEach(c -> etat.setPieceActuelle(c.getAbscisse(), c.getOrdonnee()));

        return etat;
    }

    private double getQMax(Etat etat) {
        return this.getQValues(etat).max(0).getDouble(0);
    }

    private INDArray getQValues(Etat etat) {
        if (etat == this.etatActuel) {
            return this.qValuesActuelles;
        }
        this.etatActuel = etat;
        this.qValuesActuelles = this.model.output(etat.get(), false);
        return this.qValuesActuelles;
    }

    private int getRecompense(Etat etat, Piece piece) {
        int recompense = 0;

        if (this.pose) {
            Puits puits = piece.getPuits();

            /// Récompense pour la position de la PIECE ACTUELLE
            List<Coordonnees> coordsPiece = piece.getElements().stream()
                    .map(Element::getCoord)
                    .toList();

            // Analyse de chaque élements de la pièce posé
            for (Coordonnees coordElement : coordsPiece) {
                // Analyse de ses voisins
                for (int[] delta : new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }) {
                    Coordonnees coordVoisin = new Coordonnees(
                            coordElement.getAbscisse() + delta[0],
                            coordElement.getOrdonnee() + delta[1]);

                    int x = coordVoisin.getAbscisse();
                    int y = coordVoisin.getOrdonnee();
                    if (x < 0 || puits.getLargueur() <= x || y < 0 || puits.getProfondeur() <= y) { // Si le voisin est
                                                                                                    // un mur
                        recompense += 3;
                    } else {
                        if (!coordsPiece.contains(coordVoisin)) {
                            // Analyse le voisin qui n'est donc pas un autre élement de la pièce

                            if (etat.getPieceActuelle(x, y) == 1) { // Si element
                                recompense += 4;
                            } else { // Si vide
                                if (etat.getPieceActuelle(x, y - 1) == 1 || // Si élement obstruant au-dessus
                                        etat.getPieceActuelle(x, y - 2) == 1) { // Si élement obstruant au-dessus
                                    recompense -= 2;
                                }
                            }
                        }
                    }
                }
            }

            /// Récompense pour les lignes complétées
            recompense += 10 * etat.getLignesCompletee();

            /// Malus de défaite
            recompense -= this.defaite ? 10000 : 0;

            // MAJ moyenne feedback
            this.feedback.addRecompense(recompense);
        }

        return recompense;
    }

    private Action getAction(Etat etat, Piece piece) {
        // EXPLORATION
        if (this.random.nextDouble() >= 1 - this.hp.getEpsilon()) {
            int indiceRandomAction = this.random.nextInt(Action.getNbActions());
            return Action.getAction(indiceRandomAction, piece);
        }

        // EXPLOITATION
        return Action.getAction((Nd4j.argMax(this.getQValues(etat), 0).getInt(0)), piece);
    }

    private void updateQValues(Etat etat, Action action, Etat prochainEtat, int recompense) {
        // Récupération de l'indice de l'action dans le référentiel de l'ia
        final int indiceAction = Action.getIndexOfAction(action);

        // Récupération de la Q value max du prochaine état
        final double prochainEtatQMax = this.getQMax(prochainEtat);

        // Récupération des Q values de l'état actuel pour construire la target
        INDArray qValues = this.getQValues(etat).dup();
        double targetQ = recompense + this.hp.getGamma() * prochainEtatQMax;
        qValues.putScalar(0, indiceAction, targetQ);

        // Entrainement du modèle en le fesant tendre vers r + max Q (s', a')
        this.etats.putRow(this.batchIndex, etat.get());
        this.targets.putRow(this.batchIndex, qValues);
        this.batchIndex++;

        if (this.batchIndex >= IA.BATCH_SIZE) {
            this.model.fit(this.etats, this.targets);
            this.batchIndex = 0;
        }
    }

    public void train(int nbEpisodes) {
        LOGGER.log(Level.INFO, "Entrainement du modèle d''IA sur {0} épisodes !", String.valueOf(nbEpisodes));

        // Prep feedback
        this.feedback.setNbEpisode(nbEpisodes);
        this.feedback.setEpisode(0);

        for (int episode = 1; episode <= nbEpisodes; episode++) {
            Jeu jeu = new Jeu(this.largeurPuits, this.profondeurPuits, 0, this.modeUsine);
            this.pcs.firePropertyChange(IA.EVT_CHANGEMENT_JEU, null, jeu); // Notifie quand le jeu change

            // Récupération des propriétés du jeu
            Puits puits = jeu.getPuits();
            Tas tas = puits.getTas();

            // Récupération de la pièce actuelle du puits et qui va donc être bougé par l'ia
            Piece piece = puits.getPieceActuelle();

            // S'enregistre pour recevoir les evt du jeu LIGNE_COMPLETE et
            // LIMITE_HAUTEUR_ATTEINTE
            puits.addPropertyChangeListener(this);
            tas.addPropertyChangeListener(this);

            // Set le bon état du flag défaite de telle manière pas besoin de le rabaisser
            // (cf suite)
            this.defaite = false;

            while (!this.defaite) {
                // Q Algorithme

                /// Récupération de l'état
                Etat etat = this.getEtat(puits, tas);

                /// Prédiction
                Action action = this.getAction(etat, piece);

                /// Action
                boolean collision = action.execute();

                // Si on pose la pièce c-a-d si lors d'un mvt vers le bas il a collision alors
                // on lève le flag pose qu'il faudra baisser quand la gravite sera executée.
                if (collision && action instanceof MoveDown) {
                    this.pose = true;
                }

                // Aprés l'action si l'action à engendrée la défaite le listener lève un flag
                // defaite qu'il faut rabaisser apr

                /// Récupération de la récompense
                int recompense = this.getRecompense(etat, piece);

                /// MAJ du jeu (gravité si posé <= flag genéré par l'action)
                if (this.pose) {
                    puits.gravite();
                    this.pose = false;
                }

                /// On recupère le nouvel état
                Etat nouvelEtat = this.getEtat(puits, tas);

                // Récupérience des données pour le feedback
                this.updateQValues(etat, action, nouvelEtat, recompense);
            }

            // Stats
            this.pcs.firePropertyChange(IA.EVT_CALC_STATS, null, jeu);

            /// MAJ des hyperparametres
            this.hp.update();
        }

        LOGGER.log(Level.INFO, "Entrainement fini !");
    }

    /// Changement de jeu listener

    public final void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public final void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    //
    // Overrides
    //

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Puits.LIMITE_HAUTEUR_ATTEINTE.equals(evt.getPropertyName())) {
            this.defaite = true;
        }
    }
}
