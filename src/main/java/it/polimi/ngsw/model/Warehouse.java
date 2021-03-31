package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private Shelf firstLevel;
    private Shelf secondLevel;
    private Shelf thirdLevel;
    private Shelf firstLeaderLevel;
    private Shelf secondLeaderLevel;
    private Resource firstLeaderLevelType;
    private Resource secondLeaderLevelType;
    private List<Resource> warehouseStock;

    public Warehouse(){
        firstLevel = new Shelf();
        secondLevel = new Shelf();
        thirdLevel = new Shelf();
        firstLeaderLevel = new Shelf();
        secondLeaderLevel = new Shelf();
        warehouseStock = new ArrayList<Resource>();
        this.firstLeaderLevelType = Resource.EMPTY;
        this.secondLeaderLevelType = Resource.EMPTY;
    }


    /**
     *this method adds a resource to the warehouse checking that it is legal
     * @param level
     * @param resource
     * @return true if the addition was successful false if the addition was illegal
     */
    public boolean addResourceToWarehouse(int level, Resource resource){

        if(level < 1 || level > 3){//controlla che il piano sia valido
            return false;
        }

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
    public boolean addResourceToSpecialLevel (int level, Resource resource){

        if(level < 1 || level > 2){//controllo che il livello sia valido
            return false;
        }

        switch (level){

            case 1:
                if(firstLeaderLevelType == Resource.EMPTY){//piano non attivo
                    return false;
                }
                if(firstLeaderLevelType != resource){//risorsa non valida
                    return false;
                }
                else{//risorsa valida
                    if(firstLeaderLevel.getResourceNumber()<=2){//si può aggiungere risorsa
                        firstLeaderLevel.addResourceNumber();
                        return true;
                    }
                    else{//livello pieno
                        return false;
                    }
                }

            case 2:
                if(secondLeaderLevelType == Resource.EMPTY){//piano non attivo
                    return false;
                }
                if(secondLeaderLevelType != resource){//risorsa non valida
                    return false;
                }
                else{//risorsa valida
                    if(secondLeaderLevel.getResourceNumber()<=2){//si può aggiungere risorsa
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
     * @param resource
     * @return true if the resource was successfully discarded false otherwise
     */
    public boolean discardResourceFromWarehouse (int level, Resource resource){

        if (level < 1 || level > 3){//controlla che il piano sia valido
            return false;
        }

        switch(level){

            case 1 :
                if(firstLevel.getResourceType() != resource ){//nel piano c'è un'altra risorsa
                    return false;
                }
                if(firstLevel.getResourceNumber() == 0){//nel piano non c'è niente
                    return false; //credo, dato che non deve aggiungere punti fede
                }
                //ora il piano dovrebbe possedere solamente una risorsa da scartare
                firstLevel.removeResourceNumber();
                firstLevel.setResourceType(Resource.EMPTY);
                //bisognerebbe chiamare una funzione che aggiunge un punto fede agli altri giocatori
                return true;

            case 2:
                if(secondLevel.getResourceType() != resource ){//nel piano c'è un'altra risorsa
                    return false;
                }
                if(secondLevel.getResourceNumber() == 0){//nel piano non c'è niente
                    return false; //credo, dato che non deve aggiungere punti fede
                }
                if(secondLevel.getResourceNumber() == 1){//c'è solo un elemento da scartare
                    secondLevel.removeResourceNumber();
                    secondLevel.setResourceType(Resource.EMPTY);
                    //bisognerebbe chiamare una funzione che aggiunge un punto fede agli altri giocatori
                    return true;
                }
                else{//ci sono due elementi
                    secondLevel.removeResourceNumber();
                    //bisognerebbe chiamare una funzione che aggiunge un punto fede agli altri giocatori
                    return true;
                }

            case 3:
                if(thirdLevel.getResourceType() != resource ){//nel piano c'è un'altra risorsa
                    return false;
                }
                if(thirdLevel.getResourceNumber() == 0){//nel piano non c'è niente
                    return false; //credo, dato che non deve aggiungere punti fede
                }
                if(thirdLevel.getResourceNumber() == 1){//c'è solo un elemento da scartare
                    thirdLevel.removeResourceNumber();
                    thirdLevel.setResourceType(Resource.EMPTY);
                    //bisognerebbe chiamare una funzione che aggiunge un punto fede agli altri giocatori
                    return true;
                }
                else{//ci sono due/tre elementi
                    thirdLevel.removeResourceNumber();
                    //bisognerebbe chiamare una funzione che aggiunge un punto fede agli altri giocatori
                    return true;
                }

        }
        return false;
    }

    /**
     * a method that discards a resource directly from the special shelves
     * @param level
     * @param resource
     * @return true if discarded directly, false otherwise
     */
    public boolean discardResourceFromSpecialLevel (int level, Resource resource){

        if(level < 1 || level > 2){//controllo che il livello sia valido
            return false;
        }

        switch(level){
            case 1:
                if(firstLeaderLevelType == Resource.EMPTY){//piano non attivo
                    return false;
                }
                if(firstLeaderLevelType != resource){//risorsa non valida
                    return false;
                }
                else{//risorsa valida
                    if(firstLeaderLevel.getResourceNumber() == 0){//ce ne sono 0, quindi non scarto nulla
                        return false;
                    }
                    else{
                        firstLeaderLevel.removeResourceNumber();
                        //metodo che aggiunge punti fede agli altri giocatori
                        return true;
                    }
                }
            case 2:
                if(secondLeaderLevelType == Resource.EMPTY){//piano non attivo
                    return false;
                }
                if(secondLeaderLevelType != resource){//risorsa non valida
                    return false;
                }
                else{//risorsa valida
                    if(secondLeaderLevel.getResourceNumber() == 0){//ce ne sono 0, quindi non scarto nulla
                        return false;
                    }
                    else{
                        secondLeaderLevel.removeResourceNumber();
                        //metodo che aggiunge punti fede agli altri giocatori
                        return true;
                    }
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

    /**
     * Method that return an elementof the stock
     * @param wich wich one
     * @return the resource
     */
    public Resource getStockResource(int wich){
        return warehouseStock.get(wich);
    }
}
