package fr.eseo.e3.poo.projet.blox.modele;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import fr.eseo.e3.poo.projet.blox.modele.pieces.tetrominos.Tetromino;

class UsineDePieceTest {
    //
    // Constantes de classe
    //

    public static final String BASE_MESSAGE = "Erreur dans la classe UsineDePiece de la méthode : ";
    public static final String E_CONSTRUCTEUR = "Erreur de constructeur de la classe UsineDePiece !";
    public static final String E_SET_MODE = BASE_MESSAGE + "setMode() => ";
    public static final String E_GEN_TET = BASE_MESSAGE + "genererTetromino() => ";
    public static final String E_GEN_COMPLET = BASE_MESSAGE + "genererAleatoireComplet() => ";
    public static final String E_GEN_PIECE = BASE_MESSAGE + "genererAleatoirePiece() => ";
    public static final String E_GEN_CYCLIC = BASE_MESSAGE + "genererAleatoireCyclic() => ";

    //
    // Tests constructeurs
    //

    @Nested
    class testConstructeurs {
        @Test
        void testPrivateSansParam() throws NoSuchMethodException, SecurityException {
            Constructor<UsineDePiece> cons = UsineDePiece.class.getDeclaredConstructor();
            cons.setAccessible(true);

            // Tests
            InvocationTargetException e = assertThrows(InvocationTargetException.class,
                    cons::newInstance,
                    E_CONSTRUCTEUR
                            + "le constructeur privé sans param devrait levé une exception car classe non instanciable !");
            assertTrue(e.getCause() instanceof OperationNotSupportedException,
                    E_CONSTRUCTEUR + "mauvaise erreur levé !");
            assertEquals("Classe non instanciable !", e.getCause().getMessage(),
                    E_CONSTRUCTEUR + "mauvais message d'erreur !");
        }
    }

    //
    // Tests setMode()
    //

    @Test
    void testSetMode()
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        Field f = UsineDePiece.class.getDeclaredField("mode");
        f.setAccessible(true);

        UsineDePiece.setMode(UsineDePiece.ALEATOIRE_COMPLET);

