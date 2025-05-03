package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.eseo.e3.poo.projet.blox.controleur.Routeur;
import fr.eseo.e3.poo.projet.blox.modele.Jeu;

public class VueMenu extends JPanel {
    //
    // Variables d'instance
    //
    private Routeur routeur;

    //
    // Constructeurs
    //
    public VueMenu(Routeur routeur) {
        this.routeur = routeur;

        // Layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Background
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(400, 700));

        //
        //  Contenu
        //

        this.add(Box.createVerticalGlue());
        
        // Titre
        JLabel titre = new JLabel("Falling Blox V1");
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setFont(new Font("sans-serif", Font.BOLD, 24));
        this.add(titre);

        this.add(Box.createRigidArea(new Dimension(0, 35)));
        
        // Boutons
        JButton nouvellePartie = new JButton("Nouvelle Partie");
        nouvellePartie.setAlignmentX(Component.CENTER_ALIGNMENT);
        nouvellePartie.addActionListener(_ -> {
            Jeu jeu = new Jeu();
            routeur.ajouterRoute(new VueJeu(jeu), "JEU");
            routeur.router("JEU");
        });
        this.add(nouvellePartie);

        this.add(Box.createVerticalGlue());
    }
}
