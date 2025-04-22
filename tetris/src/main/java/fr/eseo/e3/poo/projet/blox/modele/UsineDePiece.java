package fr.eseo.e3.poo.projet.blox.modele;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import javax.naming.OperationNotSupportedException;

import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.Tetromino;

public class UsineDePiece {
    // Constantes
    public static final int ALEATOIRE_COMPLET = 1;
    public static final int ALEATOIRE_PIECE = 2;
    public static final int CYCLIC = 3;

    public static final Coordonnees INIT_COORDONNEES = new Coordonnees(2, 4);

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
     * @throws IllegalArgumentException Si le mode n'est pas valide
     */
    public static Tetromino genererTetromino() {
        int nbTetromino = Tetromino.TETROMINOS.size();
        return switch (UsineDePiece.mode) {
            case ALEATOIRE_COMPLET -> genererAleatoireComplet(nbTetromino);
            case ALEATOIRE_PIECE -> genererAleatoirePiece(nbTetromino);
            case CYCLIC -> genererCyclic(nbTetromino);
            default -> {
                throw new IllegalArgumentException("Mode non valide !");
            }
        };
    }

    /**
     * Genère un tetromino en aléatoire complet donc pièce et couleur random.
     * 
     * @param nbTetromino Le nombre de types différents de tetrominos
     * @return Le tetromino aléatoire
     */
    private static Tetromino genererAleatoireComplet(int nbTetromino) {
        Tetromino t;

        // Préparation des paramètres
        Couleur[] couleurs = Couleur.values();

        int r1 = UsineDePiece.rand.nextInt(nbTetromino);
        int r2 = UsineDePiece.rand.nextInt(couleurs.length);

        Couleur couleur = Couleur.values()[r2];

        // Géneration de la pièce
        try {
            t = (Tetromino) Tetromino.TETROMINOS.get(r1).generer(INIT_COORDONNEES, couleur);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException("Problème de liste de Tetromino !");
        }

        return t;
    }

    /**
     * Genère un tetromino en aléatoire pièce donc pièce random et couleur par
     * défaut.
     * 
     * @param nbTetromino Le nombre de types différents de tetrominos
     * @return Le tetromino aléatoire
     */
    private static Tetromino genererAleatoirePiece(int nbTetromino) {
        Tetromino t;

        int r1 = UsineDePiece.rand.nextInt(nbTetromino);
        Tetromino modele = Tetromino.TETROMINOS.get(r1);

        // Géneration de la pièce
        try {
            t = (Tetromino) modele.generer(INIT_COORDONNEES, modele.getCouleurDefaut());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException("Problème de liste de Tetromino !");
        }

        return t;
    }

    /**
     * Genère un tetromino en aléatoire cyclic donc pièce cyclic et couleur par
     * défaut.
     * 
     * @param nbTetromino Le nombre de types différents de tetrominos
     * @return Le tetromino aléatoire
     */
    private static Tetromino genererCyclic(int nbTetromino) {
        Tetromino t;

        Tetromino modele = Tetromino.TETROMINOS.get(selected);

        UsineDePiece.selected++;
        if (UsineDePiece.selected >= nbTetromino) {
            UsineDePiece.selected = 0;
        }

        // Géneration de la pièce
        try {
            t = (Tetromino) modele.generer(INIT_COORDONNEES, modele.getCouleurDefaut());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException("Problème de liste de Tetromino !");
        }

        return t;
    }
}
