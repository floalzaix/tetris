package fr.eseo.e3.poo.projet.blox.modele;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import fr.eseo.e3.poo.projet.blox.modele.pieces.Fantome;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Tas;

public class Puits {
    // Constantes de classes
    public static final int LARGUEUR_PAR_DEFAUT = 10;
    public static final int PROFONDEUR_PAR_DEFAUT = 20;

    public static final String MODIFICATION_PIECE_ACTUELLE = "actuelle";
    public static final String MODIFICATION_PIECE_SUIVANTE = "suivante";
    public static final String LIMITE_HAUTEUR_ATTEINTE = "limite";

    // Attributs (variables d'instance)
    private int largueur;
    private int profondeur;

    private Piece pieceActuelle;
    private Piece pieceSuivante;

    private Fantome fantome;

    private Tas tas;

    private PropertyChangeSupport pcs;

    private int score;

    // Constructeurs
    public Puits(int largueur, int profondeur, int nbElements, int nbLignes) {
        this.largueur = largueur;
        this.profondeur = profondeur;
        this.pieceActuelle = null;
        this.pieceSuivante = null;
        this.fantome = null;

        this.tas = new Tas(this, nbElements, nbLignes);

        this.pcs = new PropertyChangeSupport(this);

        this.score = 0;
    }

    public Puits(int largueur, int profondeur) {
        this(largueur, profondeur, 0, 0);
    }

    public Puits() {
        this(LARGUEUR_PAR_DEFAUT, PROFONDEUR_PAR_DEFAUT);
    }

    // Overrides
    @Override
    public String toString() {
        String res = "Puits : Dimension " + this.largueur + " x " + this.profondeur + "\n";
        res += "Piece Actuelle : " + ((this.pieceActuelle == null) ? "<aucune>\n" : this.pieceActuelle);
        res += "Piece Suivante : " + ((this.pieceSuivante == null) ? "<aucune>\n" : this.pieceSuivante);
        return res;
    }

    /**
     * Gère la collision en disloquant la pièce actuelle sur le tas et en ajoutant
     * une pièce suivante au puits ce qui va donc pousser l'ancienne pièce suivante
     * à devenir la pièce actuelle.
     * Si la pièce est à la limite de hauteur alors déclenche un listener pour annoncer 
     * la défaite.
     */
    private void gererCollision() {
        this.tas.ajouterElements(this.pieceActuelle);
        
        // Gestion hauteur
        if (!limiteHauteurAtteinte()) {
            this.setPieceSuivante(UsineDePiece.genererTetromino());
        } else {
            this.pcs.firePropertyChange(LIMITE_HAUTEUR_ATTEINTE, 0, 1);
        }
    }


    /**
     * Tests si les éléments de la pièce actuelle est à la limite de hauteur. Dans le but de 
     * bloquer la génération de pièce si c'est le cas. Puis de fire un PropertyChangeListener
     * pour notifier de la défaite si collision à se niveau là donc par la fonction qui appelle
     * celle-ci.
     * @return True si le bloque contient des élements à la limite de hauteur false sinon.
     */
    private boolean limiteHauteurAtteinte() {
        List<Element> elements = this.pieceActuelle.getElements();

        for (Element e : elements) {
            if (e.getCoord().getOrdonnee() < 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Simule la gravité. De plus la méthode propose de tester si la pièce à atteint
     * le fond du puits ou est entrée verticalement en collision avec d'autres
     * éléments du tas
     */
    public void gravite() {
        try {
            this.pieceActuelle.deplacerDe(0, 1);
        } catch (BloxException be) {
            if (be.getType() == BloxException.BLOX_COLLISION_OU_BAS_PUITS) {
                this.gererCollision();
            }
        }
    }

    /**
     * Augmente le score du joueur de ce puits
     * 
     * @param score La valeur duquel augmenter le score
     */
    public void addScore(int score) {
        this.score += score;
    }

    // Listeners
    /**
     * Ajoute un property change listener qui se déclenchera quans setPieceSuivante
     * est appellé et qui déclenchera deux types d'évenements :
     * MODIFICATION_PIECE_ACTUELLE et MODIFICATION_PIECE_SUIVANTE
     * 
     * @param listener Le listener à ajouterau puits qui écoute quand
     *                 setPieceSuivante exécutée
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * Enlève le listener déjà mit.
     * 
     * @param listener Le listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    // Getters et setters
    public Piece getPieceActuelle() {
        return pieceActuelle;
    }

    public Piece getPieceSuivante() {
        return pieceSuivante;
    }

    public int getProfondeur() {
        return profondeur;
    }

    public int getLargueur() {
        return largueur;
    }

    /**
     * Cette fonction définie la pièce suivante du puits. Si une pièce suivante est
     * déjà définie alors celle çi devient la pièce actuelle avec les coordonnées
     * (largueurPuits/2, -4)
     * 
     * @param pieceSuivante La pièce suivante du puits
     */
    public void setPieceSuivante(Piece pieceSuivante) {
        if (this.pieceSuivante != null) {
            this.pieceSuivante.setPosition(this.largueur / 2, -4);
            this.fantome = new Fantome(this.pieceSuivante);
            this.pcs.firePropertyChange(MODIFICATION_PIECE_ACTUELLE, this.pieceActuelle, this.pieceSuivante);
            this.pieceActuelle = this.pieceSuivante;
        }
        pieceSuivante.setPuits(this);
        this.pcs.firePropertyChange(MODIFICATION_PIECE_SUIVANTE, this.pieceSuivante, pieceSuivante);
        this.pieceSuivante = pieceSuivante;
    }

    /**
     * Ce setter vérifie que la profondeur est comprise entre 15 et 25 inclus
     * 
     * @param profondeur La profondeur du puits
     * @throws IllegalArgumentException La profondeur doit être incluse entre 15 et
     *                                  25
     */
    public void setProfondeur(int profondeur) throws IllegalArgumentException {
        if (profondeur < 15 || profondeur > 25) {
            throw new IllegalArgumentException("Erreur un puits doit avoir une profondeur entre 15 et 25 unités !");
        }
        this.profondeur = profondeur;
    }

    /**
     * Ce setter vérifie que la largueur est comprise entre 5 et 15 inclus
     * 
     * @param largueur La largueur du puits
     * @throws IllegalArgumentException La largueur doit être incluse entre 5 et 15
     */
    public void setLargueur(int largueur) throws IllegalArgumentException {
        if (largueur < 5 || largueur > 15) {
            throw new IllegalArgumentException("Erreur un puits doit avoir une largueur entre 5 et 15 unités !");
        }
        this.largueur = largueur;
    }

    public Tas getTas() {
        return tas;
    }

    public void setTas(Tas tas) {
        this.tas = tas;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Fantome getFantome() {
        return fantome;
    }
}
