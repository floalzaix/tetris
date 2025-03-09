package fr.eseo.e3.poo.projet.blox.vue;

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
        this.taille = TAILLE_PAR_DEFAUT;
    }

    public VuePuits(Puits puits, int taille) {
        this.puits = puits;
        this.taille = taille;
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
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }
}
