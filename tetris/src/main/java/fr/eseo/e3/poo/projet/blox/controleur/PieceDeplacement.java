package fr.eseo.e3.poo.projet.blox.controleur;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.vue.VuePuits;

public class PieceDeplacement implements MouseMotionListener {
    // Attributs
    private Puits puits;
    private VuePuits vuePuits;
    private int colonne; // Variable qui stocke la colonne du curseur quand la méthode mouMoved à fini le
                         // traitement

    // Constructeurs
    public PieceDeplacement(VuePuits vuePuits) {
        this.vuePuits = vuePuits;
        this.puits = vuePuits.getPuits();
        this.colonne = -1;
    }

    // Implémentation de MousMotionListener
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (this.puits.getPieceActuelle() != null) {
            int nouvelleColonne = this.getColonneDuPointeur(e.getX());
            if (nouvelleColonne != this.colonne && nouvelleColonne != -1) {
                int delta = nouvelleColonne - this.colonne;
                this.puits.getPieceActuelle().deplacerDe(delta/Math.abs(delta), 0);
                this.colonne = nouvelleColonne;
                this.vuePuits.repaint();
            } else if (nouvelleColonne == -1) {
                this.colonne = -1;
            }
        }
    }

    /**
     * Permet de récupérer la colonne du pointeur de la souris associé au puits
     * 
     * @param x La position x de la souris sur la fenêtre
     * @return  La colonne du pointeur de la souris 
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
