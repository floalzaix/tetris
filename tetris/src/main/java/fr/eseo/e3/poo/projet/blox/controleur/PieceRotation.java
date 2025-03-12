package fr.eseo.e3.poo.projet.blox.controleur;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import fr.eseo.e3.poo.projet.blox.modele.BloxException;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;
import fr.eseo.e3.poo.projet.blox.vue.VuePuits;

public class PieceRotation extends MouseAdapter {
    // Attributs 
    private VuePuits vuePuits;
    private Puits puits;

    // Constructeurs
    public PieceRotation(VuePuits vuePuits) {
        this.vuePuits = vuePuits;
        this.puits = vuePuits.getPuits();
    }

    // Gestion de la rotation
    @Override
    public void mouseClicked(MouseEvent e) {
        Piece piece = this.puits.getPieceActuelle();
        if (piece != null) {
            boolean left = SwingUtilities.isLeftMouseButton(e);
            boolean right = SwingUtilities.isRightMouseButton(e);
            if (left || right) {
                try {
                    piece.tourner(right);
                } catch (BloxException be) {
                    System.out.println(be.getMessage());
                }
                this.vuePuits.repaint();
            }
        }
    }
}
