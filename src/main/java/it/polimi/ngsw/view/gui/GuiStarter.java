package it.polimi.ngsw.view.gui;

import it.polimi.ngsw.controller.ClientController;
import it.polimi.ngsw.network.client.Client;
import it.polimi.ngsw.view.gui.controller.LogoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import java.io.IOException;

public class GuiStarter extends Application {

    private Stage newStage;

    @Override
    public void start(Stage stage) throws IOException {
        Gui view = new Gui();
        ClientController clientController = new ClientController(view);
        view.addObserver(clientController);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/logo_scene.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            Client.LOGGER.severe(e.getMessage());
            System.exit(1);
        }
        LogoController controller = loader.getController();
        controller.addObserver(clientController);

        // Show the scene containing the root layout.
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(1280d);
        stage.setHeight(720d);
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setTitle("Masters of Renaissance");
        stage.show();
    }


}
