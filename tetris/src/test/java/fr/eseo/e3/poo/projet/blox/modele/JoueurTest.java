package fr.eseo.e3.poo.projet.blox.modele;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.naming.OperationNotSupportedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class JoueurTest {
    public static final String FIN = " de la classe Joueur : ";
    public static final String DEBUT = "Erreur dans la méthode ";
    public static final String E_CONSTRUCTEUR = "Erreur dans un des constructeurs" + FIN;
    public static final String E_CREATION_JEU = DEBUT + "creationJeu()" + FIN;
    public static final String E_AJOUTER_LIGNE = DEBUT + "ajouterLigne()" + FIN;

    //
    // Tests Constructeurs
    //
    @Test
    void testConstructeurCouleur() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Joueur joueur = new Joueur(Couleur.JAUNE);

        Field pcs = Joueur.class.getDeclaredField("pcs");
        pcs.setAccessible(true);

        // Tests
        assertNotNull(pcs.get(joueur), E_CONSTRUCTEUR + "le pcs n'est pas initialisé !");
    }

    //
    // Tests creationJeu()
    //
    @Test
    void testCreationJeu() {
        Joueur joueur = new Joueur(Couleur.BLEU);

        // Préparation du listener
        AtomicBoolean triggered = new AtomicBoolean(false);
        joueur.addPropertyChangeListener(e -> {
            triggered.set(true);
            assertEquals(Joueur.EVT_JEU_CREER, e.getPropertyName(), E_CREATION_JEU + "mauvaise évenement déclenché !");
            assertEquals(joueur.getJeu(), e.getNewValue(),
                    E_CREATION_JEU + "le jeu n'est pas passé en argument du pcs !");
        });

        // creationJeu()
        joueur.creationJeu(10, 20, 0, UsineDePiece.ALEATOIRE_COMPLET);

        // Tests
        assertNotNull(joueur.getJeu(), E_CREATION_JEU + "le jeu n'est pas ou mal crée !");

        assertTrue(triggered.get(), E_CREATION_JEU + "le listener n'est pas triggered !");
    }

    //
    // Tests ajouterLigne()
    //
    @Nested
    class testsAjouterLigne {
        @Test
        void testAvantCreationJeu() {
            Joueur joueur = new Joueur(Couleur.ROUGE);

            OperationNotSupportedException e = assertThrows(
                    OperationNotSupportedException.class,
                    () -> joueur.ajouterLigne(Couleur.BLEU, 2),
                    E_AJOUTER_LIGNE + "le jeu null est pas détecté !");
            assertEquals("Le jeu n'est pas encore créer !", e.getMessage(),
                    E_AJOUTER_LIGNE + "mauvais message d'erreur !");
        }

        @Test
        void testApresCreationJeu() throws OperationNotSupportedException {
            Joueur joueur = new Joueur(Couleur.ORANGE);

            joueur.creationJeu(10, 20, 0, UsineDePiece.ALEATOIRE_COMPLET);

            // ajouterLigne
            joueur.ajouterLigne(Couleur.BLEU, 2);

            assertNotEquals(0, joueur.getJeu().getPuits().getTas().getElements().size(),
                    E_AJOUTER_LIGNE + "n'appelle pas la méthode ajouter ligne du tas !");
        }
    }
}
