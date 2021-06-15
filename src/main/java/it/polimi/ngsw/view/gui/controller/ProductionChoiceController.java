package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class ProductionChoiceController extends ViewObservable implements GenericSceneController {
    @FXML
    public Pane devCards;
    @FXML
    public Pane leaderCards;
    @FXML
    public Button baseProductionPower;

    private LightModel lightModel;

    public void  setProductionChoiceController(LightModel lightModel){

    }

    @FXML
    public void initialize(){

    }


}
