package fr.eseo.e3.poo.projet.blox.modele;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CoordonneesTest {
    public static final String END_OF_MESSAGE = " de la class Coordonnee  : ";
    public static final String START_OF_MESSAGE = "Erreur dans la méthode ";
    
    public static final String ERREUR_COPY = START_OF_MESSAGE + "copy()" + END_OF_MESSAGE;
    private final String equalsError = "Erreur dans equals() : ";
    private final String hashCodeError = "Erreur dans le hashCode() : ";

    @Test
    void testToString() {
        Coordonnees coord = new Coordonnees(80, 1000);
        assertEquals("Coordonnees(80, 1000)", coord.toString(), "Erreur dans toString() : chaine probablement erronée" + END_OF_MESSAGE);
    }

    //
    //  Tests copy()
    //

    @Test
    void testCopy() {
        Coordonnees coord = new Coordonnees(0, 0);

        Coordonnees copy = (Coordonnees) coord.copy();

        // Tests
        assertNotSame(coord, copy, ERREUR_COPY + "la copie reste de la même instance !");
    }

    /**
     * Tests equals() 
     */

    @Test
    void testEqualReflexivite() {
        Coordonnees coord = new Coordonnees(5, -88);
        assertEquals(coord, coord, this.equalsError + "réflexivité non vérifiée" + END_OF_MESSAGE);
    }

    @Test
    void testEqualSymmetrie() {
        String message = this.equalsError + "symmétrie non vérifié" + END_OF_MESSAGE;
        Coordonnees c1 = new Coordonnees(-1, 6);
        Coordonnees c2 = new Coordonnees(-1, 6);
        assertEquals(c1, c2, message);
        assertEquals(c2, c1, message);
    }

    @Test
    void testEqualTransitivite() {
        String message = this.equalsError + "transitivité non vérifiée" + END_OF_MESSAGE;
        Coordonnees c1 = new Coordonnees(99, 96);
        Coordonnees c2 = new Coordonnees(99, 96);
        Coordonnees c3 = new Coordonnees(99, 96);
        assertEquals(c1, c2, message);
        assertEquals(c2, c3, message);
        assertEquals(c3, c1, message);
    }

    @Test 
    void testEqualNullite() {
        Coordonnees coord = new Coordonnees(-6, 10);
        assertNotEquals(coord, null, this.equalsError + "nullité non vérifié" + END_OF_MESSAGE);
    }

    @Test
    void testEqualClassesDifferentes() {
        Coordonnees coord = new Coordonnees(-4, -3);
        assertNotEquals(coord, new Object(), this.equalsError + "egalité avec une autre classe" + END_OF_MESSAGE);
    }

    private static Stream<Arguments> provideCoords() {
        return IntStream.rangeClosed(-2, 2)
            .boxed()
            .flatMap(x1 -> IntStream.rangeClosed(-2, 2)
                .boxed()
                .flatMap(y1 -> IntStream.rangeClosed(-2, 2)
                    .boxed()
                    .flatMap(x2 -> IntStream.rangeClosed(-2, 3)
                        .boxed()
                        .map(y2 -> Arguments.of(new Coordonnees(x1, y1), new Coordonnees(x2, y2)))
                    )
                )
            );
    }

    private static Stream<Arguments> provideNonEgalesCoords() {
        return provideCoords().filter(args -> !args.get()[0].equals(args.get()[1]));
    }

    @ParameterizedTest(name = "testEqualNonEgalite {index} c1 {0} c2 {1}")
    @MethodSource("provideNonEgalesCoords")
    void testEqualNonEgalite(Coordonnees c1, Coordonnees c2) {
        assertNotEquals(c1, c2, this.equalsError + "égalité d'abscisses alors que non" + END_OF_MESSAGE);
    }

    /**
     * Tests hashCode()
     */

    @Test
    void testHashCodeConstant() {
        Coordonnees coord = new Coordonnees(10, -3);
        assertEquals(coord.hashCode(), coord.hashCode(), this.hashCodeError + "constance non vérifiée" + END_OF_MESSAGE);
    }

    private static Stream<Arguments> provideEgalsCoords() {
        return provideCoords().filter(args -> args.get()[0].equals(args.get()[1]));
    }

    @ParameterizedTest(name = "testHashCodeEgalite {index} c1 {0} c2 {1}")
    @MethodSource("provideEgalsCoords")
    void testHashCodeEgalite() {
        Coordonnees c1 = new Coordonnees(-3, 9);
        Coordonnees c2 = new Coordonnees(-3, 9);
        assertEquals(c1.hashCode(), c2.hashCode(), this.hashCodeError + "égalité non vérifiée pour deux même Objets" + END_OF_MESSAGE);
    }

    @ParameterizedTest(name = "testHashCodeInegalite {index} c1 {0} c2 {1}")
    @MethodSource("provideNonEgalesCoords")
    void testHashCodeInegalite(Coordonnees c1, Coordonnees c2) {
        assertNotEquals(c1.hashCode(), c2.hashCode(), this.hashCodeError + "inégalité non vérifiée pour deux objets différents" + END_OF_MESSAGE);
    }
}
