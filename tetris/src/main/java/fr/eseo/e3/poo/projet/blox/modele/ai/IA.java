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
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.PoolingType;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.lossfunctions.LossFunctions;

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

    public static final int DIFFICULTE_FACILE = 4;
    public static final int DIFFICULTE_MOYENNE = 5;
    public static final int DIFFICULTE_DIFFICILE = 6;

    private static final Logger LOGGER = Logger.getLogger(IA.class.getName());

    private static final int BATCH_SIZE = 64;

    private static final int MAX_ITERATIONS_MAJ_TARGET = 1500;

    private static final Random RANDOM = new Random();

    //
    // Variables d'instance
    //
    private final Hyperparametres hp;
    private String pathToFolder;
    private boolean load;

    // Puits
    private final int largeurPuits;
    private final int profondeurPuits;
    private final int modeUsine;

    // Flags
    private boolean defaite;
    private boolean pose;

    // Réseaux de neurones
    private MultiLayerNetwork onlineNetwork;
    private MultiLayerNetwork targetNetwork;

    // Pour le changement de jeu lors de l'entrainement
    private final PropertyChangeSupport pcs;

    /// Mémoire du modèle
    
    // L'état actuel de l'entrainement
    private Etat etatActuel;
    private INDArray qValuesActuelles;

    private int batchIndex;
    private final INDArray etats;
    private final INDArray targets;

    // Mémoire pour le cloning du target model
    private int compteurMAJTarget;

    /// Stats
    private final Feedback feedback;

    //
    // Constructeurs
    //
    public IA(Hyperparametres hp, int largeurPuits, int profondeurPuits, int modeUsine, String pathToFolder, boolean load) {
        this.hp = hp;
        this.largeurPuits = largeurPuits;
        this.profondeurPuits = profondeurPuits;
        this.modeUsine = modeUsine;
        this.pathToFolder = pathToFolder;
        this.load = load;

        this.defaite = false;
        this.pose = false;
        this.pcs = new PropertyChangeSupport(this);

        // Initialisation des actions
        Action.init();

        // Préparation des tableaux pour les batchs
        this.batchIndex = 0;
        this.etats = Nd4j.create(IA.BATCH_SIZE, 1, this.profondeurPuits + Etat.PIECE_ACTUELLE_OFFSET_ORDONNEE, this.largeurPuits);
        this.targets = Nd4j.create(IA.BATCH_SIZE, Action.getNbActions());

        this.compteurMAJTarget = 0;

        // Initialisation du modèle
        this.initModeles();

        this.feedback = new Feedback(this.onlineNetwork, this.hp);
        this.addPropertyChangeListener(this.feedback);
    }
    public IA(Hyperparametres hp, int largeurPuits, int profondeurPuits, int modeUsine) {
        this(hp, largeurPuits, profondeurPuits, modeUsine, null, false);
    }

    //
    // Méthodes
    //

    private void initModeles() {
        MultiLayerConfiguration confQLearning = new NeuralNetConfiguration.Builder()
            .seed(1234)
            .updater(this.hp.getAdam())
            .list()
            .layer(0, new ConvolutionLayer.Builder()
                .activation(Activation.LEAKYRELU)
                .kernelSize(2, 2)
                .stride(1, 1)
                .nIn(1)
                .nOut(128)
                .build())
            .layer(1, new SubsamplingLayer.Builder(PoolingType.MAX)
                .kernelSize(2, 2)
                .stride(2, 2)
                .build())
            .layer(2, new ConvolutionLayer.Builder()
                .activation(Activation.LEAKYRELU)
                .kernelSize(2, 2)
                .stride(1, 1)
                .padding(1, 1)
                .nOut(64)
                .build())
            .layer(3, new SubsamplingLayer.Builder(PoolingType.MAX)
                .kernelSize(2, 2)
                .stride(2, 2)
                .build())
            .layer(4, new DenseLayer.Builder()
                .nOut(32)
                .activation(Activation.LEAKYRELU)
                .build())
            .layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                .nOut(Action.getNbActions())
                .activation(Activation.LEAKYRELU)
                .build())
            .setInputType(InputType.convolutional(this.profondeurPuits + (long) Etat.PIECE_ACTUELLE_OFFSET_ORDONNEE, this.largeurPuits, 1))
            .build();
        
        if (this.pathToFolder != null && this.load) {
            try {
                this.onlineNetwork = ModelSerializer.restoreMultiLayerNetwork(this.pathToFolder + "tetris_online_model.zip");
                this.targetNetwork = ModelSerializer.restoreMultiLayerNetwork(this.pathToFolder + "tetris_target_model.zip");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error while loading the model : {0}", e.getMessage());
            }
        } else {
            this.onlineNetwork = new MultiLayerNetwork(confQLearning);
            this.onlineNetwork.init();

            this.targetNetwork = new MultiLayerNetwork(confQLearning);
            this.targetNetwork.init();
            this.targetNetwork.setParams(this.onlineNetwork.params());
        }
    }

    /// Q LEARNING (Entrainement)

    /**
     * Récupère la valeur maximale des qValeurs du système
     * 
     * @param etat L'etat dont on veut récupèrer la valeur max de Q
     * @return La Q valeur max pour l'Etat
     */
    private double getQMax(Etat etat) {
        return this.getQValues(etat, this.targetNetwork).max(1).getDouble(0);
    }

    /**
     * Recupère les Q valeurs du système
     * 
     * @param etat L'Etat à partir duquel on veut récupérer les Q valeurs
     * @param model Le model à partir duquel on récupère les Q values
     * @return Les Q valeurs
     */
    private INDArray getQValues(Etat etat, MultiLayerNetwork model) {
        if (etat == this.etatActuel) {
            return this.qValuesActuelles;
        }
        this.etatActuel = etat;
        this.qValuesActuelles = model.output(etat.get(), false);
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
        if (IA.RANDOM.nextDouble() >= 1 - this.hp.getEpsilon()) {
            int indiceRandomAction = IA.RANDOM.nextInt(Action.getNbActions());
            return Action.getAction(indiceRandomAction, piece);
        }

        // EXPLOITATION
        return Action.getAction((Nd4j.argMax(this.getQValues(etat, this.onlineNetwork), 1).getInt(0)), piece);
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
        INDArray qValues = this.getQValues(etat, this.targetNetwork).dup();
        double targetQ = recompense + this.hp.getGamma() * prochainEtatQMax;
        qValues.putScalar(0, indiceAction, targetQ);

        // Entrainement du modèle en le fesant tendre vers r + max Q (s', a')
        this.etats.putSlice(this.batchIndex, etat.get().get(NDArrayIndex.point(0)));
        this.targets.putRow(this.batchIndex, qValues);
        this.batchIndex++;

        // Entrainement du batch
        if (this.batchIndex >= IA.BATCH_SIZE) {
            this.onlineNetwork.fit(this.etats, this.targets);
            this.batchIndex = 0;
        }

        // Cloning du online sur le target
        if (this.compteurMAJTarget >= IA.MAX_ITERATIONS_MAJ_TARGET) {
            this.targetNetwork.setParams(this.onlineNetwork.params());
            this.compteurMAJTarget = 0;
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
                Etat etat = new Etat(jeu);

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
                Etat nouvelEtat = new Etat(jeu);

                // Récupérience des données pour le feedback
                this.updateQValues(etat, action, nouvelEtat, recompense.get());
            }

            // Stats
            this.pcs.firePropertyChange(IA.EVT_CALC_STATS, null, jeu);

            // MAJ des hyperparametres
            this.hp.update();

            // Sauvegarde des réseaux
            if (episode % 10 == 0) {
                try {
                    if (this.pathToFolder != null) {
                        ModelSerializer.writeModel(this.onlineNetwork, this.pathToFolder + "tetris_online_model.zip", true);
                        ModelSerializer.writeModel(this.targetNetwork, this.pathToFolder + "tetris_target_model.zip", true);
                    }
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Problème de sauvegarde de modèle : {0}", e.getMessage());
                }
            }
        }

        LOGGER.log(Level.INFO, "Entrainement fini !");
    }

    public void play(Jeu jeu, int difficulte) {
        // Récupération des propriétés du jeu
        Puits puits = jeu.getPuits();

        // Récupération de la pièce actuelle du puits et qui va donc être bougé par l'ia
        Piece piece = puits.getPieceActuelle();

        // S'enregistre pour recevoir les evt du jeu
        // LIMITE_HAUTEUR_ATTEINTE
        puits.addPropertyChangeListener(this);

        this.defaite = false;

        while (!this.defaite) {
            /// Récupération de l'état
            Etat etat = new Etat(jeu);

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
                this.pose = false;
            }

            int delay = switch (difficulte) {
                case IA.DIFFICULTE_FACILE -> 50;
                case IA.DIFFICULTE_MOYENNE -> 25;
                case IA.DIFFICULTE_DIFFICILE -> 10;
                default -> throw new IllegalArgumentException("Mauvaise valeur de difficulté !");
            };

            try {
                Thread.sleep(delay);
            } catch (InterruptedException _) {
                System.exit(1);
            }
        }
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
