package fr.eseo.e3.poo.projet.blox.modele.ai;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

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

    public static final String PATH_TO_IA = "tetris\\src\\main\\java\\fr\\eseo\\e3\\poo\\projet\\blox\\modele\\ai\\ia_tetris.zip";

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
    public IA(int largeurPuits, int profondeurPuits, int modeUsine, boolean load) throws IOException {
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
                        .dropOut(0.5)
                        .nIn(Etat.getNumberOfInput(largeurPuits, profondeurPuits))
                        .nOut(128)
                        .activation(Activation.RELU)
                        .build())
                .layer(new DenseLayer.Builder()
                        .dropOut(0.5)
                        .nOut(64)
                        .activation(Activation.RELU)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .nOut(Action.getNbActions())
                        .activation(Activation.IDENTITY)
                        .build())
                .build();

        if (load) {
            this.model = ModelSerializer.restoreMultiLayerNetwork(PATH_TO_IA);
        } else {
            this.model = new MultiLayerNetwork(conf);
            this.model.init();
        }

        this.feedback = new Feedback(this.model, this.hp);
        this.addPropertyChangeListener(this.feedback);
    }

    //
    // Méthodes
    //

    /// Q LEARNING (Entrainement)

    /**
     * Recupère l'état actuel du système (cf Etat)
     * 
     * @param puits Le puits du jeu dont on veut récupérer l'Etat
     * @param tas   Le tas du jeu dont on veut récupérer l'Etat
     * @return L'etat en question
     */
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

    /**
     * Récupère la valeur maximale des qValeurs du système
     * 
     * @param etat L'etat dont on veut récupèrer la valeur max de Q
     * @return La Q valeur max pour l'Etat
     */
    private double getQMax(Etat etat) {
        return this.getQValues(etat).max(1).getDouble(0);
    }

    /**
     * Recupère les Q valeurs du système
     * 
     * @param etat L'Etat à partir duquel on veut récupérer les Q valeurs
     * @return Les Q valeurs
     */
    private INDArray getQValues(Etat etat) {
        if (etat == this.etatActuel) {
            return this.qValuesActuelles;
        }
        this.etatActuel = etat;
        this.qValuesActuelles = this.model.output(etat.get(), false);
        return this.qValuesActuelles;
    }

    /**
     * Recupère l'action que le modèle prédit pour un Etat donné
     * 
     * @param etat  L'état auquel on veut prédire l'action à effectuer
     * @param piece La pièce sur laquelle l'action sera effectuée
     * @return L'Action en question
     */
    private Action getAction(Etat etat, Piece piece) {
        // EXPLORATION
        if (this.random.nextDouble() >= 1 - this.hp.getEpsilon()) {
            int indiceRandomAction = this.random.nextInt(Action.getNbActions());
            return Action.getAction(indiceRandomAction, piece);
        }

        // EXPLOITATION
        return Action.getAction((Nd4j.argMax(this.getQValues(etat), 1).getInt(0)), piece);
    }

    /**
     * Met à jour les Q valeurs cible du modèle. Entraine le réseau de neuronne par
     * batch (lots de données) en enregistrant et créant donc un dataset de données
     * du jeux avec les Etat et les cibles à atteindre.
     * 
     * @param etat         L'état actuel
     * @param action       L'action réalisé à l'état
     * @param prochainEtat Le prochain état aprés avoir réalisé l'action
     * @param recompense   La récompense attribué pour avoir effectué l'action à
     *                     l'état
     */
    private void updateQValues(Etat etat, Action action, Etat prochainEtat, double recompense) {
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

    /**
     * Permet d'entrainer le modèle (plus feedback)
     * 
     * @param nbEpisodes Le nombre de partie à effectuer pour entrainer le modèle
     */
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

            // Création de l'analyseur de tas pour récupérer les données pour les
            // récompenses
            AnalyseurTas analyseur = new AnalyseurTas(tas);
            tas.setAnalyseur(analyseur);

            // Création du système de récompense de la partie
            Recompense recompense = new Recompense(analyseur);

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

                /// MAJ du jeu (gravité si posé <= flag genéré par l'action)
                if (this.pose) {
                    puits.gravite();
                    piece = puits.getPieceActuelle();
                }

                /// Récupération de la récompense
                recompense.update(action, puits, this.pose, collision, this.defaite);

                // On abaisse le flag posé
                if (this.pose) {
                    this.pose = false;
                }

                this.feedback.addRecompense(recompense.get());

                /// On recupère le nouvel état
                Etat nouvelEtat = this.getEtat(puits, tas);

                // Récupérience des données pour le feedback
                this.updateQValues(etat, action, nouvelEtat, recompense.get());
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
