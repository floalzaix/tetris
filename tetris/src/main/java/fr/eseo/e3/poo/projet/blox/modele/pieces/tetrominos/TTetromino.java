package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;

public class TTetromino extends Tetromino {
    //
    //  Constructeurs
    //
    public TTetromino() {
        super();
    }
    public TTetromino(Coordonnees coord, Couleur couleur) {
        super(coord, couleur);
    }

    @Override
    protected int[][] defGabarit() {
        return new int[][] {{0, 0}, {1, 0}, {-1, 0}, {0, 1}};
    }

    @Override
    public Couleur getCouleurDefaut() {
        return Couleur.BLEU;
    }

    @Override
    protected Tetromino copySelf() {
        return new TTetromino(this.getElements().getFirst().getCoord(), this.couleur);
    }
}
