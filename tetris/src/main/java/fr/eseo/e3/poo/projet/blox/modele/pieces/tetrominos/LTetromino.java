package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;

public class LTetromino extends Tetromino {
    //
    //  Constructeurs
    //
    public LTetromino() {
        super();
    }
    public LTetromino(Coordonnees coord, Couleur couleur) {
        super(coord, couleur);
    }

    @Override
    protected int[][] defGabarit() {
        return new int[][] {{0, 0}, {1, 0}, {0, -1}, {0, -2}};
    }

    @Override
    public Couleur getCouleurDefaut() {
        return Couleur.VERT;
    }

    @Override
    protected Tetromino copySelf() {
        return new LTetromino(this.getElements().getFirst().getCoord(), this.couleur);
    }
}
