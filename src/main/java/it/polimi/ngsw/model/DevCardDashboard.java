package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class of the part of the board with 3 spaces for the production cards, it's a 3x3 matrix of {@link DevCard}
 */

public class   DevCardDashboard {

    public static final int MAX_SLOT= 3;
    public static final int MAX_CARDS_FOR_SLOT = 3;

    private DevCard[][] devCards;   //matrix 3x3 of cards
    private int[] devCardLevel;     //level of the card in every slot
    private int devCardNumber;
    private ProductionPower leaderProductionPowerOne;
    private ProductionPower leaderProductionPowerTwo;
    private ProductionPower baseProductionPower;

    public DevCardDashboard(){
        this.devCards = new DevCard[MAX_SLOT][MAX_CARDS_FOR_SLOT];
        this.devCardLevel = new int[3];
        leaderProductionPowerOne = null;
        leaderProductionPowerTwo = null;
        initDevCardStat();
        baseProductionPower = new ProductionPower(null, null);
        baseProductionPower.setBaseProductionPowerToTrue();
        this.devCardNumber = 0;
    }

    //Methods to manage DevCard

    private void initDevCardStat() {
        for (int i = 0; i < MAX_SLOT; i++) {
            devCardLevel[i] = 0;
        }
    }

    /**
     * Method that add a DevCard inside a slot, it has to cotroll if the level and the color is right,
     * and return a boolean, alla chiamata avrò un while che cerca di inserire finchè non ho un boolean positivo
     * @param slot  the slot you want to put the card inside
     * @param newCard   the card that you want to put inside
     * @return {@code true}   if the inseriment went right
     */
    public boolean putDevCardIn(int slot, DevCard newCard){
        boolean goneRight = false;
        if(newCard.getDevLevel() == devCardLevel[slot] + 1){
            devCards[slot][devCardLevel[slot]] = newCard;
            devCardLevel[slot]++;
            goneRight = true;
            devCardNumber++;
        }
        return goneRight;
    }


    /**
     * method that gives the level of a choosen slot
     * @param slot wich slot you want to know the level of
     * @return the level
     */
    public int getLevel(int slot){
        return devCardLevel[slot-1];
    }

    public ProductionPower getBaseProductionPower(){
        return baseProductionPower;
    }

    /**
     * The method returns every Development Card owned by the player.
     * @return a List of DevCard. It may be an empty List.
     */
    public List<DevCard> getDevCards(){
        List<DevCard> devCardList = new ArrayList<>();
        for(int i=0; i<3; i++){
            for(int j=0; j<devCardLevel[i]; j++){
                devCardList.add(devCards[i][j]);
            }
        }
        return  devCardList;
    }

    /**
     * The method returns a List of active DevCards.
     * @return a List of active DevCards.
     */
    public List<DevCard> getActiveDevCards(){
        List<DevCard> devCardList = new ArrayList<>();
        for(int i=0; i<3; i++){
            if(devCardLevel[i]>0){
                devCardList.add(devCards[i][devCardLevel[i]-1]);
            }
        }
        return  devCardList;
    }

    /**
     * The method returns a map in which an Integer is linked with a DevCard. The possible Integers values are 0,1,2 and they represent the DevCard of a certain DevCardSpace.
     * @return a map Integer - DevCard
     */
    public Map<Integer, DevCard> getActiveDevCardsAsMap(){
        Map<Integer, DevCard> map = new HashMap<>();
        for(int i=0; i<3; i++){
            if(devCardLevel[i]>0 && devCards[i][devCardLevel[i]-1]!=null){
                map.put(i, devCards[i][devCardLevel[i]-1]);
            }
        }
        return map;
    }

    /**
     * The method returns a list of the active Production Powers.
     * @return a list of the active Production Powers or null if it is empty.
     */
    public List<ProductionPower> getLeaderProductionPowerList(){
        List<ProductionPower> productionPowerList = new ArrayList<>();
        if(leaderProductionPowerOne!=null){
            productionPowerList.add(leaderProductionPowerOne);
        }
        if(leaderProductionPowerTwo!=null){
            productionPowerList.add(leaderProductionPowerTwo);
        }
        return productionPowerList;
    }


