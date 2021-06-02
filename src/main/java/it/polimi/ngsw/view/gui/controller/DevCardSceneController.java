package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.observer.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class DevCardSceneController extends ViewObservable implements GenericSceneController {

    private DevCard[][] devCardMarket;

    @FXML
    private Button devCard00;
    @FXML
    private Button devCard01;
    @FXML
    private Button devCard02;
    @FXML
    private Button devCard10;
    @FXML
    private Button devCard11;
    @FXML
    private Button devCard12;
    @FXML
    private Button devCard20;
    @FXML
    private Button devCard21;
    @FXML
    private Button devCard22;
    @FXML
    private Button devCard30;
    @FXML
    private Button devCard31;
    @FXML
    private Button devCard32;

    public DevCardSceneController(DevCard[][] devCardMarket){

        this.devCardMarket = devCardMarket;

    }

    @FXML
    public void initialize(){
        devCard00.setGraphic(new ImageView("images/leader/discount1.png") );
    }



}
