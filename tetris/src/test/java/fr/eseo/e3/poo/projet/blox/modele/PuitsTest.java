package fr.eseo.e3.poo.projet.blox.modele;

import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.ITetromino;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.OTetromino;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.Tetromino;

class PuitsTest {
    public static final String END_OF_MESSAGE = "de la classe Puits  : ";
    public static final String START_OF_MESSAGE = "Erreur dans la méthode ";
    private final String errorConstructors = "Erreur dans les constructeurs : ";
    private final String errorSetPieceSuivante = "Erreur dans setPieceSuivante";
    private final String errorSetProfondeur = "Erreur dans setPosition : ";
    private final String errorSetLargueur = "Erreur dans setLargueur : ";
    private final String errorToString = "Erreur toString : ";
    public static final String ERREUR_GRAVITE = START_OF_MESSAGE + "gravite()" + END_OF_MESSAGE;
    public static final String ERREUR_GERER_COLLISION = START_OF_MESSAGE + "gerer_collision()" + END_OF_MESSAGE;
    public static final String ERREUR_LIMITE_HAUTEUR = START_OF_MESSAGE + "limiteHauteurAtteinte()" + END_OF_MESSAGE;

    /**
     * Tests constructeurs
     */

    @Test
    void testConstructeurIntIntIntInt() {
        Puits puits = new Puits(10, 20, 15, 3);
        assertNull(puits.getPieceActuelle(),
                this.errorConstructors + "piece actuelle non initialisé à null" + END_OF_MESSAGE);
        assertNull(puits.getPieceSuivante(),
                this.errorConstructors + "piece suivante non initialisé à null" + END_OF_MESSAGE);
        assertNotNull(puits.getTas(), this.errorConstructors + "le tas n'est pas initialisé" + END_OF_MESSAGE);
        assertDoesNotThrow(() -> puits.addPropertyChangeListener(_ -> {
        }), this.errorConstructors + "PropertyChangeListener mal initialisé" + END_OF_MESSAGE);
    }

    @Test
    void testConstructeurIntInt() {
        Puits puits = new Puits(10, 20);
        assertEquals(0, puits.getTas().getElements().size(),
                this.errorConstructors + "tas mal initialisé" + END_OF_MESSAGE);
    }

    @Test
    void testConstructeurVide() {
        Puits puits = new Puits();
        assertEquals(Puits.LARGUEUR_PAR_DEFAUT, puits.getLargueur(),
                this.errorConstructors + "mauvaise initialisation des tailles par défaut"
                        + END_OF_MESSAGE);
    }

    /**
     * Tests set piece suivante
     */

    @Test
    void testSetPieceSuivanteQuandPieceSuivanteNull() {
        Puits puits = new Puits(10, 20);
        OTetromino o = new OTetromino(new Coordonnees(10, 5), Couleur.ROUGE);

        // Test du déclenchement du listener
        AtomicBoolean actuelleChanged = new AtomicBoolean(false);
        AtomicBoolean suivanteChanged = new AtomicBoolean(false);
        PropertyChangeListener listener = evt -> {
            String name = evt.getPropertyName();
            actuelleChanged.set(name == null ? Puits.MODIFICATION_PIECE_ACTUELLE == null
                    : name.equals(Puits.MODIFICATION_PIECE_ACTUELLE));
            suivanteChanged.set(name == null ? Puits.MODIFICATION_PIECE_SUIVANTE == null
                    : name.equals(Puits.MODIFICATION_PIECE_SUIVANTE));
        };
        puits.addPropertyChangeListener(listener);

        puits.setPieceSuivante(o);

        assertEquals(puits, o.getPuits(),
                this.errorSetPieceSuivante + "le puits de la pièce n'est pas set" + END_OF_MESSAGE);
        assertTrue(suivanteChanged.get(), this.errorSetPieceSuivante
                + "listener non triggered lorsque la piece suivante a changé" + END_OF_MESSAGE);
        assertFalse(actuelleChanged.get(), this.errorSetPieceSuivante
                + "listener triggered lorsque la piece suivante a changé pour la piece actuelle alors que devrait pas"
                + END_OF_MESSAGE);

        assertEquals(o, puits.getPieceSuivante(),
                this.errorSetPieceSuivante + "pièce suivante pas set" + END_OF_MESSAGE);

        puits.removePropertyChangeListener(listener);
    }

