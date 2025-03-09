package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Puits;

class OTetrominoTest {
    @Test
    void testConstructeur() {
        OTetromino o2 = new OTetromino(new Coordonnees(1, 2), Couleur.ORANGE);
        Element[] elts = o2.getElements().toArray(new Element[0]);
        assertEquals(4, elts.length, "Erreur dans le constructeur !");
        assertEquals(new Element(new Coordonnees(1, 2), Couleur.ORANGE), elts[0], "Erreur dans le constructeur !");
        assertEquals(new Element(new Coordonnees(2, 2), Couleur.ORANGE), elts[1], "Erreur dans le constructeur !");
        assertEquals(new Element(new Coordonnees(2, 1), Couleur.ORANGE), elts[2], "Erreur dans le constructeur !");
        assertEquals(new Element(new Coordonnees(1, 1), Couleur.ORANGE), elts[3], "Erreur dans le constructeur !");
    }

    @Test 
    void testToString() {
        OTetromino o = new OTetromino(new Coordonnees(-1, 50), Couleur.ORANGE);
        String test = "OTetromino :\n\t(-1, 50) - ORANGE\n\t(0, 50) - ORANGE\n\t(0, 49) - ORANGE\n\t(-1, 49) - ORANGE\n";
        assertEquals(test, o.toString(), "Erreur dans le toString !");
    }

    @Test 
    void testSetPosition() {
        OTetromino o = new OTetromino(new Coordonnees(0, 0), Couleur.ORANGE);
        o.setPosition(20, 29);
        List<Element> elts = o.getElements();
        assertEquals(new Element(20, 29, Couleur.ORANGE), elts.get(0), "Erreur das setPosition !");
        assertEquals(new Element(21, 29, Couleur.ORANGE), elts.get(1), "Erreur das setPosition !");
        assertEquals(new Element(21, 28, Couleur.ORANGE), elts.get(2), "Erreur das setPosition !");
        assertEquals(new Element(20, 28, Couleur.ORANGE), elts.get(3), "Erreur das setPosition !");
    }

    @Test
    void testPuits() {
        OTetromino o = new OTetromino(new Coordonnees(0, 0), Couleur.BLEU);
        Puits puits = new Puits();
        o.setPuits(puits);
        assertEquals(puits, o.getPuits(), "Erreur dans getter ou setter du puits !");
    }
}
