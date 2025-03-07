package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Element;

public class ITetromino extends Tetromino {
    // Constructeurs
    public ITetromino(Coordonnees coord, Couleur couleur) {
        super(coord, couleur);
    }

    // Héritage de la classe abtraite Tetromino
    /**
     * Le but de cette méthode est d'initialisé les elements du ITetromino
     * 
     * L'origine du ITetromino est le 3 élement en partant du haut
     * 
     * Eexemple :   IN => (1, 2) - BLEU
     *              OUT => elements iintialisés tels que
     *                     {Element(1, 2) - Bleu,
     *                      Element(1, 3) - Bleu,
     *                      Element(1, 1) - Bleu,
     *                      Element(1, 0) - Bleu}
     * 
     * @param coord Coordonnée de l'élement de ref du ITetromino
     * @param couleur Couleur du ITetromino
     */
    @Override
    protected void setElements(Coordonnees coord, Couleur couleur) {
        int x = coord.getAbscisse();
        int y = coord.getOrdonnee();

        this.elements = new Element[] {
            new Element(x, y, couleur),
            new Element(x, y+1, couleur),
            new Element(x, y-1, couleur),
            new Element(x, y-2, couleur)
        };
    }

    /**
     * Le but de cette méthode est de changer la position du ITetromino
     * 
     * L'origine du ITetromino est le 3 élement en partant du haut
     * 
     * Eexemple :   IN => 1, 2 et la couleur est : BLEU
     *              OUT => elements tels que
     *                     {Element(1, 2) - Bleu,
     *                      Element(1, 3) - Bleu,
     *                      Element(1, 1) - Bleu,
     *                      Element(1, 0) - Bleu}
     * 
     * @param abscisse Abscisse de l'élements de ref du ITetromino
     * @param ordonnee Ordonnee de l'élements de ref du ITetromino
     */
    @Override
    public void setPosition(int abscisse, int ordonnee) {
        this.elements[0].setCoord(new Coordonnees(abscisse, ordonnee));
        this.elements[1].setCoord(new Coordonnees(abscisse, ordonnee+1));
        this.elements[2].setCoord(new Coordonnees(abscisse, ordonnee-1));
        this.elements[3].setCoord(new Coordonnees(abscisse, ordonnee-2));
    }

}
