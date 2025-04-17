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
        Coordonnees coord = this.elements[0].getCoord();
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
            if (deltaX == 0 && deltaY == 1) {
                throw new BloxException("La pièce touche le fond ou un élément du tas dû certainement à la gravité !",
                        BloxException.BLOX_COLLISION_OU_BAS_PUITS);
            }
            throw be;
        }

        // Projection de la pièce avec le fantome
        if (!(deltaX == 0 && deltaY == 1)) {
            this.puits.getFantome().projection();
        }
    }

    @Override
    public void tourner(boolean sensHoraire) throws BloxException {
        /// Rotation en tenant compte des coordonnée
        /// informatique avec multiplication par une matrice de rotation
        this.rotation(sensHoraire);

        /// Si après rotation, pièce non valide alors on la retourne dans l'autre sens
        try {
            Piece.estDehorsOuCollision(this, this.puits);
        } catch (BloxException be) {
            this.rotation(!sensHoraire);
            throw be;
        }

        // Projection de la pièce avec le fantome
        this.puits.getFantome().projection();
    }

    // Méthodes perso
    /**
     * Réalise de manière brut la rotation sans test. Son but de permettre de
     * contourner la récursivité
     * dans la fonction tourner et ainsi éviter que la méthode s'appelle de manière
     * infinie
     * 
     * @param sensHoraire Le sens de la rotation pareil que tourner
     */
    private void rotation(boolean sensHoraire) {
        // Translater vers l'origine en conservant les coordonnée
        Coordonnees coord = this.elements[0].getCoord();
        this.setPosition(0, 0);

        /// Rotation en tenant compte des coordonnée
        /// informatique avec multiplication par une matrice de rotation
        for (Element elt : this.elements) {
            Coordonnees eltCoord = elt.getCoord();
            elt.setCoord(new Coordonnees((sensHoraire ? -1 : 1) * eltCoord.getOrdonnee(),
                    (sensHoraire ? 1 : -1) * eltCoord.getAbscisse()));
        }

        // Translation inverse
        this.setPosition(coord.getAbscisse(), coord.getOrdonnee());
    }

    /**
     * Le modèle de la copie qui est le même pour tous les Tetromino.
     * 
     * @param tetromino La copie du tetromino sans avoir copier ses variables
     *                  d'instances
     * @return Une copie complète du tetromino
     */
    public Object copyModel(Tetromino tetromino) {
        Tetromino t = tetromino;
        t.setPuits(this.puits);
        List<Element> te = t.getElements();

        for (int i = 0; i < te.size(); i++) {
            Coordonnees c = this.elements[i].getCoord();

            te.get(i).setCoord((Coordonnees) c.copy());
        }

        return t;
    }

    /**
     * Recupère une copie sans avoir copié les varaibles d'instances. Prévu pour
     * être redéfini dans la classe fille juste en appelant new OTetromino par
     * exemple.
     * 
     * @return La copie sans avoir copié les variables d'instances
     */
    protected abstract Tetromino copySelf();

    // Overrides
    @Override
    public Object copy() {
        return copyModel(copySelf());
    }

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

    @Override
    public void setElements(List<Element> elements) {
        this.elements = (Element[]) elements.toArray();
    }
}
