package fr.eseo.e3.poo.projet.blox.modele.ai.actions;

import fr.eseo.e3.poo.projet.blox.modele.BloxException;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public class MoveLeft extends Action {
    //
    //  Constructeurs
    //
    public MoveLeft(Piece piece) {
        super(piece);
    }

    @Override
    protected boolean executeAction() {
        try {
            this.piece.deplacerDe(-1, 0);
            return false;
        } catch (IllegalArgumentException | BloxException _) {
            return true;
        }
    }

    @Override
    protected void undoAction() {
        try {
            this.piece.deplacerDe(1, 0);
        } catch (IllegalArgumentException | BloxException _) {
            // RAS
        }
    }
    
}
