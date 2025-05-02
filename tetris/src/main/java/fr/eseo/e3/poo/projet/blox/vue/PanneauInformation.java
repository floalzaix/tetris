package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public class PanneauInformation extends JPanel implements PropertyChangeListener {
    // Attributs
    private final int sizeInfo;
    private final Puits puits;
    private VuePiece vuePiece;
    private JLabel score;

    // Constructeurs
    public PanneauInformation(Puits puits, int sizeInfo) {
        this.sizeInfo = sizeInfo;
        this.puits = puits;

        this.puits.addPropertyChangeListener(this);

        this.setPreferredSize(new Dimension(sizeInfo + 50, this.getHeight()));

        // Affichage du score
        this.score = new JLabel("0");
        this.score.setHorizontalAlignment(SwingConstants.CENTER);
        this.score.setVerticalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());

        this.add(this.score, BorderLayout.CENTER);
    }

    // Implemente PropertyChangeListener
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Puits.MODIFICATION_PIECE_SUIVANTE.equals(evt.getPropertyName())) {
            this.vuePiece = new VuePiece((Piece) evt.getNewValue(), this.sizeInfo);
            this.score.setText(String.valueOf(puits.getScore()));
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
