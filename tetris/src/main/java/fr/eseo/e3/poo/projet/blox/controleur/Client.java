package fr.eseo.e3.poo.projet.blox.controleur;

import java.awt.Color;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import fr.eseo.e3.poo.projet.blox.modele.Couleur;
import fr.eseo.e3.poo.projet.blox.modele.Joueur;

public class Client extends WebSocketClient {
    //
    // Variables d'instance
    //
    private Joueur joueur;
    private CountDownLatch latch;

    //
    // Constructeurs
    //
    public Client(URI uri) {
        super(uri);

        this.latch = new CountDownLatch(1);
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
        if (joueur == null && "COULEUR".equals(command)) {
            System.out.println(Color.getColor(params[1]));
            this.joueur = new Joueur(Color.getColor(params[1]));
            this.latch.countDown();
        } else {
            switch (command) {
                case "JOUEUR" -> {
                    Color c = Color.getColor(params[1]);
                    if (c != this.joueur.getCouleur()) {
                        this.joueur.getAutresJoueurs().add(c);
                    }
                }
                case "START" -> {
                    this.joueur.creationJeu(
                        Integer.parseInt(params[1]),
                        Integer.parseInt(params[2]),
                        Integer.parseInt(params[3]),
                        Integer.parseInt(params[4])
                    );
                }
                case "LIGNE" -> {
                    this.joueur.ajouterLigne(
                        Couleur.getCouleur(Color.getColor(params[1])),
                        Integer.parseInt(params[2])
                    );
                }
                case "ERREUR" -> {
                    System.out.println(params[1]);
                }
                default -> {
                    System.out.println("Mauvaise commande serveur !");
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

    // Getters setters
    public Joueur getJoueur() {
        return joueur;
    }
    public CountDownLatch getLatch() {
        return latch;
    }
}
