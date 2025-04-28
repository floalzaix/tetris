package fr.eseo.e3.poo.projet.blox.modele.pieces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.OTetromino;

class TasTest {
    public static final String FIN_MESSAGE = " dans la classe Tas : ";
    public static final String ERREUR_CONSTRUCTEUR = "Erreur dans un constructeur" + FIN_MESSAGE;
    public static final String ERREUR_CONSTRUIRE_TAS = "Erreur dans la cosntruction du tas" + FIN_MESSAGE;
    public static final String ERREUR_LIGNE_COMPLETE = "Erreur dans la fonction ligneComplete()" + FIN_MESSAGE;
    public static final String ERREUR_AJOUTER_ELEMENTS = "Erreur dans la fonction ajouterElements()" + FIN_MESSAGE;

    private Puits puits;
    private Random rand;

    @BeforeEach
    void setUp() {
        this.puits = new Puits(10, 20);
        this.rand = new Random();
    }

    //
    // Tests Constructeurs
    //

    @Nested
    class TestsConstructeurs {
        @Test
        void testConstructeurPuitsIntIntRandomDepassementNbElementsNbLignes() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Tas(puits, 11, 1, rand),
                    ERREUR_CONSTRUCTEUR
                            + "exception non lancé dans le constructeur Puits Int Int Random quand le nombre d'élements est supérieur à la largueur * le nomrbe de lignes !");
            assertEquals("Erreur lors de la création du tas : trop d'éléments !", e.getMessage(),
                    ERREUR_CONSTRUCTEUR + "mauvais message d'erreur !");
        }

        @Test
        void testConstucteurPuitsIntIntRandomDepassementNbElementsProfondeur() {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> new Tas(puits, 201, 21, rand),
                    ERREUR_CONSTRUCTEUR
                            + "exception non lancé dans le constructeur Puits Int Int Random quand le nombre d'élements est supérieur à la largueur * la profondeur !");
            assertEquals("Erreur lors de la création du tas : trop d'éléments !", e.getMessage(),
                    ERREUR_CONSTRUCTEUR + "mauvais message d'erreur !");
        }

        @Test
        void testConstucteurPuitsIntIntRandomAppelConstructionTas() {
            Tas tas = new Tas(puits, 1, 3, rand);

            assertEquals(1, tas.getElements().size(),
                    ERREUR_CONSTRUCTEUR + "le constructeur n'appelle pas construireTas ou erreur dans ma fonction !");
        }

        @Test
        void testConstructeurPuits() {
            Tas tas = new Tas(puits);

            assertEquals(0, tas.getElements().size(), ERREUR_CONSTRUCTEUR
                    + "le nombre d'élements de base du tas n'est pas set à zéro dans le constructeur Puits !");
        }

        @Test
        void testConstructeurPuitsInt() {
            Tas tas = new Tas(puits, 22);

            assertEquals(22, tas.getElements().size(), ERREUR_CONSTRUCTEUR
                    + "le nombre d'élements de base du tas n'est pas le bon dans le constructeur Puits Int !");

            // Hauteur
            int hauteurMax = getHauteurMax(tas);
            assertEquals(17, hauteurMax, ERREUR_CONSTRUCTEUR
                    + "le nombre de ligne n'est pas nbElements / puits.getLargueur() + 1 dans le constructeur Puits Int");
        }

        @Test
        void testConstructeurPuitsIntInt() {
            Tas tas = new Tas(puits, 10, 2);

            assertEquals(10, tas.getElements().size(), ERREUR_CONSTRUCTEUR
                    + "le nombre d'élements de base du tas n'est pas le bon dans le constructeur Puits Int Int !");

            // Hauteur
            int hauteurMax = getHauteurMax(tas);
            assertEquals(18, hauteurMax,
                    ERREUR_CONSTRUCTEUR + "le nombre de ligne n'est pas le bon dans le constructeur Puits Int Int");
        }

        //
        // Tests construireTas()
        //

        /**
         * Récupère la hauteur max du tas sachant que le 0 EST EN HAUT
         * 
         * @param tas Le tas dont on récupère la hauteur
         * @return La hauteur max du tas
         */
        public int getHauteurMax(Tas tas) {
            int ligneMin = 20;

            List<Coordonnees> coords = tas.getElements().stream().map(Element::getCoord).toList();

            for (Coordonnees c : coords) {
                ligneMin = Math.min(c.getOrdonnee(), ligneMin);
            }

            return ligneMin;
        }

        @Nested
        class TestsConstruireTas {
            private Method method;

            @BeforeEach
            void setUpMethodConstruireTas() throws NoSuchMethodException, SecurityException {
                this.method = Tas.class.getDeclaredMethod("construireTas", int.class, int.class, Random.class);
                System.out.println(this.method);
                this.method.setAccessible(true);
            }

            @Test
            void testDepassementNbElements() {
                Tas tas = new Tas(puits, 0, 0, rand);
                InvocationTargetException e = assertThrows(InvocationTargetException.class,
                        () -> this.method.invoke(tas, 21, 2, rand), ERREUR_CONSTRUIRE_TAS
                                + "exception non lancé quand le nombre d'élements est supérieur à la largueur * le nomrbe de lignes !");
                assertTrue(e.getCause() instanceof IllegalArgumentException,
                        ERREUR_CONSTRUIRE_TAS + "mauvaise exception !");
                assertEquals("Erreur lors de la création du tas : trop d'éléments !", e.getCause().getMessage(),
                        ERREUR_CONSTRUIRE_TAS + "mauvais message d'erreur");
            }

            @Test
            void testDepassementProfondeur() {
                Tas tas = new Tas(puits, 0, 0, rand);
                InvocationTargetException e = assertThrows(InvocationTargetException.class,
                        () -> this.method.invoke(tas, 201, 21, rand), ERREUR_CONSTRUIRE_TAS
                                + "exception non lancé quand le nombre d'élements est supérieur à la largueur * la profondeur !");
                assertTrue(e.getCause() instanceof IllegalArgumentException,
                        ERREUR_CONSTRUIRE_TAS + "mauvaise exception !");
                assertEquals("Erreur lors de la création du tas : trop d'éléments !", e.getCause().getMessage(),
                        ERREUR_CONSTRUIRE_TAS + "mauvais message d'erreur !");
            }

            @Test
            void testNbrElements() throws IllegalAccessException, InvocationTargetException {
                Tas tas = new Tas(puits, 0, 0, rand);
                method.invoke(tas, 17, 2, rand);
                assertEquals(17, tas.getElements().size(),
                        ERREUR_CONSTRUIRE_TAS + "mauvais nombre d'élements lors de la construction du tas !");
            }

            @Test
            void testNbLignes() throws IllegalAccessException, InvocationTargetException {
                Tas tas = new Tas(puits, 0, 0, rand);
                method.invoke(tas, 20, 2, rand);

                // Recup des coords
                int hauteurMax = getHauteurMax(tas);

                assertTrue(hauteurMax <= 19, ERREUR_CONSTRUIRE_TAS + "le nombre de ligne n'est pas respecté !");
            }

            @Test
            void testAleatoire() throws IllegalAccessException, InvocationTargetException {
                Tas tas1 = new Tas(puits, 0, 0, rand);
                Tas tas2 = new Tas(puits, 0, 0, rand);

                // Appels méthodes

                method.invoke(tas1, 10, 20, rand);
                method.invoke(tas2, 10, 20, rand);

                assertNotEquals(tas1.getElements(), tas2.getElements(), ERREUR_CONSTRUIRE_TAS
                        + "la génération du tas n'est pas aléatoire ou alors réessayer pour être sûr !");
            }
        }
    }

    //
    // Tests ajouterElements()
    //

    @Nested
    class TestsAjouterElements {
        @Test
        void testAjouterElementsAjoutPiecesAjout() {
            Tas tas = new Tas(puits, 10, 2);
            OTetromino o = new OTetromino(new Coordonnees(0, 0), Couleur.BLEU);

            tas.ajouterElements(o);

            assertEquals(14, tas.getElements().size(), ERREUR_AJOUTER_ELEMENTS + "les élements ne sont pas ajoutés !");
        }

        @Test
        void testAjouterElementsAjoutPiecesScore() {
            Puits p = new Puits(10, 20);
            Tas tas = new Tas(p, 10, 1);
            OTetromino o = new OTetromino(new Coordonnees(0, 0), Couleur.BLEU);

            tas.ajouterElements(o);

            assertEquals(40, p.getScore(), ERREUR_AJOUTER_ELEMENTS + "le score n'est pas set quand une ligne est complète !");
        }

        //
        // Tests ligneComplete()
        //

        @Nested
        class TestsLigneComplete {
            private Method method;

            @BeforeEach
            void setUpMethodLigneComplete() throws NoSuchMethodException, SecurityException {
                method = Tas.class.getDeclaredMethod("ligneComplete");
                method.setAccessible(true);
            }

            @Test
            void testUneLigneComplete() throws IllegalAccessException, InvocationTargetException {
                Tas tas = new Tas(puits, 10, 1);

                int score = (int) method.invoke(tas);

                assertEquals(40, score, ERREUR_LIGNE_COMPLETE + "mauvais score quand une ligne !");
            }

            @Test
            void testDeuxLigneComplete() throws IllegalAccessException, InvocationTargetException {
                Tas tas = new Tas(puits, 20, 2);

                int score = (int) method.invoke(tas);

                assertEquals(100, score, ERREUR_LIGNE_COMPLETE + "mauvais score quand deux lignes !");
            }

            @Test
            void testTroisLigneComplete() throws IllegalAccessException, InvocationTargetException {
                Tas tas = new Tas(puits, 30, 3);

                int score = (int) method.invoke(tas);

                assertEquals(300, score, ERREUR_LIGNE_COMPLETE + "mauvais score quand trois lignes !");
            }

            @Test
            void testQuatreLigneComplete() throws IllegalAccessException, InvocationTargetException {
                Tas tas = new Tas(puits, 40, 4);

                int score = (int) method.invoke(tas);

                assertEquals(1200, score, ERREUR_LIGNE_COMPLETE + "mauvais score quand quatre lignes !");
            }

            @Test
            void testAucuneLigneComplete() throws IllegalAccessException, InvocationTargetException {
                Tas tas = new Tas(puits, 7);

                List<Element> save = new ArrayList<>(tas.getElements());

                int score = (int) method.invoke(tas);

                assertEquals(0, score, ERREUR_LIGNE_COMPLETE + "mauvais score quand pas de lignes !");
                assertEquals(save, tas.getElements(), ERREUR_LIGNE_COMPLETE + "pas de ligne est le tas a changé !");
            }

            @Test
            void testSupressionCouches() throws IllegalAccessException, InvocationTargetException {
                Tas tas = new Tas(puits, 40, 4);

                method.invoke(tas);

                assertEquals(0, tas.getElements().size(),
                        ERREUR_LIGNE_COMPLETE + "les lignes ne sont pas supprimés quand complètes !");
            }

            @Test
            void testDescenteElements() throws IllegalAccessException, InvocationTargetException {
                Tas tas = new Tas(new Puits(5, 20), 10, 2);

                Element e1 = new Element(new Coordonnees(0, 16));
                Element e2 = new Element(new Coordonnees(0, 15));
                Element e3 = new Element(new Coordonnees(1, 15));
                Element e4 = new Element(new Coordonnees(2, 15));
                Element e5 = new Element(new Coordonnees(3, 15));
                Element e6 = new Element(new Coordonnees(4, 15));
                Element e7 = new Element(new Coordonnees(0, 14));
                tas.getElements().add(e1);
                tas.getElements().add(e2);
                tas.getElements().add(e3);
                tas.getElements().add(e4);
                tas.getElements().add(e5);
                tas.getElements().add(e6);
                tas.getElements().add(e7);

                method.invoke(tas);

                assertEquals(18, e1.getCoord().getOrdonnee(),
                        ERREUR_LIGNE_COMPLETE + "mauvaise descente des élements !");
                assertEquals(17, e7.getCoord().getOrdonnee(),
                        ERREUR_LIGNE_COMPLETE + "mauvaise descente des élements !");
            }

            @Test
            void testTropElements() {
                Tas tas = new Tas(puits, 10, 1);
                Element elt = new Element(new Coordonnees(0, 19));
                tas.getElements().add(elt);

                InvocationTargetException e = assertThrows(InvocationTargetException.class, () -> this.method.invoke(tas),
                        ERREUR_LIGNE_COMPLETE + "pas d'erreur quand trop d'élements sur la même ligne !");
                assertTrue(e.getCause() instanceof IllegalArgumentException,
                        ERREUR_LIGNE_COMPLETE + "mauvaise erreur quand trop d'élements sur la même ligne !");
                assertEquals("Le nombre d'élements ne peut pas être plus grand que le nombre de colonnes !",
                        e.getCause().getMessage(),
                        ERREUR_LIGNE_COMPLETE + "mauvais message d'erreur quand trop d'élement sur la même ligne !");
            }
        }
    }
}
