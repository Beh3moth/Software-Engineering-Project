package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;


public class generic_error_controller extends ViewObservable implements GenericSceneController{

    @FXML
    private Button exit;

    @FXML
    public void initialize() {
        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onExitBtnClick);
    }

    public void onExitBtnClick(Event event){
        SceneController.changeScene(observers, event, "logo_scene.fxml");
    }
}
