package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Element;

public class ITetromino extends Tetromino {
    // Constructeurs
    public ITetromino(Coordonnees coord, Couleur couleur) {
        super(coord, couleur);
    }

    //
    //  Overrides
    //

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

    @Override
    public Couleur getCouleurDefaut() {
        return Couleur.ROUGE;
    } 

    @Override
    protected Tetromino copySelf() {
        return new ITetromino(this.getElements().getFirst().getCoord(), this.couleur);
    }
}
