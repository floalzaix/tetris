package fr.eseo.e3.poo.projet.blox.modele;

import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public class Puits {
    // Constantes de classes
    public static final int LARGUEUR_PAR_DEFAUT = 10;
    public static final int PROFONDEUR_PAR_DEFAUT = 20;

    // Attributs (variables d'instance)
    private int largueur;
    private int profondeur;

    private Piece pieceActuelle;
    private Piece pieceSuivante;

    // Constructeurs
    public Puits() {
        this.largueur = LARGUEUR_PAR_DEFAUT;
        this.profondeur = PROFONDEUR_PAR_DEFAUT;
    }

    public Puits(int largueur, int profondeur) {
        this.largueur = largueur;
        this.profondeur = profondeur;
    }

    // Overrides
    @Override
    public String toString() {
        String res = "Puits : Dimension " + this.largueur + " x " + this.profondeur + "\n";
        res += "Piece Actuelle : " + ((this.pieceActuelle == null) ? "<aucune>\n" : this.pieceActuelle);
        res += "Piece Suivante : " + ((this.pieceSuivante == null) ? "<aucune>\n" : this.pieceSuivante);
        return res;
    }

    // Getters et setters
    public Piece getPieceActuelle() {
        return pieceActuelle;
    }

    public Piece getPieceSuivante() {
        return pieceSuivante;
    }

    public int getProfondeur() {
        return profondeur;
    }

    public int getLargueur() {
        return largueur;
    }

    /**
     * Cette fonction définie la pièce suivante du puits. Si une pièce suivante est
     * déjà définie alors celle çi devient la pièce actuelle
     * 
     * @param pieceSuivante La pièce suivante du puits
     */
    public void setPieceSuivante(Piece pieceSuivante) {
        if (this.pieceSuivante != null) {
            this.pieceActuelle = this.pieceSuivante;
            this.pieceActuelle.setPosition(this.largueur / 2, -4);
        }
        this.pieceSuivante = pieceSuivante;
    }

    /**
     * Ce setter vérifie que la profondeur est comprise entre 15 et 25 inclus
     * 
     * @param profondeur La profondeur du puits
     * @throws IllegalArgumentException La profondeur doit être incluse entre 15 et 25
     */
    public void setProfondeur(int profondeur) throws IllegalArgumentException {
        if (profondeur < 15 || profondeur > 25) {
            throw new IllegalArgumentException("Erreur un puits doit avoir une profondeur entre 15 et 25 unités !");
        }
        this.profondeur = profondeur;
    }

    /**
     * Ce setter vérifie que la largueur est comprise entre 5 et 15 inclus
     * 
     * @param largueur La largueur du puits
     * @throws IllegalArgumentException La largueur doit être incluse entre 5 et 15
     */
    public void setLargueur(int largueur) throws IllegalArgumentException {
        if (largueur < 5 || largueur > 15) {
            throw new IllegalArgumentException("Erreur un puits doit avoir une largueur entre 5 et 15 unités !");
        }
        this.largueur = largueur;
    }
}
