package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;

public class JTetrominoTest extends TetrominoTest {

    @Override
    public Tetromino instance(Coordonnees coord, Couleur couleur) {
        return new JTetromino(coord, couleur);
    }

    @Override
    public Tetromino instance() {
        return new JTetromino();
    }

    @Override
    public int[][] getGabarit() {
        return new int[][] {{0, 0}, {-1, 0}, {0, -1}, {0, -2}};
    }

    @Override
    public Couleur getCouleurParDefaut() {
        return Couleur.JAUNE;
    }
    
}
