package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.ProductionPower;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
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
        setButtons();
    }

    public void setProductionChoiceController(LightModel lightModel) {
        this.lightModel = lightModel;
        this.leaderCardList = lightModel.getLeaderCardList();
    }

    private void setDevCards() {
        for (int i = 0; i < lightModel.getActiveDevCardMap().size(); i++) {
            ImageView imageView = (ImageView) devCards.getChildren().get(i);
            DevCard devCard = lightModel.getActiveDevCardMap().get(i);
            Image image = new Image("images/devCard/" + devCard.getCardColour().toString() + devCard.getDevLevel() + devCard.getPV() + ".png");
            imageView.setImage(image);
        }
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

    private void setButtons(){
        //devCards
        for(int i=0; i<3; i++){
            ImageView imageView = (ImageView) devCards.getChildren().get(i);
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDevCards);
        }
        //leaders
        for(int i=0; i<2; i++){
            ImageView imageView = (ImageView) leaderCards.getChildren().get(i);
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onLeaderCards);
        }
        baseProductionPower.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBaseProductionPower);
    }

    private void onDevCards(Event event){
        ImageView imageView = (ImageView) event.getSource();
        List<ProductionPower> productionPowerList = new ArrayList<>();
        if(getDevCard(imageView.getId())!=null && !lightModel.getChosenIntegerList().contains(getDevCardNumber(imageView.getId()))){
            productionPowerList.add( getDevCard(imageView.getId()).getProductionPower() );
            notifyObserver(obs -> obs.onUpdateProductionPowerList(productionPowerList, "productionPowerChosen"));
        }
    }

    private DevCard getDevCard(String id){
        switch (id) {
            case "devCard1":
                return lightModel.getActiveDevCardMap().get(0);
            case "devCard2":
                return lightModel.getActiveDevCardMap().get(1);
            case "devCard3":
                return lightModel.getActiveDevCardMap().get(2);
            default:
                return null;
        }
    }

    private int getDevCardNumber(String id){
        switch (id) {
            case "devCard1":
                return 1;
            case "devCard2":
                return 2;
            case "devCard3":
                return 3;
            default:
                return 0;
        }
    }

    private void onLeaderCards(Event event){
        //link to set scene
    }

    private void onBaseProductionPower(Event event){
        //link to set scene
    }

}