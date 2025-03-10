package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Color;
import java.awt.Dimension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.Puits;

class VuePuitsTest {
    @Test
    void testConstructeurs() {
        VuePuits vue = new VuePuits(new Puits(), VuePuits.TAILLE_PAR_DEFAUT-1);
        assertEquals(VuePuits.TAILLE_PAR_DEFAUT-1, vue.getTaille(), "Erreur dans les constructeurs !");
        assertEquals(Color.WHITE, vue.getBackground(), "Erreur dans le constructeur !");
        VuePuits vue2 = new VuePuits(new Puits());
        assertEquals(VuePuits.TAILLE_PAR_DEFAUT, vue2.getTaille(), "Erreur dans les constructeurs !");
        assertEquals(Color.WHITE, vue.getBackground(), "Erreur dans le constructeur !");
    }

    @Test
    void testGettersSetters() {
        VuePuits vue = new VuePuits(new Puits());
        vue.setTaille(VuePuits.TAILLE_PAR_DEFAUT-1);
        Puits puits = new Puits(6, 21);
        vue.setPuits(puits);
        assertEquals(VuePuits.TAILLE_PAR_DEFAUT-1, vue.getTaille(), "Erreur dans les getters setters !");
        assertEquals(puits, vue.getPuits(), "Erreur dans les getters setters !");
        assertEquals(new Dimension((VuePuits.TAILLE_PAR_DEFAUT-1)*6, (VuePuits.TAILLE_PAR_DEFAUT-1)*21), vue.getPreferredSize(), "Erreur dans les getters et setters !");
    }
}
