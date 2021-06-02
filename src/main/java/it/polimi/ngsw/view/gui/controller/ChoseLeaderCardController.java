package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.observer.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ChoseLeaderCardController extends ViewObservable implements GenericSceneController {

    private List<LeaderCard> leaderCardList = new ArrayList<>();
    private List<LeaderCard> selectedLeaderCardList = new ArrayList<>();

    @FXML
    private Button leaderCardOneButton;
    @FXML
    private Button leaderCardTwoButton;
    @FXML
    private Button leaderCardThreeButton;
    @FXML
    private Button leaderCardFourButton;
    @FXML
    private Button confirmButton;
    @FXML
    private ImageView leaderCardOneImageView;
    @FXML
    private ImageView leaderCardTwoImageView;
    @FXML
    private ImageView leaderCardThreeImageView;
    @FXML
    private ImageView leaderCardFourImageView;

    public ChoseLeaderCardController(List<LeaderCard> leaderCards){
        this.leaderCardList = leaderCards;
    }

    @FXML
    public void initialize(){
        leaderCardOneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onLeaderCardOne);
        leaderCardTwoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onLeaderCardTwo);
        leaderCardThreeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onLeaderCardThree);
        leaderCardFourButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onLeaderCardFour);
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButton);
        setLeaderCardOneImageView(this.leaderCardList);
        setLeaderCardTwoImageView(this.leaderCardList);
        setLeaderCardThreeImageView(this.leaderCardList);
        setLeaderCardFourImageView(this.leaderCardList);
    }

    //Buttons reaction

    private void onLeaderCardOne(Event event){
        if(selectedLeaderCardList.size()<2){
            selectedLeaderCardList.add(leaderCardList.get(0));
            disableButton(leaderCardOneButton);
        }
    }

    private void onLeaderCardTwo(Event event){
        if(selectedLeaderCardList.size()<2){
            selectedLeaderCardList.add(leaderCardList.get(1));
            disableButton(leaderCardTwoButton);
        }
    }

    private void onLeaderCardThree(Event event){
        if(selectedLeaderCardList.size()<2){
            selectedLeaderCardList.add(leaderCardList.get(2));
            disableButton(leaderCardThreeButton);
        }
    }

    private void onLeaderCardFour(Event event){
        if(selectedLeaderCardList.size()<2){
            selectedLeaderCardList.add(leaderCardList.get(3));
            disableButton(leaderCardFourButton);
        }
    }

    private void onConfirmButton(Event event){
        if(selectedLeaderCardList.size()==2){
            disableButton(confirmButton);
            new Thread(() -> notifyObserver(obs -> obs.onUpdateLeaderCard(selectedLeaderCardList))).start();
        }
    }

    private void disableButton(Button button){
        button.setDisable(true);
    }


    //Set the images and the LeaderCards

    public void setLeaderCardOneImageView(List<LeaderCard> leaderCardList){
        LeaderCard leaderCard = leaderCardList.get(0);
        Image img = new Image("images/leader/" + leaderCard.getAbilityName() + leaderCard.getLeaderCardId() + ".png");
        leaderCardOneImageView.setImage(img);

    }

    public void setLeaderCardTwoImageView(List<LeaderCard> leaderCardList){
        LeaderCard leaderCard = leaderCardList.get(1);
        Image img = new Image("images/leader/" + leaderCard.getAbilityName() + leaderCard.getLeaderCardId() + ".png");
        leaderCardTwoImageView.setImage(img);
    }

    public void setLeaderCardThreeImageView(List<LeaderCard> leaderCardList){
        LeaderCard leaderCard = leaderCardList.get(2);
        Image img = new Image("images/leader/" + leaderCard.getAbilityName() + leaderCard.getLeaderCardId() + ".png");
        leaderCardThreeImageView.setImage(img);
    }

    public void setLeaderCardFourImageView(List<LeaderCard> leaderCardList){
        LeaderCard leaderCard = leaderCardList.get(3);
        Image img = new Image("images/leader/" + leaderCard.getAbilityName() + leaderCard.getLeaderCardId() + ".png");
        leaderCardFourImageView.setImage(img);
    }


}
