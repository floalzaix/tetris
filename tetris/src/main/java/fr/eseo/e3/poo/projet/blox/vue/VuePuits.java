package fr.eseo.e3.poo.projet.blox.vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Optional;

import javax.swing.JPanel;

import fr.eseo.e3.poo.projet.blox.controleur.Clavier;
import fr.eseo.e3.poo.projet.blox.controleur.Souris;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Fantome;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;

/**
 * Gère l'affichage du puit
 */
public class VuePuits extends JPanel implements PropertyChangeListener {
    // Constantes
    public static final int TAILLE_PAR_DEFAUT = 20;

    // Attributs
    private Puits puits;
    private int taille;

    private VuePiece vuePiece;
    private VueFantome vueFantome;
    private final VueTas vueTas;

    private Souris souris;
    private Clavier clavier;

    // Constructeurs
    public VuePuits(Puits puits, int taille) {
        this.setPuits(puits);
        this.setTaille(taille);

        Optional<Piece> pieceActuelle = Optional.ofNullable(this.puits.getPieceActuelle()); 
        Optional<Fantome> fantome = Optional.ofNullable(this.puits.getFantome());

        pieceActuelle.ifPresent(
            p -> this.vuePiece = new VuePiece(p, taille)
        );
        fantome.ifPresent(
            f -> this.vueFantome = new VueFantome(f, taille)
        );
        this.vueTas = new VueTas(this, this.taille);

        this.setBackground(Color.WHITE);

        // Set du focus pour le clavier
        this.setFocusable(true);
    }

    public VuePuits(Puits puits) {
        this(puits, TAILLE_PAR_DEFAUT);
    }

    // Overrides
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Graphics2D
        Graphics2D g2d = (Graphics2D) g.create();

        // Grille
        /// Lignes
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= this.puits.getProfondeur() - 1; i++) {
            g2d.drawLine(0, i * this.taille, this.getWidth(), i * this.taille);
        }

        /// Colonnes
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= this.puits.getLargueur() - 1; i++) {
            g2d.drawLine(i * this.taille, 0, i * this.taille, this.getHeight());
        }

        // Piece actuelle
        if (this.vuePiece != null) {
            this.vuePiece.afficherPiece(g2d);
        }

        // Fantome
        if (this.vueFantome != null) {
            this.vueFantome.afficherFantome(g2d);
        }

        // Tas
        this.vueTas.afficher(g2d);

        // Libère la mémoire
        g2d.dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Puits.MODIFICATION_PIECE_ACTUELLE.equals(evt.getPropertyName())) {
            this.setVuePiece(new VuePiece((Piece) evt.getNewValue(), this.taille));
            this.vueFantome = new VueFantome(this.puits.getFantome(), this.taille);
        }
    }

    // Getters setter
    public Puits getPuits() {
        return this.puits;
    }

    public int getTaille() {
        return this.taille;
    }

    public VuePiece getVuePiece() {
        return this.vuePiece;
    }

    public VueTas getVueTas() {
        return this.vueTas;
    }

    public final void setPuits(Puits puits) {
        if (this.puits != null) {
            // On retire le listener de pièce suivante et actuelle
            puits.removePropertyChangeListener(this);

            // On retire la souris du puits
            if (this.souris != null) {
                this.removeMouseMotionListener(this.souris);
                this.removeMouseListener(this.souris);
                this.removeMouseWheelListener(this.souris);
                this.souris = null;
            }

            // On retire le clavier
            if (this.clavier != null) {
                this.removeKeyListener(clavier);
                this.clavier = null;
            }
        }

        this.puits = puits;

        // Ajout du listener pour quand les pièces suivante et actuelle du puits sont changées
        puits.addPropertyChangeListener(this);

        // Réglage de la taille
        this.setPreferredSize(new Dimension(this.taille * puits.getLargueur(), this.taille * puits.getProfondeur()));

        // Ajout de la souris
        this.souris = new Souris(this);
        this.addMouseMotionListener(this.souris);
        this.addMouseListener(this.souris);
        this.addMouseWheelListener(this.souris);

        // Ajout du clavier
        this.clavier = new Clavier(this);
        this.addKeyListener(this.clavier);
    }

    public final void setTaille(int taille) {
        this.taille = taille;
        this.setPreferredSize(new Dimension(taille * this.puits.getLargueur(), taille * this.puits.getProfondeur()));
    }

    private void setVuePiece(VuePiece vuePiece) {
        this.vuePiece = vuePiece;
    }
}
