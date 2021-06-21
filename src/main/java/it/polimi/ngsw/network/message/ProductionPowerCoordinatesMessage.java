package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.ProductionPower;
import it.polimi.ngsw.model.Resource;

/**
 * Message about the coordinates of a production power
 */
public class ProductionPowerCoordinatesMessage extends CoordinatesMessage{

    private ProductionPower productionPower;

    public ProductionPowerCoordinatesMessage(String nickname, Boolean[] isWarehouse, Integer[] shelfLevel, Resource[] resourceType, ProductionPower productionPower) {
        super(nickname, MessageType.PRODUCTION_POWER_COORDINATES_MESSAGE, isWarehouse, shelfLevel, resourceType);
        this.productionPower = productionPower;
    }

    public ProductionPower getProductionPower() {
        return productionPower;
    }

}
