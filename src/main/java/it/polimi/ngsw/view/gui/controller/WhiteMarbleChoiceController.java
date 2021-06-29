package it.polimi.ngsw.view.gui.controller;

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

import java.util.ArrayList;
import java.util.List;

public class WhiteMarbleChoiceController extends ViewObservable implements GenericSceneController {

    private List<Resource> resourceList;
    private Resource firstWhite;
    private Resource secondWhite;
    private LightModel lightModel;

    @FXML
    public Button firstWhiteButton;
    @FXML
    public Button secondWhiteButton;

    public WhiteMarbleChoiceController(LightModel lightModel, List<Resource> resources, Resource firstWhite, Resource secondWhite){
        this.resourceList = resources;
        this.firstWhite = firstWhite;
        this.secondWhite = secondWhite;
        this.lightModel = lightModel;
    }

    @FXML
    public void initialize(){
        firstWhiteButton.setGraphic(new ImageView("images/icons/" + getImagePath(firstWhite)));
        secondWhiteButton.setGraphic(new ImageView("images/icons/" + getImagePath(secondWhite)));
        firstWhiteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenWhiteMarble);
        secondWhiteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChosenWhiteMarble);
    }

    private String getImagePath(Resource resource){
        switch (resource) {
            case MONEY:
                return "coin.png";
            case SHIELD:
                return "shield.png";
            case SLAVE:
                return "servant.png";
            case STONE:
                return "stone.png";
            default:
                return "croce.png";
        }
    }

    private void onChosenWhiteMarble(Event event){
        List<Resource> resourceList = new ArrayList<>();
        firstWhiteButton.setDisable(true);
        secondWhiteButton.setDisable(true);
        boolean whitePowerUsed = false;
        for(Resource resource : this.resourceList){
            if(resource==Resource.EMPTY && !whitePowerUsed){
                Button button = (Button) event.getSource();
                if(button.getId().equals("firstWhiteButton")){
                    resourceList.add(firstWhite);
                    whitePowerUsed = true;
                }
                else if(button.getId().equals("secondWhiteButton")){
                    resourceList.add(secondWhite);
                    whitePowerUsed = true;
                }
            }
            else if(resource==Resource.EMPTY && whitePowerUsed){
                resourceList.add(Resource.EMPTY);
            }
            else {
                resourceList.add(resource);
            }
        }
        if(resourceList.size()==this.resourceList.size()){
            this.resourceList = resourceList;
        }
        if(this.resourceList.contains(Resource.EMPTY)){
            firstWhiteButton.setDisable(false);
            secondWhiteButton.setDisable(false);
        }
        else {
            ReorderWarehouseController controller = new ReorderWarehouseController();
            controller.addAllObservers(observers);
            controller.setReorderWarehouseController(lightModel, lightModel.getFirstShelf(), lightModel.getSecondShelf(), lightModel.getSecondShelfNumber(), lightModel.getThirdShelf(), lightModel.getThirdShelfNumber(), lightModel.getFsr(), lightModel.getFsn(), lightModel.getSsr(), lightModel.getSsn(), this.resourceList, false);
            Platform.runLater(() -> SceneController.changeScene(controller, "reorder_warehouse_scene.fxml"));
        }
    }

}
