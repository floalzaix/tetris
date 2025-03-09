package fr.eseo.e3.poo.projet.blox.modele;

import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.ITetromino;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.OTetromino;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.Tetromino;

class UsineDePieceTest {
    private final int N = 1000000;

    /**
     * Test de la génération simple avec les types de sorti les couleurs et le cycle
     */
    @Test
    void testGenererTetromino() {
        // Test aléatoire complet
        UsineDePiece.setMode(1);
        Tetromino tet = UsineDePiece.genererTetromino();
        assertTrue(tet instanceof OTetromino || tet instanceof ITetromino, "Erreur dans la generation de pièce !");

        // Test aléatoire des pièce mais pas des couleurs
        UsineDePiece.setMode(2);
        int i = 0;
        int ok = 0;
        do {
            tet = UsineDePiece.genererTetromino();
            assertTrue(tet instanceof OTetromino || tet instanceof ITetromino, "Erreur dans la generation de pièce !");
            if (tet instanceof OTetromino) {
                assertEquals(Couleur.ROUGE, tet.getElements().get(0).getCouleur(),
                        "Erreur dans la génération de pièce !");
                if (ok == 2) {
                    ok = 3;
                } else {
                    ok = 1;
                }
            } else if (tet instanceof ITetromino) {
                assertEquals(Couleur.ORANGE, tet.getElements().get(0).getCouleur(),
                        "Erreur dans la génération de pièce !");
                if (ok == 1) {
                    ok = 3;
                } else {
                    ok = 2;
                }
            }
            i++;
            if (i > 1000) {
                throw new AssertionError("Erreur dans la génération de pièce !");
            }
        } while (ok == 3);

        // Test du cycle
        UsineDePiece.setMode(3);
        tet = UsineDePiece.genererTetromino();
        assertTrue(tet instanceof OTetromino || tet instanceof ITetromino, "Erreur dans la generation de pièce !");
        int selected = 0;
        if (tet instanceof OTetromino) {
            selected = 1;
        } else if (tet instanceof ITetromino) {
            selected = 0;
        }
        tet = UsineDePiece.genererTetromino();
        assertTrue((selected == 0) ? tet instanceof OTetromino : tet instanceof ITetromino,
                "Erreur dans la génération de pièce !");

        // Test mode faux
        UsineDePiece.setMode(4);
        AssertionError e = assertThrows(AssertionError.class, UsineDePiece::genererTetromino);
        assertEquals("Mode non valide !", e.getMessage());
    }

    /**
     * Test l'aléatoire de la classe avec N le nombre ditérations pour faire les
     * moyennes
     */
    @Test
    void testGenererTetrominoAleatoire() {
        // Génération des moyennes pour l'aléatoire complet
        UsineDePiece.setMode(1);
        float nbrOTetromino = 0;
        float nbrITetromino = 0;
        final Couleur[] couleurs = Couleur.values();
        float[] nbrCouleurs = new float[couleurs.length];
        for (int i = 0; i < N; i++) {
            Tetromino tetromino = UsineDePiece.genererTetromino();
            if (tetromino == null) {
                throw new NullPointerException("Problème dans la génération des pièces !");
            }
            if (tetromino instanceof OTetromino) {
                nbrOTetromino++;
            } else if (tetromino instanceof ITetromino) {
                nbrITetromino++;
            }
            for (int j = 0; j < couleurs.length; j++) {
                if (tetromino.getElements().get(0).getCouleur() == couleurs[j]) {
                    nbrCouleurs[j]++;
                }
            }
        }

        // Test des moyennes
        testMoyennes(nbrOTetromino, nbrITetromino, couleurs, nbrCouleurs, true);

        // Génération des pièce aléatoire
        UsineDePiece.setMode(2);
        nbrOTetromino = 0;
        nbrITetromino = 0;
        for (int i = 0; i < N; i++) {
            Tetromino tetromino = UsineDePiece.genererTetromino();
            if (tetromino == null) {
                throw new NullPointerException("Problème dans la génération des pièces !");
            }
            if (tetromino instanceof OTetromino) {
                nbrOTetromino++;
            } else if (tetromino instanceof ITetromino) {
                nbrITetromino++;
            }
        }

        // Test des moyennes
        testMoyennes(nbrOTetromino, nbrITetromino, null, null, false);
    }

    private void testMoyennes(float nbrOTetromino, float nbrITetromino, Couleur[] couleurs, float[] nbrCouleurs,
            boolean testCouleur) {
        DecimalFormat df = new DecimalFormat("#.##");
        nbrOTetromino /= N;
        nbrITetromino /= N;
        assertEquals(1 / (float) 2, Float.parseFloat(df.format(nbrOTetromino).replace(",", ".")),
                "Erreur dans la génération de pièce !");
        assertEquals(1 / (float) 2, Float.parseFloat(df.format(nbrITetromino).replace(",", ".")),
                "Erreur dans la génération de pièce !");
        if (testCouleur) {
            for (int j = 0; j < couleurs.length; j++) {
                nbrCouleurs[j] /= N;
                float nbr = Float.parseFloat(df.format(1 / (float) couleurs.length).replace(",", "."));
                assertEquals(nbr, Float.parseFloat(df.format(nbrCouleurs[j]).replace(",", ".")),
                        "Erreur dans la génération de pièce !");
            }
        }
    }
}
