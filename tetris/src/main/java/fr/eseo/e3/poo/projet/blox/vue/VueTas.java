package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Tas;

public class VueTas {
    // Constantes
    public static final double MULTIPLIER_NUANCE = 0.3;

    // Attributs
    private VuePuits vuePuits;
    private int taille;
    private Tas tas;

    // Constructeurs
    public VueTas(VuePuits vuePuits, int taille) {
        this.vuePuits = vuePuits;
        this.taille = taille;

        this.tas = vuePuits.getPuits().getTas();
    }

    /**
     * Modifier la teinte d'une couleur pour la rendre plus sombre d'un facteur
     * MULTIPLIER_NUANCE
     * 
     * @param color La couleur dont on veut rendre la teinte plus sombre
     * @return Une couleur similaire mais plus sombre
     */
    public static Color nuance(Color color) {
        return new Color((int) (color.getRed() * (1 - MULTIPLIER_NUANCE)),
                (int) (color.getGreen() * (1 - MULTIPLIER_NUANCE)),
                (int) (color.getBlue() * (1 - MULTIPLIER_NUANCE)));
    }

    /**
     * Gère l'affichage des élements du tas 
     * 
     * @param g2d L'outil graphique utilisé
     */
    public void afficher(Graphics2D g2d) {
        List<Element> elts = this.tas.getElements();

        for (Element e : elts) {
            // Récupération des coordonnées de l'élément
            Coordonnees coord = e.getCoord();

            // Définition de la couleur de l'élément avec sa nuance
            g2d.setColor(nuance(e.getCouleur().getCouleurPourAffichage()));

            // Affichage sous forme de rectangle sur-élevé
            g2d.fill3DRect(coord.getAbscisse() * taille, coord.getOrdonnee() * taille, taille, taille, true);
        }
    }

    // Getters setters
    public VuePuits getVuePuits() {
        return vuePuits;
    }
}
