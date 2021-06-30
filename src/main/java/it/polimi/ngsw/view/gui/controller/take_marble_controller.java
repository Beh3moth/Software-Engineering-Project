package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.Marble;
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

public class take_marble_controller extends ViewObservable implements GenericSceneController{

    private LightModel lightModel;
    private Marble[] firstRow;
    private Marble[] secondRow;
    private Marble[] thirdRow;
    private Marble singleMarble;

    @FXML
    private Button firstrow;
    @FXML
    private Button secondrow;
    @FXML
    private Button thirdrow;
    @FXML
    private Button firstcolumn;
    @FXML
    private Button secondcolumn;
    @FXML
    private Button thirdcolumn;
    @FXML
    private Button fourthcolumn;
    @FXML
    private ImageView MARBLE00;
    @FXML
    private ImageView MARBLE01;
    @FXML
    private ImageView MARBLE02;
    @FXML
    private ImageView MARBLE03;
    @FXML
    private ImageView MARBLE10;
    @FXML
    private ImageView MARBLE20;
    @FXML
    private ImageView MARBLE11;
    @FXML
    private ImageView MARBLE21;
    @FXML
    private ImageView MARBLE12;
    @FXML
    private ImageView MARBLE22;
    @FXML
    private ImageView MARBLE13;
    @FXML
    private ImageView MARBLE23;
    @FXML
    private ImageView SINGLEMARBLE;
    @FXML
    private Button backButton;


    public take_marble_controller(LightModel lightmodel){
        this.firstRow = lightmodel.getFirstRow();
        this.secondRow = lightmodel.getSecondRow();
        this.thirdRow = lightmodel.getThirdRow();
        this.singleMarble = lightmodel.getSingleMarble();
        this.lightModel = lightmodel;
    }

