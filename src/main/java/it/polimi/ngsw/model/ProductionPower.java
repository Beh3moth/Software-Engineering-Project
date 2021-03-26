package it.polimi.ngsw.model;//import le enum delle risorse

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that describe a production situation, it describe the resource that you need to produce some resources
 */
public class ProductionPower {
    private List<Resource> resourceToPay;
    private List<Resource> resourceToproduce;

    /**
     * Constructor method: receive a list of resources and describe the production power of every cards
     * @param resourcesNeeded  resources that i need to produce
     * @param resourceToProduce   resources that i will receive
     */
    public ProductionPower(List<Resource> resourcesNeeded, List<Resource> resourceToProduce){  //ottengo una lista er entrambe, no mappa, quindi se ho due schudi e un denaro, "scudo, scudo, denaro"
        this.resourceToPay = resourcesNeeded;
        this.resourceToproduce = resourceToProduce;
    }

    /**
     * Method that return the resources that i need to have to produce
     * @return a list of resources
     */
    public List<Resource> getResourceToPay(){
        return resourceToPay;
    }

    /**
     * Method that return the resources that i will receive after production
     * @return a list of resources
     */
    public List<Resource> getResourceToReceive(){
        return resourceToproduce;
    }
}
