package it.polimi.ngsw.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  This class implements the idea of Development Cards.
 *  The implementation of the card's cost is made by using a map implemented as an HashMap.
 */
public class DevCard {

    private int devLevel;
    private DevCardColour devCardColour;
    private Map<Resource, Integer> devCost = new HashMap<>();
    private ProductionPower productionPower;                        //Manca ancora l'implementazione finale della classe productionPower
    private int PV;

    public DevCard(int devLevel, DevCardColour devCardColour, ArrayList<Resource> devCostList , ProductionPower productionPower, int PV){
        this.devLevel = devLevel;
        this.devCardColour = devCardColour;
        this.productionPower = productionPower;                     //NON DEFINITIVO
        this.PV = PV;

        //settiamo il costo con la mappa
        int shieldCost = 0;
        int slaveCost = 0;
        int moneyCost = 0;
        int stoneCost = 0;

        for(int i = 0; i < devCostList.size(); i++){
            switch(devCostList.get(i)){
                case SHIELD: shieldCost++;
                case MONEY: moneyCost++;
                case SLAVE: slaveCost++;
                case STONE: stoneCost++;
                default: ;
            }

            this.devCost.put(Resource.SHIELD, shieldCost);
            this.devCost.put(Resource.MONEY, moneyCost);
            this.devCost.put(Resource.SLAVE, slaveCost);
            this.devCost.put(Resource.STONE, stoneCost);

        }
    }

    /**
     * Method that returns the level of a Development Card.
     * @return the int that represents the level of a Development Card.
     */
    public int getDevLevel(){
        return this.devLevel;
    }

    /**
     * Method that returns the colour of a Development Card.
     * @return the colour of a Development Card.
     */
    public DevCardColour getCardColour(){
        return devCardColour;
    }

    /**
     * Method that returns the cost of a Development Card.
     * @return a map containing the cost of a Development Card.
     */
    public Map<Resource, Integer> getDevCostAsMap(){
        return devCost;
    }

    /**
     * Method that returns the production power of a Development Card.
     * @return the production power of a Development Card.
     */
    public ProductionPower getProductionPower(){
        return productionPower;
    }

    /**
     * Method that returns the victory points of a Development Card.
     * @return an int that represents the victory points of a Development Card.
     */
    public int getPV(){
        return PV;
    }

}
