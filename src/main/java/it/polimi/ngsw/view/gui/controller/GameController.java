package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.Resource;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;

public class GameController extends ViewObservable implements GenericSceneController {

    private LightModel lightModel;
    List<LeaderCard> leaderCardList;

    @FXML
    private Button take_marble;
    @FXML
    private Button devCardMarket;
    @FXML
    private Button view_other_player;
    @FXML
    private Label moneyNumber;
    @FXML
    private Label shieldNumber;
    @FXML
    private Label stoneNumber;
    @FXML
    private Label slaveNumber;
    @FXML
    private ImageView FIRSTSHELF;
    @FXML
    private ImageView SECONDSHELF1;
    @FXML
    private ImageView SECONDSHELF2;
    @FXML
    private ImageView THIRDSHELF1;
    @FXML
    private ImageView THIRDSHELF2;
    @FXML
    private ImageView THIRDSHELF3;
    @FXML
    private ImageView DEVCARD1;
    @FXML
    private ImageView DEVCARD2;
    @FXML
    private ImageView DEVCARD3;
    @FXML
    public Pane faithPath;
    @FXML
    private ImageView papalCard1;
    @FXML
    private ImageView papalCard2;
    @FXML
    private ImageView papalCard3;
    @FXML
    private Label PVnumber;
    @FXML
    private Button reorder;
    @FXML
    private Button production;

    //FaithPath


