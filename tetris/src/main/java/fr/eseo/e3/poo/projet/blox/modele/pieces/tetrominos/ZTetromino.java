package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;

public class ZTetromino extends Tetromino {
    //
    //  Constructeurs
    //
    public ZTetromino() {
        super();
    }
    public ZTetromino(Coordonnees coord, Couleur couleur) {
        super(coord, couleur);
    }

    @Override
    protected int[][] defGabarit() {
        return new int[][] {{0, 0}, {1, 0}, {0, -1}, {-1, -1}};
    }

    @Override
    public Couleur getCouleurDefaut() {
        return Couleur.CYAN;
    }

    @Override
    protected Tetromino copySelf() {
        return new ZTetromino(this.getElements().getFirst().getCoord(), this.couleur);
    }
}
