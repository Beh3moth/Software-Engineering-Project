package it.polimi.ngsw;

import it.polimi.ngsw.controller.ClientController;
import it.polimi.ngsw.network.client.Client;
import it.polimi.ngsw.view.cli.Cli;
import it.polimi.ngsw.view.gui.GuiStarter;
import javafx.application.Application;
//import it.polimi.ngsw.view.gui.JavaFXGui;
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
            cli.start();
        } else {
           Application.launch(GuiStarter.class);
        }
    }
}
