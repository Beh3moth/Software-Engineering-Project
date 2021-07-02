package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.DevCardColour;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


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
    @FXML
    private Button backButton;
    @FXML
    private Button SlotButtonOne;
    @FXML
    private Button SlotButtonTwo;
    @FXML
    private Button SlotButtonThree;

    private DevCard[][] devCardMarket;
    private LightModel lightModel;
    private int row;
    private int col;

    @FXML
    public void initialize(){
        setButtonsImages();
        setButtonsEventHandler();
    }

    private void onChosenDevCard(Event event){
        Button button = (Button) event.getSource();
        row = GridPane.getRowIndex(button);
        col = GridPane.getColumnIndex(button);
        disableEveryDevCardButton();
        activateEverySlotButton(row, col);
    }

    private void setButtonsEventHandler(){
        devCard00.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenDevCard);
        devCard01.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenDevCard);
        devCard02.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenDevCard);
        devCard10.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenDevCard);
        devCard11.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenDevCard);
        devCard12.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenDevCard);
        devCard20.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenDevCard);
        devCard21.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenDevCard);
        devCard22.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenDevCard);
        devCard30.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenDevCard);
        devCard31.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenDevCard);
        devCard32.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenDevCard);
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackButton);
        SlotButtonOne.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSlotButton);
        SlotButtonTwo.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSlotButton);
        SlotButtonThree.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSlotButton);
        disableEverySlotButton();
    }

    /**
     * The method calls the method onUpdateChooseDevCard of the ViewObserver.
     */
    private void onSlotButton(Event event){
        Button button = (Button) event.getSource();
        switch (button.getId()){
            case "SlotButtonOne":
                new Thread(() -> notifyObserver(obs -> obs.onUpdateChooseDevCard(3-row, col+1, 1))).start();
                break;
            case "SlotButtonTwo":
                new Thread(() -> notifyObserver(obs -> obs.onUpdateChooseDevCard(3-row, col+1, 2))).start();
                break;
            case "SlotButtonThree":
                new Thread(() -> notifyObserver(obs -> obs.onUpdateChooseDevCard(3-row, col+1, 3))).start();
                break;
            default:
                new Thread(() -> notifyObserver(obs -> obs.onUpdateChooseDevCard(3-row, col+1, 1))).start();
                break;
        }
    }

    /**
     * The method allows the player to go back to the GameScene.
     */
    private void onBackButton(Event event){
        GameController gameController = new GameController();
        gameController.addAllObservers(observers);
        gameController.setLightModel(lightModel);
        Platform.runLater(() -> SceneController.changeScene(gameController, "game_scene.fxml"));
    }

    /**
     * the method sets the images of the DevCards.
     */
    private void setButtonsImages(){
        setDevCardImage(devCard00, 0, 0);
        setDevCardImage(devCard10, 0, 1);
        setDevCardImage(devCard20, 0, 2);
        setDevCardImage(devCard30, 0, 3);
        setDevCardImage(devCard01, 1, 0);
        setDevCardImage(devCard11, 1, 1);
        setDevCardImage(devCard21, 1, 2);
        setDevCardImage(devCard31, 1, 3);
        setDevCardImage(devCard02, 2, 0);
        setDevCardImage(devCard12, 2, 1);
        setDevCardImage(devCard22, 2, 2);
        setDevCardImage(devCard32, 2, 3);
    }

    /**
     * The method sets the image of a DevCardButton.
     * @param button is the button to set the image of.
     * @param row is the row of the DevCard.
     * @param col is the column of the DevCard.
     */
    private void setDevCardImage(Button button, int row, int col){
        if(devCardMarket[row][col].getCardColour()== DevCardColour.EMPTY){
            button.setDisable(true);
            button.setVisible(false);
        } else {
            setImage(button, "images/devCard/" + devCardMarket[row][col].getCardColour().toString() + devCardMarket[row][col].getDevLevel() + devCardMarket[row][col].getPV() + ".png");
        }
    }

    /**
     * The method sets the image of a Button.
     * @param button is the row of the DevCard.
     * @param path is the path to take the image from.
     */
    public void setImage(Button button, String path){
        Image img = new Image(path);
        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(160);
        imageView.setFitHeight(220);
        button.setGraphic(imageView);
    }

    /**
     * The method sets the devCardMarket and the lightModel.
     */
    public void setDevCardMarket(DevCard[][] devCardMarket, LightModel lightModel){
        this.devCardMarket = devCardMarket;
        this.lightModel = lightModel;
    }

    /**
     * The method disables every button of the scene except for the slot buttons.
     */
    private void disableEveryDevCardButton(){
        devCard00.setDisable(true);
        devCard01.setDisable(true);
        devCard02.setDisable(true);
        devCard10.setDisable(true);
        devCard11.setDisable(true);
        devCard12.setDisable(true);
        devCard20.setDisable(true);
        devCard21.setDisable(true);
        devCard22.setDisable(true);
        devCard30.setDisable(true);
        devCard31.setDisable(true);
        devCard32.setDisable(true);
        backButton.setDisable(true);
    }

    /**
     * The method disables only the slot buttons.
     */
    private void disableEverySlotButton(){
        SlotButtonOne.setDisable(true);
        SlotButtonTwo.setDisable(true);
        SlotButtonThree.setDisable(true);
    }

    /**
     * The method activates only the slot buttons where the player can put the DevCard chosen.
     * @param row is the row of the DevCard chosen.
     * @param col is the column of the DevCard chosen.
     */
    private void activateEverySlotButton(int row, int col){
        if( (!lightModel.getActiveDevCardMap().containsKey(0) && (3-row)==1 ) || (lightModel.getActiveDevCardMap().containsKey(0) && lightModel.getActiveDevCardMap().get(0).getDevLevel() < (3-row))){
            SlotButtonOne.setDisable(false);
        }
        if( ( !lightModel.getActiveDevCardMap().containsKey(1) && (3-row)==1 ) || (lightModel.getActiveDevCardMap().containsKey(1) && lightModel.getActiveDevCardMap().get(1).getDevLevel() < (3-row))){
            SlotButtonTwo.setDisable(false);

        }
        if( ( !lightModel.getActiveDevCardMap().containsKey(2) && (3-row)==1 ) || (lightModel.getActiveDevCardMap().containsKey(2) && lightModel.getActiveDevCardMap().get(2).getDevLevel() < (3-row))){
            SlotButtonThree.setDisable(false);
        }
        if(SlotButtonOne.isDisable() && SlotButtonTwo.isDisable() && SlotButtonThree.isDisable()){
            DevCardSceneController controller = new DevCardSceneController();
            controller.setDevCardMarket(lightModel.getDevCardMarket(), lightModel);
            controller.addAllObservers(observers);
            SceneController.changeScene(controller, "dev_card_market_scene.fxml");
        }
    }

}
