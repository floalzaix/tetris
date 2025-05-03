package fr.eseo.e3.poo.projet.blox.controleur;

import java.awt.CardLayout;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Routeur {
    //
    //  Variables d'instances
    //
    private final JFrame frame;
    private final CardLayout layout;
    private final JPanel panels;
    private final HashMap<String, JPanel> routes;

    //
    //  Constructeurs
    //
    public Routeur(JFrame frame) {
        this.frame = frame;
        this.layout = new CardLayout();
        this.panels = new JPanel(this.layout);
        this.routes = new HashMap<>();

        frame.add(panels);
    }

    //
    //  Méthodes
    //

    /**
     * Permet d'ajouter une route (donc un JPanel) au card layout
     * @param panel La page ajouter au layout
     * @param routeName L'alias donné à la page
     */
    public void ajouterRoute(JPanel panel, String routeName) {
        this.panels.add(panel, routeName);
        this.routes.put(routeName, panel);
    }

    /**
     * Permet de changer la page actuellement affichésur la frame en changeant le layout
     * @param routeName L'alias de la route
     */
    public void router(String routeName) {
        // Récupération panel
        JPanel panel = this.routes.get(routeName);

        // Changement de panel
        this.layout.show(this.panels, routeName);

        // Adaptation de la taille
        this.panels.setPreferredSize(panel.getPreferredSize());
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
    }
}
