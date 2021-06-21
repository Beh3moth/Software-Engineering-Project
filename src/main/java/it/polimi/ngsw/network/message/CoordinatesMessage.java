package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.Resource;

/**
 * Message of the coordinates
 */
public class CoordinatesMessage extends Message {

    private Boolean[] isWarehouse;
    private Integer[] shelfLevel;
    private Resource[] resourceType;

    public CoordinatesMessage(String nickname, MessageType messageType, Boolean[] isWarehouse, Integer[] shelfLevel, Resource[] resourceType) {
        super(nickname, messageType);
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

    @Override
    public String toString() {
        return "Player " + this.getNickname() + " send coordinatss for buy";
    }
}
