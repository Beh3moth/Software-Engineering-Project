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
     * @return true if the resource is added, false otherwise.
     */
    public boolean addResource(Resource resourceType, Integer resourceNumber){
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
     * @return true if the resource is removed, false otherwise.
     */
    public boolean removeResource(Resource resourceType, Integer resourceNumber){
        if(chestResources.get(resourceType) - resourceNumber >= 0){
            chestResources.put(resourceType, chestResources.get(resourceType) - resourceNumber);
            return true;
        }
        else return false;
    }

    /**
     * The method returns the number of resources requested.
     * @param resource is the type of resource requested.
     * @return the number of a certain type of resources requested.
     */
    public int getResourceNumber(Resource resource){
        return chestResources.get(resource);
    }

    /**
     * The method returns the total number of resources of the Chest.
     * @return an integer representing the total number of resources of the Chest.
     */
    public int getTotalNumberOfResources(){
        return getShield() + getStone() + getSlave() + getMoney();
    }

    /**
     * Method that returns the number of shield's resources in the Chest.
     * @return the int number of shields in the Chest.
     */
    public int getShield(){
        return chestResources.get(Resource.SHIELD);
    }

    /**
     * Method that returns the number of stone's resources in the Chest.
     * @return the int number of stones in the Chest.
     */
    public int getStone(){
        return chestResources.get(Resource.STONE);
    }

    /**
     * Method that returns the number of slave's resources in the Chest.
     * @return the int number of slaves in the Chest.
     */
    public int getSlave(){
        return chestResources.get(Resource.SLAVE);
    }

    /**
     * Method that returns the number of money in the Chest.
     * @return the int number of money in the Chest.
     */
    public int getMoney(){
        return chestResources.get(Resource.MONEY);
    }

    /**
     * The method returns true if the chest has at least the number of resources specified.
     * @param resource is the resource to check.
     * @param numberOfResource is the number of resources to check.
     * @return true if the chest has at least the number of resources specified
     */
    public boolean contains(Resource resource, int numberOfResource){
        Integer resources = chestResources.get(resource);
        return (resources != null && resources >= numberOfResource);
    }


    public Map<Resource, Integer> getResourcesAsMap(){
        return chestResources;
    }

    //canBuy methods

    /**
     * this method return the number of the resource in the array
     * @param resources
     * @param warehouse
     * @param resource
     * @return this method return the number of the resource in the array
     */
    public int getNumberOfResourceInArray(Resource[] resources, Boolean[] warehouse, Resource resource){
        int n = 0;
        for(int i = 0; i < resources.length; i++){
            if((!warehouse[i]) && (resources[i] == resource))
                n++;
        }
        return n;
    }

    /**
     * this method control if the player can pay a single resource
     * @param resources
     * @param warehouse
     * @param resource
     * @return true if he can false otherwise
     */
    public boolean controlResource(Resource[] resources, Boolean[] warehouse, Resource resource){
        if(getNumberOfResourceInArray(resources, warehouse, resource) <= getResourceNumber(resource))
            return true;
        else return false;
    }

    /**
     * this method control if the player can pay with the chest's resources
     * @param resources
     * @param warehouse
     * @return true if the player can pay with the chest's resources false otherwise
     */
    public boolean canBuy(Resource[] resources, Boolean[] warehouse){
        for(Resource resource : Resource.values()){
            if(resource != Resource.EMPTY && resource != Resource.FAITHPOINT){
                if(!controlResource(resources, warehouse, resource))
                    return false;
            }
        }
        return true;
    }
}
