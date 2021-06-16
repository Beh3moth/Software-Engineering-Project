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

    public pay_production_power_controller(ProductionPower productionPower, LightModel lightModel){
        this.productionPower = productionPower;
        this.cost = productionPower.getResourceToPay();
        this.firstShelf = lightModel.getFirstShelf();
        this.secondShelf = lightModel.getSecondShelf();
        this.thirdShelf = lightModel.getThirdShelf();
        this.secondShelfNumber = lightModel.getSecondShelfNumber();
        this.thirdShelfNumber = lightModel.getThirdShelfNumber();
        this.Chest = lightModel.getChest();
    }

    public void initialize(){
        startingPoint();
        warehouse.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onWarehouseButtonClick);
        chest.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChestButtonClick);
        uno.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onUnoButtonClick);
        due.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDueButtonClick);
        tre.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTreButtonClick);
    }

    public void startingPoint(){
        upDateResourceToPay();
        chooseZone();
        disableShelfButton();
    }

    public void onWarehouseButtonClick(Event event){
        warehouse.setDisable(true);
        chest.setDisable(true);
        isWarehouse.add(true);
        resourceType.add(actualResource);
        chooseShelf();
    }

    public void chooseShelf(){
        if(this.firstShelf == actualResource)uno.setDisable(false);
        if(this.secondShelf == actualResource)due.setDisable(false);
        if(this.thirdShelf == actualResource)tre.setDisable(false);
    }

    public void onUnoButtonClick(Event event){
        disableShelfButton();
        this.firstShelf = Resource.EMPTY;
        this.shelfLevel.add(1);
        startingPoint();
    }

    public void onDueButtonClick(Event event){
        disableShelfButton();
        this.secondShelfNumber--;
        if(this.secondShelfNumber == 0)this.secondShelf = Resource.EMPTY;
        this.shelfLevel.add(2);
        startingPoint();
    }

    public void onTreButtonClick(Event event){
        disableShelfButton();
        this.thirdShelfNumber--;
        if(this.thirdShelfNumber == 0)this.thirdShelf = Resource.EMPTY;
        this.shelfLevel.add(3);
        startingPoint();
    }

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
    public void chooseZone(){
        disableShelfButton();
        if(controlWarehouse(actualResource)){warehouse.setDisable(false);}
        else{warehouse.setDisable(true);}
        if(controlChest(actualResource)){chest.setDisable(false);}
        else{chest.setDisable(true);}
    }

    public void disableShelfButton(){
        uno.setDisable(true);
        due.setDisable(true);
        tre.setDisable(true);
    }

    public boolean controlWarehouse(Resource resource){
        if(resource == this.firstShelf)return true;
        if(resource == this.secondShelf)return true;
        if(resource == this.thirdShelf)return true;
        return false;
    }

    public boolean controlChest(Resource resource){
        if(Chest.get(resource) > 0)return true;
        else return false;
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
}
