package it.polimi.ngsw.model;

/**
 * Class that describes the shelf of the warehouse
 */
public class Shelf {
    private Resource resourceType;
    private int resourceNumber;

    public Shelf(){
        this.resourceNumber = 0;
        this.resourceType = Resource.EMPTY;
    }

    /**
     * The number of the resources
     * @return the number
     */
    public int getResourceNumber(){
        return resourceNumber;
    }

    /**
     * The type of the resources
     * @return the type
     */
    public Resource getResourceType(){
        return resourceType;
    }

    /**
     * @param resource the new resources type
     * @return if went well
     */
    public boolean setResourceType(Resource resource){
        resourceType = resource;
        return true;
    }
    /**
     * @param number the new resources number
     * @return if went well
     */
    public boolean setResourceNumber(int number){
        resourceNumber = number;
        return true;
    }

    /**
     * @return if went well after adding a resource
     */
    public boolean addResourceNumber(){
        resourceNumber++;
        return true;
    }
    /**
     * @return if went well after removing a resource
     */
    public boolean removeResourceNumber(){
        if(resourceNumber == 0)return true;
        resourceNumber--;
        return true;
    }
}
