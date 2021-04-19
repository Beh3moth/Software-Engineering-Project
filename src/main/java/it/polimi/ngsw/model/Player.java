package it.polimi.ngsw.model;

import it.polimi.ngsw.observer.Observable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player extends Observable implements Serializable {

    private String nickName;
    private Resource discountPowerOne;
    private Resource discountPowerTwo;
    private Resource whiteMarblePowerOne;
    private Resource whiteMarblePowerTwo;
    private Chest chest;
    private DevCardDashboard devCardDashboard;
    private Warehouse warehouse;
    private List<LeaderCard> leaderCards;
    private FaithPath faithPath;
    private int PV;

    public Player(String nickname) {
        this.nickName = nickname;
        discountPowerOne = Resource.EMPTY;
        discountPowerTwo = Resource.EMPTY;
        whiteMarblePowerOne = Resource.EMPTY;
        whiteMarblePowerTwo = Resource.EMPTY;
        chest = new Chest();
        devCardDashboard = new DevCardDashboard();
        warehouse = new Warehouse();
        leaderCards = new ArrayList<>();
        faithPath = new FaithPath();
        PV = 0;
    }


    //Player general methods

    /**
     * this method allows you to change the nickName attribute
     * @param name
     */
    public void setNickName(String name){
        nickName = name;
    }

    /**
     * this method returns the string: nickName
     * @return nickName
     */
    public String getNickname(){
        return nickName;
    }

    /**
     * this method returns the Integer: PV
     * @return PV
     */
    public int GetPV(){
        return PV;
    }

    /**
     * this method receives as a parameter the life points to be added to the players and adds them
     * @param adderPV
     */
    public void addPV(int adderPV){
        PV += adderPV;
    }

    /**
     * this method returns the Chest: chest
     * @return chest
     */
    public Chest getChest() {
        return chest;
    }

    /**
     * this method returns the Resource: DiscountPowerOne
     * @return DiscountPowerOne
     */
    public Resource getDiscountPowerOne() {
        return discountPowerOne;
    }

    /**
     * this method returns the Resource: DiscountPowerTwo
     * @return DiscountPowerTwo
     */
    public Resource getDiscountPowerTwo() {
        return discountPowerTwo;
    }

    /**
     * this method returns the Resource: WhiteMarblePowerOne
     * @return WhiteMarblePowerOne
     */
    public Resource getWhiteMarblePowerOne() {
        return whiteMarblePowerOne;
    }

    /**
     * this method returns the Resource: WhiteMarblePowerTwo
     * @return WhiteMarblePowerTwo
     */
    public Resource getWhiteMarblePowerTwo() {
        return whiteMarblePowerTwo;
    }

    /**
     * this method returns the FaithPath: faithPath
     * @return faithPath
     */
    public FaithPath getFaithPath() {
        return faithPath;
    }

    /**
     * Method that returns the entire warehouse. Caution!
     * @return the entire warehouse.
     */
    public Warehouse getWarehouse(){
        return warehouse;
    }

    /**
     * this method returns the DevCardDashboard: devCardDashboard
     * @return devCardDashboard
     */
    public DevCardDashboard getDevCardDashboard(){
        return devCardDashboard;
    }

    //Marbles methods

    /**
     * This method activate the discount for the player. It con be activated by a Leader Card.
     * @param resource is the type of resource involved in the disocunt.
     * @return true if it is possible to activate the discount, false otherwise.
     */
    public boolean setDiscountPower(Resource resource){
        if(discountPowerOne.equals(Resource.EMPTY)){
            discountPowerOne = resource;
            return true;
        }
        else if(discountPowerTwo.equals(Resource.EMPTY)) {
            discountPowerTwo = resource;
            return true;
        }
        else return false;
    }

    /**
     * This method activates the white marble power that allows
     * to transform a white marble in a certain specified resource.
     * @param resourceToObtain is the resource in which the white marbles will be transformed.
     * @return true if the method succeeds, false otherwise.
     */
    public boolean setWhiteMarblePower(Resource resourceToObtain){
        if(whiteMarblePowerOne.equals(Resource.EMPTY)){
            whiteMarblePowerOne = resourceToObtain;
            return true;
        }
        else if (whiteMarblePowerTwo.equals(Resource.EMPTY)) {
            whiteMarblePowerTwo = resourceToObtain;
            return true;
        }
        else return false;
    }


    //LeaderCard methods

    public void setLeaderCard(List<LeaderCard> twoLeaderCard){
        this.leaderCards = twoLeaderCard;
    }

    /**
     * this method allows the player to activate a leadercard
     * @param leaderCardToActive
     * @return true if the card is activeted, false otherwhise
     */
    public boolean activeLeaderCard(int leaderCardToActive){
        if(this.leaderCards.get(leaderCardToActive).equals(null)){
            return false; //l'intero passato non è valido
        }
        else{
            if(!leaderCards.get(leaderCardToActive).isLeaderCardCostSatisfied(this)){
                return false; //i requirements non sono soddisfatti
            }
            else{
                leaderCards.get(leaderCardToActive).activateAbility(this);
                leaderCards.remove(leaderCardToActive);
                return true;
            }
        }
    }

    /**
     * The method receives a list of Leader Cards and initializes puts them in the player's Dashboard.
     * @param leaderCardList is a List of LeaderCards
     * @return true if the leaderCardList's size is equal to 4, false otherwise.
     */
    public boolean receiveLeaderCards(List<LeaderCard> leaderCardList){
        if(leaderCardList.size()==4){
            this.leaderCards = leaderCardList;
        }
        else return false;
        return true;
    }

    /**
     * The method receives two integers and discard the two LeaderCards from the player's list of LeaderCards.
     * @param leaderCardToDiscard1 is an integer which represents a certain LeaderCard the player wants to discard.
     * @param leaderCardToDiscard2 is an integer which represents a certain LeaderCard the player wants to discard.
     * @return true if the method is successful, false otherwise.
     */
    public boolean chooseLeaderCardsToDiscard(int leaderCardToDiscard1, int leaderCardToDiscard2){
        return ( discardLeaderCard(leaderCardToDiscard1) && discardLeaderCard(leaderCardToDiscard2) );
    }

    /**
     * this method returns the the Leader Cards vector
     * @return leaderCard[]
     */
    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    /**
     * The method receives an integer and discard the LeaderCard in that position from player's list leaderCards.
     * @param leaderCardToDiscard is an integer which represents the LeaderCard to discard.
     * @return false if the exceptions UnsupportedOperationException or IndexOutOfBoundsException occur, false otherwise.
     */
    public boolean discardLeaderCard(int leaderCardToDiscard){
        try{
            leaderCards.remove(leaderCardToDiscard);
        }
        catch (UnsupportedOperationException | IndexOutOfBoundsException e){
            return false;
        }
        return true;
    }



    //Activate Production Power

    private List<ProductionPower> paidList = new ArrayList<>();
    private List<ProductionPower> abilityList = new ArrayList<>();


    public void addProductionPowerToPaidList(ProductionPower productionPower){
        paidList.add(productionPower);
    }

    public List<ProductionPower> getPaidList(){
        return paidList;
    }

    public List<ProductionPower> getAbilityList(){
        return abilityList;
    }

    //chooseProductionPower moved in DevCardDashboard
    //setBaseProductionPowerLists moved in Production Power

    /**
     * The method returns every resource the player has as a Map.
     * @return a Map in which the Resource is the key and the Integer is the value.
     */
    public Map<Resource, Integer> getMapFromChestAndWarehouse(){
        Map<Resource, Integer> warehouseMap = this.getWarehouse().getResourcesAsMap();
        Map<Resource, Integer> chestMap = this.getChest().getResourcesAsMap();
        for(Resource resource : Resource.values()){
            if(!resource.equals(Resource.EMPTY) && !resource.equals(Resource.FAITHPOINT)){
                warehouseMap.put(resource, chestMap.get(resource) + warehouseMap.get(resource));
            }
        }
        return warehouseMap;
    }

    /**
     * The method ensures the player can afford the Production Power.
     * @param costMap is Map map where the keys are the resources (Resource.EMPTY excluded) and the Integers are the valuse.
     * @return true if the player can afford the Map, false otherwise.
     */
    public boolean canAfford(Map<Resource, Integer> costMap){

        Map<Resource, Integer> completeMap = getMapFromChestAndWarehouse();

        for(Resource resource : Resource.values()){
            if(!resource.equals(Resource.EMPTY) && !resource.equals(Resource.FAITHPOINT)){
                if(completeMap.get(resource) < costMap.get(resource)){
                    return false;
                }
            }
        }

        return true;

    }

    /**
     * The methods allows Player to pay and put a Production Power to the paidList.
     * @param productionPower is the one to be paid.
     * @param isWarehouse is a boolean that specifies if the resource is from the Chest (false) or the WareHouse (true).
     * @param shelfLevel specifies the Shelf of the Warehouse. It can be from 0 to 5.
     * @param resourceType specifies the type of resource. Resource.EMPTY is not allowed.
     * @return true if the player can buy the Production Power given as parameter, false otherwise.
     */
    public boolean payProductionPower(ProductionPower productionPower, Boolean[] isWarehouse, Integer[] shelfLevel, Resource[] resourceType){

        for(int i=0; i < resourceType.length; i++){

            if(isWarehouse[i]){
                if (!this.getWarehouse().removeResourceWarehouse(shelfLevel[i])) {
                    rejectProductionPower(productionPower);
                    return false;
                }
            } else {
                if (!this.getChest().removeResource(resourceType[i], 1)) {
                    rejectProductionPower(productionPower);
                    return false;
                }
            }

        }

        productionPower.addCoordinates(resourceType, isWarehouse, shelfLevel);
        paidList.add(productionPower);
        return true;

    }

    /**
     * The method puts the resources of a production power back in their origin and deletes the Production Power.
     * @param productionPower is the Production Power that has the coordinates of the resources.
     */
    public void rejectProductionPower(ProductionPower productionPower){

        productionPower.moveResourcesToOrigin(this);

        paidList.remove(productionPower);

        if(productionPower.isBaseProductionPower()){
            productionPower.removeBaseProductionPowerLists();
        }

    }

    /**
     * The method puts the Leader Production Powers int the abilityList and removes them from paidList.
     * @return true if the abilityList is not empty, false otherwise.
     */
    public boolean checkForLeaderProductionPowerAbility(){

        for(ProductionPower productionPower : paidList){
            for(Resource resource : productionPower.getResourceToReceive()){
                if(resource.equals(Resource.EMPTY)){
                    abilityList.add(productionPower);
                }
            }
        }

        for(ProductionPower productionPower: abilityList){
            paidList.remove(productionPower);
        }

        return !abilityList.isEmpty();

    }

    /**
     * The method set the resource to receive from a Leader Production Power beyond the FaithPoint.
     * @param resource the resource the Player wants to receive.
     * @param leaderProductionPower the Leader Production Power To Set.
     */
    public void setResourceToReceiveFromLeaderProductionPowerAbility(Resource resource, ProductionPower leaderProductionPower){
        this.getFaithPath().increaseCrossPosition();
        leaderProductionPower.setLeaderProductionPowerResourceToReceive(resource);
        paidList.add(leaderProductionPower);
        abilityList.remove(leaderProductionPower);
    }

    /**
     * The method activates every production power the player chose. It also cleans the coordinates.
     * @return true if successful, false otherwise.
     */
    public boolean activateProductionPowers(){
        for(ProductionPower productionPower : paidList){
            for(Resource resource : productionPower.getResourceToReceive()){
                this.getChest().addResource(resource, 1);
            }
            productionPower.cleanCoordinates();
        }
        paidList.clear();
        abilityList.clear();
        return true;
    }

}