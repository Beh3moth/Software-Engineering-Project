package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.ProductionPower;

import java.util.List;

public class ProductionPowerListMessage extends Message {

    private List<ProductionPower> productionPowerList;

    ProductionPowerListMessage(String nickname, MessageType messageType, List<ProductionPower> productionPowerList) {
        super(nickname, messageType);
        this.productionPowerList = productionPowerList;
    }

    public List<ProductionPower> getProductionPowerList() {
        return productionPowerList;
    }

    @Override
    public String toString() {
        return "LeaderCardMessage{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                '}';
    }

}