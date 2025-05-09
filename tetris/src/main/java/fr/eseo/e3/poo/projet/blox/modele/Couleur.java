package fr.eseo.e3.poo.projet.blox.modele;

import java.awt.Color;
import java.util.stream.Stream;

public enum Couleur {
    ROUGE(Color.RED),
    ORANGE(Color.ORANGE),
    BLEU(Color.BLUE),
    VERT(Color.GREEN),
    JAUNE(Color.YELLOW),
    CYAN(Color.CYAN),
    VIOLET(Color.MAGENTA);

    // Attributs
    private final Color couleurPourAffichage;

    // Constructeur
    private Couleur(Color couleurPourAffichage) {
        this.couleurPourAffichage = couleurPourAffichage;
    }

    //
    // Méthodes
    //

    /**
     * Réalise un mapping inverse de String à Couleur.
     * 
     * Récupère la première des Couleur qui à pour nom color
     * 
     * @param color Le nom à rechercher dans les Couleurs
     * @return La première Couleur trouvée
     */
    public static Couleur getCouleur(String color) {
        Stream<Couleur> couleurs = Stream.of(Couleur.values());
        return couleurs.filter(c -> c.name().equals(color)).findFirst().orElse(null);
    }

    // Getters et setters
    public Color getCouleurPourAffichage() {
        return couleurPourAffichage;
    }
}
