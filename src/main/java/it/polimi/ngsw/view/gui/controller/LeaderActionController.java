package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.observer.ViewObservable;
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

    public LeaderActionController(List<LeaderCard> leaderCardList){
        this.activatedLeaderCard = 0;
        this.discardedLeaderCard = 0;
        setLeaderCardsImage(leaderCardList);
    }

    @FXML
    public void initialize(){
        activateLeaderCardOneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onActivateLeaderCardOneButton);
        activateLeaderCardTwoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onActivateLeaderCardTwoButton);
        discardLeaderCardOneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDiscardLeaderCardOneButton);
        discardLeaderCardTwoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDiscardLeaderCardTwoButton);
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButton);
    }

    private void onActivateLeaderCardOneButton(Event event){
        disableButton(activateLeaderCardOneButton);
        disableButton(discardLeaderCardOneButton);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateLeaderCardActivation(1, 1))).start();
    }

    private void onActivateLeaderCardTwoButton(Event event){
        disableButton(activateLeaderCardTwoButton);
        disableButton(discardLeaderCardTwoButton);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateLeaderCardActivation(2, 1))).start();
    }

    private void onDiscardLeaderCardOneButton(Event event){
        disableButton(activateLeaderCardOneButton);
        disableButton(discardLeaderCardOneButton);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateDiscardLeaderCard(1, 1))).start();
    }

    private void onDiscardLeaderCardTwoButton(Event event){
        disableButton(activateLeaderCardTwoButton);
        disableButton(discardLeaderCardTwoButton);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateDiscardLeaderCard(2, 1))).start();
    }

    private void onConfirmButton(Event event){

    }

    private void setLeaderCardsImage(List<LeaderCard> leaderCardList){
        if(leaderCardList.size()==2){
            LeaderCard leaderCardOne = leaderCardList.get(0);
            Image img1 = new Image(getClass().getResourceAsStream("/images/leader/" + leaderCardOne.getAbilityName() + leaderCardOne.getLeaderCardId() + ".png"));
            leaderCardOneImageView = new ImageView(img1);
            LeaderCard leaderCardTwo = leaderCardList.get(1);
            Image img2 = new Image(getClass().getResourceAsStream("/images/leader/" + leaderCardOne.getAbilityName() + leaderCardOne.getLeaderCardId() + ".png"));
            leaderCardTwoImageView = new ImageView(img2);
        }
        if(leaderCardList.size()==1){
            LeaderCard leaderCardOne = leaderCardList.get(0);
            Image img1 = new Image(getClass().getResourceAsStream("/images/leader/" + leaderCardOne.getAbilityName() + leaderCardOne.getLeaderCardId() + ".png"));
            leaderCardOneImageView = new ImageView(img1);
            leaderCardTwoImageView.setDisable(true);
            activateLeaderCardTwoButton.setCancelButton(true);
        }
    }

    private void disableButton(Button button){
        button.setDisable(true);
    }

    private void activateButton(Button button){
        button.setDisable(false);
    }


}