    @Test
    void testSetPieceSuivanteAvecPieceSuivante() {
        Puits puits = new Puits(10, 20);
        OTetromino o = new OTetromino(new Coordonnees(1, 5), Couleur.ROUGE);
        ITetromino i = new ITetromino(new Coordonnees(1, 5), Couleur.ROUGE);

        // Test du déclenchement du listener
        AtomicBoolean actuelleChanged = new AtomicBoolean(false);
        AtomicBoolean suivanteChanged = new AtomicBoolean(false);
        PropertyChangeListener listener = evt -> {
            String name = evt.getPropertyName();
            if (Puits.MODIFICATION_PIECE_ACTUELLE.equals(name)) {
                actuelleChanged.set(true);
            } else if (Puits.MODIFICATION_PIECE_SUIVANTE.equals(name)) {
                suivanteChanged.set(true);
            }
        };
        puits.addPropertyChangeListener(listener);

        puits.setPieceSuivante(o);
        puits.setPieceSuivante(i);

        // Test nouvelle position de la pièce actuelle
        Coordonnees coord = o.getElements().get(0).getCoord();
        Coordonnees ref = new Coordonnees(puits.getLargueur() / 2, -4);
        assertEquals(ref, coord,
                this.errorSetPieceSuivante + "position mal/pas ajusté de la nouvelle pièce actuelle"
                        + END_OF_MESSAGE);

        assertEquals(puits, o.getPuits(),
                this.errorSetPieceSuivante + "le puits de la pièce n'est pas set" + END_OF_MESSAGE);
        assertTrue(suivanteChanged.get(), this.errorSetPieceSuivante
                + "listener non triggered lorsque la piece suivante a changé" + END_OF_MESSAGE);
        assertTrue(actuelleChanged.get(), this.errorSetPieceSuivante
                + "listener non triggered lorsque la piece actuelle a changé" + END_OF_MESSAGE);

        assertEquals(i, puits.getPieceSuivante(),
                this.errorSetPieceSuivante + "pièce suivante pas set" + END_OF_MESSAGE);
        assertEquals(o, puits.getPieceActuelle(),
                this.errorSetPieceSuivante + "pièce actuelle non set" + END_OF_MESSAGE);
    }

    @Test
    void testSetProfondeur() {
        Puits puits = new Puits(10, 20);
        puits.setProfondeur(15);
        assertEquals(15, puits.getProfondeur(),
                this.errorSetProfondeur + "profondeur non set" + END_OF_MESSAGE);
    }