    @FXML
    public void initialize(){
        take_marble.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTakeMarbleButtonClick);
        view_other_player.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onViewOtherPlayerButtonClick);
        upDateValuesOfChest();
        upDateValuesOfWarehouse();
        upDateDevCard();
        upDatePapalCard();
        upDatePV();
        devCardMarket.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDevCardMarket);
        ArrayList<Node> faithPathList = new ArrayList<>();
        faithPathList.add(faithPath.getChildren().get(24));
        setCrossPosition(lightModel.getCrossPosition());
        reorder.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onReorder);
        production.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onProduction);
    }

    public void onProduction(Event event){
        ProductionChoiceController controller = new ProductionChoiceController();
        controller.setProductionChoiceController(lightModel);
        controller.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeScene(controller, "production_choice_scene.fxml"));
    }

    public void onReorder(Event event){
        ReorderWarehouseController controller = new ReorderWarehouseController();
        controller.setReorderWarehouseController(lightModel, lightModel.getFirstShelf(), lightModel.getSecondShelf(), lightModel.getSecondShelfNumber(), lightModel.getThirdShelf(), lightModel.getThirdShelfNumber(), lightModel.getFsr(), lightModel.getFsn(), lightModel.getSsr(), lightModel.getSsn(), null, true);
        controller.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeScene(controller, "reorder_warehouse_scene.fxml"));
    }

    public void setLightModel(LightModel lightModel){
        this.lightModel = lightModel;
    }

    public void onTakeMarbleButtonClick(Event event){
        take_marble_controller takeMarbleController = new take_marble_controller(this.lightModel);
        takeMarbleController.addAllObservers(observers);
        SceneController.changeScene(takeMarbleController, "take_marble_scene.fxml");
    }

    public void onDevCardMarket(Event event){
        DevCardSceneController controller = new DevCardSceneController();
        controller.setDevCardMarket(lightModel.getDevCardMarket(), lightModel);
        controller.addAllObservers(observers);
        SceneController.changeScene(controller, "dev_card_market_scene.fxml");
    }

    public void upDateValuesOfChest(){
        moneyNumber.setText(lightModel.getChest().get(Resource.MONEY).toString());
        shieldNumber.setText(lightModel.getChest().get(Resource.SHIELD).toString());
        stoneNumber.setText(lightModel.getChest().get(Resource.STONE).toString());
        slaveNumber.setText(lightModel.getChest().get(Resource.SLAVE).toString());
    }

    public void upDateValuesOfWarehouse(){

        if(lightModel.getFirstShelf() == Resource.EMPTY){
            FIRSTSHELF.setImage(null);
        }
        else{
            Image firstshelf = new Image("images/icons/" + getTypeResourceForImage(lightModel.getFirstShelf()) + ".png");
            FIRSTSHELF.setImage(firstshelf);
        }

        if(lightModel.getSecondShelfNumber() == 0){
            SECONDSHELF1.setImage(null);
            SECONDSHELF2.setImage(null);
        }
        else{
            Image secondshelf = new Image("images/icons/" + getTypeResourceForImage(lightModel.getSecondShelf()) + ".png");
            SECONDSHELF1.setImage(secondshelf);
            if(lightModel.getSecondShelfNumber() == 2){
                SECONDSHELF2.setImage(secondshelf);
            }
            else{
                SECONDSHELF2.setImage(null);
            }
        }

        if(lightModel.getThirdShelfNumber() == 0){
            THIRDSHELF1.setImage(null);
            THIRDSHELF2.setImage(null);
            THIRDSHELF3.setImage(null);
        }
        else{
            Image thirdshelf = new Image("images/icons/" + getTypeResourceForImage(lightModel.getThirdShelf()) + ".png");
            THIRDSHELF1.setImage(thirdshelf);
            if(lightModel.getThirdShelfNumber() >= 2){
                THIRDSHELF2.setImage(thirdshelf);
            }
            else{
                THIRDSHELF2.setImage(null);
                THIRDSHELF3.setImage(null);
            }
            if(lightModel.getThirdShelfNumber() == 3){
                THIRDSHELF3.setImage(thirdshelf);
            }
            else{
                THIRDSHELF3.setImage(null);
            }
        }
    }

    public String getTypeResourceForImage(Resource resource){
            switch(resource){
                case MONEY:return "coin";
                case STONE:return "stone";
                case SLAVE:return "servant";
                case SHIELD:return "shield";
                default: return null;
            }
    }

    public void upDateDevCard(){
        if(lightModel.getActiveDevCardMap().get(0) != null){
            Image devcard1 = new Image("images/devCard/" + lightModel.getActiveDevCardMap().get(0).getCardColour().toString() + lightModel.getActiveDevCardMap().get(0).getDevLevel() + lightModel.getActiveDevCardMap().get(0).getPV() + ".png");
            DEVCARD1.setImage(devcard1);
        }
        else{DEVCARD1.setImage(null);}
        if(lightModel.getActiveDevCardMap().get(1) != null){
            Image devcard2 = new Image("images/devCard/" + lightModel.getActiveDevCardMap().get(1).getCardColour().toString() + lightModel.getActiveDevCardMap().get(1).getDevLevel() + lightModel.getActiveDevCardMap().get(1).getPV() + ".png");
            DEVCARD2.setImage(devcard2);
        }else{DEVCARD2.setImage(null);}
        if(lightModel.getActiveDevCardMap().get(2) != null){
            Image devcard3 = new Image("images/devCard/" + lightModel.getActiveDevCardMap().get(2).getCardColour().toString() + lightModel.getActiveDevCardMap().get(2).getDevLevel() + lightModel.getActiveDevCardMap().get(2).getPV() + ".png");
            DEVCARD3.setImage(devcard3);
        }else{DEVCARD3.setImage(null);}
    }

    public void setCrossPosition(int crossPosition){
        for(int i = 0; i < 25; i++){
            ImageView imageView = (ImageView) faithPath.getChildren().get(i);
            if(i!=(24-crossPosition)){
                imageView.setImage(null);
            }
            else {
                imageView.setImage(new Image("images/icons/croce.png"));
            }
        }
    }

    public void upDatePapalCard(){
        if(lightModel.isPapalCardOne()){
            papalCard1.setImage(new Image("images/icons/quadrato giallo.png"));
        }
        else{
                papalCard1.setImage(null);
                }

        if(lightModel.isPapalCardTwo()){
            papalCard2.setImage(new Image("images/icons/quadrato arancione.png"));
        }
        else{
            papalCard2.setImage(null);
        }

        if(lightModel.isPapalCardThree()){
            papalCard3.setImage(new Image("images/icons/quadrato rosso.png"));
        }
        else{
            papalCard3.setImage(null);
        }

    }

    public void upDatePV(){
        PVnumber.setText(String.valueOf((lightModel.getVictoryPoints())));
    }

    public void onViewOtherPlayerButtonClick(Event event){
        choose_nickname_player_controller cnp = new choose_nickname_player_controller();
        cnp.setLightModel(this.lightModel);
        cnp.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeScene(cnp, "choose_nickname_player_scene.fxml"));
    }


}
