package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the part of the board with 3 spaces for the production cards, it's a 3x3 matrix of {@link DevCard}
 */

public class DevCardDashboard {

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
        this.devCardLevel = new int[10];
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
        return devCardLevel[slot];
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


    //Methods to manage production powers

    /**
     * The method activates a Production Power Ability of a Leader Card. It initializes a new Production Power.
     * @param productionPowerAbility is the Production Power Ability to activate.
     * @return true if the procedure is successful, false otherwise.
     */
    public boolean activateProductionPowerAbility(LeaderCard productionPowerAbility){

        List<Resource> resourceList1 = new ArrayList<>();
        List<Resource> resourceList2 = new ArrayList<>();

        if(leaderProductionPowerOne==null){
            resourceList1.add(productionPowerAbility.getInputResource());
            resourceList2.add(Resource.EMPTY);
            leaderProductionPowerOne = new ProductionPower(resourceList1, resourceList2);
            return true;
        }
        else if(leaderProductionPowerTwo==null){
            resourceList1.add(productionPowerAbility.getInputResource());
            resourceList2.add(Resource.EMPTY);
            leaderProductionPowerTwo = new ProductionPower(resourceList1, resourceList2);
            return true;
        }
        else return false;

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

    public int getDevCardNumber(){
        return this.devCardNumber;
    }
}

