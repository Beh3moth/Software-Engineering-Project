package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.LeaderCard;
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

import java.util.List;

public class LeaderActionController extends ViewObservable implements GenericSceneController {

    private int discardedLeaderCard;
    private int activatedLeaderCard;
    private List<LeaderCard> leaderCardList;
    private LightModel lightModel;
    private int turnZone;

    @FXML
    private Button activateLeaderCardOneButton;
    @FXML
    private Button activateLeaderCardTwoButton;
    @FXML
    private Button discardLeaderCardOneButton;
    @FXML
    private Button discardLeaderCardTwoButton;
    @FXML
    private Button confirmButton;
    @FXML
    private ImageView leaderCardOneImageView;
    @FXML
    private ImageView leaderCardTwoImageView;

    public LeaderActionController(List<LeaderCard> leaderCardList, LightModel lightModel, int turnZone){
        this.activatedLeaderCard = 0;
        this.discardedLeaderCard = 0;
        this.leaderCardList = leaderCardList;
        this.lightModel = lightModel;
        this.turnZone = turnZone;
    }

    @FXML
    public void initialize(){
        activateLeaderCardOneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onActivateLeaderCardOneButton);
        activateLeaderCardTwoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onActivateLeaderCardTwoButton);
        discardLeaderCardOneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDiscardLeaderCardOneButton);
        discardLeaderCardTwoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDiscardLeaderCardTwoButton);
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButton);
        setLeaderCardsImage(this.leaderCardList);
    }

    //Button reactions

    private void onActivateLeaderCardOneButton(Event event){
        disableButton(activateLeaderCardOneButton);
        disableButton(discardLeaderCardOneButton);
        lightModel.setLeaderCardStatus(0, 2);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateLeaderCardActivation(0, turnZone))).start();
    }

    private void onActivateLeaderCardTwoButton(Event event){
        disableButton(activateLeaderCardTwoButton);
        disableButton(discardLeaderCardTwoButton);
        lightModel.setLeaderCardStatus(1, 2);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateLeaderCardActivation(1, turnZone))).start();
    }

    private void onDiscardLeaderCardOneButton(Event event){
        disableButton(activateLeaderCardOneButton);
        disableButton(discardLeaderCardOneButton);
        lightModel.setLeaderCardStatus(0, 0);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateDiscardLeaderCard(0, turnZone))).start();
    }

    private void onDiscardLeaderCardTwoButton(Event event){
        disableButton(activateLeaderCardTwoButton);
        disableButton(discardLeaderCardTwoButton);
        lightModel.setLeaderCardStatus(1, 0);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateDiscardLeaderCard(1, turnZone))).start();
    }

    private void onConfirmButton(Event event){
        if(turnZone==1){
            GameController gameController = new GameController();
            gameController.addAllObservers(observers);
            gameController.setLightModel(lightModel);
            Platform.runLater(() -> SceneController.changeScene(gameController, "game_scene.fxml"));
        }
        else if(!lightModel.isGameFinished() && turnZone == 2){
            notifyObserver(obs -> obs.onEndTurn());
        }
        else notifyObserver(obs -> obs.onUpdateCalculatePVEndGame());
    }

    private void disableButton(Button button){
        button.setDisable(true);
    }

    private void activateButton(Button button){
        button.setDisable(false);
    }

    //Set images

    private void setLeaderCardsImage(List<LeaderCard> leaderCardList){
        if(lightModel.getLeaderCardStatus()[0]==1){
            LeaderCard leaderCardOne = leaderCardList.get(0);
            Image img1 = new Image("images/leader/" + leaderCardOne.getAbilityName() + leaderCardOne.getLeaderCardId() + ".png");
            leaderCardOneImageView.setImage(img1);
        }
        else {
            leaderCardOneImageView.setImage(null);
            activateLeaderCardOneButton.setDisable(true);
            discardLeaderCardOneButton.setDisable(true);
        }
        if(lightModel.getLeaderCardStatus()[1]==1){
            LeaderCard leaderCardTwo = leaderCardList.get(1);
            Image img2 = new Image("images/leader/" + leaderCardTwo.getAbilityName() + leaderCardTwo.getLeaderCardId() + ".png");
            leaderCardTwoImageView.setImage(img2);
        }
        else {
            leaderCardTwoImageView.setImage(null);
            activateLeaderCardTwoButton.setDisable(true);
            discardLeaderCardTwoButton.setDisable(true);
        }

    }


}
