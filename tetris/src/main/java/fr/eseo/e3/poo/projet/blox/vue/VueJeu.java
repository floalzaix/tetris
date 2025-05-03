package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import fr.eseo.e3.poo.projet.blox.controleur.Gravite;
import fr.eseo.e3.poo.projet.blox.modele.Jeu;

public class VueJeu extends JPanel {
    //
    //  Params
    //

    public static final int SIZE = 30;
    public static final int SIZE_INFO = 10;

    //
    //  Variables d'instance
    //

    private final Jeu jeu;

    // Vues
    private final VuePuits vuePuits;
    private final PanneauInformation pi;
    

    //
    //  Constructeurs
    //
    public VueJeu(Jeu jeu) {
        this.jeu = jeu;

        // Création panneau du puits
        this.vuePuits = new VuePuits(jeu.getPuits(), 30);

        // Création de la panneau d'information
        this.pi = new PanneauInformation(jeu.getPuits(), SIZE_INFO);

        // Layout
        this.setLayout(new BorderLayout());

        this.add(vuePuits, BorderLayout.CENTER);
        this.add(pi, BorderLayout.EAST);

        // Gravite
        Gravite _ = new Gravite(this.vuePuits);
    }
}
