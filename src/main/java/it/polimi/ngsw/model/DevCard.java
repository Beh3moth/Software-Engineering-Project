package it.polimi.ngsw.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  This class implements the idea of Development Cards.
 *  The implementation of the card's cost is made by using a map implemented as an HashMap.
 */
public class DevCard implements Serializable {

    private int devLevel;
    private DevCardColour devCardColour;
    private Map<Resource, Integer> devCost = new HashMap<>();
    private ProductionPower productionPower;                        //Manca ancora l'implementazione finale della classe productionPower
    private int PV;
    private List<Resource> resourceToPay;


    public DevCard (int devLevel, DevCardColour devCardColour, List<Resource> devCostList , ProductionPower productionPower, int PV) {
        this.devLevel = devLevel;
        this.devCardColour = devCardColour;
        this.productionPower = productionPower;                     //NON DEFINITIVO
        this.PV = PV;

        //settiamo il costo con la mappa
        int shieldCost = 0;
        int slaveCost = 0;
        int moneyCost = 0;
        int stoneCost = 0;
        int faithPoint = 0;

        this.resourceToPay = devCostList;

        for (Resource resource : devCostList) {
            switch (resource) {
                case SHIELD:
                    shieldCost++;
                    break;
                case MONEY:
                    moneyCost++;
                    break;
                case SLAVE:
                    slaveCost++;
                    break;
                case STONE:
                    stoneCost++;
                    break;
                case FAITHPOINT:
                    faithPoint++;
                    break;
                default:
                    break;
            }

            this.devCost.put(Resource.SHIELD, shieldCost);
            this.devCost.put(Resource.MONEY, moneyCost);
            this.devCost.put(Resource.SLAVE, slaveCost);
            this.devCost.put(Resource.STONE, stoneCost);
            this.devCost.put(Resource.FAITHPOINT, faithPoint);

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

    /**
     * Method that returns the list with all resourceToPay
     * @return the list with all resourceToPay
     */
    public List<Resource> getResourceToPay(){return resourceToPay;}

}
