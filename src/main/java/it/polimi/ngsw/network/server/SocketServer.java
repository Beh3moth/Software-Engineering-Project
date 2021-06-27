package it.polimi.ngsw.network.server;

import it.polimi.ngsw.network.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Socket server that handles all the new socket connection.
 */
public class SocketServer implements Runnable {
    private final Server server;
    private final int port;
    ServerSocket serverSocket;
    ServerLauncher starter;
    boolean isStarted;

    public SocketServer(Server server, int port, ServerLauncher start, ServerSocket socket) {
        this.server = server;
        this.port = port;
        this.starter = start;
        this.isStarted = false;
        this.serverSocket = socket;
    }

    @Override
    public synchronized void run() {
        int howMany = 0;
        while (!isStarted) {
            try {
                Socket client = serverSocket.accept();
                client.setSoTimeout(5000);
                SocketClientHandler clientHandler = new SocketClientHandler(this, client);
                Thread thread = new Thread(clientHandler, "ss_handler" + client.getInetAddress());
                thread.start();
                howMany++;
                server.getGameController().getGame().waitChosenNmber();
                if(server.getGameController().getGame().getChosenPlayersNumber() == howMany){
                    this.isStarted = true;
                    this.notifyAll();
                }
            } catch (IOException e) {
                Server.LOGGER.severe("Connection dropped");
            }
        }
    }

    /**
     * Handles the addition of a new client.
     *
     * @param nickname      the nickname of the new client.
     * @param clientHandler the ClientHandler of the new client.
     */
    public  void addClient(String nickname, ClientHandler clientHandler) {
        server.addClient(nickname, clientHandler);

    }

    /**
     * Forwards a received message from the client to the Server.
     *
     * @param message the message to be forwarded.
     */
    public void onMessageReceived(Message message) {
        server.onMessageReceived(message);
    }

    /**
     * Handles a client disconnection.
     *
     * @param clientHandler the ClientHandler of the disconnecting client.
     */
    public void onDisconnect(ClientHandler clientHandler) {
        server.onDisconnect(clientHandler);
    }

    public synchronized void waitStart() {
        if(!this.isStarted){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
