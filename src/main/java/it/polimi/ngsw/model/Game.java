package it.polimi.ngsw.model;

import java.util.*;

public class Game {

    private Board board;
    private Player activePlayer;
    private List<Player> players;
    private int playerNumbers;
    private FaithPath lawrenceFaithPath = new FaithPath();
    private Deque<ActionToken> actionTokensDeque = new ArrayDeque<>(6);
    private List<LeaderCard> leaderCards = new ArrayList<>(16);

    //List of production powers the player decides to activate between among his available Production Powers.
    List<ProductionPower> listOfAffordableProductionPowers = new ArrayList<>();

    public Game(){
        initActionTokensDeque();
    }


    /**
     * Method that permit to take resources from the market, it asks the user if column or row, and wich one
     * @param activePlayer the player that do the action
     */
    public void getBoardResources(Player activePlayer){
        //chiedo al client se riga o colonna
        boolean allRight = false;
        do{

            System.out.println("Choose between column and row: 1 = column  2 = row"); //ovviamente qusti metodi lli buttero nel CLI
            Scanner input = new Scanner(System.in);
            int sector = input.nextInt();
            if(sector == 1){
                allRight = true;
                int column;
                boolean allRightTwo = false;
                do{
                System.out.println("Choose between 1 and 4");
                column= input.nextInt();
                if(column > 0 && column < 5){
                    allRightTwo = true;
                }
                }while(!allRightTwo);
                this.board.getMarbleColumn(column, activePlayer);
            }
            else if(sector == 2){
                allRight = true;
                int row;
                boolean allRightTwo = false;
                do{
                    System.out.println("Choose between 1 and 3");
                    row= input.nextInt();
                    if(row > 0 && row < 4){
                        allRightTwo = true;
                    }
                }while(!allRightTwo);
                this.board.getMarbleRow(row, activePlayer);
            }
        } while (!allRight);

        Scanner input = new Scanner(System.in);
        int up = activePlayer.getWarehouse().getStockResourceNumber();
        int j = 0;
        while (j < up) {
            boolean generalValidator = false;
            do {
                System.out.println("Choose what to do with this resource " + activePlayer.getWarehouse().getStockResource(j));
                boolean validator = false;
                int choosen;
                do {
                    System.out.println("1) Discard 2)Add Warehouse 3)Add special warehouse 4) reorder warehouse"); //potrei aggiungere
                    choosen = input.nextInt();
                    if (0 < choosen && choosen < 5) {
                        validator = true;
                    }
                    if (choosen == 3 && (activePlayer.getWarehouse().getLeaderLevelType(1) == Resource.EMPTY)) {
                        validator = false;
                        System.out.println("You don't have leader card shelf");
                    }
                }
                while (!validator);
                int level;
                if (choosen == 1) {
                    generalValidator = activePlayer.getWarehouse().removeFirstResourceFromStock();
                    addOtherFaithPoint(activePlayer);
                    j--;
                    up--;
                } else if (choosen == 2) {
                    System.out.println("Choose wich shelf of the warehouse");
                    boolean goneRight = false;
                    do {
                        level = input.nextInt();
                        if (0 < level && level < 4) {
                            goneRight = true;
                        }

                    } while (!goneRight);
                    generalValidator = activePlayer.getWarehouse().addResourceToWarehouse(level, activePlayer.getWarehouse().getStockResource(0)); //devo controllare poi se una volta dentro la inserisco veramente
                    if(generalValidator == true){activePlayer.getWarehouse().removeFirstResourceFromStock();
                        j--;
                        up--;}
                } else if (choosen == 3) {
                    int levelUp = 1;
                    if (activePlayer.getWarehouse().getLeaderLevelType(2) != Resource.EMPTY) {
                        levelUp = 2;
                    }
                    System.out.println("Choose wich specialshelf of the warehouse");
                    boolean goneRight = false;
                    do {
                        level = input.nextInt();
                        if (0 < level && level <= levelUp) {
                            goneRight = true;
                        }
                    } while (!goneRight);
                    generalValidator = activePlayer.getWarehouse().addResourceToSpecialLevel(level, activePlayer.getWarehouse().getStockResource(0));
                    if(generalValidator == true){
                        activePlayer.getWarehouse().removeFirstResourceFromStock();
                        j--;
                        up--;}
                } else if (choosen == 4) {
                    reorderWarehouse(activePlayer);
                }
            } while (!generalValidator);
            j++;

        }
    }

