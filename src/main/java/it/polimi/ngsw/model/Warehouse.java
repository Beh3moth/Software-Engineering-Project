package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class of the warehouse that is formed of three shelf, and up to two other special shelf
 */
public class Warehouse {
    private Shelf firstLevel;
    private Shelf secondLevel;
    private Shelf thirdLevel;
    private Shelf firstLeaderLevel;
    private Shelf secondLeaderLevel;
    private Resource firstLeaderLevelType;
    private Resource secondLeaderLevelType;
    private List<Resource> warehouseStock;
    private List <Resource> whitemarbleStock;

    public Warehouse(){
        firstLevel = new Shelf();
        secondLevel = new Shelf();
        thirdLevel = new Shelf();
        firstLeaderLevel = new Shelf();
        secondLeaderLevel = new Shelf();
        warehouseStock = new ArrayList<Resource>();
        whitemarbleStock = new ArrayList<Resource>();
        this.firstLeaderLevelType = Resource.EMPTY;
        this.secondLeaderLevelType = Resource.EMPTY;
    }

    /**
     * The method calculates the total number of resources
     * @return the total number of resources
     */
    public int getTotalNumberOfResources(){
        int nResource = 0;
        for(Resource resource : Resource.values()){
            if(!resource.equals(Resource.EMPTY)){
                nResource += this.getNumberOf(resource);
            }
        }
        return nResource;
    }

    /**
     * Method that put a resource to the white stock
     * @param resource the white marble
     * @return if went well
     */
    public boolean addResourceToWhiteStock(Resource resource){
        whitemarbleStock.add(resource);
        return true;
    }

    /**
     * Remove all the resources in the withe stock
     */
    public void removeFromWhiteStock(){
        whitemarbleStock.clear();
    }

    /**
     * Remove al resources from the stock of the warehouse
     */
    public void removeAllStock(){
        warehouseStock.clear();
    }

    /**
     * Method that gives the white marble stock
     * @return the white marble stock
     */
    public List<Resource> getWhiteStock(){
        return whitemarbleStock;
    }

    /**
     * @return the size of the white marble stock
     */
    public int numberOfWhiteStock(){
        return whitemarbleStock.size();
    }

    /**
     * @return the stock of the warehouse
     */
    public List<Resource> getWarehouseStock(){return warehouseStock; }

    /**
     * this method control that the special shelf can add a resource
     * @param level
     * @param resource
     * @return true if it can
     */
    public boolean controlSpecialShelf(int level, Resource resource){
        if(getShelf(level).getResourceType() != resource)
            return false;
        if(getShelf(level).getResourceNumber() == 2)
            return false;
        return true;
    }

    /**
     * this method control that the shelf can add a resource
     * @param level
     * @param resource
     * @return true if it can
     */
    public boolean controlShelf(int level, Resource resource){
        if(level == 4 || level == 5) return controlSpecialShelf(level, resource);
        if(getShelf(level).getResourceNumber() == 0)return true;
        if(getShelf(level).getResourceType() != resource && getShelf(level).getResourceType()!=Resource.EMPTY)return false;
        return true;

    }

    /**
     * Control if other shelf has the same resource
     * @param level the level of the shelf to be skipped
     * @param resource the resource to check
     * @return if 1 == not exist
     */
    public boolean controlOtherShelf(int level, Resource resource){
        for(int i = 1; i <= 3; i++){
            if(i != level && getShelf(i).getResourceType() == resource)return false;
        }
        return true;
    }

    /**
     *this method adds a resource to the warehouse checking that it is legal
     * @param level
     * @param resource
     * @return true if the addition was successful false if the addition was illegal
     */
    public boolean addResourceToWarehouse (int level, Resource resource){
        if(!controlShelf(level, resource))return false;
        if(level != 4 && level != 5){
            if(!controlOtherShelf(level, resource))return false;
        }
        else{
            if(level == 4 || level == 5){
                getShelf(level).addResourceNumber();
                return true;
            }
            else if(getShelf(level).getResourceNumber() == 0)getShelf(level).setResourceType(resource);
            getShelf(level).addResourceNumber();
            return true;
        }
        return true;
    }

