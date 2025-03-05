package fr.eseo.e3.poo.projet.blox.modele;

public class Element {
    // Attributs
    private Coordonnees coord;
    private Couleur couleur;

    // Constructeurs
    public Element(Coordonnees coord) {
        this.coord = coord;
        this.couleur = Couleur.values()[0];
    }

    public Element(int abscisse, int ordonnee) {
        this(new Coordonnees(abscisse, ordonnee));
    }

    public Element(Coordonnees coord, Couleur couleur) {
        this.coord = coord;
        this.couleur = couleur;
    }

    public Element(int abscisse, int ordonnee, Couleur couleur) {
        this(new Coordonnees(abscisse, ordonnee), couleur);
    }

    // Overrides
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((coord == null) ? 0 : coord.hashCode());
        result = prime * result + ((couleur == null) ? 0 : couleur.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Element(" + this.coord.getAbscisse() + ", " + this.coord.getOrdonnee() + ") - " + this.couleur;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Element other = (Element) obj;
        if (coord == null) {
            if (other.coord != null)
                return false;
        } else if (!coord.equals(other.coord))
            return false;
        return couleur == other.couleur;
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
