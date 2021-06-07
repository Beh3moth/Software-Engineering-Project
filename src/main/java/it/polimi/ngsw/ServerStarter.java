package it.polimi.ngsw;
import it.polimi.ngsw.controller.GameController;
import it.polimi.ngsw.network.server.Server;
import it.polimi.ngsw.network.server.SocketServer;
import it.polimi.ngsw.network.server.ServerLauncher;
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
        ServerLauncher launch = new ServerLauncher(serverPort);
        launch.start();
    }

}
