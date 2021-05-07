package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.ProductionPower;
import it.polimi.ngsw.model.Resource;

public class ProductionPowerResourceMessage extends ResourceMessage {

    ProductionPower productionPower;

    public ProductionPowerResourceMessage(String nickname, Resource resource, ProductionPower productionPower) {
        super(nickname, resource);
        this.productionPower = productionPower;
    }

    public ProductionPower getProductionPower(){
        return productionPower;
    }

}
