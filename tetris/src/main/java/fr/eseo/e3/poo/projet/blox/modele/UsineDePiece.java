package fr.eseo.e3.poo.projet.blox.modele;

import java.util.Random;

import javax.naming.OperationNotSupportedException;

import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.ITetromino;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.OTetromino;
import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.Tetromino;

public class UsineDePiece {
    // Constantes
    public static final int ALEATOIRE_COMPLET = 1;
    public static final int ALEATOIRE_PIECE = 2;
    public static final int CYCLIC = 3;

    // Attributs
    private static int mode = 2;
    private static int selected = 0; // Pour générer de manière cyclique les pièces
    private static final Random rand = new Random();

    // Constructeur privé
    @SuppressWarnings("unused")
    private UsineDePiece() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Classe non instanciable !");
    }

    /**
     * Définie le mode de l'usine
     * 
     * @param mode Mode sous forme d'int qui correspond au constantes
     *             ALEATOIRE_COMPLET, ALEATOIRE_PIECE, CYCLIC
     */
    public static void setMode(int mode) {
        UsineDePiece.mode = mode;
    }

    /**
     * Génère de manière aléatoire en fonction de mode les Tetromino
     * 
     * @return Un Tetromino aléatoire
     * @throws AssertionError Si le mode n'est pas valide
     */
    public static Tetromino genererTetromino() throws AssertionError {
        Tetromino tetromino;
        switch (UsineDePiece.mode) {
            case ALEATOIRE_COMPLET:
                int r1 = rand.nextInt();
                int r2 = rand.nextInt(Couleur.values().length);
                if (r1 % 2 == 0) {
                    tetromino = new OTetromino(new Coordonnees(2, 3), Couleur.values()[r2]);
                } else {
                    tetromino = new ITetromino(new Coordonnees(2, 3), Couleur.values()[r2]);
                }
                break;
            case ALEATOIRE_PIECE:
                int r3 = rand.nextInt(2);
                if (r3 == 0) {
                    tetromino = new OTetromino(new Coordonnees(2, 3), Couleur.ROUGE);
                } else {
                    tetromino = new ITetromino(new Coordonnees(2, 3), Couleur.ORANGE);
                }
                break;
            case CYCLIC:
                if (selected == 0) {
                    tetromino = new OTetromino(new Coordonnees(2, 3), Couleur.ROUGE);
                    selected++;
                } else {
                    tetromino = new ITetromino(new Coordonnees(2, 3), Couleur.ORANGE);
                    selected = 0;
                }
                break;
            default:
                throw new AssertionError("Mode non valide !");
        }
        return tetromino;
    }
}
