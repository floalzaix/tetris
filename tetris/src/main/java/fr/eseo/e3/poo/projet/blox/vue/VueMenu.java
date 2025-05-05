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

        // Nouvelle partie
        JButton np = new JButton("Nouvelle Partie");
        np.setAlignmentX(Component.CENTER_ALIGNMENT);
        np.setBackground(Color.WHITE);
        np.addActionListener(_ -> {
            this.routeur.router("CONFIG");
        });
        this.add(np);

        // Nouvelle partie
        JButton co = new JButton("Connexion");
        co.setAlignmentX(Component.CENTER_ALIGNMENT);
        co.setBackground(Color.WHITE);
        co.addActionListener(_ -> {
            this.routeur.router("CONNEXION");
        });
        this.add(co);

        // Nouvelle partie
        JButton nl = new JButton("Nouveau Lobby");
        nl.setAlignmentX(Component.CENTER_ALIGNMENT);
        nl.setBackground(Color.WHITE);
        nl.addActionListener(_ -> {
            this.routeur.router("LOBBY");
        });
        this.add(nl);

        this.add(Box.createVerticalGlue());
    }
}