    /**
     * Add the resource to the stock
     * @param resource the resource
     * @return if went well
     */
    public boolean addResourceToStock(Resource resource){
        warehouseStock.add(resource);
        return true;
    }

    /**
     * this method discards a resource directly from the warehouse checking that it is legal
     * @param level
     * @return true if the resource was successfully discarded false otherwise
     */
    public boolean discardResourceFromWarehouse (int level){

        if(getShelf(level).getResourceNumber() == 0)return false;
        else{
            getShelf(level).removeResourceNumber();
            if(getShelf(level).getResourceNumber() == 0 && level != 4 && level != 5)
                getShelf(level).setResourceType(Resource.EMPTY);
            return true;
        }
    }


    /**
     * this method unlocks the special shelf given by the leader card
     * @param resource
     * @return true if unlocked successfully, false otherwise
     */
    public boolean unlockLeaderLevel(Resource resource){
        if(firstLeaderLevelType == Resource.EMPTY){//la prima è ancora bloccata
            firstLeaderLevelType = resource;
            firstLeaderLevel.setResourceType(resource);
            return true;
        }
        else if(secondLeaderLevelType == Resource.EMPTY){//la seconda è ancora bloccata
            secondLeaderLevelType = resource;
            secondLeaderLevel.setResourceType(resource);
            return true;
        }
        return false;
    }



    /**
     * this method returns the shelf of the level passed by parameter
     * @param level
     * @return null if the level is not valid
     */
    public Shelf getShelf(int level){
        switch(level) {
            case 1:
                return firstLevel;
            case 2:
                return secondLevel;
            case 3:
                return thirdLevel;
            case 4:
                return firstLeaderLevel;
            case 5:
                return secondLeaderLevel;
            default:
                return null;
        }
    }

    /**
     * this method returns the leader's shelf of the level passed by parameter
     * @param level
     * @return null if the level is not valid
     */
    public Shelf getLeaderShelf(int level){
        switch(level){
            case 1:
                return firstLeaderLevel;
            case 2:
                return secondLeaderLevel;
            default:
                return null;
        }
    }

    /**
     * this method returns the leaderLevelType of the level passed by parameter
     * @param level
     * @return null if the level is not valid
     */
    public Resource getLeaderLevelType(int level){
        switch(level){
            case 1:
                return firstLeaderLevelType;
            case 2:
                return secondLeaderLevelType;
            default:
                return null;
        }
    }

    /**
     * this method returns true if the resource is present in the warehouse on the "level" floor
     * @param level
     * @param resource
     * @return true if the resource is present in the warehouse on the "level" floor
     */
    public boolean hasResource (int level, Resource resource){
        switch(level){
            case 1:
                if(firstLevel.getResourceType() == resource)return true;
                else return false;
            case 2:
                if(secondLevel.getResourceType() == resource)return true;
                else return false;
            case 3:
                if(thirdLevel.getResourceType() == resource)return true;
                else return false;
            case 4:
                if(firstLeaderLevel.getResourceNumber() >= 1)return true;
                else return false;
            case 5:
                if(secondLeaderLevel.getResourceNumber() >= 1)return true;
                else return false;
            default: return false;
        }
    }

    /**
     * this method returns true if in the warehouse there are at least n resources false otherwise
     * @param n
     * @param resource
     * @return true if in the warehouse there are at least n resources false otherwise
     */
    public boolean contains(int n,Resource resource){
        if(firstLevel.getResourceType() == resource){
            if(firstLevel.getResourceNumber() >= n)return true;
            else return false;
        }
        else if(secondLevel.getResourceType() == resource){
            if(secondLevel.getResourceNumber() >= n)return true;
            else return false;
        }
        else if(thirdLevel.getResourceType() == resource){
            if(thirdLevel.getResourceNumber() >= n)return true;
            else return false;
        }
        else if(firstLeaderLevelType == resource){
            if(firstLeaderLevel.getResourceNumber() >= n)return true;
            else return false;
        }
        else if(secondLeaderLevelType == resource){
            if(secondLeaderLevel.getResourceNumber() >= n)return true;
            else return false;
        }
        return false;
    }

    //samuele

