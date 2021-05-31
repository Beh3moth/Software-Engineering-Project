package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.Resource;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class distribute_initial_resources_controller extends ViewObservable implements GenericSceneController{

    private int resourceNumber, firstPos, secondPos;
    private Resource resourceOne, resourceTwo;

    @FXML
    private Button money;
    @FXML
    private Button stone;
    @FXML
    private Button shield;
    @FXML
    private Button slave;
    @FXML
    private TextField insertfirstshelf;
    @FXML
    private TextField insertsecondshelf;

    public distribute_initial_resources_controller(){
        this.firstPos = 0;
        this.secondPos = 0;
        this.resourceOne = Resource.EMPTY;
        this.resourceTwo = Resource.EMPTY;
    }
    @FXML
    public void initialize(){
            money.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMoneyButtonClick);
            slave.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSlaveButtonClick);
            stone.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onStoneButtonClick);
            shield.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShieldButtonClick);
    }

    public void onMoneyButtonClick(Event event){
            if(this.resourceOne == Resource.EMPTY){
            String firstPos = insertfirstshelf.getText();
            if(firstPos == "uno"){
                this.firstPos = 1;
            }
            else if(firstPos == "due"){
                this.firstPos = 2;
            }
            else if(firstPos == "tre"){
                this.firstPos = 3;
            }
            this.resourceOne = Resource.MONEY;}
            else{
                String secondPos = insertsecondshelf.getText();
                if(secondPos == "uno"){
                    this.secondPos = 1;
                }
                else if(secondPos == "due"){
                    this.secondPos = 2;
                }
                else if(secondPos == "tre"){
                    this.secondPos = 3;
                }
                this.resourceTwo = Resource.MONEY;
                notifyObserver(obs -> obs.onUpdatePickedResources(this.resourceNumber, this.resourceOne,this.resourceTwo, this.firstPos, this.secondPos));}

            if(resourceNumber == 1){
            notifyObserver(obs -> obs.onUpdatePickedResources(this.resourceNumber, this.resourceOne,Resource.EMPTY, this.firstPos, 0));}
            if(resourceNumber == 2){
                money.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMoneyButtonClick);
                slave.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSlaveButtonClick);
                stone.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onStoneButtonClick);
                shield.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShieldButtonClick);
            }
    }

    public void onSlaveButtonClick(Event event){
        if(this.resourceOne == Resource.EMPTY){
            String firstPos = insertfirstshelf.getText();
            if(firstPos == "uno"){
                this.firstPos = 1;
            }
            else if(firstPos == "due"){
                this.firstPos = 2;
            }
            else if(firstPos == "tre"){
                this.firstPos = 3;
            }
            this.resourceOne = Resource.SLAVE;}
        else{
            String secondPos = insertsecondshelf.getText();
            if(secondPos == "uno"){
                this.secondPos = 1;
            }
            else if(secondPos == "due"){
                this.secondPos = 2;
            }
            else if(secondPos == "tre"){
                this.secondPos = 3;
            }
            this.resourceTwo = Resource.SLAVE;
            notifyObserver(obs -> obs.onUpdatePickedResources(this.resourceNumber, this.resourceOne,this.resourceTwo, this.firstPos, this.secondPos));}

        if(resourceNumber == 1){
            notifyObserver(obs -> obs.onUpdatePickedResources(this.resourceNumber, this.resourceOne,Resource.EMPTY, this.firstPos, 0));}
        if(resourceNumber == 2){
            money.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMoneyButtonClick);
            slave.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSlaveButtonClick);
            stone.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onStoneButtonClick);
            shield.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShieldButtonClick);
        }
    }

    public void onStoneButtonClick(Event event){
        if(this.resourceOne == Resource.EMPTY){
            String firstPos = insertfirstshelf.getText();
            if(firstPos == "uno"){
                this.firstPos = 1;
            }
            else if(firstPos == "due"){
                this.firstPos = 2;
            }
            else if(firstPos == "tre"){
                this.firstPos = 3;
            }
            this.resourceOne = Resource.STONE;}
        else{
            String secondPos = insertsecondshelf.getText();
            if(secondPos == "uno"){
                this.secondPos = 1;
            }
            else if(secondPos == "due"){
                this.secondPos = 2;
            }
            else if(secondPos == "tre"){
                this.secondPos = 3;
            }
            this.resourceTwo = Resource.STONE;
            notifyObserver(obs -> obs.onUpdatePickedResources(this.resourceNumber, this.resourceOne,this.resourceTwo, this.firstPos, this.secondPos));}
        if(resourceNumber == 1){
            notifyObserver(obs -> obs.onUpdatePickedResources(this.resourceNumber, this.resourceOne,Resource.EMPTY, this.firstPos, 0));}
        if(resourceNumber == 2){
            money.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMoneyButtonClick);
            slave.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSlaveButtonClick);
            stone.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onStoneButtonClick);
            shield.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShieldButtonClick);
        }
    }

    public void onShieldButtonClick(Event event){
        if(this.resourceOne == Resource.EMPTY){
            String firstPos = insertfirstshelf.getText();
            if(firstPos == "uno"){
                this.firstPos = 1;
            }
            else if(firstPos == "due"){
                this.firstPos = 2;
            }
            else if(firstPos == "tre"){
                this.firstPos = 3;
            }
            this.resourceOne = Resource.SHIELD;}
        else{
            String secondPos = insertsecondshelf.getText();
            if(secondPos == "uno"){
                this.secondPos = 1;
            }
            else if(secondPos == "due"){
                this.secondPos = 2;
            }
            else if(secondPos == "tre"){
                this.secondPos = 3;
            }
            this.resourceTwo = Resource.SHIELD;
            notifyObserver(obs -> obs.onUpdatePickedResources(this.resourceNumber, this.resourceOne,this.resourceTwo, this.firstPos, this.secondPos));}

        if(resourceNumber == 1){
            notifyObserver(obs -> obs.onUpdatePickedResources(this.resourceNumber, this.resourceOne,Resource.EMPTY, this.firstPos, 0));}
        if(resourceNumber == 2){
            money.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMoneyButtonClick);
            slave.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSlaveButtonClick);
            stone.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onStoneButtonClick);
            shield.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onShieldButtonClick);
        }
    }

    public void setResourceNumber(int resourceNumber){this.resourceNumber = resourceNumber;}
}
