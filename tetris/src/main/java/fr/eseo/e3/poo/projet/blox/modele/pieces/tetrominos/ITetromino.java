package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;

public class ITetromino extends Tetromino {
    //
    // Constructeurs
    //
    public ITetromino() {
        // Ne devrait pas être appelé
        super();
    }
    public ITetromino(Coordonnees coord, Couleur couleur) {
        super(coord, couleur);
    }

    //
    //  Overrides
    //

    @Override
    protected int[][] defGabarit() {
        return new int[][] {{0, 0}, {0, 1}, {0, -1}, {0, -2}};
    }

    @Override
    public Couleur getCouleurDefaut() {
        return Couleur.ROUGE;
    } 

    @Override
    protected Tetromino copySelf() {
        return new ITetromino(this.getElements().getFirst().getCoord(), this.couleur);
    }
}
