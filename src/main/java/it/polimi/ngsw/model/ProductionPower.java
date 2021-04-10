package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that describe a production situation, it describe the resource that you need to produce some resources
 */
public class ProductionPower {
    private List<Resource> resourceToPay;
    private List<Resource> resourceToProduce;
    private List<List<Object> > coordinates = new ArrayList<>();
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
        if(!(resourceToPay.size()==2)){
            return false;
        }
        if(!(resourceToReceive.size()==1)){
            return false;
        }
        this.resourceToPay = resourceToPay;
        this.resourceToProduce = resourceToReceive;
        return true;
    }

    /**
     * Clear the lists of resources to receive and to pay from a Base Production Power.
     */
    public void removeBaseProductionPowerLists(){
        resourceToPay.clear();
        resourceToProduce.clear();
    }

    //Leader Production Power methods

    /**
     * The method sets a single resource to receive form a Production Power.
     * It is intended to be used to allow the player to set the resource to receive from a Leader Production Power.
     * @param resource is the resource to set.
     * @return true if the method is successful, false otherwise.
     */
    public boolean setResourceToReceive(Resource resource){
        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(resource);
        resourceToProduce.clear();
        resourceToProduce = resourceList;
        return resourceToProduce.equals(resourceList);
    }

    //Coordinates methods

    /**
     * The method returns the coordinates of a Production Power.
     * @return null if the coordinates are empty, the coordinates otherwise.
     */
    public List<List<Object>> getCoordinates(){
        if(coordinates.isEmpty()){
            return null;
        } else return coordinates;
    }

    /**
     * The method adds the coordinates of a certain source at the end of a list of coordinates.
     * It also check if the coordinates are correct or not.
     * @param resourceToPay is the resourceToPay type to add.
     * @param warehouse is the location of the resourceToPay. It is Warehouse if true, Chest otherwise.
     * @param shelfLevel is the number of shelfLevel of the Warehouse. It is 0 if warehouse it's false.
     * @param activePlayer is the player who's  saving the coordinates.
     * @return true if the coordinates are correct and correctly added in the list, false otherwise.
     */
    public boolean addSingleCoordinate(Resource resourceToPay, boolean warehouse, int shelfLevel, Player activePlayer){
        List<Object> tempList = new ArrayList<>(3);

        if(resourceToPay!=Resource.EMPTY && shelfLevel<6){
            if(warehouse){
                if(activePlayer.getWarehouse().hasResource(shelfLevel, resourceToPay)){
                    tempList.add(0, resourceToPay);
                    tempList.add(1, true);
                    tempList.add(2, shelfLevel);
                    coordinates.add(tempList);
                    return (coordinates.contains(tempList));
                }
                else return false;
            }
            else {
                if(activePlayer.getChest().contains(resourceToPay, 1)){
                    tempList.add(0, resourceToPay);
                    tempList.add(1, false);
                    tempList.add(2, 0);
                    coordinates.add(tempList);
                    return (coordinates.contains(tempList));
                }
            }

        } else return false;

        return false;

    }

    /**
     * The method removes the coordinates of a Production Power and it puts the Resources to the resource locations.
     * @param activePlayer is the player who decided to renounce to a Production Power activated previously.
     * @return true if the method is successful.
     */
    public boolean removeSingleCoordinate(Player activePlayer){

        Resource resourceToPay;
        boolean warehouse;
        int shelfLevel;

        for (List<Object> coordinate : coordinates) {

            resourceToPay = (Resource) coordinate.get(0);
            warehouse = (boolean) coordinate.get(1);
            if (warehouse) {
                shelfLevel = (int) coordinate.get(2);
                if(!activePlayer.getWarehouse().addResourceToWarehouse(shelfLevel, resourceToPay)){
                    return false;
                }
            } else {
                if(!activePlayer.getChest().addResourceToChest(resourceToPay, 1)){
                    return false;
                }
            }

        }

        return true;

    }

    /**
     * Removes every coordinate without putting the resources to the resource location.
     */
    public void cleanCoordinates(){
        coordinates.clear();
    }



}
