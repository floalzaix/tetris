package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.Puits;

class VueTasTest {
    @Test
    void testConstructeur() {
        VuePuits vuePuits = new VuePuits(new Puits(10, 20), 20);
        VueTas vueTas = new VueTas(vuePuits, 20);
    }

    @Test
    void testNuance() {
        Color color = VueTas.nuance(new Color(255, 255, 255));
        assertEquals(new Color((int) (255 * (1 - VueTas.MULTIPLIER_NUANCE)),
                (int) (255 * (1 - VueTas.MULTIPLIER_NUANCE)),
                (int) (255 * (1 - VueTas.MULTIPLIER_NUANCE))), color);
    }
}
