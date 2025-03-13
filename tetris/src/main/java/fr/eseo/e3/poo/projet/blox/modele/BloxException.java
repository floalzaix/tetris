package fr.eseo.e3.poo.projet.blox.modele;

public class BloxException extends Exception {
    // Constantes 
    public static final int BLOX_COLLISION = 0;
    public static final int BLOX_SORTIE_PUITS = 1;
    public static final int BLOX_COLLISION_OU_BAS_PUITS = 2;

    // Attributs
    private final int type;

    // Constructeurs
    public BloxException(String message, int type) {
        super(message);

        this.type = type;
    }

    // Getters setters
    public int getType() {
        return this.type;
    }
}
