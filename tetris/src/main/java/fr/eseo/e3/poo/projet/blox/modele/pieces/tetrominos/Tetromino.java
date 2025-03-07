package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import java.util.List;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public abstract class Tetromino implements Piece {
    // Attributs
    protected Element[] elements;
    protected Coordonnees coord;
    protected Couleur couleur;

    // Constructeurs
    protected Tetromino(Coordonnees coord, Couleur couleur) {
        this.coord = coord;
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
        this.setElements(new Coordonnees(abscisse, ordonnee), this.couleur);
    }

    // Overrides
    @Override
    public String toString() {
        String res = this.getClass().getCanonicalName() + " :\n";
        for (Element element : elements) {
            int x = element.getCoord().getAbscisse();
            int y = element.getCoord().getOrdonnee();
            res += "\t(" + x + ", " + y + ") - " + this.couleur + "\n";
        }
        return res;
    }
}
