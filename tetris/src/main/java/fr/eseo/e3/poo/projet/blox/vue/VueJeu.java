package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import fr.eseo.e3.poo.projet.blox.controleur.Gravite;
import fr.eseo.e3.poo.projet.blox.controleur.Routeur;
import fr.eseo.e3.poo.projet.blox.modele.Jeu;
import fr.eseo.e3.poo.projet.blox.modele.Puits;

public class VueJeu extends JLayeredPane implements PropertyChangeListener {
    //
    //  Params
    //

    public static final int SIZE = 30;
    public static final int SIZE_INFO = 10;

    //
    //  Variables d'instance
    //

    private final Routeur routeur;
    private final Jeu jeu;

    // Vues
    private final VuePuits vuePuits;
    private final PanneauInformation pi;
    

    //
    //  Constructeurs
    //
    public VueJeu(Routeur routeur, Jeu jeu) {
        this.routeur = routeur;
        this.jeu = jeu;

        // Création panneau du puits
        this.vuePuits = new VuePuits(jeu.getPuits(), SIZE);

        // Création de la panneau d'information
        this.pi = new PanneauInformation(jeu.getPuits(), SIZE_INFO);

        // Layout
        JPanel panelJeu = new JPanel(new BorderLayout());
        panelJeu.add(this.vuePuits, BorderLayout.CENTER);
        panelJeu.add(this.pi, BorderLayout.EAST);

        Dimension size = panelJeu.getPreferredSize();
        panelJeu.setBounds(0, 0, (int) size.getWidth(), (int) size.getHeight());

        this.add(panelJeu, JLayeredPane.DEFAULT_LAYER);

        // Tailles
        this.setPreferredSize(size);

        // Gravite
        Gravite _ = new Gravite(this.vuePuits);

        // Clavier
        SwingUtilities.invokeLater(() -> {
            Timer timer = new Timer(100, null);

            timer.addActionListener(_ -> {
                this.vuePuits.requestFocusInWindow();
                if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this.vuePuits) {
                    timer.stop();
                }
            });

            timer.start();
        });

        // Fin de partie
        this.jeu.getPuits().addPropertyChangeListener(this);
    }

    //
    //   Overrides
    //

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Puits.LIMITE_HAUTEUR_ATTEINTE.equals(evt.getPropertyName())) {
            VueFinJeu fin = new VueFinJeu(this.routeur, this.jeu);

            this.add(fin, JLayeredPane.PALETTE_LAYER);

            fin.setPreferredSize(this.getPreferredSize());

            Dimension size = fin.getPreferredSize();
            fin.setBounds(0, 0, (int) size.getWidth(), (int) size.getHeight());

            this.revalidate();
        } 
    }
}
