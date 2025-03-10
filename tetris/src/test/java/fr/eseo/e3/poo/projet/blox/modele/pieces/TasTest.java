package fr.eseo.e3.poo.projet.blox.modele.pieces;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Puits;

class TasTest {
    @Test
    void testConstructeurs() {
        Puits puits = new Puits(10, 20);

        Tas tas = new Tas(puits);
        assertEquals(puits, tas.getPuits(), "Erreur dans les constructeurs !");
        assertEquals(0, tas.getElements().size(), "Erreur dans les constructeurs !");

        tas = new Tas(puits, 20);
        assertEquals(puits, tas.getPuits(), "Erreur dans les constructeurs !");
        assertEquals(20, tas.getElements().size(), "Erreur dans les constructeurs !");
        for (Element e : tas.getElements()) {
            assertTrue(e.getCoord().getOrdonnee() >= 17, "Erreur dans les constructeurs !");
        }

        tas = new Tas(puits, 25, 10);
        assertEquals(puits, tas.getPuits(), "Erreur dans les constructeurs !");
        assertEquals(25, tas.getElements().size(), "Erreur dans les constructeurs !");
        for (Element e : tas.getElements()) {
            assertTrue(e.getCoord().getOrdonnee() >= 10, "Erreur dans les constructeurs !");
        }

        tas = new Tas(puits, 100, 10, new Random());
        assertEquals(puits, tas.getPuits(), "Erreur dans les constructeurs !");
        assertEquals(100, tas.getElements().size(), "Erreur dans les constructeurs !");
        for (Element e : tas.getElements()) {
            assertTrue(e.getCoord().getOrdonnee() >= 10, "Erreur dans les constructeurs !");
        }

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Tas(new Puits(), 1000),
                "Erreur dans les constructeur !");
        assertEquals("Erreur lors de la création du tas : trop d'éléments !", e.getMessage(),
                "Erreur dans setLargueur !");
        e = assertThrows(IllegalArgumentException.class, () -> new Tas(new Puits(15, 25), 50, 2),
                "Erreur dans les constructeur !");
        assertEquals("Erreur lors de la création du tas : trop d'éléments !", e.getMessage(),
                "Erreur dans setLargueur !");
        e = assertThrows(IllegalArgumentException.class, () -> new Tas(new Puits(15, 25), 15 * 25 + 1, 500),
                "Erreur dans les constructeur !");
        assertEquals("Erreur lors de la création du tas : trop d'éléments !", e.getMessage(),
                "Erreur dans setLargueur !");
    }
}