    public void reorderWarehouse(Player activePlayer) {
        Scanner input = new Scanner(System.in);
        int choosen;
        boolean verifier = false;
        do{
            System.out.println("Chose what to do: 1)Finish 2)Throw away resource 2)Move resources");
            choosen = input.nextInt();
            boolean goneWell = false;
            if(choosen == 1){
                verifier = true;
            }
            else if(choosen == 2){
                System.out.println("Chose a shelf to remove from (between 1 and 3");
                choosen = input.nextInt();

                if(choosen < 4 && 0 < choosen){
                    goneWell = activePlayer.getWarehouse().removeResourceWarehouse(choosen);
                }
            }
                else if(choosen == 4){
                    int levelUp = 1;
                    int level;
                    if (activePlayer.getWarehouse().getLeaderLevelType(2) != Resource.EMPTY) {
                        levelUp = 2;
                    }
                    System.out.println("Choose wich specialshelf of the warehouse");
                    boolean goneRight = false;
                    do {
                        level = input.nextInt();
                        if (0 < level && level <= levelUp) {
                            goneRight = true;
                        }

                    } while (!goneRight);
                    goneWell = activePlayer.getWarehouse().removeSpecialResourceWarehouse(level);
                }
                if(goneWell == true){
                    addOtherFaithPoint(activePlayer);
                }

            else if(choosen == 2){

            }
        }while(!verifier);
    }

    public void addOtherFaithPoint(Player activePlayer){
        for(int i = 0; i < this.playerNumbers; i++){
            if(players.get(i) != activePlayer){
                players.get(i).getFaithPath().increaseCrossPosition();
            }
        }
    }


    //ActionToken's methods

    /**
     * The method returns a Deque of ActionToken.
     * @return a Deque of ActionToken.
     */
    public Deque<ActionToken> getTokensDeque(){
        return actionTokensDeque;
    }

    /**
     * The method initialized the tokens and shuffles the Deque of ActionToken using a List as support.
     * @return true if execution is successful and does not modify the Deque.
     */
    private boolean initActionTokensDeque(){
        //Action tokens initialization
        this.actionTokensDeque.add(new DiscardDevCard(DevCardColour.BLUE));
        this.actionTokensDeque.add(new DiscardDevCard(DevCardColour.YELLOW));
        this.actionTokensDeque.add(new DiscardDevCard(DevCardColour.GREEN));
        this.actionTokensDeque.add(new DiscardDevCard(DevCardColour.PURPLE));
        this.actionTokensDeque.add(new Move());
        this.actionTokensDeque.add(new MoveAndScrum());
        return shuffleActionTokensDeque();
    }

    /**
     * The method shuffles the ActionToken's Deque using an ArrayList as support.
     * @return true if it is successful.
     */
    public boolean shuffleActionTokensDeque(){
        //Action tokens shuffle
        List<ActionToken> actionTokensList = new ArrayList<>(actionTokensDeque);
        Collections.shuffle(actionTokensList);
        for(int i=0; i<6; i++){
            actionTokensDeque.removeLast();
            actionTokensDeque.addFirst(actionTokensList.get(i));
        }
        return actionTokensDeque.size()==6;
    }

    /**
     * The method puts the first element of the deque at the end of it. Then it activates the last token's effect.
     * @return true if successful.
     */
    public boolean drawActionToken(){
        ActionToken tempActionToken = actionTokensDeque.pollFirst();
        actionTokensDeque.addLast(tempActionToken);
        actionTokensDeque.getLast().applyToken(lawrenceFaithPath, board, this);
        return true;
    }


    //Activate Production Power's methods

    /**
     * The method allows the player to choose a Production power to use. The method also ensures the player can afford the Production Power.
     * If the method i successful the Production Power chosen is stored in the list listOfAffordableProductionPowers.
     * @param activePlayer the player who decided to activate a Production Power.
     * @param productionPowerChosen an int that indicates which Production Power the player wants to activate.
     * @return true if the player can buy the Production Power, false otherwise.
     */
    public boolean chooseProductionPower(Player activePlayer, int productionPowerChosen) {

        ProductionPower productionPower = activePlayer.getDevCardDashboard().getProductionPower(productionPowerChosen);

        if(productionPower == null){
            return false;
        } else{
            if(canBuyProductionPower(activePlayer, productionPower)){
                listOfAffordableProductionPowers.add(productionPower);
                return true;
            } else return false;
        }

    }

