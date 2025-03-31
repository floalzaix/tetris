package fr.eseo.e3.poo.projet.blox.modele;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CoordonneesTest {
    private final String endOfMessage = " de la class Coordonnee !";
    
    private final String equalsError = "Erreur dans equals() : ";
    private final String hashCodeError = "Erreur dans le hashCode() : ";

    @Test
    void testToString() {
        Coordonnees coord = new Coordonnees(80, 1000);
        assertEquals("Coordonnees(80, 1000)", coord.toString(), "Erreur dans toString() : chaine probablement erronée" + this.endOfMessage);
    }

    /**
     * Tests equals() 
     */

    @Test
    void testEqualReflexivite() {
        Coordonnees coord = new Coordonnees(5, -88);
        assertEquals(coord, coord, this.equalsError + "réflexivité non vérifiée" + this.endOfMessage);
    }

    @Test
    void testEqualSymmetrie() {
        String message = this.equalsError + "symmétrie non vérifié" + this.endOfMessage;
        Coordonnees c1 = new Coordonnees(-1, 6);
        Coordonnees c2 = new Coordonnees(-1, 6);
        assertEquals(c1, c2, message);
        assertEquals(c2, c1, message);
    }

    @Test
    void testEqualTransitivite() {
        String message = this.equalsError + "transitivité non vérifiée" + this.endOfMessage;
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
        assertNotEquals(coord, null, this.equalsError + "nullité non vérifié" + this.endOfMessage);
    }

    @Test
    void testEqualClassesDifferentes() {
        Coordonnees coord = new Coordonnees(-4, -3);
        assertNotEquals(coord, new Object(), this.equalsError + "egalité avec une autre classe" + this.endOfMessage);
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
        assertNotEquals(c1, c2, this.equalsError + "égalité d'abscisses alors que non" + this.endOfMessage);
    }

    /**
     * Tests hashCode()
     */

    @Test
    void testHashCodeConstant() {
        Coordonnees coord = new Coordonnees(10, -3);
        assertEquals(coord.hashCode(), coord.hashCode(), this.hashCodeError + "constance non vérifiée" + this.endOfMessage);
    }

    private static Stream<Arguments> provideEgalsCoords() {
        return provideCoords().filter(args -> args.get()[0].equals(args.get()[1]));
    }

    @ParameterizedTest(name = "testHashCodeEgalite {index} c1 {0} c2 {1}")
    @MethodSource("provideEgalsCoords")
    void testHashCodeEgalite() {
        Coordonnees c1 = new Coordonnees(-3, 9);
        Coordonnees c2 = new Coordonnees(-3, 9);
        assertEquals(c1.hashCode(), c2.hashCode(), this.hashCodeError + "égalité non vérifiée pour deux même Objets" + this.endOfMessage);
    }

    @ParameterizedTest(name = "testHashCodeInegalite {index} c1 {0} c2 {1}")
    @MethodSource("provideNonEgalesCoords")
    void testHashCodeInegalite(Coordonnees c1, Coordonnees c2) {
        assertNotEquals(c1.hashCode(), c2.hashCode(), this.hashCodeError + "inégalité non vérifiée pour deux objets différents" + this.endOfMessage);
    }
}
