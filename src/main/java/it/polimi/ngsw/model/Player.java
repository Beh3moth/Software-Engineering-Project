package it.polimi.ngsw.model;

public class Player {

    private String nickName;
    private Resource discountPowerOne;
    private Resource discountPowerTwo;
    private Resource whiteMarblePowerOne;
    private Resource whiteMarblePowerTwo;
    private Chest chest;
    private DevCardDashboard devCardDashboard;
    private Warehouse warehouse;
    private LeaderCard[] leaderCard;
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
        leaderCard = new LeaderCard[4];
        faithPath = new FaithPath();
        PV = 0;
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
    public LeaderCard[] getLeaderCard() {
        return leaderCard;
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
        else if (discountPowerTwo.equals(Resource.EMPTY)) {
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