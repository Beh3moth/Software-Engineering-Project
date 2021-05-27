package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.observer.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class GameController extends ViewObservable implements GenericSceneController {

    @FXML
    private Pane mainPane;

    @FXML
    public void initialize(){
        mainPane.setVisible(true);
    }

}
