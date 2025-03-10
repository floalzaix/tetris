package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

/**
 * Gère l'affichage du puit
 */
public class VuePuits extends JPanel implements PropertyChangeListener {
    // Constantes
    public static final int TAILLE_PAR_DEFAUT = 20;

    // Attributs
    private Puits puits;
    private int taille;
    private VuePiece vuePiece;

    // Constructeurs
    public VuePuits(Puits puits, int taille) {
        this.vuePiece = null;
        this.setPuits(puits);
        this.setTaille(taille);

        this.setBackground(Color.WHITE);
    }

    public VuePuits(Puits puits) {
        this(puits, TAILLE_PAR_DEFAUT);
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
        for (int i = 1; i <= this.puits.getProfondeur() - 1; i++) {
            g2d.drawLine(0, i * this.taille, this.getWidth(), i * this.taille);
        }

        /// Colonnes
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= this.puits.getLargueur() - 1; i++) {
            g2d.drawLine(i * this.taille, 0, i * this.taille, this.getHeight());
        }

        // Piece
        if (this.vuePiece != null) {
            this.vuePiece.afficherPiece(g2d);
        }

        // Libère la mémoire
        g2d.dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Puits.MODIFICATION_PIECE_ACTUELLE.equals(evt.getPropertyName())) {
            this.setVuePiece(new VuePiece((Piece) evt.getNewValue(), this.taille));
        }
    }

    // Getters setter
    public Puits getPuits() {
        return puits;
    }

    public int getTaille() {
        return taille;
    }

    public VuePiece getVuePiece() {
        return vuePiece;
    }

    public final void setPuits(Puits puits) {
        if (this.puits != null) {
            puits.removePropertyChangeListener(this);
        }
        puits.addPropertyChangeListener(this);
        this.puits = puits;
        this.setPreferredSize(new Dimension(this.taille * puits.getLargueur(), this.taille * puits.getProfondeur()));
    }

    public final void setTaille(int taille) {
        this.taille = taille;
        this.setPreferredSize(new Dimension(taille * this.puits.getLargueur(), taille * this.puits.getProfondeur()));
    }

    private void setVuePiece(VuePiece vuePiece) {
        this.vuePiece = vuePiece;
    }
}
