package it.polimi.ngsw.model;//import le enum delle risorse

import java.util.ArrayList;
import java.util.List;

/**
 * Class that describe a production situation, it describe the resource that you need to produce some resources
 */
public class ProductionPower {
    private final List<Resource> resourceToPay;
    private final List<Resource> resourceToProduce;
    private List<List<Object> > coordinates = new ArrayList<>();

    /**
     * Constructor method: receive a list of resources and describe the production power of every cards
     * @param resourcesNeeded  resources that i need to produce
     * @param resourceToProduce   resources that i will receive
     */
    public ProductionPower(List<Resource> resourcesNeeded, List<Resource> resourceToProduce){  //ottengo una lista er entrambe, no mappa, quindi se ho due schudi e un denaro, "scudo, scudo, denaro"
        this.resourceToPay = resourcesNeeded;
        this.resourceToProduce = resourceToProduce;
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
        return resourceToProduce;
    }


    //Aggiunti da fede

    public List<List<Object>> getCoordinates(){
        return coordinates;
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
    public boolean addSingleCoordinate(Resource resourceToPay, boolean warehouse, Integer shelfLevel, Player activePlayer){
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
                    tempList.add(2, shelfLevel);
                    coordinates.add(tempList);
                    return (coordinates.contains(tempList));
                }
            }

        }
        else return false;

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
     * @return true if successful.
     */
    public boolean cleanCoordinates(){
        coordinates.clear();
        return true;
    }



}
