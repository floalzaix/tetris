package fr.eseo.e3.poo.projet.blox;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import fr.eseo.e3.poo.projet.blox.modele.Jeu;
import fr.eseo.e3.poo.projet.blox.vue.VueJeu;

public class FallingBloxVersion1 {
    public static void main(String[] args) {
        // Fenêtre
        JFrame frame = new JFrame("Falling Blox");

        // Layout
        CardLayout layout = new CardLayout();
        frame.setLayout(layout);

        // Panels
        JPanel panels = new JPanel(layout);
        Jeu jeu = new Jeu();
        panels.add(new VueJeu(jeu), "JEU");

        // Parametrage de la fenêtre
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(panels);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); 
    }
}