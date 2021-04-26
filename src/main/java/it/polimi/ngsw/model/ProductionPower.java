package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that describe a production situation, it describe the resource that you need to produce some resources
 */
public class ProductionPower {
    private List<Resource> resourceToPay;
    private List<Resource> resourceToProduce;
    private List<Boolean> isWarehouse = new ArrayList<>();
    private List<Integer> shelfLevel = new ArrayList<>();
    private List<Resource> resourceType = new ArrayList<>();
    boolean isBaseProductionPower = false;

    /**
     * Constructor method: receive a list of resources and describe the production power of every cards
     * @param resourcesNeeded  resources that i need to produce
     * @param resourceToProduce   resources that i will receive
     */
    public ProductionPower(List<Resource> resourcesNeeded, List<Resource> resourceToProduce){
        this.resourceToPay = resourcesNeeded;
        this.resourceToProduce = resourceToProduce;
    }

    //Standard methods

    /**
     * Method that return the resources that i need to have to produce
     * @return a list of resources
     */
    public List<Resource> getResourceToPay(){
        return resourceToPay;
    }

    /**
     * The method returns the resources to pay as a Map where the Resource is the key and the Integer is the value.
     * @return a Map<Resource, Integer>
     */
    public Map<Resource, Integer> getResourceToPayAsMap(){

        Map<Resource, Integer> resourceToPayMap = new HashMap<>();

        for(Resource resource : Resource.values()){
            if(!resource.equals(Resource.EMPTY) && !resource.equals(Resource.FAITHPOINT)){
                resourceToPayMap.put(resource, 0);
            }
        }

        for(Resource resource : resourceToPay){
            resourceToPayMap.put(resource, resourceToPayMap.get(resource)+1);
        }

        return resourceToPayMap;
    }

    /**
     * Method that return the resources that i will receive after production
     * @return a list of resources
     */
    public List<Resource> getResourceToReceive(){
        return resourceToProduce;
    }

    //Base Production Power methods

    /**
     * The method makes a Production Power a Base Production Power.
     */
    public void setBaseProductionPowerToTrue(){
        isBaseProductionPower = true;
    }

    /**
     * The method returns true if the Production Power is a Base Production Power, false otherwise.
     */
    public boolean isBaseProductionPower(){
        return isBaseProductionPower;
    }

    /**
     * The method sets the resource to receive and the two resources to pay from a Base Production Power.
     * @param resourceToPay are the two resources that the Base Production Power requires.
     * @param resourceToReceive is the single resource that the Base Production Power produces.
     * @return true if the procedure is successful, false otherwise.
     */
    public boolean setBaseProductionPowerLists(List<Resource> resourceToPay, List<Resource> resourceToReceive){
        if(!(resourceToPay.size()==2) || !(resourceToReceive.size()==1) || !isBaseProductionPower){
            return false;
        }
        else{
            this.resourceToPay = resourceToPay;
            this.resourceToProduce = resourceToReceive;
            return true;
        }
    }

    /**
     * Clear the lists of resources to receive and to pay from a Base Production Power.
     */
    public boolean removeBaseProductionPowerLists(){
        if(isBaseProductionPower && resourceToPay!=null && resourceToProduce!=null){
            resourceToPay.clear();
            resourceToProduce.clear();
            return true;
        }
        else return false;
    }

    //Leader Production Power methods

    /**
     * The method sets a single resource to receive form a Production Power.
     * It is intended to be used to allow the player to set the resource to receive from a Leader Production Power.
     * @param resource is the resource to set.
     * @return true if the method is successful, false otherwise.
     */
    public boolean setLeaderProductionPowerResourceToReceive(Resource resource){
        List<Resource> resourceList = new ArrayList<>();
        if(resource.equals(Resource.EMPTY)){
            return false;
        }
        else{
            resourceList.add(resource);
            resourceToProduce = resourceList;
            return resourceToProduce.equals(resourceList);
        }
    }

    //Coordinates methods

    public List<Boolean> getIsWarehouse(){
        return this.isWarehouse;
    }

    public List<Integer> getShelfLevel(){
        return this.shelfLevel;
    }

    public List<Resource> getResourceType(){
        return this.resourceType;
    }


    /**
     * The method adds the coordinates of a certain source at the end of a list of coordinates.
     * It also check if the coordinates are correct or not.
     * @param resourceType is the resourceToPay type to add.
     * @param isWarehouse is the location of the resourceToPay. It is Warehouse if true, Chest otherwise.
     * @param shelfLevel is the number of shelfLevel of the Warehouse. It is 0 if warehouse it's false.
     * @return true if the coordinates are correct and correctly added in the list, false otherwise.
     */
    public boolean addSingleCoordinate(Resource resourceType, boolean isWarehouse, int shelfLevel){

        if(resourceType!=Resource.EMPTY && shelfLevel<6 && shelfLevel>=0){
            this.resourceType.add(resourceType);
            this.isWarehouse.add(isWarehouse);
            this.shelfLevel.add(shelfLevel);
            return true;
        } else return false;

    }


    /**
     * The method adds the coordinates of a certain source at the end of a list of coordinates and
     * also check if the coordinates are correct or not.
     * @param resourcesTypes is the type of resource coordinate.
     * @param isWarehouse is the location (Warehouse or Chest) coordinate.
     * @param shelfLevel is the location coordinate in Warehouse.
     * @return true if successful, false otherwise.
     */
    public boolean addCoordinates(Resource[] resourcesTypes, Boolean[] isWarehouse, Integer[] shelfLevel){
        for(int i=0; i<resourcesTypes.length; i++){
            if(resourcesTypes[i].equals(Resource.EMPTY) || ( shelfLevel[i]<0 || shelfLevel[i]>5 ) ){
                return false;
            }
            this.resourceType.add(resourcesTypes[i]);
            this.isWarehouse.add(isWarehouse[i]);
            this.shelfLevel.add(shelfLevel[i]);
        }
        return true;
    }


    /**
     * The method removes the coordinates of a Production Power and puts the Resources to the origin locations.
     * @param activePlayer is the player who decided to renounce to a Production Power activated previously.
     * @return true if the method is successful.
     */
    public boolean moveResourcesToOrigin(Player activePlayer){

        for(int i=0; i<this.isWarehouse.size(); i++){

            if (isWarehouse.get(i)) {
                if(!activePlayer.getWarehouse().addResourceToWarehouse(shelfLevel.get(i), resourceType.get(i))){
                    return false;
                }
            } else {
                if(!activePlayer.getChest().addResource(resourceType.get(i), 1)){
                    return false;
                }
            }

        }

        cleanCoordinates();
        return true;

    }

    /**
     * Removes every coordinate without putting the resources to the resource location.
     * @return true if the isWarehouse, resourceType and shelfLevel aren't null, false otherwise.
     */
    public boolean cleanCoordinates(){
        if(isWarehouse!=null && resourceType!=null && shelfLevel!=null){
            this.isWarehouse.clear();
            this.resourceType.clear();
            this.shelfLevel.clear();
            return true;
        }
        else return false;
    }



}
