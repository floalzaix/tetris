package fr.eseo.e3.poo.projet.blox.modele.pieces;

import java.util.List;

import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Puits;

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

    /**
     * Cette fonction permet de déplacer une piéce d'un vecteur delta
     * 
     * @param deltaX Coordonnée X du vecteur delta
     * @param deltaY Coordonnée Y du vecteur delta
     * @throws IllegalArgumentException Si le déplacement est > 1 ou si déplacement
     *                                  vers le haut
     */
    public void deplacerDe(int deltaX, int deltaY) throws IllegalArgumentException;

    // Getters setters
    public Puits getPuits();

    public void setPuits(Puits puits);
}
