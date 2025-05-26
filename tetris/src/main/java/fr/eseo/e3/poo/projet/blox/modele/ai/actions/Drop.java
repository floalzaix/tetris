package fr.eseo.e3.poo.projet.blox.modele.ai.actions;

import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public class Drop extends Action {
    //
    //   Constructeurs
    //
    public Drop(Piece piece) {
        super(piece);
    }

    @Override
    protected boolean executeAction() {
        this.piece.faireTomber();
        return false;
    }

    @Override
    protected void undoAction() {
        /* RAS */
    }
}