        // Tests
        assertEquals(UsineDePiece.ALEATOIRE_COMPLET, f.get(null), E_SET_MODE + "le mode est mal set !");
    }

    //
    // Tests GENERATION
    //

    @Nested
    class testGeneration {
        //
        // Params
        //

        private static final int N = 100; // Nombre d'itérations pour vérifier que tous les Tetromino sont bien généré

        // Variables d'instance

        private int nbTetromino;

        @BeforeEach
        void setUp() {
            Tetromino.init();
            this.nbTetromino = Tetromino.TETROMINOS.size();
        }

        //
        // Fonctions perso
        //

        /**
         * Test si une méthode/fonction renvoie des Tetromino de manière aléatoire avec
         * couleur aléatoire.
         * 
         * Appel N fois la méthode en paramètres et enregistre les résultat pour voir si
         * les couleurs et le type de pièce sont aléatoires.
         * 
         * @param genererAleatoireComplet La méthode en question
         * @return True si aléatoire pièce et couleur false sinon
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        private boolean testAleatoireComplet(Method genererAleatoireComplet)
                throws IllegalAccessException, InvocationTargetException {
            List<Class<? extends Tetromino>> tets = new ArrayList<>();
            List<List<Couleur>> tetsCouleurs = new ArrayList<>();

            for (int i = 0; i < N; i++) {
                // Enregistre le type de pièce
                Tetromino tet = (Tetromino) genererAleatoireComplet.invoke(null);
                if (!tets.contains(tet.getClass())) {
                    tets.add(tet.getClass());
                    tetsCouleurs.add(new ArrayList<>());
                }

                // Enregistrement des nouvelles couleurs par type de pièce
                int tetIndex = tets.indexOf(tet.getClass());
                Couleur couleur = tet.getElements().getFirst().getCouleur();
                if (!tetsCouleurs.get(tetIndex).contains(couleur)) {
                    tetsCouleurs.get(tetIndex).add(couleur);
                }
            }

            return tets.size() == this.nbTetromino &&
                    tetsCouleurs.stream()
                            .map(List::size)
                            .filter(size -> size <= 1)
                            .toList()
                            .isEmpty();
        }

        /**
         * Test si la méthode/fonction retourne des Tetromino aléatoires mais juste les
         * pièce avec leurs couleur par défaut.
         * 
         * Appel N fois la méthode en paramètres et enregistre les résultat pour voir si
         * le type de pièce est aléatoire et la couleur par défaut.
         * 
         * @param genererAleatoirePiece La méthode que l'on teste
         * @return True si la méthode renvoie des pièce aléatoire avec leur couleur par
         *         défaut false sinon
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        private boolean testAleatoirePiece(Method genererAleatoirePiece)
                throws IllegalAccessException, InvocationTargetException {
            List<Class<? extends Tetromino>> tets = new ArrayList<>();
            boolean couleurParDefaut = true;

            for (int i = 0; i < N; i++) {
                // Enregistre le type de pièce
                Tetromino tet = (Tetromino) genererAleatoirePiece.invoke(null);
                if (!tets.contains(tet.getClass())) {
                    tets.add(tet.getClass());
                }

                // Test si la couleur du Tetromino n'est pas aléatoire (est celle par défaut)
                couleurParDefaut &= tet.getCouleurDefaut() == tet.getElements().getFirst().getCouleur();
            }

            return tets.size() == this.nbTetromino && couleurParDefaut;
        }

        /**
         * Test si la méthode/fonction retourne des Tetromino de manière cyclic mais
         * juste les
         * pièce avec leurs couleur par défaut.
         * 
         * Appel nbTetromino fois la méthode en paramètres et enregistre les résultat
         * pour voir si
         * le type de pièce est cyclic et la couleur par défaut.
         * 
         * @param genererAleatoireCyclic
         * @return True si type de pièce cyclic et couleur par défaut false sinon
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        private boolean testCyclic(Method genererCyclic)
                throws IllegalAccessException, InvocationTargetException {
            List<Class<? extends Tetromino>> tets = new ArrayList<>();
            boolean couleurParDefaut = true;

            for (int i = 0; i < this.nbTetromino; i++) { // <= Exécute que nbTetromino fois la boucle pour voir si
                                                         // tourne entre les Tetromnios
                // Enregistre le type de pièce
                Tetromino tet = (Tetromino) genererCyclic.invoke(null);
                if (!tets.contains(tet.getClass())) {
                    tets.add(tet.getClass());
                }

                // Test si la couleur du Tetromino n'est pas aléatoire (est celle par défaut)
                couleurParDefaut &= tet.getCouleurDefaut() == tet.getElements().getFirst().getCouleur();
            }

            return tets.size() == this.nbTetromino && couleurParDefaut;
        }

        //
        // Test genererTetromino()
        //

        @Test
        void testGenererTetrominoModeAC() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            UsineDePiece.setMode(UsineDePiece.ALEATOIRE_COMPLET);

            assertTrue(testAleatoireComplet(UsineDePiece.class.getMethod("genererTetromino")),
                    E_GEN_COMPLET + "les types ou les couleurs ne sont pas aléatoire !");
        }

        @Test
        void testGenererTetrominoModeAP() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            UsineDePiece.setMode(UsineDePiece.ALEATOIRE_PIECE);

            assertTrue(testAleatoirePiece(UsineDePiece.class.getMethod("genererTetromino")),
                    E_GEN_COMPLET + "les types ne sont pas aléatoire ou les couleurs ne sont pas celles par défaut !");
        }

        @Test
        void testGenererTetrominoModeC() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            UsineDePiece.setMode(UsineDePiece.CYCLIC);

            assertTrue(testCyclic(UsineDePiece.class.getMethod("genererTetromino")),
                    E_GEN_COMPLET + "les types ne sont pas cyclic ou les couleurs ne sont pas celles par défaut !");
        }
    }
}
