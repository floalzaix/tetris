package fr.eseo.e3.poo.projet.blox.modele;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class Joueur {
    //
    //  Constantes de classe
    //
    public static final String EVT_JEU_CREER = "JEU";

    //
    // Variables d'instance
    //
    private Color couleur;
    private final List<Color> autresJoueurs;

    private Jeu jeu;

    private PropertyChangeSupport pcs;

    //
    // Constructeurs
    //
    public Joueur(Color couleur) {
        this.couleur = couleur;
        this.autresJoueurs = new ArrayList<>();

        this.pcs = new PropertyChangeSupport(this);
    }

    // TODO : paramtres de la partie ...

    //
    // Méthodes
    //

    /**
     * Créer un nouveau jeu pour le joueur. Parametres de base de jeu si dessous :
     * 
     * @param largeur
     * @param profondeur
     * @param niveau
     * @param mode
     */
    public void creationJeu(int largeur, int profondeur, int niveau, int mode) {
        this.jeu = new Jeu(largeur, profondeur, niveau, mode);
        this.pcs.firePropertyChange(EVT_JEU_CREER, null, this.jeu);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    // Getters setters

    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    public Jeu getJeu() {
        return jeu;
    }

    public List<Color> getAutresJoueurs() {
        return autresJoueurs;
    }
}
