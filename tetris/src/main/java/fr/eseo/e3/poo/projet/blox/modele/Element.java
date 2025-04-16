package fr.eseo.e3.poo.projet.blox.modele;

import java.util.Objects;

public class Element {
    // Attributs
    private Coordonnees coord;
    private Couleur couleur;

    // Constructeurs : dépendent tous du premier
    public Element(Coordonnees coord, Couleur couleur) {
        this.coord = coord;
        this.couleur = couleur;
    }

    public Element(int abscisse, int ordonnee, Couleur couleur) {
        this(new Coordonnees(abscisse, ordonnee), couleur);
    }

    public Element(int abscisse, int ordonnee) {
        this(new Coordonnees(abscisse, ordonnee), Couleur.values()[0]);
    }

    public Element(Coordonnees coord) {
        this(coord, Couleur.values()[0]);
    }

    /**
     * Cette fonction permet de déplacer un élement d'un vecteur delta
     * 
     * @param deltaX Coordonnée X du vecteur delta
     * @param deltaY Coordonnée Y du vecteur delta
     * @throws IllegalArgumentException Si le déplacement est > 1 ou si déplacement
     *                                  vers le haut
     */
    public void deplacerDe(int deltaX, int deltaY) throws IllegalArgumentException {
        if (deltaX > 1 || deltaY > 1 || deltaX < -1 || deltaY < 0) {
            throw new IllegalArgumentException(
                    "Erreur le déplacement d'un pièce ne peut pas être supérieur à 1 ou ne peut pas aller vers le haut !");
        }
        this.coord.setAbscisse(this.coord.getAbscisse() + deltaX);
        this.coord.setOrdonnee(this.coord.getOrdonnee() + deltaY);
    }

    // Overrides
    @Override
    public int hashCode() {
        return Objects.hash(this.coord, this.couleur);
    }

    @Override
    public String toString() {
        return "Element(" + this.coord.getAbscisse() + ", " + this.coord.getOrdonnee() + ") - " + this.couleur;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true; // même référence
        if (obj == null || getClass() != obj.getClass())
            return false;

        Element other = (Element) obj;
        return Objects.equals(coord, other.coord) &&
                Objects.equals(couleur, other.couleur);
    }

    // Getters setters
    public Coordonnees getCoord() {
        return coord;
    }

    public void setCoord(Coordonnees coord) {
        this.coord = coord;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public void setCouleur(Couleur couleur) {
        this.couleur = couleur;
    }
}
