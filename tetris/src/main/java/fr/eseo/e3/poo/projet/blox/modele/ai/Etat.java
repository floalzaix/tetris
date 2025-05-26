package fr.eseo.e3.poo.projet.blox.modele.ai;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Jeu;

public class Etat {
    /**
     * Classe d'état pour l'algorithme de Q Learning en place pour entrainer l'ia.
     * Un état c'est le statut du plateau, la carte avec les élements présents dans
     * le puits sous forme de 0 et de 1 dans un tableau de float 2D.
     * 
     * On considère que la pièce suivante se situe à (2, 4) et que la pièce actuelle
     * se trouve à (largeur / 2, -4)
     * 
     * Attention : la carte est un tableau qui commence par la profondeur puis par
     * la largeur
     */

    //
    // Constantes de classe
    //
    private static final int PIECE_SUIVANTE_OFFSET_ABSCISSE = -1;

    private static final int PIECE_SUIVANTE_OFFSET_ORDONNEE = -4 + 2;
    private static final int PIECE_ACTUELLE_OFFSET_ORDONNEE = 2 * 4 + 3;

    //
    // Variables d'instance
    //
    private final Jeu jeu;

    private final int largeur;
    private final int profondeur;
    private final float[][][][] carte; // Mapping avec des 0 et des 1 du jeu

    //
    // Constructeurs
    //
    public Etat(Jeu jeu) {
        this.jeu = jeu;

        // Calc dimentions
        this.largeur = this.jeu.getPuits().getLargueur();
        this.profondeur = this.jeu.getPuits().getProfondeur();

        // Initialisation de la carte pour mapper le jeu
        this.carte = new float[1][1][this.profondeur + Etat.PIECE_ACTUELLE_OFFSET_ORDONNEE][this.largeur];

        // Map les élements
        this.mapTas();
        this.mapPieceActuelle();
        this.mapPieceSuivante();
    }

    //
    // Méthodes
    //

    /**
     * Ajoute les élements du tas à la map sous forme de 1 si élement 0 sinon
     */
    private void mapTas() {
        this.jeu.getPuits().getTas().getElements().stream()
                .map(Element::getCoord)
                .forEach(c -> this.carte[0][0][c.getOrdonnee() + PIECE_ACTUELLE_OFFSET_ORDONNEE][c.getAbscisse()] = 1);
    }

    /**
     * Ajoute les élements de la pièce actuelle du puits à la map sou forme de 1 si
     * élement 0 sinon
     */
    private void mapPieceActuelle() {
        this.jeu.getPuits().getPieceSuivante().getElements().stream()
                .map(Element::getCoord)
                .forEach(c -> this.carte[0][0][c.getOrdonnee() + PIECE_ACTUELLE_OFFSET_ORDONNEE][c.getAbscisse()] = 1);
    }

    /**
     * Ajoute les élements de la pièce actuelle du puits à la map sou forme de 1 si
     * élement 0 sinon
     */
    private void mapPieceSuivante() {
        this.jeu.getPuits().getPieceSuivante().getElements().stream()
                .map(Element::getCoord)
                .forEach(c -> this.carte[0][0][c.getOrdonnee() + PIECE_SUIVANTE_OFFSET_ORDONNEE][c.getAbscisse()
                        + PIECE_SUIVANTE_OFFSET_ABSCISSE] = 1);
    }

    /**
     * Récupère le INDArray du tableau carte pour l'envoyer probablement à l'ia
     * 
     * @return Le INDArray de carte
     */
    public INDArray get() {
        return Nd4j.create(this.carte);
    }

    //
    // Overrides
    //

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Etat :\n");

        for (float[] colonne : this.carte[0][0]) {
            for (float element : colonne) {
                b.append((element == 0) ? '0' : '1');
                b.append("  ");
            }
            b.append('\n');
        }

        return b.toString();
    }
}
