package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.Resource;

import java.util.List;
import java.util.Map;

public class ReorderWarehouseMessage extends Message{

    private static final long serialVersionUID = -6247374243399107843L;
    private Map<Resource, Integer> mapResources;
    private Resource firstLevel;
    private Resource secondLevel;
    private Boolean isIndependent;
    public ReorderWarehouseMessage(String nickname, Map<Resource, Integer> mapResources, Resource firstLevel, Resource secondLevel, Boolean isIndependent){
        super(nickname, MessageType.REORDER_WAREHOUSE);
        this.mapResources = mapResources;
        this.firstLevel =firstLevel;
        this.secondLevel = secondLevel;
        this.isIndependent = isIndependent;
    }

    public Map<Resource, Integer> getMapResources() {
        return mapResources;
    }
    public Resource getFirstLevel(){
        return firstLevel;
    }
    public Resource getSecondLevel(){
        return secondLevel;
    }

    public Boolean getIsIndependent(){
        return isIndependent;
    }
    @Override
    public String toString() {
        return "ReorderWarehouse{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                '}';
    }

}
