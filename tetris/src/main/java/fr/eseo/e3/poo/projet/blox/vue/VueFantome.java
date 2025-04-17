package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Fantome;

public class VueFantome {
    // Constantes de classe
    public static final double TRANSPARENCE_RATIO = 0.3;

    // variables d'instance
    private final Fantome fantome;
    private final int taille;

    // Constructeurs
    public VueFantome(Fantome fantome, int taille) {
        this.fantome = fantome;
        this.taille = taille;
    }

    //
    // Méthodes
    //

    /**
     * Ajoute de la transparence à la couleur actuelle
     * 
     * @param color Couleur dont la transparence doit être ajouté
     * @return La nouvelle couleur avec la transparence
     */
    private static Color transparence(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (VueFantome.TRANSPARENCE_RATIO * 255));
    }

    /**
     * Affiche le fantome donc tous les élements qui constituent la piece fantome
     * 
     * @param g2d Outils graphique prévu d'être utilisé dans le contexte d'un
     *            paintComponent().
     */
    public void afficherFantome(Graphics2D g2d) {
        List<Element> elts = fantome.getCopyPiece().getElements();
        Color color = transparence(elts.getFirst().getCouleur().getCouleurPourAffichage());

        // Affichage de la piece fantome
        g2d.setColor(color);

        for (Element e : elts) {
            Coordonnees coord = e.getCoord();

            g2d.fill3DRect(coord.getAbscisse() * this.taille, coord.getOrdonnee() * this.taille, this.taille,
                    this.taille, true);
        }
    }
}
