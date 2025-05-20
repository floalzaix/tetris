package fr.eseo.e3.poo.projet.blox.modele.ai;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import fr.eseo.e3.poo.projet.blox.modele.Jeu;

public class Feedback implements PropertyChangeListener {
    //
    //  Constantes de classe
    //
    private static final Logger LOGGER = Logger.getLogger(Feedback.class.getName());

    //
    //  Variables d'instance
    //
    private final MultiLayerNetwork model;

    private Hyperparametres hp;

    private int nbEpisode;
    private int episode;

    /// Stats par épisode

    // Score
    private int scoreMax;
    private double scoreMoyen;

    // Log loss (par batch)
    private double lossMin;
    private double lossMoyen;

    // Récompense
    private long recompenseTotaleJeu; // Modifier dans getRecompense()
    private double recompenseMoyenne;

    //
    //  Constructeurs
    //
    public Feedback(MultiLayerNetwork model, Hyperparametres hp) {
        this.model = model;
        this.hp = hp;
    }

    //
    //  Overrides
    //

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (IA.EVT_CALC_STATS.equals(evt.getPropertyName())) {
            Jeu jeu = (Jeu) evt.getNewValue();

            this.episode++;

            // Score
            int score = jeu.getPuits().getScore();
            this.scoreMax = Math.max(this.scoreMax, score);
            this.scoreMoyen = (this.scoreMoyen * (this.episode - 1) + score) / (this.episode);

            // Loss
            double loss = this.model.score();
            this.lossMin = Math.min(this.lossMin, loss);
            this.lossMoyen = (this.lossMoyen * (this.episode - 1) + loss) / (this.episode);

            // Récompense
            this.recompenseMoyenne = (this.recompenseMoyenne * (this.episode - 1) + this.recompenseTotaleJeu) / (this.episode);
            this.recompenseTotaleJeu = 0;

            // Affichage
            if (this.episode % 5 == 0) {
                LOGGER.log(Level.INFO,
                "---------------\nEpisode {0} / {1}\nSCORE MAX {2} MOYEN {3}\nLOSS MIN {4} MOYEN {5}\nR {6}\nALPHA {7} GAMMA {8} EPSILON {9}\n---------------\n",
                    new Object[] {
                        String.valueOf(this.episode),
                        String.valueOf(this.nbEpisode),
                        String.valueOf(this.scoreMax),
                        String.valueOf(this.scoreMoyen),
                        String.valueOf(this.lossMin),
                        String.valueOf(this.lossMoyen),
                        String.valueOf(this.recompenseMoyenne),
                        String.valueOf(this.hp.getAlpha()),
                        String.valueOf(this.hp.getGamma()),
                        String.valueOf(this.hp.getEpsilon())
                    }
                );
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
    public void addRecompense(int recompense) {
        this.recompenseTotaleJeu+= recompense;
    }

    // Getters setters
    public void setNbEpisode(int nbEpisode) {
        this.nbEpisode = nbEpisode;
    }
    public void setEpisode(int episode) {
        this.episode = episode;
    }
}
