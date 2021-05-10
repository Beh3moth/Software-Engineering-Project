package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.Resource;

public class DevCardCoordinatesMessage extends CoordinatesMessage{

    private DevCard devCard;
    private int slotToPut;

    public DevCardCoordinatesMessage(String nickname, Boolean[] isWarehouse, Integer[] shelfLevel, Resource[] resourceType, DevCard devCard, int slotToPut){
        super(nickname,MessageType.DEVCARD_COORDINATES_MESSAGE, isWarehouse, shelfLevel, resourceType);
        this.devCard = devCard;
        this.slotToPut = slotToPut;
    }

    public DevCard getDevCard(){return this.devCard;}
    public int getSlotToPut(){return this.slotToPut;}
    
}
