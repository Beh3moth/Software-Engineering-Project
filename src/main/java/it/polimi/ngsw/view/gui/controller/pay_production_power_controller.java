package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.ProductionPower;
import it.polimi.ngsw.model.Resource;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class pay_production_power_controller  extends ViewObservable implements GenericSceneController{

    @FXML
    private ImageView resource_to_pay;
    @FXML
    private Button warehouse;
    @FXML
    private Button chest;
    @FXML
    private Button uno;
    @FXML
    private Button due;
    @FXML
    private Button tre;
    @FXML
    private Button quattro;
    @FXML
    private Button cinque;

    private ProductionPower productionPower;
    private List<Resource> cost;
    private Resource firstShelf;
    private Resource secondShelf;
    private Resource thirdShelf;
    private int secondShelfNumber;
    private int thirdShelfNumber;
    private Map <Resource, Integer> Chest;
    private List<Boolean> isWarehouse = new ArrayList<>();
    private List<Integer> shelfLevel = new ArrayList<>();
    private List<Resource> resourceType = new ArrayList<>();
    private Resource actualResource;
    private Resource firstSpecialresource;
    private int firstSpecialNumber;
    private Resource secondSpecialResource;
    private int secondSpecialNumber;

    public pay_production_power_controller(ProductionPower productionPower, LightModel lightModel){
        this.productionPower = productionPower;
        this.cost = productionPower.getResourceToPay();
        this.firstShelf = lightModel.getFirstShelf();
        this.secondShelf = lightModel.getSecondShelf();
        this.thirdShelf = lightModel.getThirdShelf();
        this.secondShelfNumber = lightModel.getSecondShelfNumber();
        this.thirdShelfNumber = lightModel.getThirdShelfNumber();
        this.Chest = lightModel.getChest();
        this.firstSpecialresource = lightModel.getFsr();
        this.firstSpecialNumber = lightModel.getFsn();
        this.secondSpecialResource = lightModel.getSsr();
        this.secondSpecialNumber = lightModel.getSsn();
    }

    public void initialize(){
        startingPoint();
        warehouse.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onWarehouseButtonClick);
        chest.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChestButtonClick);
        uno.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onUnoButtonClick);
        due.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDueButtonClick);
        tre.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTreButtonClick);
        quattro.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onQuattroButtonClick);
        cinque.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCinqueButtonClick);
    }
    /**
     * this method is the start of the scene
     */
    public void startingPoint(){
        upDateResourceToPay();
        chooseZone();
        disableShelfButton();
    }

    /**
     * this method charges the resource with the warehouse
     * @param event
     */
    public void onWarehouseButtonClick(Event event){
        warehouse.setDisable(true);
        chest.setDisable(true);
        isWarehouse.add(true);
        resourceType.add(actualResource);
        chooseShelf();
    }

    /**
     * this method permit to choose the shelf of the warehouse where you want to pay the resource
     */
    public void chooseShelf(){
        if(this.firstShelf == actualResource)uno.setDisable(false);
        if(this.secondShelf == actualResource)due.setDisable(false);
        if(this.thirdShelf == actualResource)tre.setDisable(false);
        if(this.firstSpecialresource == actualResource && this.firstSpecialNumber > 0)quattro.setDisable(false);
        if(this.secondSpecialResource == actualResource && this.secondSpecialNumber > 0)cinque.setDisable(false);
    }

    /**
     * this method set the shelf case: one
     * @param event
     */
    public void onUnoButtonClick(Event event){
        disableShelfButton();
        this.firstShelf = Resource.EMPTY;
        this.shelfLevel.add(1);
        startingPoint();
    }

    /**
     * this method set the shelf case: two
     * @param event
     */
    public void onDueButtonClick(Event event){
        disableShelfButton();
        this.secondShelfNumber--;
        if(this.secondShelfNumber == 0)this.secondShelf = Resource.EMPTY;
        this.shelfLevel.add(2);
        startingPoint();
    }

    /**
     * this method set the shelf case: three
     * @param event
     */
    public void onTreButtonClick(Event event){
        disableShelfButton();
        this.thirdShelfNumber--;
        if(this.thirdShelfNumber == 0)this.thirdShelf = Resource.EMPTY;
        this.shelfLevel.add(3);
        startingPoint();
    }

    /**
     * this method set the shelf case: first leader shelf
     * @param event
     */
    public void onQuattroButtonClick(Event event){
        disableShelfButton();
        this.firstSpecialNumber--;
        this.shelfLevel.add(4);
        startingPoint();
    }

    /**
     * this method set the shelf case: second leader shelf
     * @param event
     */
    public void onCinqueButtonClick(Event event){
        disableShelfButton();
        this.secondSpecialNumber--;
        this.shelfLevel.add(5);
        startingPoint();
    }

    /**
     * this method charges the resource with the resource
     * @param event
     */
    public void onChestButtonClick(Event event){
        warehouse.setDisable(true);
        chest.setDisable(true);
        isWarehouse.add(false);
        resourceType.add(actualResource);
        shelfLevel.add(0);
        int n = this.Chest.get(actualResource);
        this.Chest.replace(actualResource, n - 1);
        startingPoint();
    }

    /**
     * this method update te resource to pay
     */
    public void upDateResourceToPay(){
        if(cost.size() > 0){
            Image img = new Image("images/icons/" + getTypeResourceForImage(cost.get(0)) + ".png");
            resource_to_pay.setImage(img);
            this.actualResource = cost.get(0);
            cost.remove(0);
        }
        else{
            resource_to_pay.setImage(null);
            notifyObserver(obs -> obs.onUpdatePayProductionPower(isWarehouse.toArray(new Boolean[0]), shelfLevel.toArray(new Integer[0]), resourceType.toArray(new Resource[0]), productionPower));
        }
    }

    /**
     * this method permit to chose the zone where you want to pay the resource: warehouse or chest
     */
    public void chooseZone(){
        disableShelfButton();
        if(controlWarehouse(actualResource)){warehouse.setDisable(false);}
        else{warehouse.setDisable(true);}
        if(controlChest(actualResource)){chest.setDisable(false);}
        else{chest.setDisable(true);}
    }

    /**
     * this method disable the button of the shelf
     */
    public void disableShelfButton(){
        uno.setDisable(true);
        due.setDisable(true);
        tre.setDisable(true);
        quattro.setDisable(true);
        cinque.setDisable(true);
    }

    /**
     * this method control if you can pay the resource with the warehouse
     * @param resource
     * @return true if you can pay the resource with the warehouse
     */
    public boolean controlWarehouse(Resource resource){
        if(resource == this.firstShelf)return true;
        if(resource == this.secondShelf)return true;
        if(resource == this.thirdShelf)return true;
        if(resource == this.firstSpecialresource && this.firstSpecialNumber > 0)return true;
        if(resource == this.secondSpecialResource && this.secondSpecialNumber > 0)return true;
        return false;
    }

    /**
     * this method control if you can pay the resource with the chest
     * @param resource
     * @return true if you can pay the resource with the chest
     */
    public boolean controlChest(Resource resource){
        if(Chest.get(resource) > 0)return true;
        else return false;
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
}
