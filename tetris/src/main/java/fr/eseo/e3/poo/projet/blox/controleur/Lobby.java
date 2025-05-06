package fr.eseo.e3.poo.projet.blox.controleur;

import java.net.InetSocketAddress;
import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import fr.eseo.e3.poo.projet.blox.modele.Couleur;

public class Lobby extends WebSocketServer {
    //
    // Constantes de classe
    //
    public static final int PORT = 4000;

    //
    // Variables d'instances
    //
    private final int largueur;
    private final int  profondeur;
    private final int  niveau;
    private final int  mode;
    private final HashMap<WebSocket, Couleur> users;
    private boolean started;
    private int nbJoueur;

    //
    // Constructeurs
    //
    public Lobby(int largueur, int profondeur, int niveau, int mode) {
        super(new InetSocketAddress(PORT));

        this.largueur = largueur;
        this.profondeur = profondeur;
        this.niveau = niveau;
        this.mode = mode;
        this.users = new HashMap<>();
        this.started = false;
        this.nbJoueur = 0;
    }

    //
    // Overrides
    //

    @Override
    public void onOpen(WebSocket ws, ClientHandshake ch) {
        if (users.size() >= Couleur.values().length) {
            ws.send("ERREUR|" + "Trop de joueurs !");
        } else if (this.started) {
            ws.send("ERREUR|" + "Partie déjà commencé !");
        } else {
            Couleur color = Couleur.values()[users.size()];
            ws.send("COULEUR|" + color);
            for (Couleur c : this.users.values()) {
                ws.send("JOUEUR|" + c);
            }
            users.put(ws, color);
            this.nbJoueur++;
            this.broadcast("JOUEUR|" + color);
        }
    }

    @Override
    public void onClose(WebSocket ws, int i, String string, boolean bln) {
        // Non utilsé ici !
    }

    @Override
    public void onMessage(WebSocket ws, String msg) {
        String[] params = msg.split("\\|");
        String command = params[0];
        switch (command) {
            case "LIGNES" -> {
                int nbLignes = Integer.parseInt(params[2]);
                this.broadcast("LIGNES|" + this.users.get(ws) + "|" + nbLignes);
            }
            case "START" -> {
                this.started = true;
                this.broadcast(
                        "START" + "|" + this.largueur + "|" + this.profondeur + "|" + this.niveau + "|" + this.mode);
                this.broadcast("PLACE|" + this.nbJoueur);
            }
            case "DEFAITE" -> {
                this.nbJoueur--;
                this.broadcast("PLACE|" + this.nbJoueur);
            }
            default -> {
                ws.send("ERREUR|" + "Mauvaise commande : " + command);
            }
        }
    }

    @Override
    public void onError(WebSocket ws, Exception e) {
        ws.send(e.getMessage());
    }

    @Override
    public void onStart() {
        // N'est pas utilisé
    }

}
