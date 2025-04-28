package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;

public class LTetrominoTest extends TetrominoTest {

    @Override
    public Tetromino instance(Coordonnees coord, Couleur couleur) {
        return new LTetromino(coord, couleur);
    }

    @Override
    public Tetromino instance() {
        return new LTetromino();
    }

    @Override
    public int[][] getGabarit() {
        return new int[][] {{0, 0}, {1, 0}, {0, -1}, {0, -2}};
    }

    @Override
    public Couleur getCouleurParDefaut() {
        return Couleur.VERT;
    }
    
}
