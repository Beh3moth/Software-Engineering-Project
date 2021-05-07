package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.ProductionPower;

public class ProductionPowerResponseMessage extends ResponseMessage {

    private ProductionPower productionPower;

    public ProductionPowerResponseMessage(String nickname, boolean response, String action, ProductionPower productionPower) {
        super(nickname, response, action, MessageType.PRODUCTION_POWER_RESPONSE_MESSAGE);
        this.productionPower = productionPower;
    }

    public ProductionPower getProductionPower() {
        return productionPower;
    }

    @Override
    public String toString() {
        return "ProductionPowerResponseMessage{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                '}';
    }
}
