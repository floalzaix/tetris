package fr.eseo.e3.poo.projet.blox.vue;

import javax.swing.JFrame;

import fr.eseo.e3.poo.projet.blox.modele.Puits;

class VuePuitsAffichageTest {
    /**
     * Test affichage pour le puits
     */
    private static void testConstructeurPuits() {
        JFrame frame = new JFrame("Puits");
        Puits puits = new Puits();
        VuePuits vue = new VuePuits(puits);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(vue);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private static void testConstructeurPuitsTaille() {
        JFrame frame = new JFrame("Puits");
        Puits puits = new Puits();
        VuePuits vue = new VuePuits(puits, 40);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(vue);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        testConstructeurPuits();
        testConstructeurPuitsTaille();
    }
}
