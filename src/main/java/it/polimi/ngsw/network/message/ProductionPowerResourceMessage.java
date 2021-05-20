package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.ProductionPower;
import it.polimi.ngsw.model.Resource;

import java.awt.*;

public class ProductionPowerResourceMessage extends ResourceMessage {

    ProductionPower productionPower;

    public ProductionPowerResourceMessage(String nickname, Resource resource, ProductionPower productionPower){
        super(nickname, resource, MessageType.PRODUCTION_POWER_RESOURCE);
        this.productionPower = productionPower;
    }

    public ProductionPower getProductionPower(){
        return productionPower;
    }

    @Override
    public String toString() {
        return "send the resource of the production power";
    }

}
