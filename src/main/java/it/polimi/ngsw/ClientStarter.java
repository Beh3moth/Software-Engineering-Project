package it.polimi.ngsw;

import it.polimi.ngsw.controller.ClientController;
import it.polimi.ngsw.network.client.Client;
import it.polimi.ngsw.view.cli.Cli;
import it.polimi.ngsw.view.cli.LocalCli;
import it.polimi.ngsw.view.gui.GuiStarter;
import javafx.application.Application;
//import it.polimi.ngsw.view.gui.JavaFXGui;
//import javafx.application.Application;

import java.util.logging.Level;

public class ClientStarter {
    public static void main(String[] args) {

        boolean doYouWantCli= false; // default value
        boolean doYouWantLocal= false; // default value
        for (String arg : args) {
            if (arg.equals("--cli") || arg.equals("-c")) {
                doYouWantCli = true;
                break;
            }
        }
        for (String arg : args) {
            if (arg.equals("--local") ||  arg.equals("-l")){
                doYouWantLocal = true;
                break;
            }
        }

        if (doYouWantCli && !doYouWantLocal) {
            Client.LOGGER.setLevel(Level.WARNING);
            Cli cli = new Cli();
            ClientController clientcontroller = new ClientController(cli);
            cli.addObserver(clientcontroller);
            cli.start();
        }
        else if (doYouWantCli && doYouWantLocal){
            LocalCli cli = new LocalCli();
            cli.start();
        }else {
           Application.launch(GuiStarter.class);
        }
    }
}
