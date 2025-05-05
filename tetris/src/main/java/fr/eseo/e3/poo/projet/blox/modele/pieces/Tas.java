package fr.eseo.e3.poo.projet.blox.modele.pieces;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import fr.eseo.e3.poo.projet.blox.modele.Coordonnees;
import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Element;
import fr.eseo.e3.poo.projet.blox.modele.Puits;

public class Tas {
    // Attributs
    private List<Element> elements;
    private Puits puits;

    // Constructeurs
    public Tas(Puits puits, int nbElements, int nbLignes, Random rand) throws IllegalArgumentException {
        // Vérifie le nombre d'éléments
        if (nbElements > puits.getLargueur() * nbLignes || nbElements > puits.getLargueur() * puits.getProfondeur()) {
            throw new IllegalArgumentException("Erreur lors de la création du tas : trop d'éléments !");
        }

        this.elements = new ArrayList<>();
        this.puits = puits;

        // Initialisation du tas
        if (rand == null) {
            rand = new Random();
        }
        this.construireTas(nbElements, nbLignes, rand);
    }

    public Tas(Puits puits) throws IllegalArgumentException {
        this(puits, 0, 0, null);
    }

    public Tas(Puits puits, int nbElements) throws IllegalArgumentException {
        this(puits, nbElements, nbElements / puits.getLargueur() + 1, null); 
    }

    public Tas(Puits puits, int nbElements, int nbLignes) throws IllegalArgumentException {
        this(puits, nbElements, nbLignes, null);
    }

    /**
     * Créer le tas de base avec le nombre d'éléments indiqués et sans dépassé le nb
     * de lignes
     * 
     * @param nbElements Nombre d'éléments à placé
     * @param nbLignes   Nombre de lignes max
     * @param rand       L'objet Random permettant la génération aléatoire
     * @throws IllegalArgumentException S'il y a trop d'éléments par rapport au nb
     *                                  de lignes ou au puits.
     */
    private void construireTas(int nbElements, int nbLignes, Random rand) throws IllegalArgumentException {
        if (nbElements > puits.getLargueur() * nbLignes || nbElements > puits.getLargueur() * puits.getProfondeur()) {
            throw new IllegalArgumentException("Erreur lors de la création du tas : trop d'éléments !");
        }

        int nbPlace = 0;
        Couleur[] couleurs = Couleur.values();
        while (nbPlace != nbElements) {
            int ordon = this.puits.getProfondeur() - rand.nextInt(1, nbLignes + 1);
            int absci = rand.nextInt(0, this.puits.getLargueur());

            boolean occupe = false;
            for (Element elt : this.elements) {
                if ((new Coordonnees(absci, ordon)).equals(elt.getCoord())) {
                    occupe = true;
                }
            }

            if (!occupe) {
                int indiceCouleur = rand.nextInt(couleurs.length);
                this.elements.add(new Element(new Coordonnees(absci, ordon), couleurs[indiceCouleur]));
                nbPlace++;
            }
        }
    }

    /**
     * Ajoute les élements d'une pièce dans le tas. Crée dans le but qu'une pièce
     * touche le fond du puits et puis se disloque sur le tas
     * 
     * @param piece La pièce dont on va ajouter les éléments dans le tas
     */
    public void ajouterElements(Piece piece) {
        this.elements.addAll(piece.getElements());
        puits.addScore(ligneComplete());
    }

    /**
     * Test si une ligne est complète. Si c'est le cas alors descent celle de
     * dessuss de 1 et supprime la ligne.
     * 
     * @return Le score en fonction du nombre de ligne complété : 1 -> 40 2 -> 100 3
     *         -> 300 4 -> 1200
     * @throws IllegalArgumentException
     */
    private int ligneComplete() throws IllegalArgumentException {
        Map<Integer, List<Element>> lines = elements.stream()
                .collect(Collectors.groupingBy(e -> e.getCoord().getOrdonnee()));

        TreeMap<Integer, List<Element>> sortedLines = new TreeMap<>(Comparator.reverseOrder());
        sortedLines.putAll(lines);

        int s = 0;
        for (Map.Entry<Integer, List<Element>> e : sortedLines.entrySet()) {
            int size = e.getValue().size();

            // Faire descendre les element si des couches inférieurs ont étés retiré
            for (int i = 0; i < s; i++) {
                e.getValue().stream().forEach(elt -> elt.deplacerDe(0, 1));
            }

            // Tester s'il faut retirer la couche et incrémenté le nombre de couche retirées
            if (size == this.puits.getLargueur()) {
                s += 1;
                elements.removeAll(e.getValue());
            } else if (size > this.puits.getLargueur()) {
                throw new IllegalArgumentException(
                        "Le nombre d'élements ne peut pas être plus grand que le nombre de colonnes !");
            }
        }

        // Attributing score to the lines
        return switch (s) {
            case 0 -> 0;
            case 1 -> 40;
            case 2 -> 100;
            case 3 -> 300;
            default -> 1200;
        };
    }

    public void ajouterLigne(Color couleur) {
        // TODO !!!!!!!!!!!!
    }

    // Getters setters
    public List<Element> getElements() {
        return elements;
    }

    public Puits getPuits() {
        return puits;
    }
}
