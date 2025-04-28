package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;

public class ZTetrominoTest extends TetrominoTest {

    @Override
    public Tetromino instance(Coordonnees coord, Couleur couleur) {
        return new ZTetromino(coord, couleur);
    }

    @Override
    public Tetromino instance() {
        return new ZTetromino();
    }

    @Override
    public int[][] getGabarit() {
        return new int[][] {{0, 0}, {1, 0}, {0, -1}, {-1, -1}};
    }

    @Override
    public Couleur getCouleurParDefaut() {
        return Couleur.CYAN;
    }
    
}
