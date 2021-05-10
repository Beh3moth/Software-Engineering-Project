package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.DevCard;

public class DevCardMessage extends Message{
    private DevCard devCard;
    private int slotToPut;

    public DevCardMessage(String nickname, DevCard devCard, int slotToPut){
        super(nickname, MessageType.DEVCARD);
        this.devCard = devCard;
        this.slotToPut = slotToPut;
    }

    public DevCard getDevCard() {
        return devCard;
    }
    public int getSlotToPut(){return this.slotToPut;}
}
