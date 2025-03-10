package fr.eseo.e3.poo.projet.blox.controleur;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;
import fr.eseo.e3.poo.projet.blox.vue.VuePuits;

public class PieceDeplacement extends MouseAdapter {
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

    // Listeners
    @Override
    public void mouseMoved(MouseEvent e) {
        Piece piece = this.puits.getPieceActuelle();
        if (piece != null) {
            int nouvelleColonne = this.getColonneDuPointeur(e.getX());
            if (nouvelleColonne != this.colonne && nouvelleColonne != -1) {
                int delta = nouvelleColonne - this.colonne;
                int mouvement = delta / Math.abs(delta);
                piece.deplacerDe(mouvement, 0);
                if (estDehors(piece, this.puits)) {
                    piece.deplacerDe(-mouvement, 0);
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
            if (e.getWheelRotation() < 0) {
                piece.deplacerDe(0, 1);
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

    /**
     * Test si une pièce est en dehors du puits. Cette fonction est faite pour être
     * utiliser aprés le mouvement pour vérifier sa validité
     * 
     * @param piece La pièce que l'on veut vérifié 
     * @param puits Le puits de la pièce
     * @return  Vrai si la pièce dépasse du puits (Donc si un (ou plus) élement(s) est(sont) dehors)
     */
    public static boolean estDehors(Piece piece, Puits puits) {
        for (Element elt : piece.getElements()) {
            Coordonnees coord = elt.getCoord();
            if (coord.getAbscisse() < 0 || coord.getAbscisse() > puits.getLargueur() - 1 || coord.getOrdonnee() < 0
                    || coord.getOrdonnee() > puits.getProfondeur() - 1) {
                return true;
            }
        }
        return false;
    }
}
