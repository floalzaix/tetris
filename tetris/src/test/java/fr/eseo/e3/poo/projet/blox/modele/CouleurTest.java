package fr.eseo.e3.poo.projet.blox.modele;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class CouleurTest {
    public static final String DEBUT = "Erreur dans la méthode ";
    public static final String FIN_DEBUT = " de l'enum Couleur : "; 
    public static final String ERREUR_GET_COULEUR = DEBUT + "getCouleur()" + FIN_DEBUT;

    @Test
    void testGetCouleur() {
        assertEquals(Couleur.CYAN, Couleur.getCouleur("CYAN"), ERREUR_GET_COULEUR + "couleurs mal mappées !");
    }
}
