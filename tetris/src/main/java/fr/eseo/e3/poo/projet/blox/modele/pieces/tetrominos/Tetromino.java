package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import java.util.List;

import fr.eseo.e3.poo.projet.blox.modele.BloxException;
import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public abstract class Tetromino implements Piece {
    // Attributs
    protected Element[] elements;
    protected Couleur couleur;

    private Puits puits;

    // Constructeurs
    protected Tetromino(Coordonnees coord, Couleur couleur) {
        this.couleur = couleur;

        this.setElements(coord, couleur);
    }

    // Méthodes abstraites
    protected abstract void setElements(Coordonnees coord, Couleur couleur);

    // Interface Piece
    @Override
    public List<Element> getElements() {
        return List.of(this.elements);
    }

    @Override
    public void setPosition(int abscisse, int ordonnee) {
        // Calcul le delta de position pour l'appliquer à chaque élements
        Coordonnees coord = this.elements[0].getCoord();
        Coordonnees delta = new Coordonnees(coord.getAbscisse() - abscisse, coord.getOrdonnee() - ordonnee);

        // Application du delta de position à chaque élement
        for (Element elt : this.elements) {
            Coordonnees eltCoord = elt.getCoord();
            elt.setCoord(new Coordonnees(eltCoord.getAbscisse() - delta.getAbscisse(),
                    eltCoord.getOrdonnee() - delta.getOrdonnee()));
        }
    }

    @Override
    public void deplacerDe(int deltaX, int deltaY) throws IllegalArgumentException, BloxException {
        // Sauvegarde de la position avant le deplacement
        Coordonnees coord = this.getElements().get(0).getCoord();
        int oldX = coord.getAbscisse();
        int oldY = coord.getOrdonnee();

        // Deplacement
        for (Element element : this.elements) {
            element.deplacerDe(deltaX, deltaY);
        }

        // Si dépassement ou colision alors on revient en arrière
        try {
            Piece.estDehorsOuCollision(this, this.puits);
        } catch (BloxException be) {
            this.setPosition(oldX, oldY);
            throw be;
        }
    }

    @Override
    public void tourner(boolean sensHoraire) throws BloxException {
        // Translater vers l'origine en conservant les coordonnée
        Coordonnees coord = this.elements[0].getCoord();
        this.setPosition(0, 0);

        /// Rotation en tenant compte des coordonnée
        /// informatique avec multiplication par une matrice de rotation
        this.rotation(sensHoraire);

        // Translation inverse
        this.setPosition(coord.getAbscisse(), coord.getOrdonnee());

        /// Si après rotation, pièce non valide alors on la retourne dans l'autre sens
        try {
            Piece.estDehorsOuCollision(this, this.puits);
        } catch (BloxException be) {
            this.rotation(!sensHoraire);
            throw be;
        }
    }

    // Fonctions perso
    /**
     * Réalise de manière brut la rotation sans test. Son but de permettre de contourner la récursivité
     * dans la fonction tourner et ainsi éviter que la méthode s'appelle de manière infinie
     * 
     * @param sensHoraire Le sens de la rotation pareil que tourner
     */
    private void rotation(boolean sensHoraire) {
        for (Element elt : this.elements) {
            Coordonnees eltCoord = elt.getCoord();
            elt.setCoord(new Coordonnees((sensHoraire ? -1 : 1) * eltCoord.getOrdonnee(),
                    (sensHoraire ? 1 : -1) * eltCoord.getAbscisse()));
        }
    }

    // Overrides
    @Override
    public String toString() {
        String res = this.getClass().getSimpleName() + " :\n";
        for (Element element : elements) {
            int x = element.getCoord().getAbscisse();
            int y = element.getCoord().getOrdonnee();
            res += "\t(" + x + ", " + y + ") - " + this.couleur + "\n";
        }
        return res;
    }

    @Override
    public Puits getPuits() {
        return puits;
    }

    @Override
    public void setPuits(Puits puits) {
        this.puits = puits;
    }
}
