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

    /**
     * The method allows the player to choose the first LeaderCard. It adds the chosen LeaderCard to a list called selectedLeaderCardList
     */
    private void onLeaderCardOne(Event event){
        if(selectedLeaderCardList.size()<2){
            selectedLeaderCardList.add(leaderCardList.get(0));
            disableButton(leaderCardOneButton);
            if(selectedLeaderCardList.size() == 2){
                disableAllButton();
            }
        }
    }

    /**
     * The method allows the player to choose the second LeaderCard. It adds the chosen LeaderCard to a list called selectedLeaderCardList
     */
    private void onLeaderCardTwo(Event event){
        if(selectedLeaderCardList.size()<2){
            selectedLeaderCardList.add(leaderCardList.get(1));
            disableButton(leaderCardTwoButton);
            if(selectedLeaderCardList.size() == 2){
                disableAllButton();
            }
        }
    }

    /**
     * The method allows the player to choose the third LeaderCard. It adds the chosen LeaderCard to a list called selectedLeaderCardList
     */
    private void onLeaderCardThree(Event event){
        if(selectedLeaderCardList.size()<2){
            selectedLeaderCardList.add(leaderCardList.get(2));
            disableButton(leaderCardThreeButton);
            if(selectedLeaderCardList.size() == 2){
                disableAllButton();
            }

        }
    }

    /**
     * The method allows the player to choose the fourth LeaderCard. It adds the chosen LeaderCard to a list called selectedLeaderCardList
     */
    private void onLeaderCardFour(Event event){
        if(selectedLeaderCardList.size()<2){
            selectedLeaderCardList.add(leaderCardList.get(3));
            disableButton(leaderCardFourButton);
            if(selectedLeaderCardList.size() == 2){
                disableAllButton();
            }
        }
    }

    /**
     * The method allows the player to confirm the LeaderCard chosen. The confirm action is performed only if the selectedLeaderCardList size is greater than two
     */
    private void onConfirmButton(Event event){
        if(selectedLeaderCardList.size()==2){
            disableButton(confirmButton);
            disableAllButton();
            new Thread(() -> notifyObserver(obs -> obs.onUpdateLeaderCard(selectedLeaderCardList))).start();
        }
    }

    /**
     * The method disables a button.
     * @param button is the button to disable.
     */
    private void disableButton(Button button){
        button.setDisable(true);
    }

    /**
     * The method sets the first LeaderCard image.
     * @param leaderCardList is the list of leaderCards drawn by the player.
     */
    public void setLeaderCardOneImageView(List<LeaderCard> leaderCardList){
        LeaderCard leaderCard = leaderCardList.get(0);
        Image img = new Image("images/leader/" + leaderCard.getAbilityName() + leaderCard.getLeaderCardId() + ".png");
        leaderCardOneImageView.setImage(img);

    }

    /**
     * The method sets the second LeaderCard image.
     * @param leaderCardList is the list of leaderCards drawn by the player.
     */
    public void setLeaderCardTwoImageView(List<LeaderCard> leaderCardList){
        LeaderCard leaderCard = leaderCardList.get(1);
        Image img = new Image("images/leader/" + leaderCard.getAbilityName() + leaderCard.getLeaderCardId() + ".png");
        leaderCardTwoImageView.setImage(img);
    }


    /**
     * The method sets the third LeaderCard image.
     * @param leaderCardList is the list of leaderCards drawn by the player.
     */
    public void setLeaderCardThreeImageView(List<LeaderCard> leaderCardList){
        LeaderCard leaderCard = leaderCardList.get(2);
        Image img = new Image("images/leader/" + leaderCard.getAbilityName() + leaderCard.getLeaderCardId() + ".png");
        leaderCardThreeImageView.setImage(img);
    }

    /**
     * The method sets the fourth LeaderCard image.
     * @param leaderCardList is the list of leaderCards drawn by the player.
     */
    public void setLeaderCardFourImageView(List<LeaderCard> leaderCardList){
        LeaderCard leaderCard = leaderCardList.get(3);
        Image img = new Image("images/leader/" + leaderCard.getAbilityName() + leaderCard.getLeaderCardId() + ".png");
        leaderCardFourImageView.setImage(img);
    }

    /**
     * The method allows the player to disable every Leader button of the scene.
     */
    public void disableAllButton(){
        leaderCardOneButton.setDisable(true);
        leaderCardTwoButton.setDisable(true);
        leaderCardThreeButton.setDisable(true);
        leaderCardFourButton.setDisable(true);
    }

}
