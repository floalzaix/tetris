package fr.eseo.e3.poo.projet.blox.modele;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class CouleurTest {
    @Test
    void testGetter() {
        assertEquals(Color.RED, Couleur.ROUGE.getCouleurPourAffichage(), "Erreur dans le getter !");
        assertEquals(Color.ORANGE, Couleur.ORANGE.getCouleurPourAffichage(), "Erreur dans le getter !");
        assertEquals(Color.BLUE, Couleur.BLEU.getCouleurPourAffichage(), "Erreur dans le getter !");
        assertEquals(Color.GREEN, Couleur.VERT.getCouleurPourAffichage(), "Erreur dans le getter !");
        assertEquals(Color.YELLOW, Couleur.JAUNE.getCouleurPourAffichage(), "Erreur dans le getter !");
        assertEquals(Color.CYAN, Couleur.CYAN.getCouleurPourAffichage(), "Erreur dans le getter !");
        assertEquals(Color.MAGENTA, Couleur.VIOLET.getCouleurPourAffichage(), "Erreur dans le getter !");
    }
}
