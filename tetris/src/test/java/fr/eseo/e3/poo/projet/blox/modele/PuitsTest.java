package fr.eseo.e3.poo.projet.blox.modele;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Tas;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.ITetromino;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.OTetromino;

class PuitsTest {
    @Test
    void testConstructeur() {
        Puits puits = new Puits(Puits.LARGUEUR_PAR_DEFAUT, Puits.PROFONDEUR_PAR_DEFAUT);
        Puits puits2 = new Puits();
        assertEquals(puits.getLargueur(), puits2.getLargueur(), "Erreur dans le constructeur !");
        assertEquals(puits.getProfondeur(), puits2.getProfondeur(), "Erreur dans le constructeur !");
    }

    @Test
    void testSetPieceSuivante() {
        Puits puits = new Puits(10, 20);
        assertEquals(null, puits.getPieceActuelle(), "Erreur dans setPieceSuivante !");
        assertEquals(null, puits.getPieceSuivante(), "Erreur dans setPieceSuivante !");
        Piece o = new OTetromino(new Coordonnees(1, 0), Couleur.VERT);
        puits.setPieceSuivante(o);
        assertEquals(o, puits.getPieceSuivante(), "Erreur dans setPieceSuivante !");
        Piece i = new ITetromino(new Coordonnees(1, 2), Couleur.JAUNE);
        puits.setPieceSuivante(i);
        assertEquals(i, puits.getPieceSuivante(), "Erreur dans setPieceSuivante !");
        assertEquals(o, puits.getPieceActuelle(), "Erreur dans setPieceSuivante !");
        assertEquals(new Element(5, -4, Couleur.VERT), o.getElements().get(0), "Erreur dans setPiece !");
    }

    @Test
    void testSetProfondeur() {
        Puits puits = new Puits(10, 20);
        puits.setProfondeur(16);
        assertEquals(16, puits.getProfondeur());
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> puits.setProfondeur(26));
        assertEquals("Erreur un puits doit avoir une profondeur entre 15 et 25 unités !", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> puits.setProfondeur(9));
        assertEquals("Erreur un puits doit avoir une profondeur entre 15 et 25 unités !", e.getMessage());
    }

    @Test
    void testSetLargueur() {
        Puits puits = new Puits(10, 20);
        puits.setLargueur(9);
        assertEquals(9, puits.getLargueur(), "Erreur dans setLargueur !");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> puits.setLargueur(16), "Erreur dans setLargueur !");
        assertEquals("Erreur un puits doit avoir une largueur entre 5 et 15 unités !", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> puits.setLargueur(4), "Erreur dans setLargueur !");
        assertEquals("Erreur un puits doit avoir une largueur entre 5 et 15 unités !", e.getMessage());
    }

    @Test
    void testGetSetTas() {
        Puits puits = new Puits(10, 20);
        Tas tas = new Tas(puits, 20);
        puits.setTas(tas);
        assertEquals(tas, puits.getTas());
    }

    @Test
    void testToString() {
        Puits puits = new Puits(10, 20);
        String res = "Puits : Dimension 10 x 20\nPiece Actuelle : <aucune>\nPiece Suivante : <aucune>\n";
        assertEquals(res, puits.toString());
        Piece o = new OTetromino(new Coordonnees(0, 0), Couleur.VIOLET);
        Piece i = new ITetromino(new Coordonnees(0, 0), Couleur.CYAN);
        puits.setPieceSuivante(o);
        puits.setPieceSuivante(i);
        res = "Puits : Dimension 10 x 20\n" + //
                "Piece Actuelle : " + o.toString() + //
                "Piece Suivante : " + i.toString();
        assertEquals(res, puits.toString());
    }

    @Test
    void testGravite() {
        Puits puits = new Puits(10, 20);
        OTetromino o = new OTetromino(new Coordonnees(5, 20), Couleur.ORANGE);
        OTetromino o2 = new OTetromino(new Coordonnees(3, 18), Couleur.BLEU);
        puits.setPieceSuivante(o);
        puits.setPieceSuivante(o2);
        puits.gravite();
        assertEquals(o.getElements(), puits.getTas().getElements(), "Erreur dans la gravité !");
    }
}
