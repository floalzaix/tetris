package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public class PanneauInformation extends JPanel implements PropertyChangeListener {
    // Attributs
    private final Puits puits;
    private VuePiece vuePiece;

    // Constructeurs
    public PanneauInformation(Puits puits) {
        this.puits = puits;

        this.puits.addPropertyChangeListener(this);

        this.setPreferredSize(new Dimension(70, 70));
    }

    // Implemente PropertyChangeListener
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Puits.MODIFICATION_PIECE_SUIVANTE.equals(evt.getPropertyName())) {
            this.vuePiece = new VuePiece((Piece) evt.getNewValue(), 10);
            this.repaint();
        }
    }

    // Overrides
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Outils graphique
        Graphics2D g2d = (Graphics2D) g.create();

        if (this.vuePiece != null) {
            this.vuePiece.afficherPiece(g2d);
        }
    }
}
