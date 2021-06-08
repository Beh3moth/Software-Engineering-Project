package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.Resource;
import it.polimi.ngsw.observer.ViewObservable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReorderWarehouseController extends ViewObservable implements GenericSceneController {

    @FXML
    public ImageView shelf11;
    @FXML
    public ImageView shelf21;
    @FXML
    public ImageView shelf22;
    @FXML
    public ImageView shelf31;
    @FXML
    public ImageView shelf32;
    @FXML
    public ImageView shelf33;
    @FXML
    public ImageView shelf41;
    @FXML
    public ImageView shelf42;
    @FXML
    public ImageView shelf51;
    @FXML
    public ImageView shelf52;
    @FXML
    public Label moneyLabel;
    @FXML
    public Label shieldLabel;
    @FXML
    public Label slaveLabel;
    @FXML
    public Label stoneLabel;
    @FXML
    public ImageView money;
    @FXML
    public ImageView shield;
    @FXML
    public ImageView slave;
    @FXML
    public ImageView stone;

    private Resource firstShelf;
    private Resource secondShelf;
    private int secondShelfNumber;
    private Resource thirdShelf;
    private int thirdShelfNumber;

    private Map<Resource, Integer> resourcesMap = new HashMap<>();

    public void setReorderWarehouseController(Resource firstShelf, Resource secondShelf, int secondShelfNumber, Resource thirdShelf, int thirdShelfNumber, List<Resource> resourceList) {

        resourcesMap.put(Resource.MONEY, 0);
        resourcesMap.put(Resource.STONE, 0);
        resourcesMap.put(Resource.SHIELD, 0);
        resourcesMap.put(Resource.SLAVE, 0);

        if(firstShelf!=Resource.EMPTY && firstShelf != Resource.FAITHPOINT){
            resourcesMap.put(firstShelf, 1);
        }
        if(secondShelf!=Resource.EMPTY && secondShelf != Resource.FAITHPOINT){
            resourcesMap.put(secondShelf, secondShelfNumber);
        }
        if(thirdShelf!=Resource.EMPTY && thirdShelf != Resource.FAITHPOINT){
            resourcesMap.put(thirdShelf, thirdShelfNumber);
        }
        for(Resource resource : resourceList){
            resourcesMap.put(resource, resourcesMap.get(resource)+1 );
        }

    }

    @FXML
    public void initialize() {
        if(resourcesMap.containsKey(Resource.MONEY)){
            moneyLabel.setText(String.valueOf(resourcesMap.get(Resource.MONEY)));
        }
        else {
            moneyLabel.setText(String.valueOf(0));
        }
        if(resourcesMap.containsKey(Resource.STONE)){
            stoneLabel.setText(String.valueOf(resourcesMap.get(Resource.STONE)));
        }
        else {
            stoneLabel.setText(String.valueOf(0));
        }
        if(resourcesMap.containsKey(Resource.SHIELD)){
            shieldLabel.setText(String.valueOf(resourcesMap.get(Resource.SHIELD)));
        }
        else {
            shieldLabel.setText(String.valueOf(0));
        }
        if(resourcesMap.containsKey(Resource.SLAVE)){
            slaveLabel.setText(String.valueOf(resourcesMap.get(Resource.SLAVE)));
        }
        else {
            slaveLabel.setText(String.valueOf(0));
        }

        //Drop
        //Data is over the target. Can it be dropped?
        shelf11.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                if(dragEvent.getDragboard().hasString()){
                    dragEvent.acceptTransferModes(TransferMode.ANY);
                }
            }
        });
        //Data is dragged over a target. This is the reaction.
        shelf11.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                ImageView imageView = (ImageView) dragEvent.getSource();
                if(imageView.getId().equals(shelf11.getId()) && firstShelf==null){
                    Image image = new Image("images/icons/coin.png");
                    shelf11.setImage(image);
                    firstShelf = Resource.MONEY;
                }
            }
        });


        //Drag
        //Allow to drag
        money.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Dragboard dragboard = money.startDragAndDrop(TransferMode.ANY);
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString("money");
                dragboard.setContent(clipboardContent);
                mouseEvent.consume();
            }
        });
        //When dragging is completed
        money.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                resourcesMap.put(Resource.MONEY, resourcesMap.get(Resource.MONEY)-1 );
                dragEvent.consume();
            }
        });
    }
}
