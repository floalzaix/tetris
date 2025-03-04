package fr.eseo.e3.poo.projet.blox.modele;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoordonneesTest {
    private Coordonnees coord;

    @BeforeEach
    void setUp() {
        coord = new Coordonnees(-1, 10);
    }

    @Test
    void testGetAbscisse() {
        assertEquals(-1, coord.getAbscisse(), "Erreur sur getAbscisse !");
    }

    @Test
    void testGetOrdonnee() {
        assertEquals(10, coord.getOrdonnee(), "Erreur sur getOrdonnee !");
    }

    @Test
    void testSetAbscisse() {
        coord.setAbscisse(7);
        assertEquals(7, coord.getAbscisse(), "Erreur du setter de abscisse !");
    }

    @Test
    void testSetOrdonnee() {
        coord.setOrdonnee(-100);
        assertEquals(-100, coord.getOrdonnee(), "Erreur dans le setter de l'ordonnee ! ");
    }

    @Test
    void testToString() {
        coord.setAbscisse(80);
        coord.setOrdonnee(1000);
        assertEquals("(80, 1000)", coord.toString(), "Erreur dans le test toString pour les coordonnées !");
    }

    @Test
    void testEquals() {
        Coordonnees coord2 = new Coordonnees(5, -999);
        Coordonnees coord3 = new Coordonnees(5, -999);
        Coordonnees coord4 = new Coordonnees(5, -998);
        assertNotEquals(coord, coord2, "Erreur dans le test equals pour les coordonnées !");
        assertEquals(coord, coord, "Erreur dans le test equals pour les coordonnées !");
        assertEquals(coord2, coord3, "Erreur dans le test equals pour les coordonnées !");
        assertNotEquals(coord3, coord4, "Erreur dans le test equals pour les coordonnées !");
        assertNotEquals(null, coord, "Erreur dans le test equals pour les coordonnées !");
        boolean testClass = coord.equals(new Object());
        assertFalse(testClass, "Erreur dans le test equals pour les coordonnées !");
    }

    @Test
    void testHashCode() {
        Coordonnees coord2 = new Coordonnees(5, -999);
        Coordonnees coord3 = new Coordonnees(5, -999);
        assertNotEquals(coord.hashCode(), coord2.hashCode(), "Erreur dans le test hashCode pour les coordonnées !");
        assertEquals(coord2.hashCode(), coord3.hashCode(), "Erreur dans le test hashCode pour les coordonnées !");
    }
}