    //Methods to manage production powers

    /**
     * The method returns a list of the active Production Powers.
     * @return a list of the active Production Powers or null if it is empty.
     */
    public List<ProductionPower> getActiveProductionPowerList(){
        List<ProductionPower> productionPowerList = new ArrayList<>();

        productionPowerList.add(baseProductionPower);

        for(DevCard devCard : this.getActiveDevCards()){
            productionPowerList.add(devCard.getProductionPower());
        }

        if(leaderProductionPowerOne!=null){
            productionPowerList.add(leaderProductionPowerOne);
        }
        if(leaderProductionPowerOne!=null){
            productionPowerList.add(leaderProductionPowerTwo);
        }

        return productionPowerList;
    }

    /**
     * The method activates a Production Power Ability of a Leader Card. It initializes a new Production Power.
     * @param productionPowerAbility is the Production Power Ability to activate.
     * @return true if the procedure is successful, false otherwise.
     */
    public boolean activateProductionPowerAbility(ProductionPowerAbility productionPowerAbility){

        List<Resource> resourceList1 = new ArrayList<>();
        List<Resource> resourceList2 = new ArrayList<>();

        if(leaderProductionPowerOne==null){
            resourceList1.add(productionPowerAbility.getInputResource());
            resourceList2.add(Resource.EMPTY);
            resourceList2.add(Resource.FAITHPOINT);
            this.leaderProductionPowerOne = new ProductionPower(resourceList1, resourceList2);
            this.leaderProductionPowerOne.setIsLeaderProductionPower();
            return true;
        }
        if(leaderProductionPowerTwo==null){
            resourceList1.add(productionPowerAbility.getInputResource());
            resourceList2.add(Resource.EMPTY);
            resourceList2.add(Resource.FAITHPOINT);
            leaderProductionPowerTwo = new ProductionPower(resourceList1, resourceList2);
            leaderProductionPowerTwo.setIsLeaderProductionPower();
            return true;
        }
        return false;

    }

    /**
     * The method returns a production power between the Player's ones.
     * @param slot indicates the Production Power to activate. 0 for Base Production Power; 1,2,3 for DevCard Production Powers; 4,5 for Leader Card Production Powers.
     * @return a Production Power if it exists, null if it doesn't.
     */
    public ProductionPower getProductionPower(int slot){
        if(slot==0){
            return baseProductionPower;
        }
        else if(slot>=1 && slot<=3){
            if(getLevel(slot-1) > 0){
                return devCards[slot-1][getLevel(slot-1)-1].getProductionPower();
            }
            else return null;
        }
       else if(slot == 4){
            return leaderProductionPowerOne;
        }
       else if(slot == 5){
            return leaderProductionPowerTwo;
       }
       else return null;
    }

    /**
     * Method that takes the number of devcards
     * @return the number
     */
    public int getDevCardNumber(){
        return this.devCardNumber;
    }

    /**
     * The method allows the player to choose a Production power to use.
     * @param productionPowerChosen a integer from 0 to 5 included.
     * @return the Production Power chosen if it exists, null if it doesn't or if the input is wrong.
     */
    public ProductionPower chooseProductionPower(int productionPowerChosen){
        if(productionPowerChosen>=0 && productionPowerChosen<=5){
            return this.getProductionPower(productionPowerChosen);
        }
        else return null;
    }

    /**
     * The method returns the number of Victory Points (PV) of the DevCardDashboard. The result is a sum of the Victory Points of every Development Card.
     * @return the number of Victory Points given by the Player's Development Cards.
     */
    public int getPV(){
        int PV = 0;
        for(int i=0; i<MAX_SLOT; i++){
            for(DevCard devCard : devCards[i]){
                if(devCard!=null){
                    PV += devCard.getPV();
                }
            }
        }
        return PV;
    }

}

