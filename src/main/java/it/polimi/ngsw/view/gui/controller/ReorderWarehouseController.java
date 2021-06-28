package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.*;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReorderWarehouseController extends ViewObservable implements GenericSceneController {

    @FXML
    public Pane warehouse;
    @FXML
    public Pane resourcesNumber;
    @FXML
    public Pane resourcesImageView;
    @FXML
    public Button confirmButton;
    @FXML
    public Button resetButton;

    private Resource firstShelf;
    private Resource secondShelf;
    private int secondShelfNumber;
    private Resource thirdShelf;
    private int thirdShelfNumber;
    private Resource fsr;
    private int fsn;
    private Resource ssr;
    private int ssn;
    private List<Resource> resourceList;
    private boolean isIndependent;
    private LightModel lightModel;

    private Map<Resource, Integer> resourcesMap = new HashMap<>();
    private Warehouse warehouseSurrogate = new Warehouse();

    public void setReorderWarehouseController(LightModel lightModel, Resource firstShelf, Resource secondShelf, int secondShelfNumber, Resource thirdShelf, int thirdShelfNumber, Resource fsr, int fsn, Resource ssr, int ssn, List<Resource> resourceList, boolean isIndependent) {

        this.firstShelf = firstShelf;
        this.secondShelf = secondShelf;
        this.secondShelfNumber = secondShelfNumber;
        this.thirdShelf = thirdShelf;
        this.thirdShelfNumber = thirdShelfNumber;
        this.fsr = fsr;
        this.fsn = fsn;
        this.ssr = ssr;
        this.ssn = ssn;
        this.resourceList = resourceList;
        this.isIndependent = isIndependent;
        this.lightModel = lightModel;

        if (lightModel.getFsr() != Resource.EMPTY){
            this.warehouseSurrogate.unlockLeaderLevel(lightModel.getFsr());}
        if(lightModel.getSsr() != Resource.EMPTY){
            this.warehouseSurrogate.unlockLeaderLevel(lightModel.getSsr());
        }

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
        if(fsr!=Resource.EMPTY && fsr != Resource.FAITHPOINT){
            resourcesMap.put(fsr, fsn);
        }
        if(ssr!=Resource.EMPTY && ssr != Resource.FAITHPOINT){
            resourcesMap.put(ssr, ssn);
        }
        if(resourceList!=null){
            for(Resource resource : resourceList){
                resourcesMap.put(resource, resourcesMap.get(resource)+1 );
            }
        }

    }



    @FXML
    public void initialize(){
        setLabelsValues();
        setResourceOnDragDetected();
        setResourceOnDragDone();
        setTargetOnDragOver();
        setTargetOnDragDropped();
        confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmButton);
        resetButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onResetButton);
    }

    private void onResetButton(Event event){
        setReorderWarehouseController(lightModel, firstShelf, secondShelf, secondShelfNumber, thirdShelf, thirdShelfNumber, fsr, fsn, ssr, ssn, resourceList, isIndependent);
        setLabelsValues();
        setResourceOnDragDetected();
        setResourceOnDragDone();
        setTargetOnDragOver();
        setTargetOnDragDropped();
        resetWarehouse();
    }

    private void resetWarehouse(){
        for(Node node : warehouse.getChildren()){
            ImageView imageView = (ImageView) node;
            Image image = new Image("images/icons/shelf.png");
            imageView.setImage(image);
        }
        this.warehouseSurrogate = new Warehouse();
        if (lightModel.getFsr() != Resource.EMPTY){
            this.warehouseSurrogate.unlockLeaderLevel(lightModel.getFsr());
        }
        if(lightModel.getSsr() != Resource.EMPTY){
            this.warehouseSurrogate.unlockLeaderLevel(lightModel.getSsr());
        }
    }

    private void onConfirmButton(Event event){
        Resource newFirstShelf;
        if(warehouseSurrogate.getShelf(1).getResourceType()!=Resource.EMPTY && warehouseSurrogate.getShelf(1).getResourceType()!=Resource.FAITHPOINT){
            newFirstShelf = warehouseSurrogate.getShelf(1).getResourceType();
        }
        else newFirstShelf = Resource.EMPTY;
        List<Resource> newSecondShelf = getResourceListFromShelf(warehouseSurrogate.getShelf(2));
        List<Resource> newThirdShelf = getResourceListFromShelf(warehouseSurrogate.getShelf(3));
        List<Resource> newFirstSpecialShelf = getResourceListFromShelf(warehouseSurrogate.getShelf(4));
        List<Resource> newSecondSpecialShelf = getResourceListFromShelf(warehouseSurrogate.getShelf(5));
        List<Resource> discardList = createDiscardList();
        lightModel.setCrossPosition(lightModel.getCrossPosition()+getTotalNumberOfResources());
        lightModel.setFirstShelf(newFirstShelf);
        lightModel.setSecondShelf(warehouseSurrogate.getShelf(2).getResourceType());
        lightModel.setSecondShelfNumber(warehouseSurrogate.getShelf(2).getResourceNumber());
        lightModel.setThirdShelf(warehouseSurrogate.getShelf(3).getResourceType());
        lightModel.setThirdShelfNumber(warehouseSurrogate.getShelf(3).getResourceNumber());
        lightModel.setFsr(warehouseSurrogate.getShelf(4).getResourceType());
        lightModel.setFsn(warehouseSurrogate.getShelf(4).getResourceNumber());
        lightModel.setSsr(warehouseSurrogate.getShelf(5).getResourceType());
        lightModel.setSsn(warehouseSurrogate.getShelf(5).getResourceNumber());
        this.resourcesMap.clear();
        notifyObserver(obs -> obs.onUpdateNewWarehouse(newFirstShelf, newSecondShelf, newThirdShelf, newFirstSpecialShelf, newSecondSpecialShelf, discardList, isIndependent));
    }

    private int getTotalNumberOfResources(){
        int number = 0;
        for(Resource resource : Resource.values()){
            if(resource!=Resource.EMPTY && resource!=Resource.FAITHPOINT){
                number = number + resourcesMap.get(resource);
            }
        }
        return number;
    }

    private List<Resource> createDiscardList(){
        List<Resource> discardList = new ArrayList<>();
        for(Resource resource : Resource.values()){
            if(resource!=Resource.EMPTY && resource!=Resource.FAITHPOINT && resourcesMap.containsKey(resource)){
                for(int i=0; i<resourcesMap.get(resource); i++){
                    discardList.add(resource);
                }
            }
        }
        if(discardList.size() > 0){
            return discardList;
        }
        else return null;
    }

    private List<Resource> getResourceListFromShelf(Shelf shelf){
        List<Resource> resourceList = new ArrayList<>();
        if(shelf!=null && shelf.getResourceNumber()>0){
            for(int i=0; i<shelf.getResourceNumber(); i++){
                resourceList.add(shelf.getResourceType());
            }
        }
        if(resourceList.size() > 0){
            return resourceList;
        }
        else return null;

    }

    private void setLabelsValues(){
        for(Node node : resourcesNumber.getChildren()){
            Label label = (Label) node;
            Resource resource = getResourceFromLabelName(label.getId());
            if(resourcesMap.containsKey(resource) && resource != null){
                label.setText(String.valueOf(resourcesMap.get(resource)));
            }
            else {
                label.setText(String.valueOf(0));
            }
        }
    }

    private Resource getResourceFromLabelName(String labelName){
        switch (labelName) {
            case "moneyLabel":
                return Resource.MONEY;
            case "shieldLabel":
                return Resource.SHIELD;
            case "slaveLabel":
                return Resource.SLAVE;
            case "stoneLabel":
                return Resource.STONE;
            default:
                return null;
        }
    }

    private void setResourceOnDragDetected(){
        for(Node node : resourcesImageView.getChildren()){
            ImageView imageView = (ImageView) node;
            node.setOnDragDetected(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(resourcesMap.get(getResourceFromImageViewName(imageView.getId()))>0){
                        Dragboard dragboard = node.startDragAndDrop(TransferMode.MOVE);
                        ClipboardContent clipboardContent = new ClipboardContent();
                        clipboardContent.putString(imageView.getId());
                        dragboard.setContent(clipboardContent);
                    }
                    mouseEvent.consume();
                }
            });
        }
    }

    private void setResourceOnDragDone(){
        for(Node node : resourcesImageView.getChildren()){
            node.setOnDragDone(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {

                }
            });
        }
    }

    private void setTargetOnDragOver(){
        //The iteration proceeds in this way: 11, 12, 22, 31, 32, 33, 41, 42, 51, 52
        for(Node node : warehouse.getChildren()){
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
        for(Node node : warehouse.getChildren()){
            ImageView imageView = (ImageView) node;
            node.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    ImageView imageViewSource = (ImageView) dragEvent.getGestureSource();
                    String imageViewName = imageView.getId();
                    int level = imageViewName.charAt(5);
                    level = level - 48;
                    final int definitiveLevel = level;
                    Resource resource = getResourceFromImageViewName(imageViewSource.getId());
                    if( warehouseSurrogate.addResourceToWarehouse(definitiveLevel, resource)){
                        Image image = new Image("images/icons/" + getImagePath(imageViewSource.getId()));
                        imageView.setImage(image);
                        resourcesMap.put(getResourceFromImageViewName(imageViewSource.getId()), resourcesMap.get(getResourceFromImageViewName(imageViewSource.getId()))-1 );
                        updateLabel(imageViewSource.getId(), resourcesMap.get(getResourceFromImageViewName(imageViewSource.getId())));
                        dragEvent.consume();
                    }
                }
            });
        }
    }

    private void updateLabel(String imageViewName, int numberOfResourcesLeft){
        switch (imageViewName) {
            case "money":
                Label label0 = (Label) resourcesNumber.getChildren().get(0);
                label0.setText(String.valueOf(numberOfResourcesLeft));
                break;
            case "shield":
                Label label1 = (Label) resourcesNumber.getChildren().get(1);
                label1.setText(String.valueOf(numberOfResourcesLeft));
                break;
            case "slave":
                Label label2 = (Label) resourcesNumber.getChildren().get(2);
                label2.setText(String.valueOf(numberOfResourcesLeft));
                break;
            case "stone":
                Label label3 = (Label) resourcesNumber.getChildren().get(3);
                label3.setText(String.valueOf(numberOfResourcesLeft));
                break;
            default:
                break;
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

    //It has to implement the dragging conditions

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
