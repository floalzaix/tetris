package fr.eseo.e3.poo.projet.blox;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import fr.eseo.e3.poo.projet.blox.controleur.Routeur;
import fr.eseo.e3.poo.projet.blox.vue.VueMenu;

public class FallingBloxVersion1 {
    public static void main(String[] args) {
        // Fenêtre
        JFrame frame = new JFrame("Falling Blox V1");

        // Routeur
        Routeur routeur = new Routeur(frame);
        routeur.ajouterRoute(new VueMenu(routeur), "MENU");

        // Parametrage de la fenêtre
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); 
    }
}