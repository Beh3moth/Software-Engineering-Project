package it.polimi;

import it.polimi.controller.ClientController;
import it.polimi.network.client.Client;
import it.polimi.view.cli.Cli;
//import it.polimi.view.gui.JavaFXGui;
//import javafx.application.Application;

import java.util.logging.Level;

public class ClientStarter {
    public static void main(String[] args) {

        boolean doYouWantCli= false; // default value


        for (String arg : args) {
            if (arg.equals("--cli") || arg.equals("-c")) {
                doYouWantCli = true;
                break;
            }
        }

        if (doYouWantCli) {
            Client.LOGGER.setLevel(Level.WARNING);
            Cli cli = new Cli();
            ClientController clientcontroller = new ClientController(cli);
            cli.addObserver(clientcontroller);

            cli.init();


        } else {
           // Application.launch(JavaFXGui.class);
        }
    }
}
