package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.DevCard;
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
import java.util.Map;

public class view_other_player_scene_controller extends ViewObservable implements GenericSceneController{

    private int crossPosition;
    private Map<Resource, Integer> resourcesAsMap;
    private List<DevCard> activeDevCards;
    private int[] shelfResNumber;
    private Resource[] shelfResType;
    private LightModel lightModel;

    @FXML
    public Pane faithPath;
    @FXML
    private Label moneyNumber;
    @FXML
    private Label shieldNumber;
    @FXML
    private Label stoneNumber;
    @FXML
    private Label slaveNumber;
    @FXML
    private ImageView DEVCARD1;
    @FXML
    private ImageView DEVCARD2;
    @FXML
    private ImageView DEVCARD3;
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
    private Button backButton;

    public void initialize(){
        ArrayList<Node> faithPathList = new ArrayList<>();
        faithPathList.add(faithPath.getChildren().get(24));
        setCrossPosition(crossPosition);
        upDateValuesOfChest();
        upDateDevCard();
        upDateValuesOfWarehouse();
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackButton);
    }

    /**
     * this method set the parameter of this class
     * @param crossPosition of the player
     * @param resourcesAsMap resource of player's chest
     * @param activeDevCards the player's active devcards
     * @param shelfResNumber the shelf number of the warehouse
     * @param shelfResType the shelf type of the warehouse
     * @param lightModel
     */
    public void setView_other_player_scene_controller(int crossPosition, Map<Resource, Integer> resourcesAsMap, List<DevCard> activeDevCards, int[] shelfResNumber, Resource[] shelfResType, LightModel lightModel){
        this.crossPosition = crossPosition;
        this.resourcesAsMap = resourcesAsMap;
        this.activeDevCards = activeDevCards;
        this.shelfResNumber = shelfResNumber;
        this.shelfResType = shelfResType;
        this.lightModel = lightModel;
    }

    /**
     * this method set the cross position
     * @param crossPosition
     */
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

    /**
     * this method update the values of the chest
     */
    public void upDateValuesOfChest(){
        moneyNumber.setText(resourcesAsMap.get(Resource.MONEY).toString());
        shieldNumber.setText(resourcesAsMap.get(Resource.SHIELD).toString());
        stoneNumber.setText(resourcesAsMap.get(Resource.STONE).toString());
        slaveNumber.setText(resourcesAsMap.get(Resource.SLAVE).toString());
    }

    /**
     * this method update the devcards
     */
    public void upDateDevCard(){
        if(activeDevCards.size() >= 1){
            Image devcard1 = new Image("images/devCard/" + activeDevCards.get(0).getCardColour().toString() + activeDevCards.get(0).getDevLevel() + activeDevCards.get(0).getPV() + ".png");
            DEVCARD1.setImage(devcard1);
        }
        else{DEVCARD1.setImage(null);}
        if(activeDevCards.size() >= 2){
            Image devcard2 = new Image("images/devCard/" + activeDevCards.get(1).getCardColour().toString() + activeDevCards.get(1).getDevLevel() + activeDevCards.get(1).getPV() + ".png");
            DEVCARD2.setImage(devcard2);
        }else{DEVCARD2.setImage(null);}
        if(activeDevCards.size() == 3){
            Image devcard3 = new Image("images/devCard/" + activeDevCards.get(2).getCardColour().toString() + activeDevCards.get(2).getDevLevel() + activeDevCards.get(2).getPV() + ".png");
            DEVCARD3.setImage(devcard3);
        }else{DEVCARD3.setImage(null);}
    }

    /**
     * this method update the values of the warehouse
     */
    public void upDateValuesOfWarehouse(){

        if(shelfResType[0] == Resource.EMPTY){
            FIRSTSHELF.setImage(null);
        }
        else{
            Image firstshelf = new Image("images/icons/" + getTypeResourceForImage(shelfResType[0]) + ".png");
            FIRSTSHELF.setImage(firstshelf);
        }

        if(shelfResNumber[1] == 0){
            SECONDSHELF1.setImage(null);
            SECONDSHELF2.setImage(null);
        }
        else{
            Image secondshelf = new Image("images/icons/" + getTypeResourceForImage(shelfResType[1]) + ".png");
            SECONDSHELF1.setImage(secondshelf);
            if(shelfResNumber[1] == 2){
                SECONDSHELF2.setImage(secondshelf);
            }
            else{
                SECONDSHELF2.setImage(null);
            }
        }

        if(shelfResNumber[2] == 0){
            THIRDSHELF1.setImage(null);
            THIRDSHELF2.setImage(null);
            THIRDSHELF3.setImage(null);
        }
        else{
            Image thirdshelf = new Image("images/icons/" + getTypeResourceForImage(shelfResType[2]) + ".png");
            THIRDSHELF1.setImage(thirdshelf);
            if(shelfResNumber[2] >= 2){
                THIRDSHELF2.setImage(thirdshelf);
            }
            else{
                THIRDSHELF2.setImage(null);
                THIRDSHELF3.setImage(null);
            }
            if(shelfResNumber[2] == 3){
                THIRDSHELF3.setImage(thirdshelf);
            }
            else{
                THIRDSHELF3.setImage(null);
            }
        }
    }
    /**
     * @param resource which you want to return the exactly name of the image of the resource
     * @return the exactly name of the resource
     */
    public String getTypeResourceForImage(Resource resource){
        switch(resource){
            case MONEY:return "coin";
            case STONE:return "stone";
            case SLAVE:return "servant";
            case SHIELD:return "shield";
            default: return null;
        }
    }

    /**
     * this method permit t change scene in game_scene
     * @param event
     */
    private void onBackButton(Event event){
        GameController gameController = new GameController();
        gameController.addAllObservers(observers);
        gameController.setLightModel(lightModel);
        Platform.runLater(() -> SceneController.changeScene(gameController, "game_scene.fxml"));
    }

}
