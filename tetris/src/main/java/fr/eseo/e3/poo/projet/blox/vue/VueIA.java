package fr.eseo.e3.poo.projet.blox.vue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.eseo.e3.poo.projet.blox.modele.Jeu;
import fr.eseo.e3.poo.projet.blox.modele.ai.IA;

public class VueIA extends JPanel implements PropertyChangeListener {
    //
    //  Varaibles d'instance
    //
    private final IA ia;
    private final JFrame frame;
    private VueJeu vueJeu;

    //
    //  Constructeurs
    //
    public VueIA(IA ia, JFrame frame) {
        this.ia = ia;
        this.frame = frame;
        
        // Enreistrement pour récupérer les différents jeux crées
        this.ia.addPropertyChangeListener(this);
    }

    //  Overrides
    //

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (IA.EVT_CHANGEMENT_JEU.equals(evt.getPropertyName())) {
            if (this.vueJeu != null) {
                this.remove(this.vueJeu);
            }
            this.vueJeu = new VueJeu(null, (Jeu) evt.getNewValue(), true);

            this.add(this.vueJeu);

            this.setPreferredSize(this.vueJeu.getPreferredSize());
            this.frame.pack();
            this.frame.setLocationRelativeTo(null);
            this.revalidate();
        }
    }
}
