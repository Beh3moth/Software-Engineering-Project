package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Generic_error_controller extends ViewObservable implements GenericSceneController {

    private final Stage stage;
    private double xOffset;
    private double yOffset;

    @FXML
    private Button exit;
    @FXML
    private Label message;

    @FXML
    public void initialize() {
        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onExitBtnClick);
    }


    public Generic_error_controller(){
        stage = new Stage();
        stage.initOwner(SceneController.getActiveScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
        xOffset = 0;
        yOffset = 0;
    }

    /**
     * The method closes the stage.
     */
    public void onExitBtnClick(Event event){
        stage.close();
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    /**
     * The method sets the massage to be shown.
     * @param message is a string of the message to be shown. It can't be null.
     */
    public void setMessage(String message){
        this.message.setText(message);
    }

    public void displayMessage(){
        stage.showAndWait();
    }
    
}
