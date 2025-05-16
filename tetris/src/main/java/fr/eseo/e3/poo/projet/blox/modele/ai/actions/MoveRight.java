package fr.eseo.e3.poo.projet.blox.modele.ai.actions;

import fr.eseo.e3.poo.projet.blox.modele.BloxException;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public class MoveRight extends Action {
    //
    //  Constructeurs
    //
    public MoveRight(Piece piece) {
        super(piece);
    }

    @Override
    protected boolean executeAction() {
        try {
            this.piece.deplacerDe(1, 0);
            return false;
        } catch (IllegalArgumentException | BloxException _) {
            return true;
        }
    }

    @Override
    public void undoAction() {
        try {
            this.piece.deplacerDe(-1, 0);
        } catch (IllegalArgumentException | BloxException _) {
            // RAS
        }
    }
    
}
