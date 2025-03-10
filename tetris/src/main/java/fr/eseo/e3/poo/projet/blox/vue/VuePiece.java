package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public class VuePiece {
    // Constantes
    public static final double MULTPLIER_TEINTE = 0.3;

    // Attributs
    private int taille;
    private Piece piece;

    // Constructeur
    public VuePiece(Piece piece, int taille) {
        this.piece = piece;
        this.taille = taille;
    }

    /**
     * Rend une couleur plus claire. Autrement dit genère une teinte plus claire.
     * 
     * La méthode utilisé est de rapproché la couleur du blanc c'est-à-dire faire
     * tendre les composante vers 255 en focntion de si elle sont proches de 0
     * 
     * @param couleur La couleur dont on veut une teinte plus claire
     * @return La teinte plus claire de la couleur donnée
     */
    public static Color teinte(Color couleur) {
        int[] rgb = new int[] {
                couleur.getRed(),
                couleur.getGreen(),
                couleur.getBlue()
        };
        return new Color(
                (int) (rgb[0] + (255 - rgb[0]) * MULTPLIER_TEINTE),
                (int) (rgb[1] + (255 - rgb[1]) * MULTPLIER_TEINTE),
                (int) (rgb[2] + (255 - rgb[2]) * MULTPLIER_TEINTE));
    }

    /**
     * Affiche le composant piece. Cette fonction s'appuie sur les elements de
     * pieces, ses coordonées et sa couleur
     * 
     * @param g2d L'outil graphique utilisé. Prévu pour être utilisé dans un
     *            contexte paintComponent
     */
    public void afficherPiece(Graphics2D g2d) {
        List<Element> elts = piece.getElements();
        Color couleur = elts.get(0).getCouleur().getCouleurPourAffichage();
        Color teinte = teinte(couleur);

        Coordonnees coord;

        // Affichage pièce de ref
        g2d.setColor(teinte);
        coord = elts.get(0).getCoord();
        g2d.fill3DRect(coord.getAbscisse() * taille, coord.getOrdonnee() * taille, taille, taille, true);

        // Afficher le reste
        g2d.setColor(couleur);
        for (int i = 1; i < elts.size(); i++) {
            coord = elts.get(i).getCoord();
            g2d.fill3DRect(coord.getAbscisse() * taille, coord.getOrdonnee() * taille, taille, taille, true);
        }
    }
}
