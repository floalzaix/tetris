package fr.eseo.e3.poo.projet.blox.controleur;

import javax.swing.JFrame;

import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.UsineDePiece;
import fr.eseo.e3.poo.projet.blox.vue.VuePuits;

class PieceRotationTest {
    private static void testRotation() {
        JFrame frame = new JFrame("Puits");
        Puits puits = new Puits();
        VuePuits vue = new VuePuits(puits, 40);
        UsineDePiece.setMode(1);
        puits.setPieceSuivante(UsineDePiece.genererTetromino());
        puits.setPieceSuivante(UsineDePiece.genererTetromino());

        puits.getPieceActuelle().setPosition(5, 5);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(vue);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        testRotation();
    }
}
