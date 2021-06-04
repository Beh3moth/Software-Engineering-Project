package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.Resource;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import it.polimi.ngsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class GameController extends ViewObservable implements GenericSceneController {

    private LightModel lightModel;

    @FXML
    private Button take_marble;
    @FXML
    private Button devCardMarket;
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
    //FaithPath


    @FXML
    public void initialize(){
        take_marble.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTakeMarbleButtonClick);
        upDateValuesOfChest();
        upDateValuesOfWarehouse();
        upDateDevCard();
        devCardMarket.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDevCardMarket);
        ArrayList<Node> faithPathList = new ArrayList<>();
        faithPathList.add(faithPath.getChildren().get(24));
        setCrossPosition(0);
    }

    public void setLightModel(LightModel lightModel){this.lightModel = lightModel;}

    public void onTakeMarbleButtonClick(Event event){
        take_marble_controller takeMarbleController = new take_marble_controller(this.lightModel);
        takeMarbleController.addAllObservers(observers);
        SceneController.changeScene(takeMarbleController, "take_marble_scene.fxml");
    }

    public void onDevCardMarket(Event event){
        DevCardSceneController controller = new DevCardSceneController();
        controller.setDevCardMarket(lightModel.getDevCardMarket());
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
            Image firstshelf = new Image("images/icons" + getTypeResourceForImage(lightModel.getFirstShelf()) + ".png");
            FIRSTSHELF.setImage(firstshelf);
        }

        if(lightModel.getSecondShelfNumber() == 0){
            SECONDSHELF1.setImage(null);
            SECONDSHELF2.setImage(null);
        }
        else{
            Image secondshelf = new Image("images/icons" + getTypeResourceForImage(lightModel.getSecondShelf()) + ".png");
            SECONDSHELF1.setImage(secondshelf);
            if(lightModel.getSecondShelfNumber() == 2){
                SECONDSHELF2.setImage(secondshelf);
            }
        }

        if(lightModel.getThirdShelfNumber() == 0){
            THIRDSHELF1.setImage(null);
            THIRDSHELF2.setImage(null);
            THIRDSHELF3.setImage(null);
        }
        else{
            Image thirdshelf = new Image("images/icons" + getTypeResourceForImage(lightModel.getThirdShelf()) + ".png");
            THIRDSHELF1.setImage(thirdshelf);
            if(lightModel.getThirdShelfNumber() >= 2){
                THIRDSHELF2.setImage(thirdshelf);
            }
            if(lightModel.getThirdShelfNumber() == 3){
                THIRDSHELF3.setImage(thirdshelf);
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
        if(lightModel.getActiveDevCardList().size()>=1){
            Image devcard1 = new Image("images/devCard/" + lightModel.getActiveDevCardList().get(0).getCardColour().toString() + lightModel.getActiveDevCardList().get(0).getDevLevel() + lightModel.getActiveDevCardList().get(0).getPV() + ".png");
            DEVCARD1.setImage(devcard1);
        }
        if(lightModel.getActiveDevCardList().size()>=2){
            Image devcard2 = new Image("images/devCard/" + lightModel.getActiveDevCardList().get(1).getCardColour().toString() + lightModel.getActiveDevCardList().get(1).getDevLevel() + lightModel.getActiveDevCardList().get(1).getPV() + ".png");
            DEVCARD2.setImage(devcard2);
        }
        if(lightModel.getActiveDevCardList().size()>=2){
            Image devcard3 = new Image("images/devCard/" + lightModel.getActiveDevCardList().get(2).getCardColour().toString() + lightModel.getActiveDevCardList().get(2).getDevLevel() + lightModel.getActiveDevCardList().get(2).getPV() + ".png");
            DEVCARD3.setImage(devcard3);
        }
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

}
