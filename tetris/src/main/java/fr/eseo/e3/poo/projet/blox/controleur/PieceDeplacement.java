package fr.eseo.e3.poo.projet.blox.controleur;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import fr.eseo.e3.poo.projet.blox.modele.BloxException;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;
import fr.eseo.e3.poo.projet.blox.vue.VuePuits;

public class PieceDeplacement extends MouseAdapter {
    // Attributs
    private VuePuits vuePuits;
    private Puits puits;
    private int colonne; // Variable qui stocke la colonne du curseur quand la méthode mouMoved à fini le
                         // traitement

    // Constructeurs
    public PieceDeplacement(VuePuits vuePuits) {
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
                    
                }
                this.vuePuits.repaint();
            }
            this.colonne = nouvelleColonne;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Piece piece = this.puits.getPieceActuelle();
        if (piece != null) {
            if (e.getWheelRotation() > 0) {
                try {
                    piece.deplacerDe(0, 1);
                } catch (BloxException be) {
                    
                }
                this.vuePuits.repaint();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.colonne = this.getColonneDuPointeur(e.getX());
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
