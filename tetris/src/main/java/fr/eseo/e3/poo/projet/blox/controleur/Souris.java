package fr.eseo.e3.poo.projet.blox.controleur;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

import fr.eseo.e3.poo.projet.blox.modele.BloxException;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;
import fr.eseo.e3.poo.projet.blox.vue.VuePuits;

public class Souris extends MouseAdapter {
    // Attributs
    private final VuePuits vuePuits;
    private final Puits puits;
    private int colonne; // Variable qui stocke la colonne du curseur quand la méthode mouMoved à fini le
                         // traitement

    // Constructeurs
    public Souris(VuePuits vuePuits) {
        this.vuePuits = vuePuits;
        this.puits = vuePuits.getPuits();
        this.colonne = -1;
    }

    // Listeners
    @Override
    public void mouseMoved(MouseEvent e) {
        Piece piece = this.puits.getPieceActuelle();
        if (piece != null) {
            int nouvelleColonne = this.getColonneDuPointeur(e.getX());
            if (nouvelleColonne != this.colonne && nouvelleColonne != -1) {
                int delta = nouvelleColonne - this.colonne;
                int mouvement = delta / Math.abs(delta);
                try {
                    piece.deplacerDe(mouvement, 0);
                } catch (BloxException be) {
                    // Exception ignorée car la collision est géré par la gravité du puits
                }
                this.vuePuits.repaint();
            }
            this.colonne = nouvelleColonne;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Piece piece = this.puits.getPieceActuelle();
        if (piece != null && e.getWheelRotation() > 0) {
                try {
                    piece.deplacerDe(0, 1);
                } catch (BloxException be) {
                    // Exception ignorée car la collision est géré par la gravité du puits
                }
                this.vuePuits.repaint();
            }
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.colonne = this.getColonneDuPointeur(e.getX());
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
                    // Exception ignorée car la collision est géré par la gravité du puits
                }
                this.vuePuits.repaint();
            }
        }
    }

    // Fonctions perso

    /**
     * Permet de récupérer la colonne du pointeur de la souris associé au puits
     * 
     * @param x La position x de la souris sur la fenêtre
     * @return La colonne du pointeur de la souris
     */
    private int getColonneDuPointeur(int x) {
        for (int i = 0; i < this.puits.getLargueur(); i++) {
            if (x >= i * this.vuePuits.getTaille() && x < (i + 1) * this.vuePuits.getTaille()) {
                return i;
            }
        }
        return -1;
    }
}
