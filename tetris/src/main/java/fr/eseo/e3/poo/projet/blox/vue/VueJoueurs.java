package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.InetSocketAddress;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.eseo.e3.poo.projet.blox.controleur.Client;
import fr.eseo.e3.poo.projet.blox.controleur.Routeur;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Jeu;
import fr.eseo.e3.poo.projet.blox.modele.Joueur;

public class VueJoueurs extends JPanel implements PropertyChangeListener {
    //
    // Variables d'instance
    //
    private final Routeur routeur;
    private final Joueur joueur;
    private final Client client;
    private final boolean admin;

    //
    // Constructeurs
    //
    public VueJoueurs(Routeur routeur, Joueur joueur, Client client, boolean admin) {
        this.routeur = routeur;
        this.joueur = joueur;
        this.client = client;
        this.admin = admin;

        // Layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Background
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(400, 700));

        // Border
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //
        // Contenu
        //

        this.afficherContenu();

        // Enregistrement pour la création du jeu et l'ajout de joueurs
        this.joueur.addPropertyChangeListener(this);
        this.client.addPropertyChangeListener(this);
    }

    //
    //  Méthodes
    //

    /**
     * Affiche le contenu de la page
     */
    private void afficherContenu() {
        this.removeAll();

        this.add(Box.createVerticalGlue());

        // Titre
        InetSocketAddress add = this.client.getRemoteSocketAddress();
        String host = add.getAddress().getHostAddress();
        int port = add.getPort();
        JLabel titre = new JLabel("Lobby : " + "ws://" + host + ":" + port);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setFont(new Font("sans-serif", Font.BOLD, 20));
        this.add(titre);

        this.add(Box.createRigidArea(new Dimension(0, 35)));

        // Liste des joueurs
        JPanel j = new JPanel();
        j.setBackground(this.joueur.getCouleur().getCouleurPourAffichage());
        j.setMaximumSize(new Dimension((int) this.getPreferredSize().getWidth(), 100));
        this.add(j);
        this.add(Box.createRigidArea(new Dimension(0, 15)));
        for (Couleur couleur : this.joueur.getAutresJoueurs()) {
            j = new JPanel();
            j.setBackground(couleur.getCouleurPourAffichage());
            j.setMaximumSize(new Dimension((int) this.getPreferredSize().getWidth(), 100));
            this.add(j);
            this.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        // Bouton démarrer si admin
        if (admin) {
            JButton start = new JButton("Start");
            start.setAlignmentX(Component.CENTER_ALIGNMENT);
            start.setBackground(Color.WHITE);
            start.addActionListener(_ -> {
                client.startJeu();
            });
            this.add(start);
        }

        this.add(Box.createVerticalGlue());
    }

    //
    // Overrides
    //

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Joueur.EVT_JEU_CREER.equals(evt.getPropertyName())) {
            Jeu jeu = (Jeu) evt.getNewValue();
            this.routeur.ajouterRoute(new VueJeu(routeur, jeu, false), "JEU");
            this.routeur.router("JEU");
        }
        if (Client.EVT_NOUVEAU_JOUEUR.equals(evt.getPropertyName())) {
            this.afficherContenu();
            this.revalidate();
            this.repaint();
        }
    }
}
