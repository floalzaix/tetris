package fr.eseo.e3.poo.projet.blox.modele;

import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.Tetromino;

public class Jeu {
    //
    //  Variables d'instance
    //

    private final Puits puits;

    //
    //  Constructeurs
    //
    public Jeu() {
        //
        //  Init
        //

        Tetromino.init();

        // Selection du mode de l'usine
        UsineDePiece.setMode(UsineDePiece.ALEATOIRE_PIECE);

        // Puits
        this.puits = new Puits(10, 20);
        puits.setPieceSuivante(UsineDePiece.genererTetromino());
        puits.setPieceSuivante(UsineDePiece.genererTetromino());
    }

    // Getters setters
    public Puits getPuits() {
        return puits;
    }
}
