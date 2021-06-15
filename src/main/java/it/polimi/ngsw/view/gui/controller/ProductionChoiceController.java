package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;


public class ProductionChoiceController extends ViewObservable implements GenericSceneController {

    @FXML
    public Pane devCards;
    @FXML
    public Pane leaderCards;
    @FXML
    public Button baseProductionPower;

    private LightModel lightModel;
    private List<LeaderCard> leaderCardList;

    @FXML
    public void initialize() {
        setDevCards();
        setLeaderCards();
    }

    private void setDevCards() {
        for (int i = 0; i < lightModel.getActiveDevCardMap().size(); i++) {
            ImageView imageView = (ImageView) devCards.getChildren().get(i);
            DevCard devCard = lightModel.getActiveDevCardMap().get(i);
            Image image = new Image("images/devCard/" + devCard.getCardColour().toString() + devCard.getDevLevel() + devCard.getPV() + ".png");
            imageView.setImage(image);
        }
    }

    public void setProductionChoiceController(LightModel lightModel) {
        this.lightModel = lightModel;
        this.leaderCardList = lightModel.getLeaderCardList();
    }

    public void setLeaderCards(){
        for (int i = 0; i < 2; i++) {
            ImageView imageView = (ImageView) leaderCards.getChildren().get(i);
            if(lightModel.getLeaderCardStatus()[i]==2){
                LeaderCard leaderCard = leaderCardList.get(i);
                Image image = new Image("images/leader/" + leaderCard.getAbilityName() + leaderCard.getLeaderCardId() + ".png");
                imageView.setImage(image);
            }
            else {
                imageView.setImage(null);
            }
        }
    }

}