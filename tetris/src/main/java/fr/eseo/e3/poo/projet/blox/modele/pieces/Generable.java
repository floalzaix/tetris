package fr.eseo.e3.poo.projet.blox.modele.pieces;

import java.lang.reflect.InvocationTargetException;

import fr.eseo.e3.poo.projet.blox.modele.initialisation.Initialisable;

public interface Generable extends Initialisable {
    /**
     * S'enregistre dans une liste des différentes classes qui sont dans ces
     * relations d'héritage.
     * 
     * Par exemple si on créer une list de Tetromino dans Tetromino static qui
     * contient toutes les distinctes instances des Tetromino.
     * 
     * Par exemple pour OTetromino:
     * 
     * if (aucun element de la liste n'est un OTetromino) {ajoute cette OTetromino à
     * la liste}
     * 
     * Comme ça une autre classe peut géneré tous les types de Tetromino avec la
     * fonction generer
     */
    public void register();

    /**
     * Genère un l'objet similaire à celui à partir duquel cette méthode est
     * appelée.
     * 
     * Ex OTetromino :
     * 
     * if (args.length != 2) {
     * throw Exception
     * }
     * 
     * if (!(args[0] instanceof Coordonnee)) {
     * throw Exception
     * }
     * 
     * return new OTetromino(coord, couleur);
     * 
     * @param args
     * @throws IllegalArgumentException Si le nombre d'arguments n'est pas le bon et
     *                                  leurs types
     * @return The object with the same type but the given params
     */
    public Object generer(Object... args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException;
}
