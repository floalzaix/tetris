package fr.eseo.e3.poo.projet.blox.modele.pieces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.BloxException;
import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.ITetromino;

class PieceTest {
    private Puits puits;
    private ITetromino i;

    @BeforeEach
    void setUp() {
        puits = new Puits(10, 20);
        i = new ITetromino(new Coordonnees(5, 5), Couleur.VERT);
        i.setPuits(puits);
    }    

    // Tests pour la fonction estDehorsOuCollision
    /// Haut
    @Test
    void testEstDehorsEnHaut() {
        i.setPosition(5, 2);
        try {
            Piece.estDehorsOuCollision(i, puits);
        } catch (BloxException be) {
            throw new AssertionError("Pas de encore dehors normalement dans les test dehors !");
        }
        i.setPosition(5, 1);
        BloxException be = assertThrows(BloxException.class, () -> Piece.estDehorsOuCollision(i, puits), "Erreur dans le test dehors en haut !");
        assertEquals(be.getType(), BloxException.BLOX_COLLISION, "Erreur dans le test dehors en haut !");
    }

    /// Bas
    @Test
    void testEstDehorsEnBas() {
        i.setPosition(5, 18);
        try {
            Piece.estDehorsOuCollision(i, puits);
        } catch (BloxException be) {
            throw new AssertionError("Pas de encore dehors normalement dans les test dehors !");
        }
        i.setPosition(5, 20);
        BloxException be = assertThrows(BloxException.class, () -> Piece.estDehorsOuCollision(i, puits), "Erreur dans le test dehors en bas !");
        assertEquals(be.getType(), BloxException.BLOX_COLLISION, "Erreur dans le test dehors en bas !");
    }

    /// Gauche
    @Test
    void testEstDehorsAGauche() {
        i.setPosition(0, 5);
        try {
            Piece.estDehorsOuCollision(i, puits);
        } catch (BloxException be) {
            throw new AssertionError("Pas de encore dehors normalement dans les test dehors !");
        }
        i.setPosition(-1, 5);
        BloxException be = assertThrows(BloxException.class, () -> Piece.estDehorsOuCollision(i, puits), "Erreur dans le test dehors a gauche !");
        assertEquals(be.getType(), BloxException.BLOX_SORTIE_PUITS, "Erreur dans le test dehors à gauche !");
    }

    /// Droite
    @Test
    void testEstDehorsADroite() {
        i.setPosition(9, 5);
        try {
            Piece.estDehorsOuCollision(i, puits);
        } catch (BloxException be) {
            throw new AssertionError("Pas de encore dehors normalement dans les test dehors !");
        }
        i.setPosition(10, 5);
        BloxException be = assertThrows(BloxException.class, () -> Piece.estDehorsOuCollision(i, puits), "Erreur dans le test dehors à droite !");
        assertEquals(be.getType(), BloxException.BLOX_SORTIE_PUITS, "Erreur dans le test dehors à droite !");
    }

    // Tests collision
    @Test 
    void testCollision() {
        puits = new Puits(10, 20, 10, 1);
        i.setPosition(5, 17);
        try {
            Piece.estDehorsOuCollision(i, puits);
        } catch (BloxException be) {
            throw new AssertionError("Pas de encore collision normalement dans le test collision !");
        }
        i.setPosition(5, 18);
        BloxException be = assertThrows(BloxException.class, () -> Piece.estDehorsOuCollision(i, puits), "Erreur dans le test collision !");
        assertEquals(be.getType(), BloxException.BLOX_COLLISION, "Erreur dans le test collision ! ");
    }
}
