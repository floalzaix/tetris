package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;

public class JTetromino extends Tetromino {
    //
    //  Constructeurs
    //
    public JTetromino() {
        super();
    }
    public JTetromino(Coordonnees coord, Couleur couleur) {
        super(coord, couleur);
    }

    @Override
    protected int[][] defGabarit() {
        return new int[][] {{0, 0}, {-1, 0}, {0, -1}, {0, -2}};
    }

    @Override
    public Couleur getCouleurDefaut() {
        return Couleur.JAUNE;
    }

    @Override
    protected Tetromino copySelf() {
        return new JTetromino(this.getElements().getFirst().getCoord(), this.couleur);
    }
}
