package fr.eseo.e3.poo.projet.blox.modele.pieces;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Element;

class ElementTest {
    private final String endOfMessage = " de la classe Element !";

    private final String errorConstructors = "Erreur dans un des constructeurs : ";
    private final String errorDeplacerDe = "Erreur dans deplacerDe() : ";
    private final String equalsError = "Erreur dans equals() : ";
    private final String hashCodeError = "Erreur dans le hashCode() : ";

    /**
     * Test des constructeurs
     */

    @Test
    void testConstructeurIntIntCouleur() {
        Element e = new Element(1, -1, Couleur.BLEU);
        Coordonnees coord = new Coordonnees(1, -1);
        assertEquals(coord, e.getCoord(), this.errorConstructors
                + "le constructeur int int couleur définit mal les coordonnées" + this.endOfMessage);
    }

    @Test
    void testConstructeurIntIntDefCoord() {
        Element e = new Element(10, -20);
        Coordonnees coord = new Coordonnees(10, -20);
        assertEquals(coord, e.getCoord(),
                this.errorConstructors + "le constructeur int int définit mal les coordonnées" + this.endOfMessage);
    }

    @Test
    void testConstructeurIntIntDefCouleur() {
        Element e = new Element(3, -1);
        assertEquals(Couleur.values()[0], e.getCouleur(), this.errorConstructors
                + "le constructeur int int définit mal la couleur par défaut" + this.endOfMessage);
    }

    /**
     * Tests de deplacerDe
     */

    private static Stream<Arguments> provideValidesDXDYCoords() {
        return IntStream.rangeClosed(-1, 1)
                .boxed()
                .flatMap(dx -> IntStream.rangeClosed(0, 1)
                        .boxed()
                        .map(dy -> Arguments.of(dx, dy)));
    }

    @ParameterizedTest(name = "testDeplacerDe {index} dx {0} dy {1}")
    @MethodSource("provideValidesDXDYCoords")
    void testDeplacerDe(int dx, int dy) {
        Element e = new Element(2, -3);
        Coordonnees c = new Coordonnees(2 + dx, -3 + dy);
        e.deplacerDe(dx, dy);
        assertEquals(c, e.getCoord(),
                this.errorDeplacerDe + "le déplacement est set mal les abscisse ou/et ordonnee" + this.endOfMessage);
    }

    private static Stream<Arguments> provideNonValidesDXDYCoords() {
        return IntStream.of(-2, 2)
                .boxed()
                .flatMap(dx -> IntStream.of(-1, 2)
                        .boxed()
                        .map(dy -> Arguments.of(dx, dy)));
    }

    @ParameterizedTest(name = "testDeplacerDeExceptions {index} dx {0} dy {1}")
    @MethodSource("provideNonValidesDXDYCoords")
    void testDeplacerDeExceptions(int dx, int dy) {
        String message = this.errorDeplacerDe + "un deplacement invalide n'est pas détecté ou mauvais message d'erreur"
                + this.endOfMessage;
        Element e = new Element(new Coordonnees(5, -4));
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> e.deplacerDe(dx, dy),
                message);
        assertEquals(
                "Erreur le déplacement d'un pièce ne peut pas être supérieur à 1 ou ne peut pas aller vers le haut !",
                error.getMessage(), message);
    }

    @Test
    void testToString() {
        Element e = new Element(0, 0, Couleur.ROUGE);
        assertEquals("Element(0, 0) - ROUGE", e.toString(),
                "Erreur dans toString() : la fonction ne renvoie pas la bonne chaine de caractères"
                        + this.endOfMessage);
    }

    /**
     * Tests equals()
     */

    @Test
    void testEqualReflexivite() {
        Element coord = new Element(5, -88);
        assertEquals(coord, coord, this.equalsError + "réflexivité non vérifiée" + this.endOfMessage);
    }

    @Test
    void testEqualSymmetrie() {
        String message = this.equalsError + "symmétrie non vérifié" + this.endOfMessage;
        Element e1 = new Element(-1, 6);
        Element e2 = new Element(-1, 6);
        assertEquals(e1, e2, message);
        assertEquals(e2, e1, message);
    }

    @Test
    void testEqualTransitivite() {
        String message = this.equalsError + "transitivité non vérifiée" + this.endOfMessage;
        Element e1 = new Element(99, 96, Couleur.JAUNE);
        Element e2 = new Element(99, 96, Couleur.JAUNE);
        Element e3 = new Element(99, 96, Couleur.JAUNE);
        assertEquals(e1, e2, message);
        assertEquals(e2, e3, message);
        assertEquals(e3, e1, message);
    }

    @Test
    void testEqualNullite() {
        Element e = new Element(-6, 10);
        assertNotEquals(e, null, this.equalsError + "nullité non vérifié" + this.endOfMessage);
    }

    @Test
    void testEqualClassesDifferentes() {
        Element e = new Element(-4, -3);
        assertNotEquals(e, new Object(), this.equalsError + "egalité avec une autre classe" + this.endOfMessage);
    }

    private static Stream<Arguments> provideElements() {
        return IntStream.rangeClosed(-2, 2)
                .boxed()
                .flatMap(x1 -> IntStream.rangeClosed(-2, 2)
                        .boxed()
                        .flatMap(y1 -> IntStream.rangeClosed(-2, 2)
                                .boxed()
                                .flatMap(x2 -> IntStream.rangeClosed(-2, 3)
                                        .boxed()
                                        .flatMap(y2 -> Stream.of(Couleur.BLEU, Couleur.ROUGE)
                                                .flatMap(c1 -> Stream.of(Couleur.BLEU, Couleur.ROUGE)
                                                        .map(c2 -> Arguments.of(new Element(x1, y1, c1),
                                                                new Element(x2, y2, c2))))))));
    }

    private static Stream<Arguments> provideNonEgalsElements() {
        return provideElements().filter(args -> !args.get()[0].equals(args.get()[1]));
    }

    @ParameterizedTest(name = "testEqualNonEgalite {index} e1 {0} e2 {1}")
    @MethodSource("provideNonEgalsElements")
    void testEqualNonEgalite(Element e1, Element e2) {
        assertNotEquals(e1, e2, this.equalsError + "des éléments sont détecté égaux alors que non" + this.endOfMessage);
    }

    /**
     * Tests hashCode()
     */
    @Test
    void testHashCodeConstant() {
        Element e = new Element(10, -3);
        assertEquals(e.hashCode(), e.hashCode(), this.hashCodeError + "constance non vérifiée" + this.endOfMessage);
    }

    private static Stream<Arguments> provideEgalsElements() {
        return provideElements().filter(args -> args.get()[0].equals(args.get()[1]));
    }

    @ParameterizedTest(name = "testHashCodeEgalite {index} e1 {0} e2 {1}")
    @MethodSource("provideEgalsElements")
    void testHashCodeEgalite(Element e1, Element e2) {
        assertEquals(e1.hashCode(), e2.hashCode(),
                this.hashCodeError + "égalité non vérifiée pour deux même Objets" + this.endOfMessage);
    }

    @ParameterizedTest(name = "testHashCodeInegalite {index} x1 {0} y1 {1} x2 {2} y2 {3}")
    @MethodSource("provideNonEgalsElements")
    void testHashCodeInegalite(Element e1, Element e2) {
        assertNotEquals(e1.hashCode(), e2.hashCode(),
                this.hashCodeError + "inégalité non vérifiée pour deux objets différents" + this.endOfMessage);
    }
}
