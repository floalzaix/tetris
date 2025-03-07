package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Element;

public class OTetromino {
    // Attributs
    private Coordonnees coord;
    private Couleur couleur;
    private Element[] elements;

    // Constructeurs
    public OTetromino(Coordonnees coord, Couleur couleur) {
        this.coord = coord;
        this.couleur = couleur;

        this.setElements(coord, couleur);
    }

    /**
     * Cette fonction a pour but d'initialiser les elements du OTetromino donc la
     * variable this.elements
     * 
     * L'origine du OTetromino est considérée dans le coin inférieur gauche.
     * 
     * Exemple : IN => (1, 2) bleu
     *           POST => elements initialisés tels que :
     *                  {Element(1, 2) - bleu,
     *                   Element(2, 2) - bleu,
     *                   Element(2, 1) - bleu,
     *                   Element(1, 1) - bleu}
     * 
     * @param coord   Les coordonées du OTetromino
     * @param couleur La couleur du OTetromino
     */
    protected void setElements(Coordonnees coord, Couleur couleur) {
        int x = coord.getAbscisse();
        int y = coord.getOrdonnee();

        this.elements = new Element[] {
                new Element(x, y, couleur),
                new Element(x + 1, y, couleur),
                new Element(x + 1, y - 1, couleur),
                new Element(x, y - 1, couleur)
        };
    }

    // Overrides
    @Override
    public String toString() {
        String res = "OTetromino :\n";
        for (int i  = 0; i < 4; i++) {
            int x = this.elements[i].getCoord().getAbscisse();
            int y = this.elements[i].getCoord().getOrdonnee();
            res+= "\t(" + x + ", " + y +") - " + this.couleur + "\n";
        }
        return res;
    }

    // Getters
    public Element[] getElements() {
        return this.elements;
    }
}
