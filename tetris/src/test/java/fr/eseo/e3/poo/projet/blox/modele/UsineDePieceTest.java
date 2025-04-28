package fr.eseo.e3.poo.projet.blox.modele;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
    void testSetMode() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        Field f = UsineDePiece.class.getDeclaredField("mode");
        f.setAccessible(true);
        
        UsineDePiece.setMode(UsineDePiece.ALEATOIRE_COMPLET);
        
        // Tests
        assertEquals(UsineDePiece.ALEATOIRE_COMPLET, f.get(null), E_SET_MODE + "le mode est mal set !");
    }
}
