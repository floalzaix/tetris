package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.eseo.e3.poo.projet.blox.controleur.Routeur;

public class VueConnexion extends JPanel {
    //
    //   Variables d'instance
    //
    private Routeur routeur;

    //
    //  Constructeurs
    //
    public VueConnexion(Routeur routeur) {
        this.routeur = routeur;

        // Layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Background
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(400, 700));

        // Border
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //
        //  Contenu
        //

        this.add(Box.createVerticalGlue());

        // Titre
        JLabel titre = new JLabel("Connexion");
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setFont(new Font("sans-serif", Font.BOLD, 20));
        this.add(titre);

        this.add(Box.createRigidArea(new Dimension(0, 35)));

        // Uri
        JTextField uri = new JTextField();
        uri.setText("Saisissez votre uri");
        uri.setMaximumSize(new Dimension((int) this.getPreferredSize().getWidth(), 50));
        this.add(uri);

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        // Confirmation
        JButton conf = new JButton("Confirmation");
        conf.setAlignmentX(Component.CENTER_ALIGNMENT);
        conf.setBackground(Color.WHITE);
        conf.addActionListener(_ -> {
            
        });
        this.add(conf);

        this.add(Box.createVerticalGlue());
    }
}
