package fr.eseo.e3.poo.projet.blox.controleur;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import fr.eseo.e3.poo.projet.blox.modele.BloxException;
import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Piece;
import fr.eseo.e3.poo.projet.blox.vue.VuePuits;

public class Clavier extends KeyAdapter {
    // Atributs
    private final VuePuits vuePuits;
    private final Puits puits;
    
    // Constructeurs
    public Clavier(VuePuits vuePuits) {
        this.vuePuits = vuePuits;
        this.puits = vuePuits.getPuits();
    }

    // Overrides
    @Override
    public void keyPressed(KeyEvent e) {
        Piece piece = this.puits.getPieceActuelle();
        if (piece != null) {
            System.out.println("test");
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> {
                    try {
                        piece.tourner(true);
                    } catch (BloxException e1) {
                        // Gestion des collision dans la gravité exception non nécessairement gérer ici
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    try {
                        piece.deplacerDe(0, 1);
                    } catch (BloxException e1) {
                        // Gestion des collision dans la gravité exception non nécessairement gérer ici
                    }
                }
                case KeyEvent.VK_LEFT -> {
                    try {
                        piece.deplacerDe(-1, 0);
                    } catch (BloxException e1) {
                        // Gestion des collision dans la gravité exception non nécessairement gérer ici
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    try {
                        piece.deplacerDe(1, 0);
                    } catch (BloxException e1) {
                        // Gestion des collision dans la gravité exception non nécessairement gérer ici
                    }
                }
                default -> {/* Rien ne se passe si une autre touche est pressée */}
            }
            vuePuits.repaint();
        }
    }
}
