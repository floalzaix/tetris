package fr.eseo.e3.poo.projet.blox.modele.ai.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.reflections.Reflections;

import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public abstract class Action {
    /**
     * Classe qui permet de stocker une 'action' autrement-dit déplacer à droite, à
     * gauche ... pour une pièce. Prévu pour usage dans le contexte de l'ia.
     */

    //
    // Variables de classe
    //
    protected static List<Class<? extends Action>> actions;

    //
    // Variables d'instance
    //
    protected final Piece piece;
    protected boolean executed;

    //
    // Constructeurs
    //
    protected Action(Piece piece) {
        this.piece = piece;
    }

    //
    //
    //

    //
    // Méthodes
    //

    /**
     * Squelettes de la méthode execute, la méthodo faire l'action
     */
    protected abstract boolean executeAction();

    /**
     * Méthode qui permet d'éxecuter l'action.
     * 
     * @return True si collision avec qqc
     */
    public boolean execute() {
        boolean res = this.executeAction();
        if (!res) {
            this.executed = true;
        }
        return res;
    }

    /**
     * Squelettes de la méthode undo, la méthodo pour défaire l'action faite.
     */
    protected abstract void undoAction();

    /**
     * Méthode qui permet de défaire l'action faite précemment
     */
    public void undo() {
        if (executed) {
            this.undoAction();
        }
    }

    //
    // Fonctions
    //

    /**
     * Initialise la list d'action ACTIONS en enregistrant ses sous classes
     */
    public static void init() {
        Reflections r = new Reflections("fr.eseo.e3.poo.projet.blox.modele.ai.actions");

        Action.actions = new ArrayList<>(r.getSubTypesOf(Action.class));
    }

    public static int getNbActions() {
        return Action.actions.size();
    }

    public static Action getAction(int index, Piece piece) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return Action.actions.get(index).getConstructor(Piece.class).newInstance(piece);
    }

    /**
     * Recuèpre l'indice de l'action selon la collection des actions
     * 
     * @param action L'action dotn on veur récupérer l'indice
     * @return L'indice de l'action en question
     */
    public static int getIndexOfAction(Action action) {
        return Action.actions.indexOf(action.getClass());
    }
}
