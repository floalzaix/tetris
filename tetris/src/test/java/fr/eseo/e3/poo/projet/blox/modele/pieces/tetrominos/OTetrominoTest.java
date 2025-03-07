package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Element;

class OTetrominoTest {
    private OTetromino o;

    @BeforeEach
    void setUp() {
        o = new OTetromino(new Coordonnees(-1, 50), Couleur.ORANGE);
    }

    @Test
    void testConstructeur() {
        OTetromino o2 = new OTetromino(new Coordonnees(1, 2), Couleur.ORANGE);
        Element[] elts = o2.getElements();
        assertEquals(4, elts.length, "Erreur dans le constructeur !");
        assertEquals(new Element(new Coordonnees(1, 2), Couleur.ORANGE), elts[0], "Erreur dans le constructeur !");
        assertEquals(new Element(new Coordonnees(2, 2), Couleur.ORANGE), elts[1], "Erreur dans le constructeur !");
        assertEquals(new Element(new Coordonnees(2, 1), Couleur.ORANGE), elts[2], "Erreur dans le constructeur !");
        assertEquals(new Element(new Coordonnees(1, 1), Couleur.ORANGE), elts[3], "Erreur dans le constructeur !");
    }

    @Test 
    void testToString() {
        String test = "OTetromino :\n\t(-1, 50) - ORANGE\n\t(0, 50) - ORANGE\n\t(0, 49) - ORANGE\n\t(-1, 49) - ORANGE\n";
        assertEquals(test, o.toString(), "Erreur dans le toString !");
    }
}
