package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.ProductionPower;

public class DevCardResponseMessage extends ResponseMessage{

    private DevCard devCard;
    private int slotToPut;

    public DevCardResponseMessage(String nickname, boolean response, String action, DevCard devCard, int slotToPut) {
        super(nickname, response, action, MessageType.DEVCARD_RESPONSE_MESSAGE);
        this.devCard = devCard;
        this.slotToPut = slotToPut;
    }

    public DevCard getDevCard() {
        return this.devCard;
    }
    public int getSlotToPut(){return this.slotToPut;}

    @Override
    public String toString() {
        return "DevCardResponseMessage{" +
                "nickname = " + getNickname() +
                ", messageType = " + getMessageType() +
                '}';
    }
}
