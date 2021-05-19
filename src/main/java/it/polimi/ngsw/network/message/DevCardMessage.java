package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.Resource;

public class DevCardMessage extends Message{
    private DevCard devCard;
    private int slotToPut;
    private Resource discountPowerOne;
    private Resource discountPowerTwo;

    public DevCardMessage(String nickname, DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo){
        super(nickname, MessageType.DEVCARD);
        this.devCard = devCard;
        this.slotToPut = slotToPut;
        this.discountPowerOne = discountPowerOne;
        this.discountPowerTwo = discountPowerTwo;
    }

    public DevCard getDevCard() {
        return devCard;
    }
    public int getSlotToPut(){return this.slotToPut;}
    public Resource getDiscountPowerOne(){return this.discountPowerOne;}
    public Resource getDiscountPowerTwo(){return this.discountPowerTwo;}

    @Override
    public String toString() {
        return "Player " + this.getNickname() + "choose to buy this devcard";
    }

}
