package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public boolean addResourceToWhiteStock(Resource resource){
        whitemarbleStock.add(resource);
        return true;
    }
    public void removeFromWhiteStock(){
        whitemarbleStock.remove(0);
    }
    public int numberOfWhiteStock(){
        return whitemarbleStock.size();
    }
    /**
     *this method adds a resource to the warehouse checking that it is legal
     * @param level
     * @param resource
     * @return true if the addition was successful false if the addition was illegal
     */
    public boolean addResourceToWarehouse (int level, Resource resource){

        /*if(level < 1 || level > 3){//controlla che il piano sia valido
            return false;
        }*/

        switch(level) {

            case 1:
                if(firstLevel.getResourceNumber() == 0){//il piano è vuoto
                    if(secondLevel.getResourceType() == resource || thirdLevel.getResourceType() == resource){//c'è già un altro piano con la stessa risorsa
                        return false;
                    }
                    firstLevel.addResourceNumber();
                    firstLevel.setResourceType(resource);
                    return true;
                }
                //se il piano non è vuoto non si può aggiungere niente dato che la capienza massima è uno
                return false;

            case 2:
                if(secondLevel.getResourceNumber() == 0){//il piano è vuoto
                    if(firstLevel.getResourceType() == resource || thirdLevel.getResourceType() == resource){//c'è già un altro piano con la stessa risorsa
                        return false;
                    }
                    secondLevel.addResourceNumber();
                    secondLevel.setResourceType(resource);
                    return true;
                }
                if(secondLevel.getResourceType() != resource){//nel piano c'è un'altra risorsa
                    return false;
                }
                if(secondLevel.getResourceNumber() == 2){//il piano è pieno
                    return false;
                }
                //allora si può aggiungere
                secondLevel.addResourceNumber();
                return true;

            case 3:
                if(thirdLevel.getResourceNumber() == 0){//il piano è vuoto
                    if(firstLevel.getResourceType() == resource || secondLevel.getResourceType() == resource){//c'è già un altro piano con la stessa risorsa
                        return false;
                    }
                    thirdLevel.addResourceNumber();
                    thirdLevel.setResourceType(resource);
                    return true;
                }
                if(thirdLevel.getResourceType() != resource){//nel piano c'è un'altra risorsa
                    return false;
                }
                if(thirdLevel.getResourceNumber() == 3){//il piano è pieno
                    return false;
                }
                //allora si può aggiungere
                thirdLevel.addResourceNumber();
                return true;
        }

        return false;
    }

    /**
     * this method adds a resource in a floor added thanks to the ability of the leader card, while also checking if the floor is active
     * @param level
     * @param resource
     * @return true if added correctly, false otherwise
     */
    public boolean addResourceToSpecialLevel (int level, Resource resource){ //ok, faccio che finchè non ho vero, chiamo questo metodo


        switch (level){

            case 1:
                if(firstLeaderLevelType != resource){//risorsa non valida
                    return false;
                }
                else{//risorsa valida
                    if(firstLeaderLevel.getResourceNumber()<2){//si può aggiungere risorsa
                        firstLeaderLevel.addResourceNumber();
                        return true;
                    }
                    else{//livello pieno
                        return false;
                    }
                }

            case 2:
                if(secondLeaderLevelType != resource){//risorsa non valida
                    return false;
                }
                else{//risorsa valida
                    if(secondLeaderLevel.getResourceNumber()<2){//si può aggiungere risorsa
                        secondLeaderLevel.addResourceNumber();
                        return true;
                    }
                    else{//livello pieno
                        return false;
                    }
                }
        }

        return false;
    }

    /**
     * this method discards a resource directly from the warehouse checking that it is legal
     * @param level
     * @return true if the resource was successfully discarded false otherwise
     */
    public boolean discardResourceFromWarehouse (int level){

        switch(level){

            case 1 :
                if(firstLevel.getResourceNumber() == 0){//nel piano non c'è niente
                    return false;
                }
                firstLevel.removeResourceNumber();
                firstLevel.setResourceType(Resource.EMPTY);
                return true;

            case 2 :
                if(secondLevel.getResourceNumber() == 0){//nel piano non c'è niente
                    return false;
                }
                if(secondLevel.getResourceNumber() == 1){//c'è solo un elemento da scartare
                    secondLevel.removeResourceNumber();
                    secondLevel.setResourceType(Resource.EMPTY);
                    return true;
                }
                else{
                    secondLevel.removeResourceNumber();
                    return true;
                }

            case 3:
                if(thirdLevel.getResourceNumber() == 0){//nel piano non c'è niente
                    return false;
                }
                if(thirdLevel.getResourceNumber() == 1){//c'è solo un elemento da scartare
                    thirdLevel.removeResourceNumber();
                    thirdLevel.setResourceType(Resource.EMPTY);
                    return true;
                }
                else{//ci sono due/tre elementi
                    thirdLevel.removeResourceNumber();
                    return true;
                }

        }
        return false;
    }

    /**
     * a method that discards a resource directly from the special shelves
     * @param level
     * @return true if discarded directly, false otherwise
     */
    public boolean discardResourceFromSpecialLevel (int level){

        if(level < 1 || level > 2){//controllo che il livello sia valido
            return false;
        }

        switch(level){
            case 1:
                if(firstLeaderLevel.getResourceNumber() == 0){//ce ne sono 0, quindi non scarto nulla
                    return false;
                }
                else{
                    firstLeaderLevel.removeResourceNumber();
                    //metodo che aggiunge punti fede agli altri giocatori
                    return true;

                }
            case 2:
                if(secondLeaderLevel.getResourceNumber() == 0){//ce ne sono 0, quindi non scarto nulla
                    return false;
                }
                else{
                    secondLeaderLevel.removeResourceNumber();
                    //metodo che aggiunge punti fede agli altri giocatori
                    return true;
                }

        }
        return false;
    }

    /**
     * this method adds a resource to the stock if it is valid
     * @param resource
     * @return true if successfully added false otherwise
     */
    public boolean addResourceToStock(Resource resource){
        //controlliamo che le risorse possano essere aggiunte al magazzino prima di inserirle nello stock
        //quindi non possono essere EMPTY oppure FAITHPOINT
        if(resource == Resource.EMPTY)return false;
        if(resource == Resource.FAITHPOINT)return false;
        //aggiungiamo
        warehouseStock.add(resource);
        return true;
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
     * this method checks that the resource is present in the warehouse, removes a resource from the warehouse and places it in the stock
     * @param resource
     * @return true if the resource was found, removed, and successfully placed in stock, false otherwise
     */
    public boolean moveResourceToStock(Resource resource){

        //ovviamente devo controllare che la risorsa sia nel warehouse
        if(firstLevel.getResourceType() == resource){//la risorsa è nel primo livello
            warehouseStock.add(resource);
            firstLevel.removeResourceNumber();
            if(firstLevel.getResourceNumber() == 0){
                firstLevel.setResourceType(Resource.EMPTY);
            }
            return true;
        }

        else if(secondLevel.getResourceType() == resource){//la risorsa è nel secondo livell
            warehouseStock.add(resource);
            secondLevel.removeResourceNumber();
            if(secondLevel.getResourceNumber() == 0){
                secondLevel.setResourceType(Resource.EMPTY);
            }
            return true;
        }

        else if(thirdLevel.getResourceType() == resource){//la risorsa è nel terzo livello
            warehouseStock.add(resource);
            thirdLevel.removeResourceNumber();
            if(thirdLevel.getResourceNumber() == 0){
                thirdLevel.setResourceType(Resource.EMPTY);
            }
            return true;
        }

        else if(firstLeaderLevel.getResourceType() == resource){//la risorsa è nel primo livello speciale
            warehouseStock.add(resource);
            firstLeaderLevel.removeResourceNumber();
            return true;
        }

        else if(secondLeaderLevel.getResourceType() == resource){//la risorsa è nel secondo livello speciale
            warehouseStock.add(resource);
            secondLeaderLevel.removeResourceNumber();
            return true;
        }

        else {//la risorsa non c'è
            return false;
        }
    }

    /**
     * this method discards a resource from stock
     * @param resource
     * @return true if the resource was found and discarded successfully, false otherwise
     */
    public boolean discardResourceFromStock(Resource resource){
        if(warehouseStock.contains(resource) == false){//la risorsa non è presente nello stock
            return false;
        }
        else{
            warehouseStock.remove(resource);
            //metodo che aumenta di uno i punti fede degli altri giocatori
            return true;
        }
    }

    /**
     * this method removes the indicated resource from stock
     * @param resource
     * @return true if it was successfully removed, false otherwise
     */
    public boolean removeResourceFromStock(Resource resource){
        if(warehouseStock.contains(resource) == false){//la risorsa non è presente nello stock
            return false;
        }
        else{
            warehouseStock.remove(resource);
            return true;
        }
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
                default: return null;
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


    //AARON


    /**
     * Method that return an elemnt of the stock
     * @param wich wich one
     * @return the resoure
     */
    public Resource getStockResource(int wich){
        return warehouseStock.get(wich);
    }
    public boolean removeFirstResourceFromStock(){
        warehouseStock.remove(0);
        return true;
    }

    public int getStockResourceNumber(){
        return warehouseStock.size();
    }

    public boolean removeResourceWarehouse(int level){
        switch(level){
            case 1 :
                if(firstLevel.getResourceNumber() == 0){
                    return false;
                }
                firstLevel.removeResourceNumber();
                firstLevel.setResourceType(Resource.EMPTY);
                return true;

            case 2:
                if(secondLevel.getResourceNumber() == 0){
                    return false;
                }
                if(secondLevel.getResourceNumber() == 1){
                    secondLevel.removeResourceNumber();
                    secondLevel.setResourceType(Resource.EMPTY);
                    return true;
                }
                else{
                    secondLevel.removeResourceNumber();
                    return true;
                }

            case 3:
                if(thirdLevel.getResourceNumber() == 0){
                    return false;
                }
                if(thirdLevel.getResourceNumber() == 1){
                    thirdLevel.removeResourceNumber();
                    thirdLevel.setResourceType(Resource.EMPTY);
                    return true;
                }
                else{
                    thirdLevel.removeResourceNumber();
                    return true;
                }

        }
        return false;
    }


    public boolean removeSpecialResourceWarehouse(int level){
        switch(level){
            case 1:
                if(firstLeaderLevel.getResourceNumber() == 0){
                    return false;
                }
                if(firstLeaderLevel.getResourceNumber() == 1){
                    firstLeaderLevel.removeResourceNumber();
                    firstLeaderLevel.setResourceType(Resource.EMPTY);
                    return true;
                }
                else{
                    firstLeaderLevel.removeResourceNumber();
                    return true;
                }

            case 2:
                if(secondLeaderLevel.getResourceNumber() == 0){
                    return false;
                }
                if(secondLeaderLevel.getResourceNumber() == 1){
                    secondLeaderLevel.removeResourceNumber();
                    secondLeaderLevel.setResourceType(Resource.EMPTY);
                    return true;
                }
                else{
                    secondLeaderLevel.removeResourceNumber();
                    return true;
                }
        }
        return false;
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

    public int getLevel(Resource resource){
        if(firstLevel.getResourceType() == resource )return 1;
        else if(secondLevel.getResourceType() == resource)return 2;
        else if(thirdLevel.getResourceType() == resource)return 3;
        //else if(firstLeaderLevel.getResourceType() == resource && firstLeaderLevel.getResourceNumber() >= 1)return 4;
        //else if(secondLeaderLevel.getResourceType() == resource && secondLeaderLevel.getResourceNumber() >= 1)return 5;
        else return 0;
    }

    //Fede

    //To test

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
     * @param resources
     * @param warehouse
     * @param level
     * @param resource
     * @return true if this resource is legal
     */
    public boolean controlResource(Resource[] resources, boolean[] warehouse, int[] level, Resource resource){
        if((resource == Resource.EMPTY) || (resource == Resource.FAITHPOINT)){
            return false;
        }

        for(int l = 1; l < 6; l++){
            int n = 0;
            for(int i = 0; i < resources.length; i++){
                if((resources[i] == resource) && (l == level[i])){
                    n++;
                }
            }
            if(n < getShelf(l).getResourceNumber())return false;
        }

        return true;
    }

    /**
     * this method control if in the warehouse there are the resource in "resources" list
     * @param resources
     * @param warehouse
     * @param level
     * @return true if the player can pay with this three array false otherwise
     */
    public boolean canBuy(Resource[] resources, boolean[] warehouse, int[] level){

        for(int i = 0; i < level.length; i++){
            if(getShelf(level[i]).getResourceType() != resources[i] )return false;
        }

        for(Resource resource : Resource.values()) {
                if (!controlResource(resources, warehouse, level, resource))return false;
            }

        return true;
        }

    }

