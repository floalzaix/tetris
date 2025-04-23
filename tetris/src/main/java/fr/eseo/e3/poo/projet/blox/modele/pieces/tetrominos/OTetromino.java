package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;

public class OTetromino extends Tetromino {
    //
    // Constructeurs
    //
    public OTetromino() {
        // Ne devrait pas être appelé
        super();
    }
    public OTetromino(Coordonnees coord, Couleur couleur) {
        super(coord, couleur);
    }

    //
    //  Overrides
    //

    @Override
    protected int[][] defGabarit() {
        return new int[][] {{0, 0}, {1, 0}, {1, -1}, {0, -1}};
    }

    @Override
    public Couleur getCouleurDefaut() {
        return Couleur.ROUGE;
    }

    @Override
    public void tourner(boolean sensHoraire) {
        // Un OTetromino ne peut pas tourner
    }

    @Override
    protected Tetromino copySelf() {
        return new OTetromino(this.getElements().getFirst().getCoord(), this.couleur);
    }
}
