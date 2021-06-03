package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.observer.ViewObservable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;


public class DevCardSceneController extends ViewObservable implements GenericSceneController {

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

    private DevCard[][] devCardMarket;

    @FXML
    public void initialize(){
        setButtonsImages();
        devCard00.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenDevCard);
    }

    public void onChosenDevCard(Event event){
        Button button = (Button) event.getSource();
        int row = GridPane.getRowIndex(button);
        int col = GridPane.getColumnIndex(button);
        button.setDisable(true);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateChooseDevCard(2-row, col, 1))).start();
    }

    public void setButtonsImages(){
        setImage(devCard00, "images/devCard/" + devCardMarket[0][0].getCardColour().toString() + devCardMarket[0][0].getDevLevel() + devCardMarket[0][0].getPV() + ".png");
        setImage(devCard01, "images/devCard/" + devCardMarket[1][0].getCardColour().toString() + devCardMarket[1][0].getDevLevel() + devCardMarket[1][0].getPV() + ".png");
        setImage(devCard02, "images/devCard/" + devCardMarket[2][0].getCardColour().toString() + devCardMarket[2][0].getDevLevel() + devCardMarket[2][0].getPV() + ".png");
        setImage(devCard10, "images/devCard/" + devCardMarket[0][1].getCardColour().toString() + devCardMarket[0][1].getDevLevel() + devCardMarket[0][1].getPV() + ".png");
        setImage(devCard11, "images/devCard/" + devCardMarket[1][1].getCardColour().toString() + devCardMarket[1][1].getDevLevel() + devCardMarket[1][2].getPV() + ".png");
        setImage(devCard12, "images/devCard/" + devCardMarket[2][1].getCardColour().toString() + devCardMarket[2][1].getDevLevel() + devCardMarket[2][1].getPV() + ".png");
        setImage(devCard20, "images/devCard/" + devCardMarket[0][2].getCardColour().toString() + devCardMarket[0][2].getDevLevel() + devCardMarket[0][2].getPV() + ".png");
        setImage(devCard21, "images/devCard/" + devCardMarket[1][2].getCardColour().toString() + devCardMarket[1][2].getDevLevel() + devCardMarket[1][2].getPV() + ".png");
        setImage(devCard22, "images/devCard/" + devCardMarket[2][2].getCardColour().toString() + devCardMarket[2][2].getDevLevel() + devCardMarket[2][2].getPV() + ".png");
        setImage(devCard30, "images/devCard/" + devCardMarket[0][3].getCardColour().toString() + devCardMarket[0][3].getDevLevel() + devCardMarket[0][3].getPV() + ".png");
        setImage(devCard31, "images/devCard/" + devCardMarket[1][3].getCardColour().toString() + devCardMarket[1][3].getDevLevel() + devCardMarket[1][3].getPV() + ".png");
        setImage(devCard32, "images/devCard/" + devCardMarket[2][3].getCardColour().toString() + devCardMarket[2][3].getDevLevel() + devCardMarket[2][3].getPV() + ".png");
    }

    public void setImage(Button button, String path){
        Image img = new Image(path);
        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(160);
        imageView.setFitHeight(220);
        button.setGraphic(imageView);
    }

    public void setDevCardMarket(DevCard[][] devCardMarket){
        this.devCardMarket = devCardMarket;
    }

}
