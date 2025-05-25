package fr.eseo.e3.poo.projet.blox.modele.ai;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class Etat {
    /**
     * Classe d'état pour l'algorithme de Q Learning en place pour entrainer l'ia.
     * Un état c'est le statut du plateau, la carte avec les élements présents dans
     * le puits sous forme de 0 et de 1 dans un tableau de float 1D. Les méthodes
     * permettent cependant d'avoir l'impression de 2D.
     */

    //
    // Constantes de classe
    //
    private static final int PIECE_SUIVANTE_OFFSET_ABSCISSE = -1;
    private static final int PIECE_SUIVANTE_OFFSET_ORDONNEE = -4 + 2;
    private static final int PIECE_ACTUELLE_OFFSET_ORDONNEE = 2 * 4 + 3;

    private static final int TAS_OFFSET_ORDONNEE = 2 * 4 + 3;

    //
    // Variables d'instance
    //
    private final int largeur;
    private final int profondeur;
    private final float[] carteJeu;

    private final int etatLargeur;
    private final int etatProfondeur;

    //
    // Constructeurs
    //
    public Etat(int largeur, int profondeur) {
        this.largeur = largeur;
        this.profondeur = profondeur;

        this.etatLargeur = this.largeur;
        this.etatProfondeur = this.profondeur + Etat.TAS_OFFSET_ORDONNEE;

        this.carteJeu = new float[this.largeur * (this.profondeur + Etat.TAS_OFFSET_ORDONNEE)];
    }

    //
    // Méthodes
    //

    /**
     * Récupère le INDArray du tableau carteJeu pour l'envoyer probablement à l'ia
     * 
     * @return Le INDArray de carteJeu
     */
    public INDArray get() {
        return Nd4j.create(this.carteJeu).reshape(1, this.carteJeu.length);
    }

    /**
     * Récupère la valeur de la présence d'un élement au coord x, y dans le puits
     * pour un élement du tas
     * pour le tas c-a-d ordonnee normale + 2 * nombre élement dans une pièce + 2
     * pour les deux ligne de négatif (cf def TAS_OFFSET_ORDONNE)
     * 
     * @param x Coordonnée x de l'élement dans le puits
     * @param y Coordonnée y de l'élement dans le puits
     * @return 1 s'il y a un élement présent, 0 sinon.
     */
    public int getTas(int x, int y) {
        return (int) this.carteJeu[x + this.largeur * (y + Etat.TAS_OFFSET_ORDONNEE)];
    }

    /**
     * Récupère la valeur de la présence d'un élement au coord x, y dans le puits
     * pour un élement de pièce actuelle
     * pour le tas c-a-d ordonnee normale + 2 * nombre élement dans une pièce + 2
     * pour les deux ligne de négatif (cf def TAS_OFFSET_ORDONNE)
     * 
     * @param x Coordonnée x de l'élement dans le puits
     * @param y Coordonnée y de l'élement dans le puits
     * @return 1 s'il y a un élement présent, 0 sinon.
     */
    public int getPieceActuelle(int x, int y) {
        return (int) this.carteJeu[x + this.largeur * (y + Etat.PIECE_ACTUELLE_OFFSET_ORDONNEE)];
    }

    /**
     * Set à 1 le coefficient x, y de l'état pour le tas
     * c-a-d ordonnee normale + 2 * nombre élement dans une pièce + 2 pour les deux
     * ligne de négatif (cf def TAS_OFFSET_ORDONNE)
     * 
     * @param x Coordonnée x de l'élement dans le puits
     * @param y Coordonnée y de l'élement dans le puits
     */
    public void setTas(int x, int y) {
        this.carteJeu[x + this.largeur * (y + Etat.TAS_OFFSET_ORDONNEE)] = 1;
    }

    /**
     * Set à 1 le coefficient x, y de l'état pour la pièce actuelle
     * c-a-d ordonnee normale + 1 * nombre élement dans une pièce (pièce suivante)
     * 
     * @param x Coordonnée x de l'élement dans le puits
     * @param y Coordonnée y de l'élement dans le puits
     */
    public void setPieceActuelle(int x, int y) {
        this.carteJeu[x + this.largeur * (y + Etat.PIECE_ACTUELLE_OFFSET_ORDONNEE)] = 1;
    }

    /**
     * Set à 1 le coefficient x, y de l'état pour la pièce suivante
     * c-a-d ordonnee normale + 0 * nombre élement dans une pièce + -2 en abscisse
     * pour ses coordonnées initiales.
     * 
     * @param x Coordonnée x de l'élement dans le puits
     * @param y Coordonnée y de l'élement dans le puits
     */
    public void setPieceSuivante(int x, int y) {
        this.carteJeu[(x + Etat.PIECE_SUIVANTE_OFFSET_ABSCISSE)
                + this.largeur * (y + Etat.PIECE_SUIVANTE_OFFSET_ORDONNEE)] = 1;
    }

    /**
     * Set à 0 le coefficient x, y de l'état.
     * 
     * @param x Coordonnée x de l'élement dans l'état
     * @param y Coordonnée y de l'élement dans l'état
     */
    public void unset(int x, int y) {
        this.carteJeu[x + this.largeur * y] = 0;
    }

    //
    //  Overrrides
    //
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Etat :\n");
        for (int y = 0; y < this.etatProfondeur; y++) {
            for (int x = 0; x < this.largeur; x++) {
                int value = (int) this.carteJeu[x + this.etatLargeur * y];
                b.append("  ");
                b.append((value == 1) ? "x" : ".");
            }
            b.append('\n');
        }
        return b.toString();
    }

    //
    //  Functions
    //

    public static int getNumberOfInput(int largeurPuits, int profondeurPuits) {
        return largeurPuits * (profondeurPuits + Etat.TAS_OFFSET_ORDONNEE); 
    }

    // Getters setters
    
    public int getLargeur() {
        return this.largeur;
    }

    public int getProfondeur() {
        return this.profondeur;
    }
}
