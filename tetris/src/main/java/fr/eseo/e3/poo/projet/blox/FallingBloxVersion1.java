package fr.eseo.e3.poo.projet.blox;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import fr.eseo.e3.poo.projet.blox.controleur.Gravite;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.UsineDePiece;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Tas;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.Tetromino;
import fr.eseo.e3.poo.projet.blox.vue.PanneauInformation;
import fr.eseo.e3.poo.projet.blox.vue.VuePuits;

public class FallingBloxVersion1 {
    //
    //  Params
    //

    public static final int SIZE = 30;
    public static final int SIZE_INFO = 10;

    public static void main(String[] args) {
        //
        //  Init
        //

        Tetromino.init();

        JFrame frame = new JFrame("Falling Blox");

        // Création du puits
        Puits puits;
        switch (args.length) {
            case 2 -> {
                puits = new Puits(10, 20, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            }
            case 1 -> {
                puits = new Puits(10, 20);
                puits.setTas(new Tas(puits, Integer.parseInt(args[0])));
            }
            default -> {
                puits = new Puits();
            }
        }

        // Création panneau du puits
        VuePuits vuePuits = new VuePuits(puits, 30);

        // Création de la panneau d'information
        PanneauInformation pi = new PanneauInformation(puits, SIZE_INFO);

        // Selection du mode de l'usine
        UsineDePiece.setMode(UsineDePiece.ALEATOIRE_PIECE);

        // Création des premières pièces
        puits.setPieceSuivante(UsineDePiece.genererTetromino());
        puits.setPieceSuivante(UsineDePiece.genererTetromino());

        // Parametrage de la fenêtre
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(vuePuits, BorderLayout.CENTER);
        frame.add(pi, BorderLayout.EAST);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); 

        // Activation de la gravité
        Gravite _ = new Gravite(vuePuits);
    }
}