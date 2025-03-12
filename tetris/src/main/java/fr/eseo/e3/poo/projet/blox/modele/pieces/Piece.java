package fr.eseo.e3.poo.projet.blox.modele.pieces;

import java.util.List;

import fr.eseo.e3.poo.projet.blox.modele.BloxException;
import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
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
     * en s'aasurant que l'élement de ref est au coordonnées passées en argument
     */
    public void setPosition(int abscisse, int ordonnee);

    /**
     * Cette fonction permet de déplacer une piéce d'un vecteur delta
     * 
     * @param deltaX Coordonnée X du vecteur delta
     * @param deltaY Coordonnée Y du vecteur delta
     * @throws IllegalArgumentException Si le déplacement est > 1 ou si déplacement
     *                                  vers le haut
     * @throws BloxException            Si le déplacement entraine une collision :
     *                                  BLOX_COLLISION ou si entraine une sortie de
     *                                  la pièce : BLOX_SORTIE_PUITS
     */
    public void deplacerDe(int deltaX, int deltaY) throws IllegalArgumentException, BloxException;

    /**
     * Permet de faire tourner la pièce autour de son élement de ref
     * 
     * @param sensHoraire Sens de la rotation
     * 
     * @throws BloxException Si la rotation entraine une collision :
     *                       BLOX_COLLISION ou si entraine une sortie de
     *                       la pièce : BLOX_SORTIE_PUITS
     */
    public void tourner(boolean sensHoraire) throws BloxException;

    // Fonction perso
    /**
     * Test si une pièce est en dehors du puits ou si rentre en collision avec le
     * tas. Cette fonction est faite pour être
     * utiliser aprés le mouvement pour vérifier sa validité
     * 
     * @param piece La pièce que l'on veut vérifié
     * @param puits Le puits de la pièce
     * @return False si la pièce n'est pas dehors ou est en collision. Sinon lève
     *         une exception
     * @throws BloxException Si la pièce rentre en collision avec un élement du tas
     *                       alors lève une exception BloxException avec le
     *                       paramètre BLOX_COLLISION
     *                       Si la pièce sort du puits lève la même exception avec
     *                       le paramètre BLOX_SORTIE_PUITS
     */
    public static boolean estDehorsOuCollision(Piece piece, Puits puits) throws BloxException {
        List<Element> tasElts = puits.getTas().getElements();
        for (Element elt : piece.getElements()) {
            // Récupération des coordonnées
            Coordonnees coord = elt.getCoord();
            int x = coord.getAbscisse();
            int y = coord.getOrdonnee();

            // Test si en dehors
            if (x < 0 || x > puits.getLargueur() - 1) {
                throw new BloxException("Une partie de la pièce est en dehors du puits !",
                        BloxException.BLOX_SORTIE_PUITS);
            }

            // Tests collisions
            /// Avec le fond
            if (y < 0 || y > puits.getProfondeur() - 1) {
                throw new BloxException("Une partie de la pièce est en dehors du fond du puits donc collision !",
                        BloxException.BLOX_COLLISION);
            }
            /// Avec un élement du tas
            for (Element tasElt : tasElts) {
                if (tasElt.getCoord().equals(coord)) {
                    throw new BloxException("Collision avec au moins un éléments !", BloxException.BLOX_COLLISION);
                }
            }
        }
        return false;
    }

    // Getters setters
    public Puits getPuits();

    public void setPuits(Puits puits);
}
