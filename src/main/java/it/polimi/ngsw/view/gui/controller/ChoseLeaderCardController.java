package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.observer.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChoseLeaderCardController extends ViewObservable implements GenericSceneController {

    public ChoseLeaderCardController(List<LeaderCard> leaderCards){
        setLeaderCardOneImageView(leaderCards);
    }

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
    private ImageView leaderCardTwoImageView = new ImageView();
    @FXML
    private ImageView leaderCardThreeImageView = new ImageView();
    @FXML
    private ImageView leaderCardFourImageView = new ImageView();

    @FXML
    public void initialize(){
        leaderCardOneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onLeaderCardOne);
        leaderCardTwoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onLeaderCardTwo);
        leaderCardThreeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onLeaderCardThree);
        leaderCardFourButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onLeaderCardFour);
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButton);
    }

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
        if(selectedLeaderCardList.size()>=2){
            new Thread(() -> notifyObserver(obs -> obs.onUpdateLeaderCard(selectedLeaderCardList))).start();
        }
    }

    private void disableButton(Button button){
        button.setDisable(true);
    }


    //Set the images and the LeaderCards

    public void setLeaderCardList(List<LeaderCard> leaderCardList){
        this.leaderCardList = leaderCardList;
    }

    public void setLeaderCardOneImageView(List<LeaderCard> leaderCardList){
        LeaderCard leaderCard = leaderCardList.get(0);

        /*
        InputStream stream = null;
        try {
            stream = new FileInputStream("src/main/java/it/polimi/ngsw/resources/images/leader/" + leaderCard.getAbilityName() + leaderCard.getLeaderCardId() + ".png");
        }
        catch (FileNotFoundException e) {
            System.err.println("Leader Image Error");
        }
        assert stream != null;
        Image img = new Image(stream);
        leaderCardOneImageView.setImage(img);
         */
        Image img = new Image(getClass().getResourceAsStream("/images/leader/" + leaderCard.getAbilityName() + leaderCard.getLeaderCardId() + ".png"));
        leaderCardOneImageView = new ImageView(img);

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
