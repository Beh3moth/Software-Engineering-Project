package it.polimi.ngsw.model;


public class Shelf {
    private Resource resourceType;
    private int resourceNumber;

    public Shelf(){
        this.resourceNumber = 0;
        this.resourceType = Resource.EMPTY;
    }

    public int getResourceNumber(){
        return resourceNumber;
    }

    public Resource getResourceType(){
        return resourceType;
    }

    public boolean setResourceType(Resource resource){
        resourceType = resource;
        return true;
    }

    public boolean setResourceNumber(int number){
        resourceNumber = number;
        return true;
    }

    public boolean addResourceNumber(){
        resourceNumber++;
        return true;
    }

    public boolean removeResourceNumber(){
        if(resourceNumber == 0)return true;
        resourceNumber--;
        return true;
    }
}
