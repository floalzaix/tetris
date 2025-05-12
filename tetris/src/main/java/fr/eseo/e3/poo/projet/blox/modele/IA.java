package fr.eseo.e3.poo.projet.blox.modele;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;

import fr.eseo.e3.poo.projet.blox.modele.pieces.Tas;

public class IA {
    //
    //  Constantes de classe
    //
    private static final int NB_ELEMENTS_PIECES = 4;

    //
    //  Variables d'instance
    //
    private final Jeu jeu;
    private final Puits puits;
    private final Tas tas;
    private final MultiLayerConfiguration conf;
    private final MultiLayerNetwork model;

    //
    //  Constructeurs
    //
    // TODO : difficulté / niveau
    public IA(Jeu jeu) {
        this.jeu = jeu;
        this.puits = jeu.getPuits();
        this.tas = this.puits.getTas();
        
        this.conf = new NeuralNetConfiguration.Builder()
            .updater(new Adam(0.01))
            .list()
            .layer(new DenseLayer.Builder()
                .activation(Activation.RELU)
                .nIn(this.puits.getLargueur() * this.puits.getProfondeur() + 2 * NB_ELEMENTS_PIECES)
                .nOut(128)
                .build())
            .layer(new DenseLayer.Builder()
                .activation(Activation.RELU)
                .nIn(128)
                .nOut(64)
                .build())
            .layer(new OutputLayer.Builder()
                .activation(Activation.IDENTITY)
                .nIn(64)
                .nOut(6)
                .build())
            .build();

        this.model = new MultiLayerNetwork(conf);
    }

    //
    //  Méthodes
    //
    public void train(int nbOfEpisodes) {

    }

}
