package fr.eseo.e3.poo.projet.blox.modele;

import java.awt.Color;

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

    // Getters et setters
    public Color getCouleurPourAffichage() {
        return couleurPourAffichage;
    }
}
