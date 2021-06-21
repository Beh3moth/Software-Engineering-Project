package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.Resource;

/**
 * Message of the coordinates of a devcard
 */
public class DevCardCoordinatesMessage extends CoordinatesMessage{

    private DevCard devCard;
    private int slotToPut;
    private Resource discountPowerOne;
    private Resource discountPowerTwo;

    public DevCardCoordinatesMessage(String nickname, Boolean[] isWarehouse, Integer[] shelfLevel, Resource[] resourceType, DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo){
        super(nickname,MessageType.DEVCARD_COORDINATES_MESSAGE, isWarehouse, shelfLevel, resourceType);
        this.devCard = devCard;
        this.slotToPut = slotToPut;
        this.discountPowerOne = discountPowerOne;
        this.discountPowerTwo = discountPowerTwo;
    }

    public DevCard getDevCard(){return this.devCard;}
    public int getSlotToPut(){return this.slotToPut;}
    public Resource getDiscountPowerOne(){return this.discountPowerOne;}
    public Resource getDiscountPowerTwo(){return this.discountPowerTwo;}
    
}
