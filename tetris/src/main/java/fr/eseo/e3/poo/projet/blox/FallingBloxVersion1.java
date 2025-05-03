package fr.eseo.e3.poo.projet.blox;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import fr.eseo.e3.poo.projet.blox.controleur.Router;
import fr.eseo.e3.poo.projet.blox.modele.Jeu;
import fr.eseo.e3.poo.projet.blox.vue.VueJeu;

public class FallingBloxVersion1 {
    public static void main(String[] args) {
        // Fenêtre
        JFrame frame = new JFrame("Falling Blox");

        // Jeu
        Jeu jeu = new Jeu();

        // Router
        Router router = new Router(frame);
        router.ajouterRoute(new VueJeu(jeu), "JEU");

        // Parametrage de la fenêtre
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); 
    }
}