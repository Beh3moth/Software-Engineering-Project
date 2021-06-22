package it.polimi.ngsw.network.server;
import it.polimi.ngsw.controller.GameController;
import it.polimi.ngsw.network.server.Server;
import it.polimi.ngsw.network.server.SocketServer;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Class that permit to launch the server
 */
public class ServerLauncher {
    private final int serverPort;
    private boolean continueListening;
    private ServerSocket serversocket;
    public ServerLauncher(int port){
        this.serverPort = port;
        this.continueListening = true;
    }
    public void start() {
        try {
            serversocket = new ServerSocket(serverPort);
            Server.LOGGER.info(() -> "Socket server started on port " + serverPort + ".");

        } catch (IOException e) {
            Server.LOGGER.severe("Server could not start!");
            return;
        }
        while (continueListening) {
            GameController gameController = new GameController();
            Server server = new Server(gameController);
            SocketServer socketServer = new SocketServer(server, serverPort, this, serversocket);
            Thread thread = new Thread(socketServer, "socketserver_");
            thread.start();
            socketServer.waitStart();
        }
    }

}
