package fr.eseo.e3.poo.projet.blox.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Timer;

import fr.eseo.e3.poo.projet.blox.modele.Puits;
import fr.eseo.e3.poo.projet.blox.vue.VuePuits;

public class Gravite implements ActionListener, PropertyChangeListener {
    // Atttributs
    private final Timer timer;

    private final VuePuits vuePuits;
    private final Puits puits;

    public Gravite(VuePuits vuePuits) {
        this.vuePuits = vuePuits;
        this.puits = vuePuits.getPuits();

        this.timer = new Timer(400, this);
        this.timer.start();

        // S'enregistre au listeners de puits
        this.puits.addPropertyChangeListener(this);
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Puits.LIMITE_HAUTEUR_ATTEINTE.equals(evt.getPropertyName())) {
            this.timer.stop();
        }
    }
}
