package fr.eseo.e3.poo.projet.blox.modele;

import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.ITetromino;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.OTetromino;

class PuitsTest {
    public static final String END_OF_MESSAGE = "de la classe Puits !";
    private final String errorConstructors = "Erreur dans les constructeurs : ";
    private final String errorSetPieceSuivante = "Erreur dans setPieceSuivante";
    private final String errorSetProfondeur = "Erreur dans setPosition : ";
    private final String errorSetLargueur = "Erreur dans setLargueur : ";
    private final String errorToString = "Erreur toString : ";
    public static final String ERROR_GRAVITE = "Erreur dans gravite : ";

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
        assertEquals(0, puits.getTas().getElements().size(), this.errorConstructors + "tas mal initialisé" + END_OF_MESSAGE);
    }

    @Test
    void testConstructeurVide() {
        Puits puits = new Puits();
        assertEquals(Puits.LARGUEUR_PAR_DEFAUT, puits.getLargueur(),
                this.errorConstructors + "mauvaise initialisation des tailles par défaut" + END_OF_MESSAGE);
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
        assertEquals(ref, coord, this.errorSetPieceSuivante + "position mal/pas ajusté de la nouvelle pièce actuelle"
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
        assertEquals(15, puits.getProfondeur(), this.errorSetProfondeur + "profondeur non set" + END_OF_MESSAGE);
    }

    @Test
    void testSetProfondeurInf15() {
        Puits puits = new Puits(10, 20);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> puits.setProfondeur(14),
                this.errorSetProfondeur + "erreur non levé alors que profondeur inf à 15" + END_OF_MESSAGE);
        assertEquals("Erreur un puits doit avoir une profondeur entre 15 et 25 unités !", e.getMessage(),
                this.errorSetProfondeur + "erreur non levé alors que profondeur inf à 15" + END_OF_MESSAGE);
    }

    @Test
    void testSetProfondeurSup25() {
        Puits puits = new Puits(10, 20);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> puits.setProfondeur(26),
                this.errorSetProfondeur + "erreur non levé alors que profondeur sup à 25" + END_OF_MESSAGE);
        assertEquals("Erreur un puits doit avoir une profondeur entre 15 et 25 unités !", e.getMessage(),
                this.errorSetProfondeur + "erreur non levé alors que profondeur inf à 15" + END_OF_MESSAGE);
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
                this.errorSetLargueur + "exception non levé alors que la largueur inf à 4" + END_OF_MESSAGE);
        assertEquals("Erreur un puits doit avoir une largueur entre 5 et 15 unités !", e.getMessage(),
                this.errorSetLargueur + "mauvais message d'erreur" + END_OF_MESSAGE);
    }

    @Test
    void testSetLargueurSup15() {
        Puits puits = new Puits(10, 20);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> puits.setLargueur(16),
                this.errorSetLargueur + "exception non levé alors que la largueur inf à 15" + END_OF_MESSAGE);
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
                "mauvais retour de la fonction toString quand la piece suivante n'est pas vide" + END_OF_MESSAGE);
    }

    //
    //  Tests gravite()
    //
    
    @Nested
    class TestsGravite {
        @Test
        void testGravite() {
                Puits puits = new Puits(10, 21);
                OTetromino o = new OTetromino(new Coordonnees(5, 20), Couleur.ORANGE);
                OTetromino o2 = new OTetromino(new Coordonnees(3, 18), Couleur.BLEU);
                Coordonnees ref = new Coordonnees(5, 20);
                puits.setPieceSuivante(o);
                puits.setPieceSuivante(o2);
                puits.getPieceActuelle().setPosition(5, 20);
                puits.gravite();
                assertEquals(ref, o.getElements().getFirst().getCoord(), ERROR_GRAVITE
                        + "la gravite ne déplace pas de 1 verticalement les éléments de la pièce" + END_OF_MESSAGE);
        }

        @Test
        void testGraviteCollision() {
                Puits puits = new Puits(10, 20);
                OTetromino o = new OTetromino(new Coordonnees(5, 20), Couleur.ORANGE);
                OTetromino o2 = new OTetromino(new Coordonnees(3, 18), Couleur.BLEU);
                puits.setPieceSuivante(o);
                puits.setPieceSuivante(o2);
                puits.getPieceActuelle().setPosition(5, 20);
                puits.gravite();
                assertEquals(o.getElements(), puits.getTas().getElements(), "Erreur dans la gravité !");
                assertNotNull(puits.getPieceSuivante(),
                        ERROR_GRAVITE + "lors de la collision la pièce suivante n'a pas été changée" + END_OF_MESSAGE);
                assertNotEquals(puits.getPieceSuivante(), o2,
                        ERROR_GRAVITE + "lors de la collision la pièce suivante n'a pas été changée" + END_OF_MESSAGE);
        }
    }
}
