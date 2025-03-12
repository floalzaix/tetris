package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.OTetromino;

class VuePieceTest {
    @Test
    void testConstructeur() {
        VuePiece vue = new VuePiece(new OTetromino(new Coordonnees(5, 5), Couleur.ROUGE), 10);
    }

    @Test
    void testTeinte() {
        Color color = VuePiece.teinte(new Color(0, 0, 0));
        assertEquals(new Color((int) (255 * VuePiece.MULTPLIER_TEINTE), (int) (255 * VuePiece.MULTPLIER_TEINTE),
                (int) (255 * VuePiece.MULTPLIER_TEINTE)), color);
    }
}