    @FXML
    public void initialize() {
        this.firstrow.setDisable(false);
        this.secondrow.setDisable(false);
        this.thirdrow.setDisable(false);
        this.firstcolumn.setDisable(false);
        this.secondcolumn.setDisable(false);
        this.thirdcolumn.setDisable(false);
        this.fourthcolumn.setDisable(false);
        firstrow.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onFirstrowButtonClick);
        secondrow.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSecondrowButtonClick);
        thirdrow.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onThirdrowButtonClick);
        firstcolumn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onFirstcolumnButtonClick);
        secondcolumn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSecondcolumnButtonClick);
        thirdcolumn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onThirdcolumnButtonClick);
        fourthcolumn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onFourthcolumnButtonClick);
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackButton);
        setMarble(this.firstRow, this.secondRow, this.thirdRow, this.singleMarble);
    }

    /**
     * this method set the line of the market wich the player want to take case: first row
     * @param event
     */
    public void onFirstrowButtonClick(Event event){
        reorderRow(1);
        this.firstrow.setDisable(true);
        this.secondrow.setDisable(true);
        this.thirdrow.setDisable(true);
        this.firstcolumn.setDisable(true);
        this.secondcolumn.setDisable(true);
        this.thirdcolumn.setDisable(true);
        this.fourthcolumn.setDisable(true);
        notifyObserver(obs -> obs.onUpdateBuyFromMarket(1, 1));
    }

    /**
     * this method set the line of the market wich the player want to take case: second row
     * @param event
     */
    public void onSecondrowButtonClick(Event event){
        reorderRow(2);
        this.firstrow.setDisable(true);
        this.secondrow.setDisable(true);
        this.thirdrow.setDisable(true);
        this.firstcolumn.setDisable(true);
        this.secondcolumn.setDisable(true);
        this.thirdcolumn.setDisable(true);
        this.fourthcolumn.setDisable(true);
        notifyObserver(obs -> obs.onUpdateBuyFromMarket(1, 2));
    }

    /**
     * this method set the line of the market wich the player want to take case: third row
     * @param event
     */
    public void onThirdrowButtonClick(Event event){
        reorderRow(3);
        this.firstrow.setDisable(true);
        this.secondrow.setDisable(true);
        this.thirdrow.setDisable(true);
        this.firstcolumn.setDisable(true);
        this.secondcolumn.setDisable(true);
        this.thirdcolumn.setDisable(true);
        this.fourthcolumn.setDisable(true);
        notifyObserver(obs -> obs.onUpdateBuyFromMarket(1, 3));
    }

    /**
     * this method set the line of the market wich the player want to take case: first column
     * @param event
     */
    public void onFirstcolumnButtonClick(Event event){
        reorderColumn(1);
        this.firstrow.setDisable(true);
        this.secondrow.setDisable(true);
        this.thirdrow.setDisable(true);
        this.firstcolumn.setDisable(true);
        this.secondcolumn.setDisable(true);
        this.thirdcolumn.setDisable(true);
        this.fourthcolumn.setDisable(true);
        notifyObserver(obs -> obs.onUpdateBuyFromMarket(0, 1));
    }

    /**
     * this method set the line of the market wich the player want to take case: second column
     * @param event
     */
    public void onSecondcolumnButtonClick(Event event){
        reorderColumn(2);
        this.firstrow.setDisable(true);
        this.secondrow.setDisable(true);
        this.thirdrow.setDisable(true);
        this.firstcolumn.setDisable(true);
        this.secondcolumn.setDisable(true);
        this.thirdcolumn.setDisable(true);
        this.fourthcolumn.setDisable(true);
        notifyObserver(obs -> obs.onUpdateBuyFromMarket(0, 2));
    }

    /**
     * this method set the line of the market wich the player want to take case: third column
     * @param event
     */
    public void onThirdcolumnButtonClick(Event event){
        reorderColumn(3);
        this.firstrow.setDisable(true);
        this.secondrow.setDisable(true);
        this.thirdrow.setDisable(true);
        this.firstcolumn.setDisable(true);
        this.secondcolumn.setDisable(true);
        this.thirdcolumn.setDisable(true);
        this.fourthcolumn.setDisable(true);
        notifyObserver(obs -> obs.onUpdateBuyFromMarket(0, 3));
    }

    /**
     * this method set the line of the market wich the player want to take case: fourth column
     * @param event
     */
    public void onFourthcolumnButtonClick(Event event){
        reorderColumn(4);
        this.firstrow.setDisable(true);
        this.secondrow.setDisable(true);
        this.thirdrow.setDisable(true);
        this.firstcolumn.setDisable(true);
        this.secondcolumn.setDisable(true);
        this.thirdcolumn.setDisable(true);
        this.fourthcolumn.setDisable(true);
        notifyObserver(obs -> obs.onUpdateBuyFromMarket(0, 4));
    }

    /**
     * this method reorder the column of the market after the player chose a column
     * @param choseColumn the column that the player has chosen
     */
    public void reorderColumn(int choseColumn){
        Marble support = this.singleMarble;
        this.singleMarble = (this.firstRow[choseColumn - 1]);
        this.firstRow[choseColumn - 1] =  this.secondRow[choseColumn - 1];
        this.secondRow[choseColumn - 1] = this.thirdRow[choseColumn - 1];
        this.thirdRow[choseColumn - 1] = support;
        setMarble(this.firstRow, this.secondRow, this.thirdRow, this.singleMarble);
    }

    /**
     * this method reorder the column of the market after the player chose a row
     * @param choseRow the row that the player has chosen
     */
    public void reorderRow(int choseRow){
        Marble support = this.singleMarble;
        if (choseRow == 1) {
            singleMarble = this.firstRow[0];
            this.firstRow[0] = this.firstRow[1];
            this.firstRow[1] = this.firstRow[2];
            this.firstRow[2] = this.firstRow[3];
            this.firstRow[3] = support;
        } else if (choseRow == 2) {
            singleMarble = this.secondRow[0];
            this.secondRow[0] = this.secondRow[1];
            this.secondRow[1] = this.secondRow[2];
            this.secondRow[2] = this.secondRow[3];
            this.secondRow[3] = support;
        } else if (choseRow == 3) {
            singleMarble = this.thirdRow[0];
            this.thirdRow[0] = this.thirdRow[1];
            this.thirdRow[1] = this.thirdRow[2];
            this.thirdRow[2] = this.thirdRow[3];
            this.thirdRow[3] = support;
        }
        setMarble(this.firstRow, this.secondRow, this.thirdRow, this.singleMarble);
    }

    //set the image of the marble

    /**
     * this method set the rows of the market
     * @param firstRow of the market
     * @param secondRow of the market
     * @param thirdRow of the market
     * @param singleMarble of the market
     */
    public void setMarble(Marble[] firstRow, Marble[] secondRow, Marble[] thirdRow, Marble singleMarble){
        Image marble00 = new Image("images/icons/marbles/" + firstRow[0].getColour().toString() + ".png");
        MARBLE00.setImage(marble00);
        Image marble01 = new Image("images/icons/marbles/" + firstRow[1].getColour().toString() + ".png");
        MARBLE01.setImage(marble01);
        Image marble02 = new Image("images/icons/marbles/" + firstRow[2].getColour().toString() + ".png");
        MARBLE02.setImage(marble02);
        Image marble03 = new Image("images/icons/marbles/" + firstRow[3].getColour().toString() + ".png");
        MARBLE03.setImage(marble03);
        Image marble10 = new Image("images/icons/marbles/" + secondRow[0].getColour().toString() + ".png");
        MARBLE10.setImage(marble10);
        Image marble11 = new Image("images/icons/marbles/" + secondRow[1].getColour().toString() + ".png");
        MARBLE11.setImage(marble11);
        Image marble12 = new Image("images/icons/marbles/" + secondRow[2].getColour().toString() + ".png");
        MARBLE12.setImage(marble12);
        Image marble13 = new Image("images/icons/marbles/" + secondRow[3].getColour().toString() + ".png");
        MARBLE13.setImage(marble13);
        Image marble20 = new Image("images/icons/marbles/" + thirdRow[0].getColour().toString() + ".png");
        MARBLE20.setImage(marble20);
        Image marble21 = new Image("images/icons/marbles/" + thirdRow[1].getColour().toString() + ".png");
        MARBLE21.setImage(marble21);
        Image marble22 = new Image("images/icons/marbles/" + thirdRow[2].getColour().toString() + ".png");
        MARBLE22.setImage(marble22);
        Image marble23 = new Image("images/icons/marbles/" + thirdRow[3].getColour().toString() + ".png");
        MARBLE23.setImage(marble23);
        Image singlemarble = new Image("images/icons/marbles/" + singleMarble.getColour().toString() + ".png");
        SINGLEMARBLE.setImage(singlemarble);
    }

    /**
     * this method permit to change scene in game_scene
     * @param event
     */
    private void onBackButton(Event event){
        GameController gameController = new GameController();
        gameController.addAllObservers(observers);
        gameController.setLightModel(lightModel);
        Platform.runLater(() -> SceneController.changeScene(gameController, "game_scene.fxml"));
    }
}
