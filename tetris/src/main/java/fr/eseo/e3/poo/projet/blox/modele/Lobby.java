package fr.eseo.e3.poo.projet.blox.modele;

import java.awt.Color;
import java.net.InetSocketAddress;
import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Lobby extends WebSocketServer {
    //
    // Constantes de classe
    //
    public static final int PORT = 4000;

    //
    // Variables d'instances
    //
    private int largueur, profondeur, niveau, mode;
    private final HashMap<WebSocket, Color> users;
    private boolean started = false;

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
            Color color = Couleur.values()[users.size()].getCouleurPourAffichage();
            for (Color c : this.users.values()) {
                ws.send("JOUEUR|" + c);
            }
            users.put(ws, color);
            ws.send("COULEUR|" + color);
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
                int nbLignes = Integer.parseInt(msg);
                this.broadcast("LIGNES|" + this.users.get(ws) + "|" + nbLignes);
            }
            case "START" -> {
                this.started = true;
                this.broadcast(
                        "START" + "|" + this.largueur + "|" + this.profondeur + "|" + this.niveau + "|" + this.mode);
            }
            default -> {
                ws.send("ERREUR|" + "Mauvaise commande !");
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
