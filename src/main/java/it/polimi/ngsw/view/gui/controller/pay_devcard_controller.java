package it.polimi.ngsw.view.gui.controller;

import it.polimi.ngsw.model.DevCard;
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

public class pay_devcard_controller extends ViewObservable implements GenericSceneController{

    private DevCard devCard;
    private Resource discountPowerOne;
    private Resource discountPowerTwo;
    private Map<Resource, Integer> cost;
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
    private int slotToPut;

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


    public pay_devcard_controller(DevCard devCard, LightModel lightModel, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo){
        this.cost = devCard.getDevCostAsMap();
        this.firstShelf = lightModel.getFirstShelf();
        this.secondShelf = lightModel.getSecondShelf();
        this.thirdShelf = lightModel.getThirdShelf();
        this.secondShelfNumber = lightModel.getSecondShelfNumber();
        this.thirdShelfNumber = lightModel.getThirdShelfNumber();
        this.Chest = lightModel.getChest();
        this.slotToPut = slotToPut;
        this.devCard = devCard;
        this.discountPowerOne = discountPowerOne;
        this.discountPowerTwo = discountPowerTwo;
        setCost();
    }

    @FXML
    public void initialize(){
        startingPoint();
        warehouse.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onWarehouseButtonClick);
        chest.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onChestButtonClick);
    }

    public void startingPoint(){
        upDateResourceToPay();
        chooseZone();
    }

    public void chooseZone(){
        disableShelfButton();
        if(controlWarehouse(actualResource)){warehouse.setDisable(false);}
        else{warehouse.setDisable(true);}
        if(controlChest(actualResource)){chest.setDisable(false);}
        else{chest.setDisable(true);}
    }

    public void onWarehouseButtonClick(Event event){
        warehouse.setDisable(true);
        chest.setDisable(true);
        isWarehouse.add(true);
        resourceType.add(actualResource);
        chooseShelf();
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

    public void chooseShelf(){
        if(this.firstShelf == actualResource)uno.setDisable(false);
        if(this.secondShelf == actualResource)due.setDisable(false);
        if(this.thirdShelf == actualResource)tre.setDisable(false);
        uno.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onUnoButtonClick);
        due.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDueButtonClick);
        tre.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onTreButtonClick);
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

    public void disableShelfButton(){
        uno.setDisable(true);
        due.setDisable(true);
        tre.setDisable(true);
    }

    public void upDateResourceToPay(){
        if(cost.get(Resource.MONEY) > 0){
            int n = cost.get(Resource.MONEY);
            Image img = new Image("images/icons/coin.png");
            resource_to_pay.setImage(img);
            cost.replace(Resource.MONEY, n -1);
            this.actualResource = Resource.MONEY;
        }
        else if(cost.get(Resource.SHIELD) > 0){
            int n = cost.get(Resource.SHIELD);
            Image img = new Image("images/icons/shield.png");
            resource_to_pay.setImage(img);
            cost.replace(Resource.SHIELD, n -1);
            this.actualResource = Resource.SHIELD;
        }
        else if(cost.get(Resource.SLAVE) > 0){
            int n = cost.get(Resource.SLAVE);
            Image img = new Image("images/icons/servant.png");
            resource_to_pay.setImage(img);
            cost.replace(Resource.SLAVE, n -1);
            this.actualResource = Resource.SLAVE;
        }
        else if(cost.get(Resource.STONE) > 0){
            int n = cost.get(Resource.STONE);
            Image img = new Image("images/icons/stone.png");
            resource_to_pay.setImage(img);
            cost.replace(Resource.STONE, n -1);
            this.actualResource = Resource.STONE;
        }
        else if(cost.get(Resource.MONEY) == 0 && cost.get(Resource.SHIELD) == 0 && cost.get(Resource.SLAVE) == 0 && cost.get(Resource.STONE) == 0){
            resource_to_pay.setImage(null);
            new Thread(() ->notifyObserver(obs -> obs.onUpdatePayDevCard(isWarehouse.toArray(new Boolean[0]), shelfLevel.toArray(new Integer[0]), resourceType.toArray(new Resource[0]), devCard, slotToPut, discountPowerOne, discountPowerTwo))).start();
        }
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

    public void setCost(){
        if(this.discountPowerOne != Resource.EMPTY){
            int n = this.cost.get(this.discountPowerOne);
            if(n > 0){
                this.cost.replace(discountPowerOne, n -1);
            }
        }
        if(this.discountPowerTwo != Resource.EMPTY){
            int n = this.cost.get(this.discountPowerTwo);
            if(n > 0){
                this.cost.replace(discountPowerTwo, n -1);
            }
        }
    }

}
