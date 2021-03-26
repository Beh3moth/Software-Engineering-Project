package it.polimi.ngsw.model;
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
    private Resource resourceCostType;
    private int resourceCostNumber;
    private ProductionPower productionPower;                        //Manca ancora l'implementazione finale della classe productionPower
    private int PV;

    public DevCard(int devLevel, DevCardColour devCardColour, Resource resourceCostType, int resourceCostNumber, ProductionPower productionPower, int PV){
        this.devLevel = devLevel;
        this.devCardColour = devCardColour;
        this.devCost.put(resourceCostType, resourceCostNumber);
        this.productionPower = productionPower;                     //NON DEFINITIVO
        this.PV = PV;
    }

    /**
     * Method that returns the level of a Development Card.
     * @return the int that represents the level of a Development Card.
     */
    public int getDevLevel(){
        return devLevel;
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