    /**
     * The method ensures the player can afford the Production Power.
     * @param activePlayer the player who decided to activate the Production Power.
     * @param productionPower is the Production Power the player wants to activate.
     * @return true if the player can buy the Production Power, false otherwise.
     */
    public boolean canBuyProductionPower(Player activePlayer, ProductionPower productionPower){

        List<Resource> resourceToPay = productionPower.getResourceToPay();

        int shieldsToPay = 0;
        int slavesToPay = 0;
        int moneyToPay = 0;
        int stoneToPay = 0;

        for (Resource resource : resourceToPay) {
            if (resource.equals(Resource.SHIELD)) {
                shieldsToPay++;
            }
            if (resource.equals(Resource.SLAVE)) {
                slavesToPay++;
            }
            if (resource.equals(Resource.MONEY)) {
                moneyToPay++;
            }
            if (resource.equals(Resource.STONE)) {
                stoneToPay++;
            }
        }

        boolean affordable;

        affordable = activePlayer.getChest().contains(Resource.SHIELD, shieldsToPay);
        affordable = activePlayer.getChest().contains(Resource.SLAVE, slavesToPay);
        affordable = activePlayer.getChest().contains(Resource.MONEY, moneyToPay);
        affordable = activePlayer.getChest().contains(Resource.STONE, stoneToPay);
        affordable = activePlayer.getWarehouse().contains(shieldsToPay, Resource.SHIELD);
        affordable = activePlayer.getWarehouse().contains(slavesToPay, Resource.SLAVE);
        affordable = activePlayer.getWarehouse().contains(moneyToPay, Resource.MONEY);
        affordable =  activePlayer.getWarehouse().contains(stoneToPay, Resource.STONE);

        return affordable;

    }

    /**
     * The method allows the player to pay the production power. If the player gives wrong coordinates nothing change in the Warehouse or Chest.
     * @param activePlayer is the player who wants to pay a production power.
     * @param coordinates are the coordinates of the resources that the player chooses.
     * @param productionPower is the Production Power the player wants to pay.
     * @return true if the player manage to pay the production power, false otherwise.
     */
    public boolean playerPaysProductionPower(Player activePlayer, List<List<Object>> coordinates, ProductionPower productionPower){

        //Correct resource control
        for(List<Object> coordinate : coordinates) {

            Resource resource = (Resource) coordinate.get(0);
            boolean warehouse = (boolean) coordinate.get(1);
            int shelfLevel = (int) coordinate.get(2);

            if(warehouse){
                if (!activePlayer.getWarehouse().hasResource(shelfLevel, resource)) {
                    removeResourcesFormProductionPower(activePlayer, productionPower);
                    return false;
                }
                else {
                    activePlayer.getWarehouse().removeResourceWarehouse(shelfLevel);
                    productionPower.addSingleCoordinate(resource, true, shelfLevel, activePlayer);
                }
            } else {
                if (!activePlayer.getChest().contains(resource, 1)) {
                    removeResourcesFormProductionPower(activePlayer, productionPower);
                    return false;
                }
                else {
                    activePlayer.getChest().removeResourceFromChest(resource, 1);
                }

            }

        }

        return true;

    }

    /**
     * The method puts the resources of a production power back in their resource.
     * @param activePlayer is the Player who has the Production Power.
     * @param productionPower is the Production Power that has the coordinates of the resources.
     * @return true if successful, false otherwise.
     */
    public boolean removeResourcesFormProductionPower(Player activePlayer, ProductionPower productionPower){
        for(List<Object> coordinate : productionPower.getCoordinates()){
            productionPower.removeSingleCoordinate(activePlayer);
        }
        return true;
    }

    /**
     * The method activates every production power the player chose. It also cleans the coordinates.
     * @return true if successful, false otherwise.
     */
    public boolean activateProductionPowers(){
        for(ProductionPower productionPower : listOfAffordableProductionPowers){
            for(Resource resource : productionPower.getResourceToPay()){
                activePlayer.getChest().addResourceToChest(resource, 1);
            }
            productionPower.cleanCoordinates();
        }
        return true;
    }


}


