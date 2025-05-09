package fr.eseo.e3.poo.projet.blox.modele;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

public class Joueur {
    //
    // Constantes de classe
    //
    public static final String EVT_JEU_CREER = "JEU";

    //
    // Variables d'instance
    //
    private Couleur couleur;
    private final List<Couleur> autresJoueurs;

    private Jeu jeu;

    private final PropertyChangeSupport pcs;

    //
    // Constructeurs
    //
    public Joueur(Couleur couleur) {
        this.couleur = couleur;
        this.autresJoueurs = new ArrayList<>();

        this.pcs = new PropertyChangeSupport(this);
    }

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

    /**
     * Reprends juste la fonction du puits pour la rendre plus accessible avec les
     * même paramètres.
     * 
     * @param couleur
     * @param nb
     * @param OperationNotSupportedException Si le jeu n'est pas set avant
     */
    public void ajouterLigne(Couleur couleur, int nb) throws OperationNotSupportedException {
        if (this.jeu == null) {
            throw new OperationNotSupportedException("Le jeu n'est pas encore créer !");
        }
        this.jeu.getPuits().getTas().ajouterLignes(couleur, nb);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    // Getters setters

    public Couleur getCouleur() {
        return couleur;
    }

    public void setCouleur(Couleur couleur) {
        this.couleur = couleur;
    }

    public Jeu getJeu() {
        return jeu;
    }

    public List<Couleur> getAutresJoueurs() {
        return autresJoueurs;
    }
}
