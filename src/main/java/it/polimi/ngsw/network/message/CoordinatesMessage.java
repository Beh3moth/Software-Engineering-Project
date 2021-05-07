package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.Resource;

public class CoordinatesMessage extends Message {

    private Boolean[] isWarehouse;
    private Integer[] shelfLevel;
    private Resource[] resourceType;

    public CoordinatesMessage(String nickname, Boolean[] isWarehouse, Integer[] shelfLevel, Resource[] resourceType) {
        super(nickname, MessageType.PRODUCTION_POWER_COORDINATES_MESSAGE);
        this.isWarehouse = isWarehouse;
        this.shelfLevel = shelfLevel;
        this.resourceType = resourceType;
    }

    public Boolean[] getIsWarehouse() {
        return isWarehouse;
    }

    public Integer[] getShelfLevel() {
        return shelfLevel;
    }

    public Resource[] getResourceType() {
        return resourceType;
    }
}
