package fr.eseo.e3.poo.projet.blox.modele.initialisation;

public interface Initialisable {
    /**
     * Méthode pour initialiser du contenu par exemple la liste des types de
     * tetromino. Cette méthode est faite pour être appelée dans une unce classe
     * Initialiseur qui appelera toutes les méthodes init pour rassembler cette
     * logique.
     */
    public void init();
}
