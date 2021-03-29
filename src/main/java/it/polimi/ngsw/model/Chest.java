package it.polimi.ngsw.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The Chest's class represents a stock without limits of storage.
 */
public class Chest {

    private Map<Resource, Integer> chestResources = new HashMap<>();

    public Chest(){
        chestResources.put(Resource.SHIELD, 0);
        chestResources.put(Resource.STONE, 0);
        chestResources.put(Resource.SLAVE, 0);
        chestResources.put(Resource.MONEY, 0);
    }

    /**
     * Method that add a number of resources of a certain type to the Chest.
     * @param resourceType is the parameter used as key to set the resource type.
     * @param resourceNumber is the parameter used as value to set the number of resources. It can't be negative.
     */
    public boolean addResourceToChest(Resource resourceType, Integer resourceNumber){
        if(resourceNumber>=0) {
            chestResources.put(resourceType, chestResources.get(resourceType) + resourceNumber);
            return true;
        }
        else return false;
    }

    /**
     * Method that remove a certain number of resources of a certain type from the Chest.
     * The method avoid the user from setting the number of resources below zero.
     * @param resourceType is the parameter used as key to set the resource type.
     * @param resourceNumber  is the parameter used as value to set the number of resources.
     */
    public boolean removeResourceFromChest(Resource resourceType, Integer resourceNumber){
        if(chestResources.get(resourceType) - resourceNumber >= 0){
            chestResources.put(resourceType, chestResources.get(resourceType) - resourceNumber);
            return true;
        }
        else return false;
    }

    /**
     * Method that returns the number of shield's resources in the Chest.
     * @return the int number of shields in the Chest.
     */
    public int getShieldFromChest(){
        return chestResources.get(Resource.SHIELD);
    }

    /**
     * Method that returns the number of stone's resources in the Chest.
     * @return the int number of stones in the Chest.
     */
    public int getStoneFromChest(){
        return chestResources.get(Resource.STONE);
    }

    /**
     * Method that returns the number of slave's resources in the Chest.
     * @return the int number of slaves in the Chest.
     */
    public int getSlaveFromChest0(){
        return chestResources.get(Resource.SLAVE);
    }

    /**
     * Method that returns the number of money in the Chest.
     * @return the int number of money in the Chest.
     */
    public int getMoneyFromChest(){
        return chestResources.get(Resource.MONEY);
    }

}
