package it.polimi;
import it.polimi.controller.GameController;
import it.polimi.network.server.Server;
import it.polimi.network.server.SocketServer;
/**
 * Main of the server app.
 */
public class ServerStarter {
    public static void main(String[] args) {
        int serverPort = 16847; // default value
        for (int i = 0; i < args.length; i++) {
            if (args.length >= 2 && (args[i].equals("--port") || args[i].equals("-p"))) {
                try {
                    serverPort = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    Server.LOGGER.warning("Invalid port specified. Using default port.");
                }
            }
        }
        GameController gameController = new GameController();
        Server server = new Server(gameController);
        SocketServer socketServer = new SocketServer(server, serverPort);
        Thread thread = new Thread (socketServer, "socketserver_");
        thread.start();
    }
}
