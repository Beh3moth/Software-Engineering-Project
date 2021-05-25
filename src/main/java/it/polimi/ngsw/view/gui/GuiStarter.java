package it.polimi.ngsw.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class GuiStarter extends Application {

    private Stage newStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameScene.fxml"));
        Parent root = loader.load();
        newStage = primaryStage;
        newStage.setTitle("Masters of the Renaissance");
        newStage.setResizable(false);
        newStage.setScene(new Scene(root));
        newStage.show();
    }


}
