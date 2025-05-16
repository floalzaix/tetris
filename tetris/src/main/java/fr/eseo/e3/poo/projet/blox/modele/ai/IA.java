package fr.eseo.e3.poo.projet.blox.modele.ai;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Jeu;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.ai.actions.Action;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Tas;

public class IA implements PropertyChangeListener {
    //
    // Constante de classe
    //
    public static final String EVT_CHANGEMENT_JEU = "JEU";

    private static final Logger LOGGER = Logger.getLogger(IA.class.getName());

    //
    // Variables d'instance
    //

    // Hyperparametres
    private float alpha;
    private float epsilon;
    private float gamma;

    // update des hyperparametres
    private UnaryOperator<Float> alphaDecay;
    private UnaryOperator<Float> epsilonDecay;
    private UnaryOperator<Float> gammaDecay;

    // Flags
    private boolean defaite;
    private int lignesComplete;
    private boolean pose;

    // Modèle
    private MultiLayerNetwork model;

    // Pour le changement de jeu lors de l'entrainement
    private PropertyChangeSupport pcs;

    //
    // Constructeurs
    //
    public IA(float alphaInit, float epsilonInit, float gammaInit, UnaryOperator<Float> alphaDecay,
            UnaryOperator<Float> epsilonDecay, UnaryOperator<Float> gammaDecay) {
        this.alpha = alphaInit;
        this.epsilon = epsilonInit;
        this.gamma = gammaInit;

        this.alphaDecay = alphaDecay;
        this.epsilonDecay = epsilonDecay;
        this.gammaDecay = gammaDecay;

        this.defaite = false;
        this.lignesComplete = 0;
        this.pose = false;

        // Initialisation du modèle
        // ...
    }

    //
    // Méthodes
    //

    /// Q LEARNING (Entrainement)

    private Etat getEtat(Puits puits, Tas tas) {
        Etat etat = new Etat(puits.getLargueur(), puits.getProfondeur());

        // Met des un dans le tableau de l'etat aux endroits où il y a des élements pour le TAS
        tas.getElements().stream()
            .map(Element::getCoord)
            .forEach(c -> {
                etat.setTas(c.getAbscisse(), c.getOrdonnee());
            });
        
        // Pour la PIECE SUIVANTE
        puits.getPieceSuivante().getElements().stream()
            .map(Element::getCoord)
            .forEach(c -> {
                etat.setPieceSuivante(c.getAbscisse(), c.getOrdonnee());
            });

        // Pour la PIECE ACTUELLE
        puits.getPieceActuelle().getElements().stream()
            .map(Element::getCoord)
            .forEach(c -> {
                etat.setPieceActuelle(c.getAbscisse(), c.getOrdonnee());
            });
    }

    // getQValue(s, a)

    // getReward(s, piece) ***

    // getAction(s) <- avec epsilon greedy

    // updateQValues(***)

    // updateHyperParamtres()

    public void train(int largeur, int profondeur, int taillePiece, int modeUsine, int nbEpisodes) {
        LOGGER.log(Level.INFO, "Entrainement du mod\u00e8le d''IA sur {0} \u00e9pisodes !", nbEpisodes);

        for (int episode = 1; episode <= nbEpisodes; episode++) {
            Jeu jeu = new Jeu(largeur, profondeur, 0, modeUsine);
            this.pcs.firePropertyChange(IA.EVT_CHANGEMENT_JEU, null, jeu); // Notifie quand le jeu change

            // Récupération des propriétés du jeu
            Puits puits = jeu.getPuits();
            Tas tas = puits.getTas();

            // S'enregistre pour recevoir les evt du jeu LIGNE_COMPLETE et LIMITE_HAUTEUR_ATTEINTE
            puits.addPropertyChangeListener(this);
            tas.addPropertyChangeListener(this);

            // Initialisation des actions
            Action.init();

            while (!defaite) {
                //
                //  Q Algorithme
                //
                
                /// Récupération de l'état
                
                Etat etat = 

                /// Prédiction
                
                /// Action

                // Aprés l'action si l'action à complété une ligne alors le listener lève un flag 
                // lignesCompletee avec le nombre de lignes complétées qu'il faut rabaisser

                // Aprés l'action si l'action à engendrée la défaite le listener lève un flag defaite 
                // qu'il faut rabaisser
                
                /// Récupération de la récompense

                /// MAJ du jeu (gravité si posé <= flag genéré par l'action)

                /// Correction
            }

            LOGGER.log(Level.INFO, "Episode {0} / {1}", new int[] {episode, nbEpisodes});
        }

        LOGGER.log(Level.INFO, "Entrainement fini !");
    }

    //
    // Overrides
    //

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    // Getters setters
}
