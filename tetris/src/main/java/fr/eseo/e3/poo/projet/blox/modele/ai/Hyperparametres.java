package fr.eseo.e3.poo.projet.blox.modele.ai;

import java.util.function.UnaryOperator;

import org.nd4j.linalg.learning.config.Adam;

public class Hyperparametres {
    //
    // Constantes de classe
    //

    // Init
    private static final double ALPHA_INIT = 0.01;
    private static final double GAMMA_INIT = 0.95;
    private static final double EPSILON_INIT = 0.9;

    // Growth
    private static final double GAMMA_GROWTH = 1.007;

    // Decays
    private static final double ALPHA_DECAY = 0.95;
    private static final double EPSILON_DECAY = 0.95;

    // Bornes
    private static final double ALPHA_MIN = 0.01;
    private static final double GAMMA_MAX = 0.95;
    private static final double EPSILON_MIN = 0.1;

    // Update des hyperparametres
    private static final UnaryOperator<Double> ALPHA_UPDATE = a -> Math.max(ALPHA_DECAY * a, ALPHA_MIN);
    private static final UnaryOperator<Double> GAMMA_UPDATE = g -> Math.min(GAMMA_GROWTH * g, GAMMA_MAX);
    private static final UnaryOperator<Double> EPSILON_UPDATE = e -> Math.max(EPSILON_DECAY * e, EPSILON_MIN);

    //
    // Variables d'instance
    //
    private double alpha;
    private double epsilon;
    private double gamma;

    private final Adam adam;

    //
    // Constructeurs
    //
    public Hyperparametres() {
        this.alpha = ALPHA_INIT;
        this.gamma = GAMMA_INIT;
        this.epsilon = EPSILON_INIT;

        this.adam = new Adam(this.alpha);
    }

    //
    // Méthodes
    //

    /**
     * Met à jour les hyperparamètres du modèle suivant la politique mise en place
     * par les PARAM_UPDATE
     */
    public void update() {
        this.alpha = ALPHA_UPDATE.apply(this.alpha);
        this.adam.setLearningRate(this.alpha);
        this.gamma = GAMMA_UPDATE.apply(this.gamma);
        this.epsilon = EPSILON_UPDATE.apply(this.epsilon);
    }

    // Getters setters
    public Adam getAdam() {
        return adam;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public double getGamma() {
        return gamma;
    }
}
