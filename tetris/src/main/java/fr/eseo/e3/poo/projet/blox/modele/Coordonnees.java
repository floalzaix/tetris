package fr.eseo.e3.poo.projet.blox.modele;

import java.util.Objects;

public class Coordonnees implements Copiable {
    // Attributs
    private int abscisse;
    private int ordonnee;

    // Constructeurs
    public Coordonnees(int abscisse, int ordonnee) {
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
    }

    // Overrides
    @Override
    public Object copy() {
        return new Coordonnees(this.abscisse, this.ordonnee);
    }

    @Override
    public String toString() {
        return "Coordonnees(" + this.abscisse + ", " + this.ordonnee + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.abscisse, this.ordonnee);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coordonnees other = (Coordonnees) obj;
        if (abscisse != other.abscisse)
            return false;
        return ordonnee == other.ordonnee;
    }

    // Getters setters
    public int getAbscisse() {
        return abscisse;
    }

    public void setAbscisse(int abscisse) {
        this.abscisse = abscisse;
    }

    public int getOrdonnee() {
        return ordonnee;
    }

    public void setOrdonnee(int ordonnee) {
        this.ordonnee = ordonnee;
    }

}
