package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import fr.eseo.e3.poo.projet.blox.modele.Puits;

/**
 * Gère l'affichage du puit
 */
public class VuePuits extends JPanel {
    // Constantes
    public static final int TAILLE_PAR_DEFAUT = 20;

    // Attributs
    private Puits puits;
    private int taille;

    // Constructeurs
    public VuePuits(Puits puits) {
        this.puits = puits;
        this.setTaille(TAILLE_PAR_DEFAUT);

        this.setBackground(Color.WHITE);
    }

    public VuePuits(Puits puits, int taille) {
        this.puits = puits;
        this.setTaille(taille);

        this.setBackground(Color.WHITE);
    }

    // Overrides
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Graphics2D
        Graphics2D g2d = (Graphics2D) g.create();

        // Grille
        /// Lignes
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= this.puits.getProfondeur()-1; i++) {
            g2d.drawLine(0, i*this.taille, this.getWidth(), i*this.taille);
        }

        ///Colonnes
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= this.puits.getLargueur()-1; i++) {
            g2d.drawLine(i*this.taille, 0, i*this.taille, this.getHeight());
        }

        // Libère la mémoire
        g2d.dispose();
    }

    // Getters setter
    public Puits getPuits() {
        return puits;
    }

    public int getTaille() {
        return taille;
    }

    public void setPuits(Puits puits) {
        this.puits = puits;
        this.setPreferredSize(new Dimension(this.taille*puits.getLargueur(), this.taille*puits.getProfondeur()));
    }

    public final void setTaille(int taille) {
        this.taille = taille;
        this.setPreferredSize(new Dimension(taille*this.puits.getLargueur(), taille*this.puits.getProfondeur()));
    }
}
