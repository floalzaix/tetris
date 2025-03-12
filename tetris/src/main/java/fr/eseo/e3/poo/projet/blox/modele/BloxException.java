package fr.eseo.e3.poo.projet.blox.modele;

public class BloxException extends Exception {
    // Constantes 
    public static int BLOX_COLLISION = 0;
    public static int BLOX_SORTIE_PUITS = 1;

    // Attributs
    private int type;

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
