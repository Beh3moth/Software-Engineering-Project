package it.polimi.ngsw.model;
//devCard library
//import ProductionPower library
//import le enum delle risorse
//import libreria player
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class of the part of the board with 3 spaces for the production cards, it's a 3x3 matrix of {@link DevCard}
 */

public class DevCardDashboard {

    public static final int MAX_SLOT= 3;
    public static final int MAX_CARDS_FOR_SLOT = 3;

    //import devCard library
    private DevCard[][] devCards;   //matrix 3x3 of cards
    private int[] devCardLevel;     //level of the card in every slot
    private boolean lockProductionPowerOne;
    private boolean lockProductionPowerTwo;

    //import ProductionPower library
    private ProductionPower leaderProductionPowerOne;
    private ProductionPower leaderProductionPowerTwo;

    public DevCardDashboard(){
        this.devCards = new DevCard[MAX_SLOT][MAX_CARDS_FOR_SLOT];  //qui devo valutare se inizializzare come nuova carta
        // this.leaderProductionPowerOne = new ProductionPower();      //qui è nullo all'inizio, valuto se mi serve il metodo costruttore o lo inizializzo come nullo
        //this.leaderProductionPowerTwo = new ProductionPower();
        initDevCardStat();                                         //inizializza i livelli degli slot e i boolean dei poteri di produzione extra

    }

    private void initDevCardStat() {
        for (int i = 0; i < MAX_SLOT; i++) {
            devCardLevel[i] = 0;
        }
        this.lockProductionPowerOne = false;
        this.lockProductionPowerTwo = false;
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


    //public void setLevel(){}   inutile? aumento il livello solo quando aggiungo
    /** serve? posso fare da controller, devo togliere due risorse di qualsiasi tipo e metterne una nuova nel forziere
     *
     **/
    public void baseProductionPower(List<Resource> resourceNeeded, Resource resourceProduced){

        //serie di metodi che chiedono al client di scegliere in quali piani togliere ppure al magazzino, il controller verifica la correttezza
        for(int i=0; i<2; i++){
            int fromFloor; //ci vorrà messaggio al client che mi dice quale piano
            boolean goneRight = false;
            do{
                //   goneRight = removeResource(fromFloor, resourceNeeded.get(i));
            }
            while(!goneRight);
        }
        //metodo che aggiunge al forziere la risorsa prodotta nuova
        boolean goneRight = false;
        do{
            int toFloor;

            //goneRight = addResource(floor, resourceProduced);
        }
        while(!goneRight);
    } //intero metodo da riguardare


    /**
     * Method that activate a production power of a given slot, it calls the warehouseor or chest to ask for resource and return resource  to the chest
     * @param slot wich of the three slot
     * @return a boolean if it has gone right
     */

    public boolean activateProductionPower(int slot){
        boolean goneRight = false;
        if(devCardLevel[slot] == 0){
            return false;
        }
        else if(devCardLevel[slot] > 0 && devCardLevel[slot] < 4){
            ProductionPower productionPower;
            int fromFloor = 0;
            productionPower = devCards[slot][devCardLevel[slot]].getProductionPower();
            //stesso discorso di prima, devo chiedere al magazzino o forziere di rimuovere risorse e poi aggiungere
            //removeResource(fromFloor, productionPower.getResourceToPay()); //qua è da rivedere il metodo, perchè posso avere più risorse da dover pagare, non abbiamo più la classe resourceNumber

        }
        return goneRight;
    }


    /**
     * Permit to receive a faithpoint and a choosen resource by the player, all for a resource choosen by the leader card
     * @param resource the resource i have to pay
     * @return a boolean if the method gone right
     */
    public boolean activateLeaderProduction(Resource resource, Player activePlayer){
        boolean goneRight = false;  //devo anche scegliere quale dei due possibili production power
        int fromFloor = 0;
        // goneRight = removeResource(fromFloor, resource); //da rivedere il metodo, devo chiedere al client quale floor voglio o se forziere
        if(goneRight){
            activePlayer.getFaithPath().increaseCrossPosition(); //aggiunge un punto fede al traceFaith
            // devo anche vedere se è andato bene (nel test)
        }
        return goneRight;
    }


    /**
     * unlock the possibility to use the first leader prduction power
     */
    public void unlockOne(){
        this.lockProductionPowerOne = true;
    }


    /**
     * unlock the possibility to use the first leader prduction power
     */
    public void unlockTwo(){
        this.lockProductionPowerTwo = true;
    }


}
