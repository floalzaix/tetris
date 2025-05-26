package fr.eseo.e3.poo.projet.blox.modele.ai;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.ai.actions.Action;
import fr.eseo.e3.poo.projet.blox.modele.ai.actions.Drop;
import fr.eseo.e3.poo.projet.blox.modele.ai.actions.MoveDown;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

public class Recompense {
    /**
     * Calcul les récompenses attribués à une action pour entrainer l'ia.
     */

    //
    //  Variables d'instance
    //
    private final AnalyseurTas analyseur;
    
    private double recompenseTot;

    private Action action;
    private Puits puits; 
    private boolean collision;
    private boolean defaite;

    // Anciennes valeurs mémoire
    private int oldOrdonneeMin;
    private int oldNbLignes;
    private int oldNbTrousCrees;
    private int oldNbEmpilements;
    private double oldMoyElementsParLignes;

    // Deltas
    private int deltaOrdonneeMin;
    private int deltaNbLignes;
    private int deltaNbTrousCrees;
    private int deltaNbEmpilements;
    private double deltaMoyElementsParLignes;

    //
    //  Constructeurs
    //
    public Recompense(AnalyseurTas analyseur) {
        this.analyseur = analyseur;

        this.oldOrdonneeMin = analyseur.getOrdonneeMin();
    }

    //
    //  Méthodes
    //
    public void update(Action action, Puits puits, boolean pose, boolean collision, boolean defaite) {
        this.recompenseTot = 0;

        this.action = action;
        this.puits = puits;
        this.collision = collision;
        this.defaite = defaite;

        // MAJ de la récompense
        this.appliqueBonusOrdonneeElementsPiece();

        this.appliqueMalusCollision(); // Pour éviter les mouvements inutiles
        this.appliqueBonusDescente();

        if (pose) {
            // Calc des deltas
            this.deltaOrdonneeMin = this.analyseur.getOrdonneeMin() - this.oldOrdonneeMin;
            this.deltaNbLignes = this.analyseur.getNbLignes() - this.oldNbLignes;
            this.deltaNbTrousCrees = this.analyseur.getNbTrousCrees() - this.oldNbTrousCrees;
            this.deltaNbEmpilements = this.analyseur.getNbEmpilements() - this.oldNbEmpilements;
            this.deltaMoyElementsParLignes = this.analyseur.getMoyElementsParLignes() - this.oldMoyElementsParLignes;

            // MAJ de la mémoire
            this.oldOrdonneeMin = this.analyseur.getOrdonneeMin();
            this.oldNbLignes = this.analyseur.getNbLignes();
            this.oldNbTrousCrees= this.analyseur.getNbTrousCrees();
            this.oldNbEmpilements = this.analyseur.getNbEmpilements();
            this.oldMoyElementsParLignes = this.analyseur.getMoyElementsParLignes();

            boolean AFF = false; // Temp

            this.appliqueBonusLignesCompletees();
            if (AFF) System.out.println("Lignes : " + this.recompenseTot);
            this.appliqueMalusDefaite();
            if (AFF) System.out.println("Defaite : " + this.recompenseTot);
            this.appliqueMalusHauteurTas();
            if (AFF) System.out.println("Hauteur : " + this.recompenseTot);
            this.appliqueMalusTrous();
            if (AFF) System.out.println("Trous : " + this.recompenseTot);
            this.appliqueBonusEmpilements();
            if (AFF) System.out.println("Empilements : " + this.recompenseTot);
            this.appliqueBonusMalusNbElementsParLignes();
            if (AFF) System.out.println("Moy elements par lignes : " + this.recompenseTot);
            this.appliqueBonusMinCoord();
            if (AFF) System.out.println("MinCoords : " + this.recompenseTot);

            try {
                if (AFF) Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }
        }
    }

    public double get() {
        return this.recompenseTot;
    }

    private void appliqueBonusOrdonneeElementsPiece() {
        Piece fantome = this.puits.getFantome().getCopyPiece();
        for (Element e : fantome.getElements()) {
            Coordonnees c = e.getCoord();
            this.recompenseTot += 6 * c.getOrdonnee() / (double) puits.getProfondeur() + 3;
        }
    }

    private void appliqueBonusLignesCompletees() {
        this.recompenseTot += 2500 * this.deltaNbLignes;
    }

    private void appliqueMalusDefaite() {
        this.recompenseTot -= (this.defaite) ? 1000 : 0;
    }

    private void appliqueMalusCollision() {
        if (this.collision && !(this.action instanceof MoveDown)) {
            this.recompenseTot -= 30;
        }
    }
    
    private void appliqueBonusDescente() {
        if (this.action instanceof MoveDown || this.action instanceof Drop) {
            this.recompenseTot += 1;
        }
    }

    private void appliqueMalusHauteurTas() {
        this.recompenseTot += 100 * this.deltaOrdonneeMin;
    }

    private void appliqueMalusTrous() {
        this.recompenseTot -= 20 * this.deltaNbTrousCrees;
    }

    private void appliqueBonusEmpilements() {
        this.recompenseTot += 5 * this.deltaNbEmpilements;
    }

    private void appliqueBonusMalusNbElementsParLignes() {
        this.recompenseTot += 2 * this.deltaMoyElementsParLignes;
    }

    private void appliqueBonusMinCoord() {
        Piece fantome = this.puits.getFantome().getCopyPiece();
        int xRef = this.analyseur.getMinCoord().getAbscisse();
        int yRef = this.analyseur.getMinCoord().getOrdonnee();
        for (Element e : fantome.getElements()) {
            int xF = e.getCoord().getAbscisse();
            int yF = e.getCoord().getOrdonnee();

            this.recompenseTot -= 12 * this.puits.getLargueur() - Math.sqrt(Math.pow((xRef - xF), 2) + Math.pow((yRef - yF), 2));
        }
    }
}