package fr.eseo.e3.poo.projet.blox.modele.pieces;

import fr.eseo.e3.poo.projet.blox.modele.BloxException;

public class Fantome {
    // Variables d'instance
    private final Piece piece;
    private Piece copyPiece;

    // Constructeurs
    public Fantome(Piece piece) {
        this.piece = piece;

        this.projection();
    }

    //
    // Fonctions
    //

    /**
     * Projection de la piece ce qui veut dire prediction de où seront les 
     * élements si la pièce est était déposée instantanement au fond du puits.
     */
    public final void projection() {
        this.copyPiece = (Piece) piece.copy();

        for (int i = 0; i <= piece.getPuits().getProfondeur() + 10; i++) {
            try {
                copyPiece.deplacerDe(0, 1);
            } catch (BloxException _) {
                break;
            }
        }
    }

    // Getters setters
    
    public Piece getCopyPiece() {
        return copyPiece;
    }
}
