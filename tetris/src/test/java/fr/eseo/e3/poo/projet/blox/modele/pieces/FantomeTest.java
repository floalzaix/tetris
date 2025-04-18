package fr.eseo.e3.poo.projet.blox.modele.pieces;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.OTetromino;

public class FantomeTest {
    public static final String START_OF_MESSAGE = "Erreur dans la méthode ";
    public static final String END_OF_MESSAGE = " dans la classe Fantome : ";
    public static final String ERREUR_CONSTRUCTEUR = "Erreur dans le constructeur" + END_OF_MESSAGE;
    public static final String ERREUR_PROJECTION = START_OF_MESSAGE + "projection()" + END_OF_MESSAGE;

    //
    //  Tests Constructeurs
    //

    @Test
    void testConstructeurPiece() {
        Puits puits = new Puits(10, 20);
        Coordonnees coord = new Coordonnees(0, 0);
        OTetromino o = new OTetromino(coord, Couleur.VIOLET);
        o.setPuits(puits);
        Fantome f = new Fantome(o);

        // Test
        assertNotNull(f.getCopyPiece(), ERREUR_CONSTRUCTEUR + "la fonction projection n'a pas été appelé !");
    }

    //
    //  Tests projection()
    //

    @Test
    void testProjection() {
        Puits puits = new Puits(10, 20);
        Coordonnees coord = new Coordonnees(0, 0);
        OTetromino o = new OTetromino(coord, Couleur.VIOLET);
        o.setPuits(puits);
        Fantome f = new Fantome(o);

        // projection
        f.projection();

        // Tests
        assertNotEquals(o.getElements(), f.getCopyPiece().getElements(),
                ERREUR_PROJECTION + "la méthode n'abaisse pas les élements du fantome !");
    }
}
