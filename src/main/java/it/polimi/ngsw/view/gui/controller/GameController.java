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

import java.util.ArrayList;

public class GameController extends ViewObservable implements GenericSceneController {

    private LightModel lightModel;

    @FXML
    private Pane mainPain;
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
    public Pane faithPath;
    //FaithPath


    @FXML
    public void initialize(){
        //mainPain.setVisible(true);
        take_marble.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTakeMarbleButtonClick);
        moneyNumber.setText(lightModel.getChest().get(Resource.MONEY).toString());
        shieldNumber.setText(lightModel.getChest().get(Resource.SHIELD).toString());
        stoneNumber.setText(lightModel.getChest().get(Resource.STONE).toString());
        slaveNumber.setText(lightModel.getChest().get(Resource.SLAVE).toString());
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

    public void upDateValues(){
        moneyNumber.setText(lightModel.getChest().get(Resource.MONEY).toString());
        shieldNumber.setText(lightModel.getChest().get(Resource.SHIELD).toString());
        stoneNumber.setText(lightModel.getChest().get(Resource.STONE).toString());
        slaveNumber.setText(lightModel.getChest().get(Resource.SLAVE).toString());
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
