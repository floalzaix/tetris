package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;

class ITetrominoTest extends TetrominoTest {
    //
    // Tests setElements()
    //

    @Test
    void testSetElements() {
        this.bonsElements(new int[][] {{0, 0}, {0, 1}, {0, -1}, {0, -2}});
    }

    //
    //  Overrides
    //
    @Override
    public Tetromino instance(Coordonnees coord, Couleur couleur) {
        return new ITetromino(coord, couleur);
    }
}
