package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;

class OTetrominoTest extends TetrominoTest {
    //
    //  Overrides
    //
    
    @Override
    public Tetromino instance(Coordonnees coord, Couleur couleur) {
        return new OTetromino(coord, couleur);
    }

    @Override
    public Tetromino instance() {
        return new OTetromino();
    }

    @Override
    public int[][] getGabarit() {
        return new int[][] {{0, 0}, {1, 0}, {1, -1}, {0, -1}};
    }

    @Override
    public Couleur getCouleurParDefaut() {
        return Couleur.ROUGE;
    }

    
}
