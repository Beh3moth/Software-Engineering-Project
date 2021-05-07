package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.ProductionPower;
import it.polimi.ngsw.model.Resource;

public class ProductionPowerCoordinatesMessage extends CoordinatesMessage{

    private ProductionPower productionPower;

    public ProductionPowerCoordinatesMessage(String nickname, Boolean[] isWarehouse, Integer[] shelfLevel, Resource[] resourceType, ProductionPower productionPower) {
        super(nickname, isWarehouse, shelfLevel, resourceType);
        this.productionPower = productionPower;
    }

    public ProductionPower getProductionPower() {
        return productionPower;
    }

}
