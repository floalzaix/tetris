package fr.eseo.e3.poo.projet.blox.modele;

import fr.eseo.e3.poo.projet.blox.modele.pieces.Tas;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.Tetromino;

public class Jeu {
    //
    //  Variables d'instance
    //

    private final Puits puits;

    //
    //  Constructeurs
    //
    public Jeu(int largueur, int profondeur, int niveau, int modeUsine) {
        //
        //  Init
        //

        Tetromino.init();

        // Selection du mode de l'usine
        UsineDePiece.setMode(modeUsine);

        // Puits
        this.puits = new Puits(largueur, profondeur);
        this.puits.setTas(new Tas(this.puits, niveau));
        puits.setPieceSuivante(UsineDePiece.genererTetromino());
        puits.setPieceSuivante(UsineDePiece.genererTetromino());
    }

    // Getters setters
    public Puits getPuits() {
        return puits;
    }
}
