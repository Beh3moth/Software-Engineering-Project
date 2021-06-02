package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.Marble;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class take_marble_controller extends ViewObservable implements GenericSceneController{

    private LightModel lightmodel;

    private Marble[] firstRow;
    private Marble[] secondRow;
    private Marble[] thirdRow;
    private Marble singleMarble;

    GridPane marbleGrid = new GridPane ();

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



    public take_marble_controller(){
        this.firstRow = lightmodel.getFirstRow();
        this.secondRow = lightmodel.getSecondRow();
        this.thirdRow = lightmodel.getThirdRow();
        this.singleMarble = lightmodel.getSingleMarble();
    }

    @FXML
    public void initialize() {
        firstrow.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onFirstrowButtonClick);
        secondrow.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSecondrowButtonClick);
        thirdrow.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onThirdrowButtonClick);
        firstcolumn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onFirstcolumnButtonClick);
        secondcolumn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSecondcolumnButtonClick);
        thirdcolumn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onThirdcolumnButtonClick);
        fourthcolumn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onFourthcolumnButtonClick);
        setMarble(this.firstRow, this.secondRow, this.thirdRow, this.singleMarble);
    }

    public void onFirstrowButtonClick(Event event){
        new Thread(() -> notifyObserver(obs -> obs.onUpdateBuyFromMarket(1, 1))).start();
    }

    public void onSecondrowButtonClick(Event event){
        new Thread(() -> notifyObserver(obs -> obs.onUpdateBuyFromMarket(1, 2))).start();
    }

    public void onThirdrowButtonClick(Event event){
        new Thread(() -> notifyObserver(obs -> obs.onUpdateBuyFromMarket(1, 3))).start();
    }

    public void onFirstcolumnButtonClick(Event event){
        new Thread(() -> notifyObserver(obs -> obs.onUpdateBuyFromMarket(0, 1))).start();
    }

    public void onSecondcolumnButtonClick(Event event){
        new Thread(() -> notifyObserver(obs -> obs.onUpdateBuyFromMarket(0, 2))).start();
    }

    public void onThirdcolumnButtonClick(Event event){
        new Thread(() -> notifyObserver(obs -> obs.onUpdateBuyFromMarket(0, 3))).start();
    }

    public void onFourthcolumnButtonClick(Event event){
        new Thread(() -> notifyObserver(obs -> obs.onUpdateBuyFromMarket(0, 4))).start();
    }

    public void setLightmodel(LightModel lightmodel){this.lightmodel = lightmodel;}

    //set the image of the marble

    public void setMarble(Marble[] firstRow, Marble[] secondRow, Marble[] thirdRow, Marble singleMarble){
        Image marble00 = new Image("images/icons/" + firstRow[0].getMarblecolour() + ".png");
        MARBLE00.setImage(marble00);
        Image marble01 = new Image("images/icons/" + firstRow[1].getMarblecolour() + ".png");
        MARBLE01.setImage(marble01);
        Image marble02 = new Image("images/icons/" + firstRow[2].getMarblecolour() + ".png");
        MARBLE02.setImage(marble02);
        Image marble03 = new Image("images/icons/" + firstRow[3].getMarblecolour() + ".png");
        MARBLE03.setImage(marble03);
        Image marble10 = new Image("images/icons/" + secondRow[0].getMarblecolour() + ".png");
        MARBLE10.setImage(marble10);
        Image marble11 = new Image("images/icons/" + secondRow[1].getMarblecolour() + ".png");
        MARBLE11.setImage(marble11);
        Image marble12 = new Image("images/icons/" + secondRow[2].getMarblecolour() + ".png");
        MARBLE12.setImage(marble12);
        Image marble13 = new Image("images/icons/" + secondRow[3].getMarblecolour() + ".png");
        MARBLE13.setImage(marble13);
        Image marble20 = new Image("images/icons/" + thirdRow[0].getMarblecolour() + ".png");
        MARBLE20.setImage(marble20);
        Image marble21 = new Image("images/icons/" + thirdRow[1].getMarblecolour() + ".png");
        MARBLE21.setImage(marble21);
        Image marble22 = new Image("images/icons/" + thirdRow[2].getMarblecolour() + ".png");
        MARBLE22.setImage(marble22);
        Image marble23 = new Image("images/icons/" + thirdRow[3].getMarblecolour() + ".png");
        MARBLE23.setImage(marble23);
        Image singlemarble = new Image("images/icons/" + singleMarble.getMarblecolour() + ".png");
        SINGLEMARBLE.setImage(singlemarble);
    }


}
