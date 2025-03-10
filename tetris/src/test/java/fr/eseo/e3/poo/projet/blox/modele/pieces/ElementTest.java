package fr.eseo.e3.poo.projet.blox.modele.pieces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Element;

class ElementTest {
    private Element elt;

    @BeforeEach
    void setUp() {
        elt = new Element(10, 11, Couleur.BLEU);
    }

    @Test
    void testConstructeurs() {
        Coordonnees coord = new Coordonnees(-5, 0);
        assertEquals(new Element(coord, Couleur.values()[0]), new Element(coord), "Erreur dans les constructeurs !");
        assertEquals(new Element(coord, Couleur.values()[0]), new Element(coord.getAbscisse(), coord.getOrdonnee()),
                "Erreur dans les constructeurs !");
    }

    @Test
    void testDeplacerDe() {
        elt.setCoord(new Coordonnees(5, -6));
        elt.deplacerDe(1, 1);
        assertEquals(new Coordonnees(6, -5), elt.getCoord());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> elt.deplacerDe(1, 2));
        assertEquals("Erreur le déplacement d'un pièce ne peut pas être supérieur à 1 ou ne peut pas aller vers le haut !", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> elt.deplacerDe(2, 0));
        assertEquals("Erreur le déplacement d'un pièce ne peut pas être supérieur à 1 ou ne peut pas aller vers le haut !", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> elt.deplacerDe(0, -1));
        assertEquals("Erreur le déplacement d'un pièce ne peut pas être supérieur à 1 ou ne peut pas aller vers le haut !", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> elt.deplacerDe(-2, 0));
        assertEquals("Erreur le déplacement d'un pièce ne peut pas être supérieur à 1 ou ne peut pas aller vers le haut !", e.getMessage());
    }

    @Test
    void testGetCoordonnees() {
        assertEquals(new Coordonnees(10, 11), elt.getCoord(), "Erreur dans getCoordonnees !");
    }

    @Test
    void testGetCouleur() {
        assertEquals(Couleur.BLEU, elt.getCouleur(), "Erreur dans le getCouleur !");
    }

    @Test
    void testSetCoordonnees() {
        Coordonnees coord = new Coordonnees(1, -1);
        elt.setCoord(coord);
        assertEquals(coord, elt.getCoord(), "Erreur dans le setCoordonnees ! ");
    }

    @Test
    void testSetCouleur() {
        elt.setCouleur(Couleur.VIOLET);
        assertEquals(Couleur.VIOLET, elt.getCouleur(), "Erreur dans le setCouleur !");
    }

    @Test
    void testToString() {
        elt.setCoord(new Coordonnees(0, 0));
        elt.setCouleur(Couleur.ROUGE);
        assertEquals("Element(0, 0) - ROUGE", elt.toString(), "Erreur dans le toString !");
    }

    @Test
    void testEquals() {
        Element elt1 = new Element(10, 10, Couleur.CYAN);
        Element elt2 = new Element(-5, 10, Couleur.CYAN);
        Element elt3 = new Element(10, -6, Couleur.CYAN);
        Element elt4 = new Element(10, 10, Couleur.VERT);
        Element elt5 = new Element(10, 10, Couleur.CYAN);
        Element elt6 = new Element(null);
        Element elt7 = new Element(null, Couleur.CYAN);
        boolean test = elt1.equals(elt1);
        assertTrue(test, "Erreur dans le equals");
        test = elt1.equals(elt5);
        assertTrue(test, "Erreur dans le equals");
        test = elt1.equals(elt2);
        assertFalse(test, "Erreur dans le equals");
        test = elt1.equals(elt3);
        assertFalse(test, "Erreur dans le equals");
        test = elt1.equals(elt4);
        assertFalse(test, "Erreur dans le equals");
        test = elt1.equals(null);
        assertFalse(test, "Erreur dans le equals");
        test = elt6.equals(elt1);
        assertFalse(test, "Erreur dans le equals");
        test = elt6.equals(elt7);
        assertFalse(test, "Erreur dans le equals");
        test = elt1.equals(new Object());
        assertFalse(test, "Erreur dans le equals !");
    }

    @Test
    void testHashCode() {
        Element elt1 = new Element(10, 10, Couleur.CYAN);
        Element elt2 = new Element(-5, 10, Couleur.CYAN);
        Element elt3 = new Element(10, -6, Couleur.CYAN);
        Element elt4 = new Element(1, 10, Couleur.VERT);
        Element elt5 = new Element(10, 10, Couleur.CYAN);
        Element elt6 = new Element(null);
        Element elt7 = new Element(null, null);
        assertNotEquals(elt1.hashCode(), elt2.hashCode(), "Erreur dans le hashCode !");
        assertNotEquals(elt1.hashCode(), elt3.hashCode(), "Erreur dans le hashCode !");
        assertNotEquals(elt1.hashCode(), elt4.hashCode(), "Erreur dans le hashCode !");
        assertNotEquals(elt3.hashCode(), elt4.hashCode(), "Erreur dans le hashCode !");
        assertNotEquals(elt6.hashCode(), elt7.hashCode());
        assertEquals(elt6.hashCode(), elt6.hashCode());
        assertEquals(elt7.hashCode(), elt7.hashCode());
        assertEquals(elt1.hashCode(), elt5.hashCode(), "Erreur dans le hashCode !");
    }
}
