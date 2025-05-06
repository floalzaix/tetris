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
import fr.eseo.e3.poo.projet.blox.modele.Puits;

public class VueFinJeu extends JPanel {
    //
    //  Variables d'instance
    //
    private final Jeu jeu;
    private final Puits puits;

    //
    //  Constructeurs
    //
    public VueFinJeu(Routeur routeur, Jeu jeu) {
        this.jeu = jeu;
        this.puits = jeu.getPuits();

        // Layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Background
        Color fond = new Color(0, 0, 0, 200);
        this.setBackground(fond);

        this.add(Box.createVerticalGlue());

        // Titre
        JLabel titre = new JLabel("Fin de partie");
        titre.setFont(new Font("sans-serif", Font.BOLD, 20));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setForeground(Color.WHITE);
        this.add(titre);
        
        // Score
        JLabel score = new JLabel("Score : " + this.puits.getScore() + " pts");
        score.setFont(new Font("sans-serif", Font.ITALIC, 19));
        score.setAlignmentX(Component.CENTER_ALIGNMENT);
        score.setForeground(Color.WHITE);
        this.add(score);
        // Score
        JLabel place = new JLabel("Place : " + this.jeu.getPlace());
        place.setFont(new Font("sans-serif", Font.ITALIC, 19));
        place.setAlignmentX(Component.CENTER_ALIGNMENT);
        place.setForeground(Color.WHITE);
        this.add(place);

        this.add(Box.createRigidArea(new Dimension(0, 25)));

        // Bouton retour menu
        JButton menu = new JButton("Menu");
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.setBackground(Color.WHITE);
        menu.addActionListener(_ -> {
            routeur.router("MENU");
        });
        this.add(menu);

        this.add(Box.createVerticalGlue());
    }
}
