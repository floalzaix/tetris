package fr.eseo.e3.poo.projet.blox.controleur;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Joueur;
import fr.eseo.e3.poo.projet.blox.modele.pieces.Tas;

public class Client extends WebSocketClient implements PropertyChangeListener {
    //
    //  Constantes de classe
    //
    public static final String EVT_NOUVEAU_JOUEUR = "NJ";
    
    //
    // Variables d'instance
    //
    private Joueur joueur;
    private CountDownLatch latch;

    private final PropertyChangeSupport pcs;

    //
    // Constructeurs
    //
    public Client(URI uri) {
        super(uri);

        this.latch = new CountDownLatch(1);

        // Nouveau Joueur
        this.pcs = new PropertyChangeSupport(this);
    }

    //
    // Méthodes
    //

    /**
     * Démarre la partie avec tous les joueurs présents dans le lobby
     */
    public void startJeu() {
        this.send("START");
    }

    //
    // Overrides
    //
    @Override
    public void onOpen(ServerHandshake sh) {
        // Pas utilisé ici !
    }

    @Override
    public void onMessage(String msg) {
        String[] params = msg.split("\\|");
        String command = params[0];
        if (joueur == null) {
            if ("COULEUR".equals(command)) {
                this.joueur = new Joueur(Couleur.getCouleur(params[1]));
                this.latch.countDown();
            }
        } else {
            switch (command) {
                case "JOUEUR" -> {
                    Couleur c = Couleur.getCouleur(params[1]);
                    if (c != this.joueur.getCouleur()) {
                        this.joueur.getAutresJoueurs().add(c);
                    }
                    this.pcs.firePropertyChange(EVT_NOUVEAU_JOUEUR, null, c);
                }
                case "START" -> {
                    this.joueur.creationJeu(
                        Integer.parseInt(params[1]),
                        Integer.parseInt(params[2]),
                        Integer.parseInt(params[3]),
                        Integer.parseInt(params[4])
                    );
                    this.joueur.getJeu().getPuits().getTas().addPropertyChangeListener(this);
                }
                case "LIGNES" -> {
                    Couleur couleur = Couleur.getCouleur(params[1]);
                    if (this.joueur.getCouleur() != couleur) {
                        this.joueur.ajouterLigne(
                        couleur,
                        Integer.parseInt(params[2])
                    );
                    }
                }
                case "ERREUR" -> {
                    System.out.println(params[1]);
                }
                default -> {
                    System.out.println("Mauvaise commande serveur :" + command);
                }
            }
        }
    }

    @Override
    public void onClose(int i, String string, boolean bln) {
        // Pas utilisé
    }

    @Override
    public void onError(Exception e) {
        System.out.println(e.getMessage());
    }

    // PropertyChangeListener
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Tas.EVT_LIGNE_COMPLETE.equals(evt.getPropertyName())) {
            this.send("LIGNES" + "|" + joueur.getCouleur() + "|" + evt.getNewValue());
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    // Getters setters
    public Joueur getJoueur() {
        return joueur;
    }
    public CountDownLatch getLatch() {
        return latch;
    }
}
