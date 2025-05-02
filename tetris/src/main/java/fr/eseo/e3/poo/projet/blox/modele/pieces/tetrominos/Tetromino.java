package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import fr.eseo.e3.poo.projet.blox.modele.BloxException;
import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Generable;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Fantome;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public abstract class Tetromino implements Generable, Piece {
    //
    // Variables de classe
    //
    public static final List<Tetromino> TETROMINOS = new ArrayList<>();

    //
    // Variables d'instance
    //
    protected Element[] elements;
    protected Couleur couleur;

    private Puits puits;

    //
    // Constructeurs
    //
    protected Tetromino() {
        this.register();
    }

    protected Tetromino(Coordonnees coord, Couleur couleur) {
        this.elements = new Element[4];
        this.couleur = couleur;

        // GABARIT
        int[][] gabarit = this.defGabarit();
        this.setElements(coord, couleur, gabarit);
    }

    //
    // Fonctions
    //

    public static void init() {
        Reflections r = new Reflections("fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos");

        Set<Class<? extends Tetromino>> sousClasses = r.getSubTypesOf(Tetromino.class);
        for (Class<? extends Tetromino> classe : sousClasses) {
            try {
                classe.getConstructor().newInstance();
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                    | InvocationTargetException _) {
                throw new IllegalArgumentException("Problème d'héritage avec les Tetrominos !");
            }
        }
    }

    //
    // Méthodes
    //

    /**
     * Méthode qui permet de définir le gabarit de la pièce (pour un OTetromino par
     * exemple : [[0, 0], [1, 0], [1, -1], [0, -1]]). Fait pour être appelé dans le
     * constucteur pour être passé à la méthode set élements, qui va ensuite définir
     * les élement de la pièce.
     * 
     * @return Le gabarit en question.
     */
    protected abstract int[][] defGabarit();

    /**
     * Défini la possitions des différents élements du Tetromino
     * 
     * @param coord   Les coordonnées de l'élement de base
     * @param couleur Couleur qui sera attribué au élements du Tetromino
     * @param gabarit Le gabarit de la pièce (pour un OTetromino par exemple : [[0,
     *                0], [1, 0], [1, -1], [0, -1]])
     */
    protected void setElements(Coordonnees coordRef, Couleur couleur, int[][] gabarit) {
        if (gabarit == null || gabarit.length != 4) {
            throw new IllegalArgumentException("Mauvais gabarit !");
        }

        for (int i = 0; i < 4; i++) {
            Coordonnees coord = new Coordonnees(coordRef.getAbscisse() + gabarit[i][0],
                    coordRef.getOrdonnee() + gabarit[i][1]);
            Element e = new Element(coord, couleur);

            this.elements[i] = e;
        }
    }

    /**
     * Récupère la couleur par défaut de la pièce si besoins (ex: OTetromino =>
     * ROUGE)
     * 
     * Ex de def :
     * return Couleur.ROUGE;
     * 
     * @return La couleur par défaut de la pièce.
     */
    public abstract Couleur getCouleurDefaut();

    //
    // Interface Generable
    //

    @Override
    public void register() {
        boolean instance = true;
        for (Tetromino t : Tetromino.TETROMINOS) {
            if (this.getClass().isInstance(t)) {
                instance = false;
                break;
            }
        }
        if (instance) {
            Tetromino.TETROMINOS.add(this);
        }
    }

    @Override
    public Object generer(Object... args) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Mauvais nombres d'aguments !");
        }
        if (!(args[0] instanceof Coordonnees && args[1] instanceof Couleur)) {
            throw new IllegalArgumentException("Mauvais types d'arguments !");
        }

        return this.getClass().getConstructor(Coordonnees.class, Couleur.class).newInstance(args[0], args[1]);
    }

    //
    // Interface Piece
    //

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
            Fantome fantome = this.puits.getFantome();
            if (fantome != null) {
                fantome.projection();
            }
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
        Fantome fantome = this.puits.getFantome();
        if (fantome != null) {
            fantome.projection();
        }
    }

    @Override
    public Object copy() {
        return copyModel(copySelf());
    }

    //
    // Méthodes perso
    //

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
    private Object copyModel(Tetromino tetromino) {
        Tetromino t = tetromino;
        t.setPuits(this.puits); // Puits non copié reste le même
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
        this.elements = elements.toArray(Element[]::new);
    }
}