    /**
     * Method that takes the level of the shelf that has a given resource type
     * @param resource the type of resource
     * @return the level
     */
    public int getLevel(Resource resource){
        if(firstLevel.getResourceType() == resource )return 1;
        else if(secondLevel.getResourceType() == resource)return 2;
        else if(thirdLevel.getResourceType() == resource)return 3;
        else return 0;
    }

    /**
     * this method return the number of the resource
     * @param resource
     * @return the number of the resource
     */
    public int getNumberOf(Resource resource){
        int nResource = 0;
        if(getShelf(getLevel(resource)) != null){
            nResource = getShelf(getLevel(resource)).getResourceNumber();}
        for(int i = 1; i < 3; i++){
            if(getLeaderLevelType(i) == resource)nResource += getLeaderShelf(i).getResourceNumber();
        }
        return nResource;
    }

    /**
     * this method calculate the total number of resources
     * @return the total number of resources
     */
    public int getTotalNumber(){
        int nResource = 0;
        for(Resource resource : Resource.values()){
            nResource += this.getNumberOf(resource);
        }
        return nResource;
    }

    /**
     * this method create a map with the resource in the warehouse and the number the resource
     * @return this map
     */
    public Map<Resource, Integer> getResourcesAsMap(){
        Map<Resource, Integer> map = new HashMap<>();
        for(Resource resource : Resource.values()) {
            if(!resource.equals(Resource.EMPTY) && !resource.equals(Resource.FAITHPOINT)){
                map.put(resource, getNumberOf(resource));
            }
        }
        return map;
    }

    //canBuy methods

    /**
     * this method control a resource end three resource
     * @param level
     * @return true if this resource is legal
     */
    public boolean controlResource(Integer[] level){
        for(int l = 1; l < 6; l++){
            int n = 0;
            for(int i = 0; i < level.length; i++){
                if(level[i] == l)n++;
            }
            if(!checkShelf(n, l))return false;
        }
        return true;
    }

    /**
     * Check if the shelf is full of resources
     * @param nResource the number of resources
     * @param level the shelf level
     * @return if is full or not
     */
    public boolean checkShelf(int nResource, int level){
        if(nResource <= getShelf(level).getResourceNumber())return true;
        else return false;
    }

    /**
     * this method control if in the warehouse there are the resource in "resources" list
     * @param resources
     * @param warehouse
     * @param level
     * @return true if the player can pay with this three array false otherwise
     */
    public boolean canBuy(Resource[] resources, Boolean[] warehouse, Integer[] level){

        for(int i = 0; i < level.length; i++){
            if(warehouse[i]){
                if(getShelf(level[i]).getResourceType() != resources[i] )return false;
            }
        }

            if (!controlResource(level))return false;

        return true;
    }


    /**
     * Method that build the new first shelf
     * @param newResource the resources that will build the shelf
     */
    public void newFirstShelf(Resource newResource){
        this.firstLevel.setResourceType(newResource);
        this.firstLevel.setResourceNumber(1);
    }

    /**
     * Method that build the new second shelf
     * @param newResources the resources that will build the shelf
     */
    public void newSecondShelf(List<Resource> newResources){
        this.secondLevel.setResourceType(newResources.get(0));
        this.secondLevel.setResourceNumber(newResources.size());
    }
    /**
     * Method that build the new third shelf
     * @param newResources the resources that will build the shelf
     */
    public void newThirdShelf(List<Resource> newResources){
        this.thirdLevel.setResourceType(newResources.get(0));
        this.thirdLevel.setResourceNumber(newResources.size());
    }
    /**
     * Method that build the new first special shelf
     * @param newResources the resources that will build the shelf
     */
    public void newFirstSpecialShelf(List<Resource> newResources){
        this.firstLeaderLevel.setResourceType(newResources.get(0));
        this.firstLeaderLevel.setResourceNumber(newResources.size());
    }
    /**
     * Method that build the new second special shelf
     * @param newResources the resources that will build the shelf
     */
    public void newSecondSpecialShelf(List<Resource> newResources){
        this.secondLeaderLevel.setResourceType(newResources.get(0));
        this.secondLeaderLevel.setResourceNumber(newResources.size());
    }

}

