package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.Resource;
import it.polimi.ngsw.observer.ViewObservable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class SetBaseController extends ViewObservable implements GenericSceneController {

    private List<Resource> resourcesToPay = new ArrayList<>(0);
    private List<Resource> resourceToReceive = new ArrayList<>(0);

    @FXML
    public Pane resourcesToChose;
    @FXML
    public Pane resourcesChosen;
    @FXML
    public Button confirmButton;

    @FXML
    public void initialize(){
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButton);
        setResourceOnDragDetected();
        setTargetOnDragOver();
        setTargetOnDragDropped();
    }

    private void onConfirmButton(Event event){
        if(resourcesToPay.size()==2 && resourceToReceive.size() == 1){
            notifyObserver(obs -> obs.onUpdateTwoResourceList(resourcesToPay, resourceToReceive, "setBaseProductionPower"));
        }
    }

    private void setResourceOnDragDetected(){
        for(Node node : resourcesToChose.getChildren()){
            ImageView imageView = (ImageView) node;
            node.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Dragboard dragboard = node.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent clipboardContent = new ClipboardContent();
                    clipboardContent.putString(imageView.getId());
                    dragboard.setContent(clipboardContent);
                    mouseEvent.consume();
                }
            });
        }
    }

    private void setTargetOnDragOver(){
        //The iteration proceeds in this way: 11, 12, 22, 31, 32, 33, 41, 42, 51, 52
        for(Node node : resourcesChosen.getChildren()){
            node.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    if(dragEvent.getDragboard().hasString()){
                        dragEvent.acceptTransferModes(TransferMode.MOVE);
                        dragEvent.consume();
                    }
                }
            });
        }
    }

    private void setTargetOnDragDropped(){
        for(Node node : resourcesChosen.getChildren()){
            ImageView imageView = (ImageView) node;
            node.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    ImageView imageViewSource = (ImageView) dragEvent.getGestureSource();
                    String imageViewName = imageView.getId();
                    Resource resource = getResourceFromImageViewName(imageViewSource.getId());
                    Image image = new Image("images/icons/" + getImagePath(imageViewSource.getId()));
                    if((node.getId().equals("1") || node.getId().equals("2")) && resourcesToPay.size()<2){
                        imageView.setImage(image);
                        resourcesToPay.add(resource);
                        dragEvent.consume();
                    }
                    else if(node.getId().equals("3") && resourceToReceive.size()<1){
                        imageView.setImage(image);
                        resourceToReceive.add(resource);
                        dragEvent.consume();
                    }
                }
            });
        }
    }

    private Resource getResourceFromImageViewName(String imageViewName){
        switch (imageViewName) {
            case "money":
                return Resource.MONEY;
            case "shield":
                return Resource.SHIELD;
            case "slave":
                return Resource.SLAVE;
            case "stone":
                return Resource.STONE;
            default:
                return null;
        }
    }

    private String getImagePath(String imageViewId){
        switch (imageViewId) {
            case "money":
                return "coin.png";
            case "shield":
                return "shield.png";
            case "slave":
                return "servant.png";
            case "stone":
                return "stone.png";
            default:
                return "croce.png";
        }
    }


}