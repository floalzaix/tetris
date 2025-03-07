package fr.eseo.e3.poo.projet.blox.modele.pieces;

import java.util.List;

import fr.eseo.e3.poo.projet.blox.modele.Element;

public interface Piece {
    /**
     * Méthode qui récupère les élements d'une pièce
     * 
     * @return Les élements d'une pièce sous forme de List
     */
    public List<Element> getElements();

    /**
     * Méthode qui définit la position de la pièce en métant à jour ses coordonnées
     * en s'aasurant que l'élement de ref est au coordonnées passées
     */
    public void setPosition(int abscisse, int ordonnee);
}
