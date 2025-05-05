package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import fr.eseo.e3.poo.projet.blox.controleur.Client;
import fr.eseo.e3.poo.projet.blox.controleur.Lobby;
import fr.eseo.e3.poo.projet.blox.controleur.Routeur;
import fr.eseo.e3.poo.projet.blox.modele.UsineDePiece;

public class VueLobby extends JPanel {
    //
    //  Variables d'instance
    //
    private Routeur routeur;

    //
    //  Constructeur
    //
    public VueLobby(Routeur routeur) {
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
        JLabel titre = new JLabel("Nouveau Lobby");
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setFont(new Font("sans-serif", Font.BOLD, 20));
        this.add(titre);

        this.add(Box.createRigidArea(new Dimension(0, 35)));
        
        // Largueur
        JSlider largueur = this.ajouterSlider("Largueur du puits", 5, 15, 10, 1, 1);

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        // Profondeur
        JSlider profondeur = this.ajouterSlider("Profondeur du puits", 15, 25, 20, 1, 1);

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        // Niveau
        JSlider niveau = this.ajouterSlider("Niveau", 0, 50, 0, 10, 1);

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        // Modes
        HashMap<String, Integer> options = new HashMap<>();
        options.put("Aléatoire complet", UsineDePiece.ALEATOIRE_COMPLET);
        options.put("Aléatoire pièce", UsineDePiece.ALEATOIRE_PIECE);
        options.put("Cyclique", UsineDePiece.CYCLIC);
        JComboBox<String> modes = new JComboBox<>(options.keySet().toArray(String[]::new));
        modes.setBackground(Color.WHITE);
        modes.setMaximumSize(new Dimension((int) this.getPreferredSize().getWidth(), 65));
        modes.setSelectedItem("Aléatoire pièce");
        this.add(modes);

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        // Port
        JSpinner port = new JSpinner(new SpinnerNumberModel(4000, 0, 65535, 1));
        port.setAlignmentX(Component.CENTER_ALIGNMENT);
        port.setBackground(Color.WHITE);
        port.setMaximumSize(new Dimension((int) this.getPreferredSize().getWidth(), 100));
        port.setBorder(BorderFactory.createTitledBorder("Port"));
        this.add(port);

        this.add(Box.createRigidArea(new Dimension(0, 15)));

        // Confirmation
        JButton conf = new JButton("Confirmation");
        conf.setAlignmentX(Component.CENTER_ALIGNMENT);
        conf.setBackground(Color.WHITE);
        conf.addActionListener(_ -> {
            Lobby lobby = new Lobby(largueur.getValue(), profondeur.getValue(), niveau.getValue(), options.get(String.valueOf(modes.getSelectedItem())));
            lobby.start();

            String host = lobby.getAddress().getHostName();
            try {
                URI uri = new URI("ws", null, host, (int) port.getValue(), null, null, null);

                Client client = new Client(uri);
                client.connect();

                client.getLatch().await();

                this.routeur.ajouterRoute(new VueJoueurs(routeur, client.getJoueur(), client, true), "JOUEURS");
                this.routeur.router("JOUEURS");
            } catch (URISyntaxException e) {
                System.out.println("A ENLEVER DANS la VUELOBBY : " + e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("A ENLEVER DANS la VUELOBBY : " + e.getMessage());
            }
        });
        this.add(conf);

        this.add(Box.createVerticalGlue());
    }

    //
    //  Méthode d'affichage
    //

    /**
     * Ajoute un slider à la fenêtre
     */
    private JSlider ajouterSlider(String name, int min, int max, int defaut, int bigSteps, int smallSteps) {
        JSlider slider = new JSlider(min, max, defaut);

        slider.setBorder(BorderFactory.createTitledBorder(name));
        slider.setMajorTickSpacing(bigSteps);
        slider.setMinorTickSpacing(smallSteps);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBackground(Color.WHITE);

        this.add(slider);

        return slider;
    }
}
