package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.ProductionPower;
import it.polimi.ngsw.model.Resource;
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
    @FXML
    public Button activate;

    private LightModel lightModel;
    private List<LeaderCard> leaderCardList;

    @FXML
    public void initialize() {
        setButtonsEventHandlers();
        setDevCards();
        setLeaderCards();
        disableChosenProductionPowers();
    }

    /**
     * The method sets the light model and the leader cards.
     * @param lightModel can't be null.
     */
    public void setProductionChoiceController(LightModel lightModel) {
        this.lightModel = lightModel;
        this.leaderCardList = lightModel.getLeaderCardList();
    }

    /**
     * The method sets the DevCards images.
     */
    private void setDevCards() {
        for (int i = 0; i < lightModel.getActiveDevCardMap().size(); i++) {
            ImageView imageView = (ImageView) devCards.getChildren().get(i);
            DevCard devCard = lightModel.getActiveDevCardMap().get(i);
            Image image = new Image("images/devCard/" + devCard.getCardColour().toString() + devCard.getDevLevel() + devCard.getPV() + ".png");
            imageView.setImage(image);
            if(lightModel.getChosenIntegerList().contains(i+1)){
                imageView.setDisable(true);
            }
        }
    }

    /**
     * The method sets the LeaderCards images.
     */
    public void setLeaderCards(){
        for (int i = 0; i < 2; i++) {
            ImageView imageView = (ImageView) leaderCards.getChildren().get(i);
            if(lightModel.getLeaderCardStatus()[i]==2 && leaderCardList.get(i).getAbilityName().equals("production power")){
                LeaderCard leaderCard = leaderCardList.get(i);
                Image image = new Image("images/leader/" + leaderCard.getAbilityName() + leaderCard.getLeaderCardId() + ".png");
                imageView.setImage(image);
            }
            if(lightModel.getChosenIntegerList().contains(i+4)){
                imageView.setDisable(true);
            }
        }
    }

    /**
     * The method sets the event handlers for the button of the leader card and for the dev cards.
     */
    private void setButtonsEventHandlers(){
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
        if(lightModel.getChosenIntegerList().contains(0)){
            baseProductionPower.setDisable(true);
        }
        activate.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onActivate);
    }

    /**
     * The method allows the player to end the action. It can be done only if the player has chosen and paid at least one ProductionPower.
     */
    private void onActivate(Event event){
        if(lightModel.getChosenIntegerList().size()>0){
            for(ProductionPower productionPower : lightModel.getPaidProductionPowerList()){
                if(productionPower.isLeaderProductionPower()){
                    lightModel.setCrossPosition(lightModel.getCrossPosition()+1);
                }
                else {
                    for(Resource resource : productionPower.getResourceToReceive()){
                        if(resource.equals(Resource.FAITHPOINT)){
                            lightModel.setCrossPosition(lightModel.getCrossPosition()+1);
                        }
                    }
                }

            }
            disableEveryButton();
            notifyObserver(obs -> obs.onUpdateProductionPowerActivation());
        }
    }

    /**
     * The method calls the method onUpdateProductionPowerList of the ViewObserver.
     */
    private void onDevCards(Event event){
        ImageView imageView = (ImageView) event.getSource();
        List<ProductionPower> productionPowerList = new ArrayList<>();
        if(getDevCard(imageView.getId())!=null && !lightModel.getChosenIntegerList().contains(getDevCardNumber(imageView.getId()))){
            productionPowerList.add( getDevCard(imageView.getId()).getProductionPower() );
            lightModel.getChosenIntegerList().add(getDevCardNumber(imageView.getId()));
            disableEveryButton();
            notifyObserver(obs -> obs.onUpdateProductionPowerList(productionPowerList, "productionPowerChosen"));
        }
    }

    /**
     * The method returns the DevCard chosen by the player.
     * @param id is the id of the button clicked by the player.
     * @return the DevCard chosen by the player.
     */
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

    /**
     * The method returns the int of the DevCard chosen by the player.
     * @param id is the id of the button clicked by the player.
     * @return the int of the DevCard chosen by the player.
     */
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

    /**
     * The method changes the scene to the SetLeaderCardController.
     */
    private void onLeaderCards(Event event){
        ProductionPower productionPower = null;
        ImageView imageView = (ImageView) event.getSource();
        if(imageView.getId().equals("leader1") && lightModel.getLeaderProductionPowerList().size()>=1){
            productionPower = lightModel.getLeaderProductionPowerList().get(0);
            lightModel.getChosenIntegerList().add(4);
        }
        else if(imageView.getId().equals("leader2") && lightModel.getLeaderProductionPowerList().size()>=2){
            productionPower = lightModel.getLeaderProductionPowerList().get(1);
            lightModel.getChosenIntegerList().add(5);
        }
        if(productionPower!=null){
            SetLeaderCardController controller = new SetLeaderCardController(productionPower);
            controller.addAllObservers(observers);
            disableEveryButton();
            Platform.runLater(() -> SceneController.changeScene(controller, "set_leaderCard_scene.fxml"));
        }
    }

    /**
     * The method changes the scene to set the base ProductionPower.
     */
    private void onBaseProductionPower(Event event){
        lightModel.getChosenIntegerList().add(0);
        SetBaseController setBaseController = new SetBaseController();
        setBaseController.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeScene(setBaseController, "set_base_scene.fxml"));
    }

    /**
     * The method disables the button of the ProductionPowers already paid.
     */
    private void disableChosenProductionPowers(){
        if(lightModel.getChosenIntegerList().contains(0)){
            baseProductionPower.setDisable(true);
        }
    }

    /**
     * The method disables every button of the scene.
     */
    private void disableEveryButton(){
        //devCards
        for(int i=0; i<3; i++){
            ImageView imageView = (ImageView) devCards.getChildren().get(i);
            imageView.setDisable(true);
        }
        //leaders
        for(int i=0; i<2; i++){
            ImageView imageView = (ImageView) leaderCards.getChildren().get(i);
            imageView.setDisable(true);
        }
        baseProductionPower.setDisable(true);
    }

}