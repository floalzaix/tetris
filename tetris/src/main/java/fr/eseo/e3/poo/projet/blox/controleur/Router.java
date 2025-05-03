package fr.eseo.e3.poo.projet.blox.controleur;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Router {
    //
    //  Variables d'instances
    //
    private final CardLayout layout;
    private final JPanel panels;

    //
    //  Constructeurs
    //
    public Router(JFrame frame) {
        this.layout = new CardLayout();
        this.panels = new JPanel(this.layout);

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
    }

    /**
     * Permet de changer la page actuellement affichésur la frame en changeant le layout
     * @param routeName L'alias de la route
     */
    public void router(String routeName) {
        this.layout.show(panels, routeName);
    }
}
