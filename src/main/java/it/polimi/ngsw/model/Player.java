package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

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

    public Player(){
        nickName = new String();
        discountPowerOne = Resource.EMPTY;
        discountPowerTwo = Resource.EMPTY;
        whiteMarblePowerOne = Resource.EMPTY;
        whiteMarblePowerTwo = Resource.EMPTY;
        chest = new Chest();
        //Da rivedere! devCardDashboardd
        devCardDashboard = new DevCardDashboard();
        warehouse = new Warehouse();
        leaderCards = new ArrayList<>();
        faithPath = new FaithPath();
        PV = 0;
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
    public String getNickName(){
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
    public DevCardDashboard getDevCardDashboard() {
        return devCardDashboard;
    }
     **/

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



    //AGGIUNTI DA FEDE

    /**
     * Method that returns the entire warehouse. Caution!
     * @return the entire warehouse.
     */
    public Warehouse getWarehouse(){
        return warehouse;
    }

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
    /**
     * this method returns the DevCardDashboard: devCardDashboard
     * @return devCardDashboard
     */
    public DevCardDashboard getDevCardDashboard(){
        return devCardDashboard;
    }
}