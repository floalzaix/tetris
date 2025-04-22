package fr.eseo.e3.poo.projet.blox.modele.initialisation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Initialisateur {
    //
    // Variables d'instance
    //
    public final List<Initialisable> inits;

    //
    // Constructeurs
    //
    public Initialisateur() {
        this.inits = new ArrayList<>();
    }

    //
    // Méthodes
    //

    /**
     * Initialise toutes les classes ajoutée à l'initialisateur
     */
    public void initAll() {
        for (Initialisable i : this.inits) {
            i.init();
        }
    }

    public void ajouterInitialisable(Initialisable init) {
        this.inits.add(init);
    }

    /**
     * Pour ajouter aux classes à initialiser une classe dont la méthode init ne
     * dépend pas de l'instance
     */
    public void ajouterClasseInitialisable(Class<Initialisable> classe) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException, IllegalArgumentException {
        Constructor<Initialisable> cons = classe.getConstructor();
        this.inits.add(cons.newInstance());
    }
}
