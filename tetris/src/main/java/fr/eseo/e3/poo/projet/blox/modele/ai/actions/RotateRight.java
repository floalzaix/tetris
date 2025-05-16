package fr.eseo.e3.poo.projet.blox.modele.ai.actions;

import fr.eseo.e3.poo.projet.blox.modele.BloxException;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public class RotateRight extends Action {
    //
    //  Constructeur
    //
    public RotateRight(Piece piece) {
        super(piece);
    }

    @Override
    protected boolean executeAction() {
        try {
            this.piece.tourner(true);
            return false;
        } catch (BloxException _) {
            return true;
        }
    }

    @Override
    protected void undoAction() {
         try {
            this.piece.tourner(true);
        } catch (BloxException _) {
            // RAS
        }
    }
    
}
