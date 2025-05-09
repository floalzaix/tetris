package fr.eseo.e3.poo.projet.blox.modele;

import fr.eseo.e3.poo.projet.blox.modele.pieces.Tas;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.Tetromino;

public class Jeu {
    //
    // Variables d'instance
    //
    private final Puits puits;
    private int place;

    //
    // Constructeurs
    //
    public Jeu(int largueur, int profondeur, int niveau, int modeUsine) {
        this.place = 1;

        //
        // Init
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

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}
