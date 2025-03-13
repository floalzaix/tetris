package fr.eseo.e3.poo.projet.blox.controleur;

import javax.swing.JFrame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.UsineDePiece;
import fr.eseo.e3.poo.projet.blox.vue.VuePuits;

class GraviteTest {
    @Test
    void testConstructeur() {
        Gravite grav = new Gravite(new VuePuits(new Puits()));
    }

    @Test 
    void testGetterSetterPeriodicite() {
        Gravite grav = new Gravite(new VuePuits(new Puits()));
        grav.setPeriodicite(15020);
        assertEquals(15020, grav.getPeriodicite(), "Erreur dans getPeriodicite ou setPeriodicite !");
    }

    private static void testGravite() {
        JFrame frame = new JFrame("Puits");
        Puits puits = new Puits(10, 20);
        VuePuits vue = new VuePuits(puits, 70);
        UsineDePiece.setMode(1);
        puits.setPieceSuivante(UsineDePiece.genererTetromino());
        puits.setPieceSuivante(UsineDePiece.genererTetromino());

        puits.getPieceActuelle().setPosition(5, 5);

        new Gravite(vue);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(vue);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        testGravite();
    }
}