    @Test
    void testSetProfondeurInf15() {
        Puits puits = new Puits(10, 20);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> puits.setProfondeur(14),
                this.errorSetProfondeur + "erreur non levé alors que profondeur inf à 15"
                        + END_OF_MESSAGE);
        assertEquals("Erreur un puits doit avoir une profondeur entre 15 et 25 unités !", e.getMessage(),
                this.errorSetProfondeur + "erreur non levé alors que profondeur inf à 15"
                        + END_OF_MESSAGE);
    }

    @Test
    void testSetProfondeurSup25() {
        Puits puits = new Puits(10, 20);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> puits.setProfondeur(26),
                this.errorSetProfondeur + "erreur non levé alors que profondeur sup à 25"
                        + END_OF_MESSAGE);
        assertEquals("Erreur un puits doit avoir une profondeur entre 15 et 25 unités !", e.getMessage(),
                this.errorSetProfondeur + "erreur non levé alors que profondeur inf à 15"
                        + END_OF_MESSAGE);
    }

    @Test
    void testSetLargueur() {
        Puits puits = new Puits(10, 20);
        puits.setLargueur(5);
        assertEquals(5, puits.getLargueur(), this.errorSetLargueur + "largueur non set" + END_OF_MESSAGE);
    }

    @Test
    void testSetLargueurInf5() {
        Puits puits = new Puits(10, 20);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> puits.setLargueur(4),
                this.errorSetLargueur + "exception non levé alors que la largueur inf à 4"
                        + END_OF_MESSAGE);
        assertEquals("Erreur un puits doit avoir une largueur entre 5 et 15 unités !", e.getMessage(),
                this.errorSetLargueur + "mauvais message d'erreur" + END_OF_MESSAGE);
    }

    @Test
    void testSetLargueurSup15() {
        Puits puits = new Puits(10, 20);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> puits.setLargueur(16),
                this.errorSetLargueur + "exception non levé alors que la largueur inf à 15"
                        + END_OF_MESSAGE);
        assertEquals("Erreur un puits doit avoir une largueur entre 5 et 15 unités !", e.getMessage(),
                this.errorSetLargueur + "mauvais message d'erreur" + END_OF_MESSAGE);
    }

    @Test
    void testToStringPieceSuivanteVide() {
        Puits puits = new Puits(10, 20);
        String res = "Puits : Dimension 10 x 20\nPiece Actuelle : <aucune>\nPiece Suivante : <aucune>\n";
        assertEquals(res, puits.toString(), this.errorToString
                + "mauvais retour de la fonction quand la pièce suivante est vide" + END_OF_MESSAGE);
    }

    @Test
    void testToStringPieceEtActuelleSet() {
        Puits puits = new Puits(10, 20);
        Piece o = new OTetromino(new Coordonnees(0, 0), Couleur.VIOLET);
        Piece i = new ITetromino(new Coordonnees(0, 0), Couleur.CYAN);
        puits.setPieceSuivante(o);
        puits.setPieceSuivante(i);
        String res = "Puits : Dimension 10 x 20\nPiece Actuelle : " //
                + o.toString() + //
                "Piece Suivante : " + i.toString();
        assertEquals(res, puits.toString(),
                "mauvais retour de la fonction toString quand la piece suivante n'est pas vide"
                        + END_OF_MESSAGE);
    }

    //
    // Tests gravite()
    //

    @Nested
    class TestsGravite {
        @Test
        void testDeplacementVertical() {
            Puits puits = new Puits(10, 20);
            Coordonnees coord = new Coordonnees(0, 0);
            OTetromino o = new OTetromino((Coordonnees) coord.copy(), Couleur.ORANGE);
            puits.setPieceSuivante(o);
            puits.setPieceSuivante(o);
            o.setPosition(0, 0);

            // Target
            coord.setOrdonnee(1);

            // gravite
            puits.gravite();

            // Tests
            assertEquals(coord, o.getElements().getFirst().getCoord(),
                    ERREUR_GRAVITE + "la gravité n'abaisse pas la pièce pour un déplacement valable !");
        }

        @Test
        void testAppelGestionCollision() {
            Puits puits = new Puits(10, 20);
            Coordonnees coord = new Coordonnees(0, 0);
            OTetromino o = new OTetromino((Coordonnees) coord.copy(), Couleur.ORANGE);
            puits.setPieceSuivante(o);
            puits.setPieceSuivante((OTetromino) o.copy());
            o.setPosition(5, 20);

            // gravite
            puits.gravite();

            // Tests
            assertNotSame(o, puits.getPieceActuelle(), ERREUR_GRAVITE
                    + "la méthode gererCollision() n'est pas appelé alors que déplacement entraine collision dû à la gravité !");
        }

        //
        // Test gererCollision()
        //

        @Nested
        class TestsGererCollision {
            private Method gererCollision;

            @BeforeEach
            void setUp() throws NoSuchMethodException, SecurityException {
                this.gererCollision = Puits.class.getDeclaredMethod("gererCollision");
                this.gererCollision.setAccessible(true);
            }

            @Test
            void testAjoutsElements() throws IllegalAccessException, InvocationTargetException {
                Puits puits = new Puits(10, 16);
                Coordonnees coord = new Coordonnees(5, 0);
                Tetromino o = new OTetromino(coord, Couleur.CYAN);
                puits.setPieceSuivante(o);
                puits.setPieceSuivante(o);
                o.setPosition(5, 16);

                // gererCollision
                this.gererCollision.invoke(puits);

                // Tests
                assertEquals(o.getElements(), puits.getTas().getElements(),
                        ERREUR_GERER_COLLISION + "les élements ne sont pas ajouté au tas alors que collision !");
            }

            @Test
            void testLimiteHauteurPasAtteinte() throws IllegalAccessException, InvocationTargetException {
                Puits puits = new Puits(10, 16);
                Coordonnees coord = new Coordonnees(5, 0);
                Tetromino o = new OTetromino(coord, Couleur.CYAN);
                Tetromino o2 = new OTetromino(coord, Couleur.CYAN);
                puits.setPieceSuivante(o);
                puits.setPieceSuivante(o2);
                o.setPosition(5, 16);

                // gererCollision
                this.gererCollision.invoke(puits);

                // Tests
                assertNotSame(o, puits.getPieceActuelle(),
                        ERREUR_GERER_COLLISION + "pièce actuele inchangé alors qu'il n'y a pas de défaites !");
                assertNotSame(o2, puits.getPieceSuivante(),
                        ERREUR_GERER_COLLISION + "pièce suivante inchangé alors qu'il y a pas défaite !");
            }

            @Test
            void testLimiteHauteurAtteinte() throws IllegalAccessException, InvocationTargetException {
                Puits puits = new Puits(10, 16);
                Coordonnees coord = new Coordonnees(5, 0);
                Tetromino i = new ITetromino(coord, Couleur.CYAN);
                puits.setPieceSuivante(i);
                puits.setPieceSuivante(i);
                i.setPosition(5, 1);

                // Listener
                AtomicBoolean triggered = new AtomicBoolean(false);
                PropertyChangeListener listener = evt -> {
                    triggered.set(true);
                    assertEquals(Puits.LIMITE_HAUTEUR_ATTEINTE,
                            evt.getPropertyName(),
                            ERREUR_GERER_COLLISION
                                    + "listener triggered mais mauvaise erreur pour la défaite et l'arrêt du controlleur de gravité !");
                };

                puits.addPropertyChangeListener(listener);

                // gererCollision
                this.gererCollision.invoke(puits);

                // Test si triggered défaite
                assertTrue(triggered.get(), ERREUR_GERER_COLLISION
                        + "listener pas triggered pour la défaite et l'arrêt du controlleur de gravité !");
            }
        }

        //
        //  Tests limiteHauteurAtteinte()
        //

        @Nested
        class TestsLimiteHauteurAtteinte {
            private Method limiteHauteurAtteinte;

            @BeforeEach
            void setUp() throws NoSuchMethodException {
                this.limiteHauteurAtteinte = Puits.class.getDeclaredMethod("limiteHauteurAtteinte");
                this.limiteHauteurAtteinte.setAccessible(true);
            }

            @Test
            void testLimiteAtteinte() throws IllegalAccessException, InvocationTargetException {
                Puits puits = new Puits(10, 20);
                Coordonnees coord = new Coordonnees(0, 0);
                Tetromino t = new OTetromino(coord, Couleur.BLEU);
                puits.setPieceSuivante(t);
                puits.setPieceSuivante(t);

                // Test
                assertTrue((boolean) this.limiteHauteurAtteinte.invoke(puits), ERREUR_LIMITE_HAUTEUR + "la méthode ne renvoie pas true alors que la limite est atteinte !");
            }

            @Test
            void tesElementValide() throws IllegalAccessException, InvocationTargetException {
                Puits puits = new Puits(10, 20);
                Coordonnees coord = new Coordonnees(0, 0);
                Tetromino t = new OTetromino(coord, Couleur.BLEU);
                puits.setPieceSuivante(t);
                puits.setPieceSuivante(t);
                t.setPosition(5, 5);

                // Test
                assertFalse((boolean) this.limiteHauteurAtteinte.invoke(puits), ERREUR_LIMITE_HAUTEUR + "la méthode ne renvoie pas false alors que la limite n'est pas atteinte !");
            }
        }
    }
}
