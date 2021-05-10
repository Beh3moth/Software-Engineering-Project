package it.polimi.ngsw.model;

import it.polimi.ngsw.observer.Observable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
     * The method returns the total number of Victory Points owned by the Player.
     * It includes the Victory Points of the FaithPath, the DevCardDashboard, the Leader Cards and the total number of PLayer's resources.
     * @return an integer representing the number of Victory Points.
     */
    public int getPV(){
        int PV = 0;
        PV += this.getPVFromLeaderCards();
        PV += this.devCardDashboard.getPV();
        PV += this.faithPath.getPV();
        PV += getPVFormResources();
        return PV;
    }

    /**
     * The method returns the Victory Points given by the Player's Leader Cards.
     * @return an integer representing the number of Victory Points.
     */
    public int getPVFromLeaderCards(){
        int PV = 0;
        for(LeaderCard leaderCard : leaderCards){
            PV += leaderCard.getPV();
        }
        return PV;
    }

    public int getPVFormResources(){
        int PV = 0;
        int totalNumberOfResources = this.getChest().getTotalNumberOfResources() + this.getWarehouse().getTotalNumberOfResources();
        PV = totalNumberOfResources/5;
        return PV;
    }

    /**
     * this method returns the Chest: chest
     * @return chest
     */
    public Chest getChest() {
        return chest;
    }

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
     *e this mthod allows the player to activate a leadercard
     * @param leaderCardToActive
     * @return true if the card is activeted, false otherwhise
     */
    public boolean activeLeaderCard(int leaderCardToActive){
        if(!leaderCards.get(leaderCardToActive).isLeaderCardCostSatisfied(this)){
            return false; //i requirements non sono soddisfatti
        }
        else{
            leaderCards.get(leaderCardToActive).activateAbility(this);
            //leaderCards.remove(leaderCardToActive);   non penso di rimuovere, alla fine ho controllo su cli, meglio tenerle, da pensare
            return true;
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
                if(!this.getWarehouse().discardResourceFromWarehouse(shelfLevel[i])) {
                    productionPower.moveResourcesToOrigin(this);
                    return false;
                }
                if(!productionPower.addSingleCoordinate(resourceType[i], true, shelfLevel[i])){
                    productionPower.moveResourcesToOrigin(this);
                    return false;
                }
            } else {
                if(!this.getChest().removeResource(resourceType[i], 1)){
                    productionPower.moveResourcesToOrigin(this);
                    return false;
                }
                if(!productionPower.addSingleCoordinate(resourceType[i], false, 0)) {
                    productionPower.moveResourcesToOrigin(this);
                    return false;
                }
            }
        }

        return paidList.add(productionPower);

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
     * The method activates every production power the player chose. It also cleans the coordinates.
     * @return true if successful, false otherwise.
     */
    public boolean activateProductionPowers(){
        for(ProductionPower productionPower : paidList){
            for(Resource resource : productionPower.getResourceToReceive()){
                if(productionPower.isLeaderProductionPower()){
                    this.getFaithPath().increaseCrossPosition();
                }
                if(!resource.equals(Resource.EMPTY)){
                    this.getChest().addResource(resource, 1);
                }
            }
            productionPower.cleanCoordinates();
            if(productionPower.isLeaderProductionPower()){
                productionPower.resetLeaderProductionPower();
            }
            if(productionPower.isBaseProductionPower()){
                productionPower.resetBaseProductionPower();
            }
        }
        paidList.clear();
        abilityList.clear();
        return true;
    }

    /*
     * The method puts the Leader Production Powers int the abilityList and removes them from paidList.
     * @return true if the abilityList is not empty, false otherwise.

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



     * The method put the resources to receive from the Production Powers in the paidList in the player's chest.
     * @return true if successful, false otherwise.

    public boolean getResourcesFromProductionPowers(){
        for(ProductionPower productionPower : paidList){
            for (Resource resource : productionPower.getResourceToReceive()){
                if(!this.getChest().addResource(resource, 1)) return false;
            }
        }
        return true;
    }
    */

    //buyDevCard methods

    /**
     * this method permit to choose a devCard if it is legal this method remove the card at the dashboard
     * @param board
     * @param level
     * @param colour
     * @param slotToPut
     * @return DevCard if is legal and if is remove correctly
     */
    public DevCard chooseDevCard(Board board, int level, DevCardColour colour, int slotToPut){
        int devColumn = board.getDevCardColumn(colour);
        Map<Resource, Integer> Cost = new HashMap<>();
        DevCard devCard;

        if(board.getDevCardSpace(level - 1, devColumn).getNumberOfCards() == 0){
            return null;
        }

        else{
            if(level != this.getDevCardDashboard().getLevel(slotToPut) + 1)return null;
            Cost = board.getDevCardSpace(level-1, devColumn).firstDevCard().getDevCostAsMap();
            if(!canAfford(Cost))return null;
            devCard = board.getDevCardSpace(level - 1,devColumn).firstDevCard();
            board.getDevCardSpace(level-1, devColumn).removeFirstCard();
            return devCard;
        }
    }

    /**
     * this method permit to buy a devCard
     * @param devCard
     * @param resource
     * @param warehouse
     * @param level
     * @param slotToPut
     * @return true id the card is buyed correctly
     */
    public boolean buyDevCard(DevCard devCard,Resource[] resource, Boolean[] warehouse, Integer[] level, int slotToPut){
        if(!canBuyDevCard(resource, warehouse, level))return false;
        for(int i = 0; i < resource.length; i++){
            if(warehouse[i]) {
                this.getWarehouse().discardResourceFromWarehouse(level[i]);
            }
            else{
                this.getChest().removeResource(resource[i], 1);
            }
        }
        this.getDevCardDashboard().putDevCardIn(slotToPut, devCard);
        return true;
    }

    /**
     * this method control that the crard can is buyed by the player
     * @param resource
     * @param warehouse
     * @param level
     * @return true id the card can is buyed
     */
    public boolean canBuyDevCard(Resource[] resource, Boolean[] warehouse, Integer[] level){
        if(!this.getWarehouse().canBuy(resource, warehouse, level))return false;
        if(!this.getChest().canBuy(resource, warehouse))return false;
        return true;
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

}