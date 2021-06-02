package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class GameController extends ViewObservable implements GenericSceneController {

    private LightModel lightModel;

    @FXML
    private Pane mainPane;
    @FXML
    private Button take_marble;

    @FXML
    public void initialize(){
        mainPane.setVisible(true);
        take_marble.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTakeMarbleButtonClick);
    }

    public void setLightModel(LightModel lightModel){this.lightModel = lightModel;}

    public void onTakeMarbleButtonClick(Event event){
        take_marble_controller takeMarbleController = new take_marble_controller(this.lightModel);
        takeMarbleController.addAllObservers(observers);
        SceneController.changeScene(takeMarbleController, "take_marble_scene.fxml");
    }


}
