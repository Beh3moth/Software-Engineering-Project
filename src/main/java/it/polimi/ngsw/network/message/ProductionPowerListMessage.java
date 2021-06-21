package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.ProductionPower;

import java.util.List;

/**
 * Message of the list of all the production powers
 */
public class ProductionPowerListMessage extends Message {

    private List<ProductionPower> productionPowerList;
    private String action;

    public ProductionPowerListMessage(String nickname, List<ProductionPower> productionPowerList, String action) {
        super(nickname, MessageType.PRODUCTION_POWER_LIST);
        this.productionPowerList = productionPowerList;
        this.action = action;
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

    public String getAction() {
        return action;
    }
}