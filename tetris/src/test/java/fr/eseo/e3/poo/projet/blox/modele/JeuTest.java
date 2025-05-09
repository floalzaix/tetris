package fr.eseo.e3.poo.projet.blox.modele;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.Tetromino;

public class JeuTest {
    public static final String DEBUT = "Erreur dans la méthode ";
    public static final String FIN = " de la classe Jeu : ";
    public static final String E_CONSTRUCTEUR = "Erreur dans le constructeur" + FIN;

    //
    //  Tests Constructeurs
    //
    @Nested
    class testsConstructeurs {
        @Test
        void testIntIntIntInt() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
            Jeu jeu = new Jeu(10, 20, 9, UsineDePiece.CYCLIC);

            // Tests
            assertEquals(1, jeu.getPlace(), "la place n'est pas ou mal set !");

            assertNotEquals(0, Tetromino.TETROMINOS.size(), E_CONSTRUCTEUR + "les tetrominos ne sont pas initialisé !");

            Field mode = UsineDePiece.class.getDeclaredField("mode");
            mode.setAccessible(true);
            assertEquals(UsineDePiece.CYCLIC, mode.get(null), E_CONSTRUCTEUR + "le mode de l'usine n'est pas set ou mal !");

            assertEquals(10, jeu.getPuits().getLargueur(), E_CONSTRUCTEUR + "la largueur du puits est mal ou pas set !");
            assertEquals(20, jeu.getPuits().getProfondeur(), E_CONSTRUCTEUR + "la profondeur du puits est mal ou pas set !");

            assertEquals(9, jeu.getPuits().getTas().getElements().size(), E_CONSTRUCTEUR + "le tas est mal set !");

            assertNotNull(jeu.getPuits().getPieceActuelle(), E_CONSTRUCTEUR + "la pièce actuelle est pas ou mal set !");
            assertNotNull(jeu.getPuits().getPieceSuivante(), E_CONSTRUCTEUR + "la pièce suivant est pas ou mal set !");
        }
    }
}
