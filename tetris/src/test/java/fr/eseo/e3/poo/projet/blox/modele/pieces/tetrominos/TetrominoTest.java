package fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import fr.eseo.e3.poo.projet.blox.modele.BloxException;
import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public abstract class TetrominoTest {
    // Constantes de classe
    public static final String START_OF_MESSAGE = "Erreur de la méthode ";
    private static final String END_OF_MESSAGE = " dans la classe Tetromino : ";
    public static final String ERREUR_CONSTRUCTEUR = "Erreur dans un constructeur" + END_OF_MESSAGE;
    public static final String ERREUR_SET_ELEMENTS = START_OF_MESSAGE + "setElements()" + END_OF_MESSAGE;
    public static final String ERREUR_SET_POSITION = "Erreur dans la méthode setPosition()" + END_OF_MESSAGE;
    public static final String ERREUR_COPY_SELF = START_OF_MESSAGE + "copySelf()" + END_OF_MESSAGE;
    public static final String ERREUR_DEPLACER_DE = "Erreur dans la méthode deplacerDe()" + END_OF_MESSAGE;
    public static final String ERREUR_TOURNER = START_OF_MESSAGE + "tourner()" + END_OF_MESSAGE;
    public static final String ERREUR_COPY = "Erreur dans la méthode copy()";
    public static final String ERREUR_ROTATION = "Erreur dans la méthode rotation()" + END_OF_MESSAGE;
    public static final String ERREUR_COPY_MODEL = "Erreur dans la méthode copyModel()" + END_OF_MESSAGE;

    /**
     * Crée une instance de la classe fille
     * 
     * @return La classe à tester
     */
    public abstract Tetromino instance(Coordonnees coord, Couleur couleur);

    //
    // Constructeurs
    //

    @Nested
    class TestConstructeurs {
        @Test
        void testConstructeurCoordonneesCouleurAppelSetElements() {
            Tetromino t = instance(new Coordonnees(1, 5), Couleur.BLEU);

            assertNotNull(t.getElements(),
                    ERREUR_CONSTRUCTEUR + "le constructeur n'appelle pas setElements ou setElements est mal def !");
        }
    }

    //
    // Fonctions perso
    //

    /**
     * Test le setElement des sous-classes de Tetromino c'est a dire test si elles
     * ont les bons élements. A utiliser par les classes de test comme OTetromino
     * par exemple dans la fonction testSetElements.
     * 
     * @param gabarit Le model de la sous-classe de Tetromino (ex: pour OTetromino
     *                gabarit = [[0, 0], [1, 0],
     *                [1, -1], [0, -1]])
     */
    protected void bonsElements(int[][] gabarit) {
        int x = 4, y = 5;
        Coordonnees c = new Coordonnees(x, y);
        Tetromino t = instance(c, Couleur.VERT); // Appel automatiquement setElements normalement

        // Récupération des coordonnées
        List<Coordonnees> coords = t.getElements().stream()
                .map(Element::getCoord)
                .toList();

        // Targets
        List<Coordonnees> coordsRef = creerCoordonneesRef(x, y, gabarit);

        // Tests
        for (int i = 0; i < coords.size(); i++) {
            assertEquals(coords.get(i), coordsRef.get(i), ERREUR_SET_ELEMENTS + "élements mals set ou pas set !");
        }
    }

    //
    // Tests copySelf()
    //

    @Test
    void testCopySelf() {
        Coordonnees coord = new Coordonnees(0, 0);
        Tetromino o = instance(coord, Couleur.JAUNE);

        // Tests
        assertEquals(o.getClass(), o.copySelf().getClass(),
                ERREUR_COPY_SELF + "la copie n'est pas une instance du bon type !");
        assertNotSame(o, o.copySelf(),
                ERREUR_COPY_SELF + "l'instance et la copie sont les même alors qu'elles ne devraient pas !");
    }

    /**
     * Méthode complémentaire de bonsElements qui créer les coordonnées de
     * réferences avec lesquelles comparer les coordonnées créer par setElements.
     * 
     * @param x       Abscisse de l'élement d'origine
     * @param y       Ordonnée de l'élement d'origine
     * @param gabarit cf bonsElements()
     * @return La liste des coordonnées de réferences
     */
    private List<Coordonnees> creerCoordonneesRef(int x, int y, int[][] gabarit) {
        List<Coordonnees> coordsRef = new ArrayList<>();
        for (int[] g : gabarit) {
            coordsRef.add(new Coordonnees(x + g[0], y + g[1]));
        }
        return coordsRef;
    }

    /**
     * Test si les coordonnees de deux Tetrominos sont égales
     * 
     * @param tInitial
     * @param tTarget
     * @return True si les coodonnees sont égales false sinon
     */
    public boolean coordEgales(Tetromino tInitial, Tetromino tTarget) {
        // Récupération des coordonnées
        List<Coordonnees> initial = tInitial.getElements().stream()
                .map(Element::getCoord)
                .toList();

        List<Coordonnees> target = tTarget.getElements().stream()
                .map(Element::getCoord)
                .toList();

        // Tests d'égalités des coordonnées
        boolean res = true;
        for (int i = 0; i < initial.size(); i++) {
            res &= initial.get(i).equals(target.get(i));
        }

        return res;
    }

    //
    // Tests setPosition()
    //

    @Test
    void testSetPosition() {
        // Définition de la position initiale et de la position cible
        Coordonnees cInitial = new Coordonnees(1, 2);
        Coordonnees cTarget = new Coordonnees(5, 10);
        Tetromino tInitial = instance(cInitial, Couleur.JAUNE);
        Tetromino tTarget = instance(cTarget, Couleur.JAUNE);

        // setPosition
        tTarget.setPosition(cInitial.getAbscisse(), cInitial.getOrdonnee());

        // Test
        assertTrue(coordEgales(tInitial, tTarget),
                ERREUR_SET_POSITION + "les coordonnées ne sont pas celles attendues à la suite de la fonction !");
    }

    //
    // Tests deplacerDe()
    //

    @Nested
    class TestsDeplacerDe {
        /**
         * Donne des valeurs de deltaX et deltaY valides
         * 
         * @return Les valeurs en questions
         */
        public static Stream<Arguments> distributeurDeltasValides() {
            return IntStream.rangeClosed(-1, 1)
                    .boxed()
                    .flatMap(deltaX -> IntStream.rangeClosed(0, 1)
                            .boxed()
                            .filter(deltaY -> deltaX == 0 || deltaY == 0)
                            .map(deltaY -> Arguments.of(deltaX, deltaY)));
        }

        @ParameterizedTest(name = "testDeplacementValide")
        @MethodSource("distributeurDeltasValides")
        void testDeplacementValide(int deltaX, int deltaY) throws IllegalArgumentException, BloxException {
            // Définition des objectifs
            Puits puits = new Puits(10, 20);
            Coordonnees initialCoords = new Coordonnees(5, 6);
            Coordonnees targetCoords = new Coordonnees(5 + deltaX, 6 + deltaY);
            Tetromino initialTetromino = instance(initialCoords, Couleur.ROUGE);
            Tetromino targetTetromino = instance(targetCoords, Couleur.ROUGE);
            initialTetromino.setPuits(puits);
            targetTetromino.setPuits(puits);

            // deplacerDe
            initialTetromino.deplacerDe(deltaX, deltaY);

            // Test
            assertTrue(coordEgales(initialTetromino, targetTetromino),
                    ERREUR_DEPLACER_DE + "les coordonnées ne sont pas celles attendus !");
        }

        //
        // Exceptions
        //

        @Test
        void testBloxExceptionSimple() {
            Puits puits = new Puits(10, 20);
            Coordonnees initialCoords = new Coordonnees(-10, -10);
            Tetromino t = instance(initialCoords, Couleur.BLEU);
            t.setPuits(puits);

            // Test exception
            assertThrows(BloxException.class,
                    () -> t.deplacerDe(-1, 0),
                    ERREUR_DEPLACER_DE + "exception non lancée alors que la pièce est sortie !");
        }

        @Test
        void testBloxExceptionGravite() {
            Puits puits = new Puits(10, 20);
            Coordonnees initialCoords = new Coordonnees(-10, -10);
            Tetromino t = instance(initialCoords, Couleur.BLEU);
            t.setPuits(puits);

            // Test exception
            BloxException be = assertThrows(BloxException.class,
                    () -> t.deplacerDe(0, 1),
                    ERREUR_DEPLACER_DE
                            + "exception non lancée alors que la pièce est rentrée en collision ou sortie dû à la gravité !");

            // Test message d'erreur
            assertEquals(
                    "La pièce touche le fond ou un élément du tas dû certainement à la gravité !",
                    be.getMessage(),
                    ERREUR_DEPLACER_DE + "mauvais message d'erreur lancé !");
        }

        //
        // Projection
        //

        @Test
        void testProjectionDeltasValables() throws IllegalArgumentException, BloxException {
            Puits puits = new Puits(10, 20);
            Coordonnees initialCoords = new Coordonnees(5, 5);
            Tetromino t = instance(initialCoords, Couleur.JAUNE);
            t.setPuits(puits);
            puits.setPieceSuivante(t);
            puits.setPieceSuivante(t);
            puits.getPieceActuelle().setPosition(5, 5);

            // Enregistrement de la copie initiale
            Piece copyInitiale = (Piece) puits.getFantome().getCopyPiece().copy();

            // deplacerDe
            t.deplacerDe(1, 0);

            // Test
            assertNotEquals(copyInitiale.getElements(), puits.getFantome().getCopyPiece().getElements(),
                    ERREUR_DEPLACER_DE
                            + "la méthode projection() du puits n'a pas été appelée alors qu'elle aurait dût !");
        }

        @Test
        void testProjectionGravite() throws IllegalArgumentException, BloxException {
            Puits puits = new Puits(10, 20);
            Coordonnees initialCoords = new Coordonnees(5, 5);
            Tetromino t = instance(initialCoords, Couleur.JAUNE);
            t.setPuits(puits);
            puits.setPieceSuivante(t);
            puits.setPieceSuivante(t);
            puits.getPieceActuelle().setPosition(5, 5);

            // Enregistrement de la copie initiale
            Piece copyInitiale = (Piece) puits.getFantome().getCopyPiece().copy();

            // deplacerDe
            t.deplacerDe(0, 1);

            // Test
            assertEquals(copyInitiale.getElements(),
                    puits.getFantome().getCopyPiece().getElements(),
                    ERREUR_DEPLACER_DE
                            + "la méthode projection() du puits a été appelée alors qu'elle n'aurait pas dûe !");
        }
    }

    //
    // Tests tourner()
    //

    @Nested
    class testsTourner {
        //
        //  Opérations
        //

        @Test
        void testOperationValide() throws BloxException {
            Puits puits = new Puits(10, 20);
            Coordonnees coord = new Coordonnees(5, 5);
            Tetromino ref = instance(coord, Couleur.ORANGE);
            Tetromino t = instance(coord, Couleur.ORANGE);
            t.setPuits(puits);

            // tourner
            t.tourner(false);

            // Tests
            if (t instanceof OTetromino) {
                assertEquals(ref.getElements(), t.getElements(),
                        ERREUR_TOURNER + "la rotation d'un OTetromino ne doit pas être possible !");
            } else {
                assertNotEquals(ref.getElements(), t.getElements(),
                        ERREUR_TOURNER + "la pièce n'a pas tourné sur un mouvement valide !");
            }
        }

        @Test
        void testOperationNonValideRetourEnArriere() throws BloxException {
            Puits puits = new Puits(10, 20);
            Coordonnees coord = new Coordonnees(-10, -10);
            Tetromino ref = instance(coord, Couleur.ORANGE);
            Tetromino t = instance(coord, Couleur.ORANGE);
            t.setPuits(puits);

            // tourner
            if (t instanceof OTetromino) {
                t.tourner(false);
            } else {
                assertThrows(BloxException.class,
                    () -> t.tourner(false),
                    ERREUR_TOURNER + "exception non lancée alors que devrait de type BloxException !"
                );
            }

            // Tests
            assertEquals(ref.getElements(), t.getElements(),
                    ERREUR_TOURNER + "la pièce ne revient pas en errière sur un mouvement invalide !");
        }

        //
        //  Projection
        //

        @Test
        void testProjection() throws BloxException {
            Puits puits = new Puits(10, 20);
            Coordonnees initialCoords = new Coordonnees(5, 5);
            Tetromino t = instance(initialCoords, Couleur.JAUNE);
            t.setPuits(puits);
            puits.setPieceSuivante(t);
            puits.setPieceSuivante(t);
            puits.getPieceActuelle().setPosition(5, 5);

            // Enregistrement de la copie initiale
            Piece copyInitiale = (Piece) puits.getFantome().getCopyPiece().copy();

            // deplacerDe
            t.tourner(false);

            // Test
            if (!(t instanceof OTetromino)) {
                assertNotEquals(copyInitiale.getElements(),
                    puits.getFantome().getCopyPiece().getElements(),
                    ERREUR_SET_POSITION
                            + "la méthode projection() du puits n'a pas été appelée alors qu'elle aurait dûe !");
            }
        }
    }

    //
    // Tests copy()
    //

    @Test
    void testCopy() {
        Puits puits = new Puits(10, 20);
        Coordonnees initialCoords = new Coordonnees(5, 5);
        Tetromino t = instance(initialCoords, Couleur.VIOLET);
        t.setPuits(puits);

        // Tests
        assertTrue(t.copy() instanceof Tetromino, ERREUR_COPY + "la copie n'est pas une instance de Tetromino");
        assertNotSame(t, t.copy(), ERREUR_COPY + "l'instance de tertromino a mal été copiée !");
    }

    //
    // Tests rotation()
    //

    @Nested
    class TestsRotation {
        private Coordonnees initialCoords;
        private Tetromino tRef;
        private Tetromino t;
        private Method rotation;

        private boolean coordsTourne(Tetromino tRef, Tetromino t, boolean sens) {
            // Récupération des élements
            List<Element> eltsRef = tRef.getElements();
            List<Element> elts = t.getElements();

            // Tests
            boolean res = true;
            for (int i = 0; i < elts.size(); i++) {
                Coordonnees coordRef = eltsRef.get(i).getCoord();
                Coordonnees coord = elts.get(i).getCoord();

                res &= (sens ? -1 : 1) * coordRef.getOrdonnee() == coord.getAbscisse()
                        && (sens ? 1 : -1) * coordRef.getAbscisse() == coord.getOrdonnee();
            }

            return res;
        }

        @BeforeEach
        void setUp() throws NoSuchMethodException, SecurityException {
            this.initialCoords = new Coordonnees(0, 0);
            this.tRef = instance(initialCoords, Couleur.VERT);
            this.t = instance(initialCoords, Couleur.VERT);

            this.rotation = Tetromino.class.getDeclaredMethod("rotation", boolean.class);
            this.rotation.setAccessible(true);
        }

        @Test
        void testRotationSensAntiHoraire() throws IllegalAccessException, InvocationTargetException {
            // rotation
            this.rotation.invoke(this.t, false);

            // Tests rotation de coordonée
            assertTrue(coordsTourne(this.tRef, this.t, false), ERREUR_ROTATION
                    + "dans le sens anti horaire, les coodonnées des élements ne sont pas modifié ou pas correctement !");
        }

        @Test
        void testRotationSensHoraire() throws IllegalAccessException, InvocationTargetException {
            // rotation
            this.rotation.invoke(this.t, true);

            // Tests rotation de coordonée
            assertTrue(coordsTourne(this.tRef, this.t, true), ERREUR_ROTATION
                    + "dans le sens horaire, les coodonnées des élements ne sont pas modifié ou pas correctement !");
        }
    }

    //
    // Tests copyModel()
    //

    @Test
    void testCopyModel()
            throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
        Coordonnees coord = new Coordonnees(1, 10);
        Tetromino t = instance(coord, Couleur.ROUGE);

        Method copyModel = Tetromino.class.getDeclaredMethod("copyModel", Tetromino.class);
        copyModel.setAccessible(true);

        // copyModel
        Tetromino copy = (Tetromino) copyModel.invoke(t, instance(coord, Couleur.ROUGE));

        // Tests
        assertEquals(t.getPuits(), copy.getPuits(), ERREUR_COPY_MODEL
                + "le puits n'a pas été bien set dans la copie ou a été copié alors qu'il faut laisser le même !");

        // Elements
        List<Element> elts = t.getElements();
        List<Element> eltsCopy = copy.getElements();
        for (int i = 0; i < elts.size(); i++) {
            assertNotSame(elts.get(i), eltsCopy.get(i), ERREUR_COPY_MODEL
                    + "les élements de la copie sont les même que l'originale ce qui n'est pas normal !");
        }
    }
}
