package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.ProductionPower;
import it.polimi.ngsw.model.Resource;

/**
 * The responde of the action devcard
 */
public class DevCardResponseMessage extends ResponseMessage{

    private DevCard devCard;
    private int slotToPut;
    private Resource discountPowerOne;
    private Resource discountPowerTwo;

    public DevCardResponseMessage(String nickname, boolean response, String action, DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo) {
        super(nickname, response, action, MessageType.DEVCARD_RESPONSE_MESSAGE);
        this.devCard = devCard;
        this.slotToPut = slotToPut;
        this.discountPowerOne = discountPowerOne;
        this.discountPowerTwo = discountPowerTwo;
    }

    public DevCard getDevCard() {
        return this.devCard;
    }
    public int getSlotToPut(){return this.slotToPut;}
    public Resource getDiscountPowerOne(){return this.discountPowerOne;}
    public Resource getDiscountPowerTwo(){return this.discountPowerTwo;}

    @Override
    public String toString() {
        return "DevCardResponseMessage{" +
                "nickname = " + getNickname() +
                ", messageType = " + getMessageType() +
                '}';
    }
}
