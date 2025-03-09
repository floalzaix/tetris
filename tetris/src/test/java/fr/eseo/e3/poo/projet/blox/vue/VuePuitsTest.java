package fr.eseo.e3.poo.projet.blox.vue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.Puits;

class VuePuitsTest {
    @Test
    void testConstructeurs() {
        VuePuits vue = new VuePuits(new Puits());
        VuePuits vue2 = new VuePuits(new Puits(), VuePuits.TAILLE_PAR_DEFAUT);
        assertEquals(vue2, vue, "Erreur dans les constructeurs !");
    }

    @Test
    void testGettersSetters() {
        VuePuits vue = new VuePuits(new Puits());
        vue.setTaille(VuePuits.TAILLE_PAR_DEFAUT-1);
        Puits puits = new Puits(6, 21);
        vue.setPuits(puits);
        assertEquals(VuePuits.TAILLE_PAR_DEFAUT-1, vue.getTaille(), "Erreur dans les getters setters !");
        assertEquals(puits, vue.getPuits(), "Erreur dans les getters setters !");
    }
}
