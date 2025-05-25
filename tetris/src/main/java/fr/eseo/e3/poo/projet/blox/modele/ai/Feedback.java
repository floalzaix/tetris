package fr.eseo.e3.poo.projet.blox.modele.ai;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import fr.eseo.e3.poo.projet.blox.modele.Jeu;

public class Feedback implements PropertyChangeListener {
    //
    // Constantes de classe
    //
    private static final Logger LOGGER = Logger.getLogger(Feedback.class.getName());

    private static final int NB_ACTION_MAX = 10000;

    //
    // Variables d'instance
    //
    private final MultiLayerNetwork model;

    private final Hyperparametres hp;

    private int nbEpisode;
    private int episode;

    /// Stats par épisode

    // Score
    private double scoreMoyen;
    private int scoreMax;

    // Log loss
    private double lossMoyen;

    // Récompense
    private double recompenseTotaleJeu; // Modifier dans train()
    private double recompenseMin;
    private double recompenseMoyenne;
    private double recompenseMax;

    // Récompense par action toutes les 150 actions
    private int nbAction = 0;
    private double recompenseMinAction;
    private double recompenseMoyenneAction;
    private double recompenseMaxAction;

    //
    // Constructeurs
    //
    public Feedback(MultiLayerNetwork model, Hyperparametres hp) {
        this.model = model;
        this.hp = hp;
    }

    //
    // Overrides
    //

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (IA.EVT_CALC_STATS.equals(evt.getPropertyName())) {
            Jeu jeu = (Jeu) evt.getNewValue();

            this.episode++;

            // Score
            int score = jeu.getPuits().getScore();
            this.scoreMoyen = (this.scoreMoyen * (this.episode - 1) + score) / (this.episode);
            this.scoreMax = Math.max(this.scoreMax, score);

            // Loss
            double loss = this.model.score();
            this.lossMoyen = (this.lossMoyen * (this.episode - 1) + loss) / (this.episode);

            // Récompense
            this.recompenseMin = Math.min(this.recompenseMin, this.recompenseTotaleJeu);
            this.recompenseMoyenne = (this.recompenseMoyenne * (this.episode - 1) + this.recompenseTotaleJeu)
                    / (this.episode);
            this.recompenseMax = Math.max(this.recompenseMax, this.recompenseTotaleJeu);
            this.recompenseTotaleJeu = 0;

            // Affichage
            if (this.episode % 5 == 0) {
                LOGGER.log(Level.INFO,
                        "\n---------------\nEpisode {0} / {1}\nSCORE moy {2} max {3}\nLOSS moy {4}\nR/jeu min {5} moy {6} max {7} \nALPHA {8} GAMMA {9} EPSILON {10}\n---------------\n",
                        new Object[] {
                                String.valueOf(this.episode),
                                String.valueOf(this.nbEpisode),
                                String.valueOf(this.scoreMoyen),
                                String.valueOf(this.scoreMax),
                                String.valueOf(this.lossMoyen),
                                String.valueOf(this.recompenseMin),
                                String.valueOf(this.recompenseMoyenne),
                                String.valueOf(this.recompenseMax),
                                String.valueOf(this.hp.getAlpha()),
                                String.valueOf(this.hp.getGamma()),
                                String.valueOf(this.hp.getEpsilon())
                        });
            }

            // Enregistre le modèle
            try {
                ModelSerializer.writeModel(model, IA.PATH_TO_IA, true);
            } catch (IOException e) {
                // A changer ...
                e.printStackTrace();
            }
        }
    }

    /**
     * Ajoute la récompense calculé au mouvement pour avoir le total par jeu.
     * 
     * Pensé pour être appelé dans getRecompense() de la class IA
     * 
     * @param recompense La récompense calculé par la méthode.
     */
    public void addRecompense(double recompense) {
        this.recompenseTotaleJeu += recompense;

        // Récompense action
        this.nbAction++;
        this.recompenseMinAction = Math.min(this.recompenseMinAction, recompense);
        this.recompenseMoyenneAction = (this.recompenseMoyenneAction * (this.nbAction - 1) + recompense)
                / (this.nbAction);
        this.recompenseMaxAction = Math.max(this.recompenseMaxAction, recompense);
        if (this.nbAction == Feedback.NB_ACTION_MAX) {
            LOGGER.log(Level.INFO, "\n---------------\nRECOMPENSE sur {0} actions MIN {1} MOY {2} MAX {3}\n---------------\n",
                    new Object[] {
                            String.valueOf(Feedback.NB_ACTION_MAX),
                            String.valueOf(this.recompenseMinAction),
                            String.valueOf(this.recompenseMoyenneAction),
                            String.valueOf(this.recompenseMaxAction)
                    });

            this.recompenseMinAction = 0;
            this.recompenseMoyenneAction = 0;
            this.recompenseMaxAction = 0;
            this.nbAction = 0;
        }
    }

    // Getters setters
    public void setNbEpisode(int nbEpisode) {
        this.nbEpisode = nbEpisode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }
}
