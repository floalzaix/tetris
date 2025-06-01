package fr.eseo.e3.poo.projet.blox.modele.ai;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Tas;

public class AnalyseurTas {
    /**
     * Permet d'analyser les paramètres du tas.
     */

    //
    // Variables d'instance
    //
    private int ordonneeMin;
    private int nbLignes;
    private int nbTrousCrees;
    private int nbEmpilements;
    private double moyElementsParLignes;
    private Coordonnees minCoord;

    private final int largeur;
    private final int profondeur;
    private final int[][] map; // Mapping x, y du tas => plus opti pour les calculs

    //
    // Constructeurs
    //
    public AnalyseurTas(Tas tas) {
        Puits puits = tas.getPuits();
        this.largeur = puits.getLargueur();
        this.profondeur = puits.getProfondeur();
        this.map = new int[this.largeur][this.profondeur];

        this.ordonneeMin = this.profondeur - 1;
        this.nbTrousCrees = 0;
        this.nbEmpilements = 0;
        this.moyElementsParLignes = 0;
        this.minCoord = new Coordonnees(0, 0);
    }

    //
    // Méthodes
    //

    /**
     * Met à jour l'analyseur lorsque la méthode ajouterPiece du tas est appelée
     * 
     * @param piece La pièce ajouté au tas
     */
    public void updateAnalyseurPiece(Piece piece) {
        this.updateMap(piece);

        this.calcOrdonneeMin(piece);

        this.calcNbTrousCrees(piece);

        this.calcMoyElementsParLignes();

        this.calcMinCoordonnees();
    }

    /**
     * MAJ de la map en descendant les élement affecté par la ligne faite
     * 
     * @param ordonnee Ordonnee de ligne qui est faite
     */
    public void updateAnalyseurLigne(int ordonnee) {
        for (int y = ordonnee; y >= 0; y--) {
            for (int[] ligne : this.map) {
                ligne[y] = (y == 0) ? 0 : ligne[y - 1];
            }
        }

        this.ordonneeMin++;
        this.nbLignes++;

        this.calcMinCoordonnees();
    }

    private void updateMap(Piece piece) {
        for (Element e : piece.getElements()) {
            Coordonnees c = e.getCoord();
            if (c.getOrdonnee() >= 0) {
                this.map[c.getAbscisse()][c.getOrdonnee()] = 1;
            }
        }
    }

    private void calcOrdonneeMin(Piece piece) {
        for (Element e : piece.getElements()) {
            this.ordonneeMin = Math.min(this.ordonneeMin, e.getCoord().getOrdonnee());
        }
    }

    private void calcNbTrousCrees(Piece piece) {
        for (Element e : piece.getElements()) {
            Coordonnees c = e.getCoord();

            if (c.getOrdonnee() >= 0) {
                if (c.getOrdonnee() + 1 >= this.profondeur || this.map[c.getAbscisse()][c.getOrdonnee() + 1] == 1) {
                    this.nbEmpilements++;
                } else {
                    this.nbTrousCrees++;
                }
            }
        }
    }

    private void calcMoyElementsParLignes() {
        this.moyElementsParLignes = 0;
        int nbLignesNonVide = 0;
        for (int[] ligne : this.map) {
            int s = 0;

            for (int e : ligne) {
                s+= e;
            }

            if (s != 0) {
                this.moyElementsParLignes+= s;
                nbLignesNonVide++;
            }
        }
        this.moyElementsParLignes/= (nbLignesNonVide != 0) ?  nbLignesNonVide : 1;
    }

    private void calcMinCoordonnees() {
        boolean trouve = false;
        for (int y = this.profondeur - 1; y >= 0; y--) {
            for (int x = 0; x < this.largeur; x++) {
                if (this.map[x][y] == 0) {
                    boolean trou = false;
                    int i = y - 1;
                    while (!trou && i > 0) {
                        if (this.map[x][i] == 1) {
                            trou = true;
                        }
                        i--;
                    }
                    if (!trou && !trouve) {
                        this.minCoord = new Coordonnees(x, y);
                        trouve = true;
                        break;
                    }
                }
            }
        }
    }

    //
    //  Overrides
    //
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("\nAnalyseur de tas : ");

        b.append("Ordonnee minimum actuelle : ");
        b.append(this.ordonneeMin);
        b.append('\n');

        b.append("Nombre de lignes faites : ");
        b.append(this.nbLignes);
        b.append('\n');

        b.append("Nombre de trous crées : ");
        b.append(this.nbTrousCrees);
        b.append('\n');

        b.append("Nombre de d'empilements faits : ");
        b.append(this.nbEmpilements);
        b.append('\n');

        b.append("Moyenne d'élements par lignes : ");
        b.append(this.moyElementsParLignes);
        b.append('\n');

        b.append("Coords du point le plus bas : ");
        b.append(this.minCoord);
        b.append('\n');

        return b.toString();
    }

    // Getters setters
    public int getOrdonneeMin() {
        return ordonneeMin;
    }
    public int getNbLignes() {
        return nbLignes;
    }
    public int getNbTrousCrees() {
        return nbTrousCrees;
    }
    public int getNbEmpilements() {
        return nbEmpilements;
    }
    public double getMoyElementsParLignes() {
        return moyElementsParLignes;
    }
    public Coordonnees getMinCoord() {
        return minCoord;
    }
}
