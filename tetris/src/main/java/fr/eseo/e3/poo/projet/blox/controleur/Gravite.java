package fr.eseo.e3.poo.projet.blox.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.vue.VuePuits;

public class Gravite implements ActionListener {
    // Atttributs
    private final Timer timer;

    private final VuePuits vuePuits;
    private final Puits puits;

    public Gravite(VuePuits vuePuits) {
        this.vuePuits = vuePuits;
        this.puits = vuePuits.getPuits();

        this.timer = new Timer(1000, this);
        this.timer.start();
    }

    // Overrides
    /// Simule la gravité en appelant la fonction gravité du puits associé à la vue
    @Override
    public void actionPerformed(ActionEvent e) {
        this.puits.gravite();
        this.vuePuits.repaint();
    }

    // Getters setters
    public int getPeriodicite() {
        return this.timer.getDelay();
    }

    public void setPeriodicite(int periodicite) {
        this.timer.setDelay(periodicite);
    }
}
