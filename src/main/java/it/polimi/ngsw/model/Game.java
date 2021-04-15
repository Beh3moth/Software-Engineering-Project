package it.polimi.ngsw.model;

import it.polimi.network.message.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.Serializable;
import it.polimi.observer.Observable;

public class Game  extends Observable implements Serializable{
    private static Game instance;
    public static final int MAX_PLAYERS = 4;
    public static final String SERVER_NICKNAME = "server";
    private Board board = new Board();
    private Player activePlayer;
    private List<Player> players;
    private int playerNumbers;
    private FaithPath lawrenceFaithPath = new FaithPath();
    private Deque<ActionToken> actionTokensDeque = new ArrayDeque<>(6);

    private List<LeaderCard> leaderCards = new ArrayList<>();
    private LeaderCardParser leaderCardParser = new LeaderCardParser();
    
    public Game(){
        initActionTokensDeque();
        this.leaderCards = initLeaderCards();
    }

    public Board getBoard(){
        return this.board;
    }

    /**
     * this method allows the player to purchase a DevCard and add it to his dashboard
     * @param activePlayer player who wants to buy a card
     * @param colour_to_buy color of the card he wants to buy
     * @param level_to_buy level of the card he wants to buy
     * @param slotToPut slot on the dashboard where he wants to insert the card
     * @return true if the card was purchased and inserted correctly, false if there was some problem
     */
    public boolean buyDevCard(Player activePlayer, DevCardColour colour_to_buy, int level_to_buy, int slotToPut){
        Map<Resource, Integer> Cost = new HashMap<>();
        //controllo che i parametri siano validi
        if(level_to_buy != activePlayer.getDevCardDashboard().getLevel(slotToPut) + 1)return false;
        //ora vado a vedere quanto costa la carta e se se la può permettere
        //se se la può permettere mi salvo il costo, aggiungo alla dashboard e la rimuovo

        switch(colour_to_buy){
            case GREEN:
                Cost = this.board.getDevCardSpace(level_to_buy-1, 0).firstDevCard().getDevCostAsMap();
                if(!canBuyDevCard(this.activePlayer,Cost))return false;
                activePlayer.getDevCardDashboard().putDevCardIn(slotToPut, this.board.getDevCardSpace(level_to_buy-1, 0).firstDevCard());
                this.board.getDevCardSpace(level_to_buy-1, 0).removeFirstCard();
            case BLUE:
                Cost = this.board.getDevCardSpace(level_to_buy-1, 1).firstDevCard().getDevCostAsMap();
                if(!canBuyDevCard(this.activePlayer,Cost))return false;
                activePlayer.getDevCardDashboard().putDevCardIn(slotToPut, this.board.getDevCardSpace(level_to_buy-1, 1).firstDevCard());
                this.board.getDevCardSpace(level_to_buy-1, 1).removeFirstCard();
            case YELLOW:
                Cost = this.board.getDevCardSpace(level_to_buy-1, 2).firstDevCard().getDevCostAsMap();
                if(!canBuyDevCard(this.activePlayer,Cost))return false;
                activePlayer.getDevCardDashboard().putDevCardIn(slotToPut, this.board.getDevCardSpace(level_to_buy-1, 2).firstDevCard());
                this.board.getDevCardSpace(level_to_buy-1, 2).removeFirstCard();
            case PURPLE:
                Cost = this.board.getDevCardSpace(level_to_buy-1, 3).firstDevCard().getDevCostAsMap();
                if(!canBuyDevCard(this.activePlayer,Cost))return false;
                activePlayer.getDevCardDashboard().putDevCardIn(slotToPut, this.board.getDevCardSpace(level_to_buy-1, 3).firstDevCard());
                this.board.getDevCardSpace(level_to_buy-1, 3).removeFirstCard();
            case EMPTY: return false;
        }

        //ora in Cost ho il costo della devcard devo scalare le risorse
        //-----inizio scalo risorse-----
        int shieldNumber = 0;
        int slaveNumber = 0;
        int moneyNumber = 0;
        int stoneNumber = 0;
        int choose = 0; //variabile che permette al player di scegliere dove pagare
        int removeLevel = 0; //livello da cui rimuovere la risorsa

        shieldNumber = Cost.get(Resource.SHIELD);
        slaveNumber = Cost.get(Resource.SLAVE);
        moneyNumber = Cost.get(Resource.MONEY);
        stoneNumber = Cost.get(Resource.STONE);

        if(shieldNumber != 0){
            for(int i = 0; i < shieldNumber; i++){
            if(activePlayer.getWarehouse().contains(1, Resource.SHIELD) && activePlayer.getChest().contains(Resource.SHIELD, 1)){ //sono entrambi true quindi deve scegliere
                //chiedo al controller di dirmi come vuole pagare le shield
                //0:warehouse 1:chest, nella variabile:choose
                //ovviamente solo se la risorsa è presente in entrambi i luoghi
                //ci sarà tipo choose = ...
                if(choose == 0){
                    //rimuovo da warehouse
                    removeLevel = activePlayer.getWarehouse().getLevel(Resource.SHIELD);
                    if(removeLevel < 4) {
                        activePlayer.getWarehouse().removeResourceWarehouse(removeLevel);
                    }
                    else if(removeLevel == 4){
                        activePlayer.getWarehouse().removeSpecialResourceWarehouse(1);
                    }
                    else{
                        activePlayer.getWarehouse().removeSpecialResourceWarehouse(2);
                    }
                }
                else{
                    //rimuovo da chest
                    activePlayer.getChest().removeResourceFromChest(Resource.SHIELD, 1);
                }
            }
            else {
                if(activePlayer.getWarehouse().contains(1, Resource.SHIELD)){
                    removeLevel = activePlayer.getWarehouse().getLevel(Resource.SHIELD);
                    if(removeLevel < 4) {
                        activePlayer.getWarehouse().removeResourceWarehouse(removeLevel);
                    }
                    else if(removeLevel == 4){
                        activePlayer.getWarehouse().removeSpecialResourceWarehouse(1);
                    }
                    else{
                        activePlayer.getWarehouse().removeSpecialResourceWarehouse(2);
                    }
                }
                else if (activePlayer.getChest().contains(Resource.SHIELD, 1)){
                    activePlayer.getChest().removeResourceFromChest(Resource.SHIELD, 1);
                }
                else return false; //ulteriore controllo, se non se lo può permettere ritorna false
            }
            }
        }

        if(slaveNumber != 0){
            for(int i = 0; i < slaveNumber; i++){
                if(activePlayer.getWarehouse().contains(1, Resource.SLAVE) && activePlayer.getChest().contains(Resource.SLAVE, 1)){ //sono entrambi true quindi deve scegliere
                    //chiedo al controller di dirmi come vuole pagare le slave
                    //0:warehouse 1:chest, nella variabile:choose
                    //ovviamente solo se la risorsa è presente in entrambi i luoghi
                    //ci sarà tipo choose = ...
                    if(choose == 0){
                        //rimuovo da warehouse
                        removeLevel = activePlayer.getWarehouse().getLevel(Resource.SLAVE);
                        if(removeLevel < 4) {
                            activePlayer.getWarehouse().removeResourceWarehouse(removeLevel);
                        }
                        else if(removeLevel == 4){
                            activePlayer.getWarehouse().removeSpecialResourceWarehouse(1);
                        }
                        else{
                            activePlayer.getWarehouse().removeSpecialResourceWarehouse(2);
                        }
                    }
                    else{
                        //rimuovo da chest
                        activePlayer.getChest().removeResourceFromChest(Resource.SLAVE, 1);
                    }
                }
                else {
                    if(activePlayer.getWarehouse().contains(1, Resource.SLAVE)){
                        removeLevel = activePlayer.getWarehouse().getLevel(Resource.SLAVE);
                        if(removeLevel < 4) {
                            activePlayer.getWarehouse().removeResourceWarehouse(removeLevel);
                        }
                        else if(removeLevel == 4){
                            activePlayer.getWarehouse().removeSpecialResourceWarehouse(1);
                        }
                        else{
                            activePlayer.getWarehouse().removeSpecialResourceWarehouse(2);
                        }
                    }
                    else if (activePlayer.getChest().contains(Resource.SLAVE, 1)){
                        activePlayer.getChest().removeResourceFromChest(Resource.SLAVE, 1);
                    }
                    else return false; //ulteriore controllo, se non se lo può permettere ritorna false
                }
            }
        }

        if(moneyNumber != 0){
            for(int i = 0; i < moneyNumber; i++){
                if(activePlayer.getWarehouse().contains(1, Resource.MONEY) && activePlayer.getChest().contains(Resource.MONEY, 1)){ //sono entrambi true quindi deve scegliere
                    //chiedo al controller di dirmi come vuole pagare i money
                    //0:warehouse 1:chest, nella variabile:choose
                    //ovviamente solo se la risorsa è presente in entrambi i luoghi
                    //ci sarà tipo choose = ...
                    if(choose == 0){
                        //rimuovo da warehouse
                        removeLevel = activePlayer.getWarehouse().getLevel(Resource.MONEY);
                        if(removeLevel < 4) {
                            activePlayer.getWarehouse().removeResourceWarehouse(removeLevel);
                        }
                        else if(removeLevel == 4){
                            activePlayer.getWarehouse().removeSpecialResourceWarehouse(1);
                        }
                        else{
                            activePlayer.getWarehouse().removeSpecialResourceWarehouse(2);
                        }
                    }
                    else{
                        //rimuovo da chest
                        activePlayer.getChest().removeResourceFromChest(Resource.MONEY, 1);
                    }
                }
                else {
                    if(activePlayer.getWarehouse().contains(1, Resource.MONEY)){
                        removeLevel = activePlayer.getWarehouse().getLevel(Resource.MONEY);
                        if(removeLevel < 4) {
                            activePlayer.getWarehouse().removeResourceWarehouse(removeLevel);
                        }
                        else if(removeLevel == 4){
                            activePlayer.getWarehouse().removeSpecialResourceWarehouse(1);
                        }
                        else{
                            activePlayer.getWarehouse().removeSpecialResourceWarehouse(2);
                        }
                    }
                    else if (activePlayer.getChest().contains(Resource.MONEY, 1)){
                        activePlayer.getChest().removeResourceFromChest(Resource.MONEY, 1);
                    }
                    else return false; //ulteriore controllo, se non se lo può permettere ritorna false
                }
            }
        }

        if(stoneNumber != 0){
            for(int i = 0; i < stoneNumber; i++){
                if(activePlayer.getWarehouse().contains(1, Resource.STONE) && activePlayer.getChest().contains(Resource.STONE, 1)){ //sono entrambi true quindi deve scegliere
                    //chiedo al controller di dirmi come vuole pagare le stone
                    //0:warehouse 1:chest, nella variabile:choose
                    //ovviamente solo se la risorsa è presente in entrambi i luoghi
                    //ci sarà tipo choose = ...
                    if(choose == 0){
                        //rimuovo da warehouse
                        removeLevel = activePlayer.getWarehouse().getLevel(Resource.STONE);
                        if(removeLevel < 4) {
                            activePlayer.getWarehouse().removeResourceWarehouse(removeLevel);
                        }
                        else if(removeLevel == 4){
                            activePlayer.getWarehouse().removeSpecialResourceWarehouse(1);
                        }
                        else{
                            activePlayer.getWarehouse().removeSpecialResourceWarehouse(2);
                        }
                    }
                    else{
                        //rimuovo da chest
                        activePlayer.getChest().removeResourceFromChest(Resource.STONE, 1);
                    }
                }
                else {
                    if(activePlayer.getWarehouse().contains(1, Resource.STONE)){
                        removeLevel = activePlayer.getWarehouse().getLevel(Resource.STONE);
                        if(removeLevel < 4) {
                            activePlayer.getWarehouse().removeResourceWarehouse(removeLevel);
                        }
                        else if(removeLevel == 4){
                            activePlayer.getWarehouse().removeSpecialResourceWarehouse(1);
                        }
                        else{
                            activePlayer.getWarehouse().removeSpecialResourceWarehouse(2);
                        }
                    }
                    else if (activePlayer.getChest().contains(Resource.STONE, 1)){
                        activePlayer.getChest().removeResourceFromChest(Resource.STONE, 1);
                    }
                    else return false; //ulteriore controllo, se non se lo può permettere ritorna false
                }
            }
        }
        //-----fine scalo risorse-----
        return true; // se è arrivato fino a qui è andato tutto bene
    }

    /**
     * this method tells if the player has the necessary resources to be able to purchase the desired card
     * @param activePlayer
     * @param cost is a map that has the resources as keys and the number of associated resources as an integer
     * @return true if the player can buy the card false otherwise
     */
    public boolean canBuyDevCard(Player activePlayer, Map<Resource, Integer> cost){

        int shieldsToPay = 0;
        int slavesToPay = 0;
        int moneyToPay = 0;
        int stoneToPay = 0;

        shieldsToPay = cost.get(Resource.SHIELD);
        slavesToPay = cost.get(Resource.SLAVE);
        moneyToPay = cost.get(Resource.MONEY);
        stoneToPay = cost.get(Resource.STONE);

        boolean affordable = false;

        if(shieldsToPay > 0){
            affordable = (activePlayer.getChest().contains(Resource.SHIELD, shieldsToPay) ||
            activePlayer.getWarehouse().contains(shieldsToPay, Resource.SHIELD));
            if(!affordable)return false;
        }

        if(slavesToPay > 0){
            affordable = (activePlayer.getChest().contains(Resource.SLAVE, slavesToPay) ||
                    activePlayer.getWarehouse().contains(slavesToPay, Resource.SLAVE));
            if(!affordable)return false;
        }

        if(moneyToPay > 0){
            affordable = (activePlayer.getChest().contains(Resource.MONEY, moneyToPay) ||
                    activePlayer.getWarehouse().contains(moneyToPay, Resource.MONEY));
            if(!affordable)return false;
        }

        if(stoneToPay > 0){
            affordable = (activePlayer.getChest().contains(Resource.STONE, stoneToPay) ||
                    activePlayer.getWarehouse().contains(stoneToPay, Resource.STONE));
            if(!affordable)return false;
        }

        return affordable;
    }

    public void getBoardResourcesToStock(Player activePlayer, int columnOrRow, int wich){
        if(columnOrRow == 1){
            this.board.getMarbleColumn(wich, activePlayer);
        }
        else if(columnOrRow == 2){
            this.board.getMarbleRow(wich, activePlayer);
        }
    }

    public void manageWhiteResources(Player activePlayer, int whitePower){ //avrò un while che in base al numero di risorce nel white stock, scelgo per ognuna cosa voglio e chiamo questo metodo
        if(whitePower == 1){
            activePlayer.getWarehouse().addResourceToStock(activePlayer.getWhiteMarblePowerOne());
            activePlayer.getWarehouse().removeFromWhiteStock();
        }
        else if(whitePower == 2){
            activePlayer.getWarehouse().addResourceToStock(activePlayer.getWhiteMarblePowerTwo());
            activePlayer.getWarehouse().removeFromWhiteStock();
        }
    }
    /**
     * Method that permit to take resources from the market, it asks the user if column or row, and which one
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
                    //addOtherFaithPoint(activePlayer);
                    j--;
                    up--;
                } else if (choosen == 2) {
                    System.out.println("Choose wich shelf of the warehouse");
                    boolean goneRight = false;
                    do {
                        level = input.nextInt();
                        if (0 < level && level < 4) {
                            goneRight = true;
                        }else{System.out.println("Not valid, choose again");}

                    } while (!goneRight);
                    generalValidator = activePlayer.getWarehouse().addResourceToWarehouse(level, activePlayer.getWarehouse().getStockResource(0)); //devo controllare poi se una volta dentro la inserisco veramente
                    if(generalValidator == true){activePlayer.getWarehouse().removeFirstResourceFromStock();
                        j--;
                        up--;}
                    //QUA DEVO METTERE SOLO SE è ANDATO POSITIVOOOOOOO
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

                } else if (choosen == 4) { //da finireeeeeeeeeee
                    reorderWarehouse(activePlayer);
                }
            } while (!generalValidator);
            j++;

        }
        System.out.println(activePlayer.getWarehouse().getShelf(1).getResourceType() + " " +  activePlayer.getWarehouse().getShelf(1).getResourceNumber() + "  " + "  " + activePlayer.getWarehouse().getShelf(2).getResourceType() +  " " +  activePlayer.getWarehouse().getShelf(2).getResourceNumber() + "  " + "  " + " " + activePlayer.getWarehouse().getShelf(3).getResourceType() + "  " + " " +  activePlayer.getWarehouse().getShelf(3).getResourceNumber() + "  " + "  ");
        reorderWarehouse(activePlayer);
    }

    public void reorderWarehouse(Player activePlayer) {
        Scanner input = new Scanner(System.in);
        int quantityOfElementSupport = 0;
        int choosen;
        boolean verifier = false; //se true, finish process
        List<Resource> support = new ArrayList<Resource>();
        do{
            System.out.println("The situation: First shelf " + activePlayer.getWarehouse().getShelf(1).getResourceType() + " " + activePlayer.getWarehouse().getShelf(1).getResourceNumber());
            System.out.println("Second shelf " + activePlayer.getWarehouse().getShelf(2).getResourceType() + " " + activePlayer.getWarehouse().getShelf(2).getResourceNumber());
            System.out.println("Third shelf " + activePlayer.getWarehouse().getShelf(3).getResourceType() + " " + activePlayer.getWarehouse().getShelf(3).getResourceNumber());
            System.out.println("First special shelf " + activePlayer.getWarehouse().getLeaderShelf(1).getResourceType()+ " " + activePlayer.getWarehouse().getLeaderShelf(1).getResourceNumber());
            System.out.println("Second special shelf " + activePlayer.getWarehouse().getLeaderShelf(2).getResourceType()+ " " + activePlayer.getWarehouse().getLeaderShelf(2).getResourceNumber());
            System.out.println("Chose what to do: 1)Finish process 2)Throw away a resource 3) throw away a leader resource 4)Move resources to Stock 5)RePut resource to warehouse");
            choosen = input.nextInt();

            boolean goneWell = false; //if true, adds others faithpoint


            if(choosen == 1){
                if(support.isEmpty()){
                    verifier = true;}
                else{ System.out.println("You still have some resources to manage ");
                    //qua potrei elencare quali
                }
            }
            else if(choosen == 2){
                System.out.println("Chose a shelf to remove from (between 1 and 3");
                int choosenTwo = input.nextInt();

                if(choosenTwo < 4 && 0 < choosenTwo){
                    goneWell = activePlayer.getWarehouse().discardResourceFromWarehouse(choosenTwo);
                }
                if(goneWell == false){
                    System.out.println("Nothing was throw away");
                }
            }
            else if(choosen == 3){
                int levelUp = 0;
                int level = 0;
                if (activePlayer.getWarehouse().getLeaderLevelType(1) != Resource.EMPTY) {
                    levelUp = 1;
                }
                if (activePlayer.getWarehouse().getLeaderLevelType(1) != Resource.EMPTY && activePlayer.getWarehouse().getLeaderLevelType(2) != Resource.EMPTY) {
                    levelUp = 2;
                }
                boolean goneRight = false;
                if(levelUp == 0){
                    System.out.println("You don't have special shelf");
                }
                else if(levelUp > 0) {
                    System.out.println("Choose wich specialshelf of the warehouse");
                    do {
                        level = input.nextInt();
                        if (0 < level && level <= levelUp) {
                            goneRight = true;
                        }

                    } while (!goneRight);
                }
                goneWell = activePlayer.getWarehouse().discardResourceFromSpecialLevel(level);
            }
            else if(choosen == 4){
                System.out.println("Choose a floor to take resource from 1-3 normal shelf, 4-5 leader shelf");
                int choosenTwo = input.nextInt();
                boolean okParam = false;
                if(choosenTwo < 4 && 0 < choosenTwo){
                    if(activePlayer.getWarehouse().getShelf(choosenTwo).getResourceType() != Resource.EMPTY) {
                        support.add(activePlayer.getWarehouse().getShelf(choosenTwo).getResourceType());
                        quantityOfElementSupport++;
                    }
                    okParam = activePlayer.getWarehouse().removeResourceWarehouse(choosenTwo);
                    if(okParam == false){
                        System.out.println("Not valid");
                        //support.remove(quantityOfElementSupport - 1);
                    }

                }
                else if(choosenTwo == 4 && activePlayer.getWarehouse().getLeaderLevelType(1) != Resource.EMPTY){
                    if(activePlayer.getWarehouse().getLeaderShelf(1).getResourceType() != Resource.EMPTY) {
                        support.add(activePlayer.getWarehouse().getLeaderShelf(1).getResourceType());
                        quantityOfElementSupport++;
                    }
                    okParam = activePlayer.getWarehouse().removeSpecialResourceWarehouse(1);
                    if(okParam == false){
                        System.out.println("Not valid");
                        //if(activePlayer.getWarehouse().getLeaderShelf(1).getResourceType() != Resource.EMPTY) {
                        //support.remove(quantityOfElementSupport - 1);
                        //}
                    }
                }
                else if(choosenTwo == 5 && activePlayer.getWarehouse().getLeaderLevelType(2) != Resource.EMPTY){
                    if(activePlayer.getWarehouse().getLeaderShelf(2).getResourceType() != Resource.EMPTY) {
                        support.add(activePlayer.getWarehouse().getLeaderShelf(2).getResourceType());
                        quantityOfElementSupport++;
                    }
                    okParam = activePlayer.getWarehouse().removeSpecialResourceWarehouse(2);
                    if(okParam == false){
                        System.out.println("Not valid");
                        //if(activePlayer.getWarehouse().getLeaderShelf(2).getResourceType() != Resource.EMPTY) {
                        // support.remove(quantityOfElementSupport - 1);}
                    }
                }
                else{
                    System.out.println("Not valid");
                }
            }
            else if(choosen == 5){
                System.out.println("All the resources that you have to manage: ");
                // if(quantityOfElementSupport == 0){
                //     System.out.println("Nothing");
                // }
                for(int i = 0; i < quantityOfElementSupport; i++){
                    System.out.println(support.get(i));
                }
                int i = 0;
                while(i < quantityOfElementSupport){
                    System.out.println("What do you want to do with this?  " + support.get(i) + " 1) Put inside Warehouse 2) Discard completaly   (Others do nothing)");
                    int choosenTwo = input.nextInt();
                    boolean Validator = false;
                    if(choosenTwo == 2){
                        support.remove(i);
                        addOtherFaithPoint(activePlayer);
                        System.out.println("Aggiunta agli altri dei faith point");
                        i--;
                        quantityOfElementSupport--;
                    }
                    else if(choosenTwo == 1){
                        System.out.println("Inside wich shelf 1-3 normal, 4-5");
                        int choosenThree = input.nextInt();
                        if(choosenThree > 0 && choosenThree < 4) {
                            Validator = activePlayer.getWarehouse().addResourceToWarehouse(choosenThree, support.get(i)); //devo controllare poi se una volta dentro la inserisco veramente
                        }
                        else if(choosenThree < 6 && choosenThree > 3){
                            Validator = activePlayer.getWarehouse().addResourceToSpecialLevel(choosenThree - 3, support.get(i));
                        }
                        if(Validator == true){
                            support.remove(i);
                            i--;
                            quantityOfElementSupport--;
                        }else {
                            System.out.println("Failed, not valid to insert, chose again what to do with this resource");
                            i--;
                        }
                    }
                    else{
                        System.out.println("Resource skipped, remember to go back again later");
                    }
                    i++;

                }

            }

            if(goneWell == true){
                addOtherFaithPoint(activePlayer);
                System.out.println("Aggiunta agli altri dei faith point");
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


    //LeaderCard methods

    /**
     * The method return a list of every LeaderCard of the game.
     * @return a list of LeaderCards.
     */
    private List<LeaderCard> initLeaderCards() {
        try{
            return leaderCardParser.initLeaderCards();
        }
        catch (FileNotFoundException e){
            return null;
        }
    }

    /**
     * The method removes the last four LeaderCards from the leaderCards list of the player and returning them.
     * @return a list of LeaderCards.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public List<LeaderCard> removeAndReturnTheLastFourLeaderCards() throws IndexOutOfBoundsException {
        List<LeaderCard> leaderCardList = new ArrayList<>();
        for(int i=0; i<4; i++){
            leaderCardList.add(leaderCards.remove(leaderCards.size()-1));
        }
        return leaderCardList;
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
    private void initActionTokensDeque(){
        this.actionTokensDeque.add(new DiscardDevCard(DevCardColour.BLUE));
        this.actionTokensDeque.add(new DiscardDevCard(DevCardColour.YELLOW));
        this.actionTokensDeque.add(new DiscardDevCard(DevCardColour.GREEN));
        this.actionTokensDeque.add(new DiscardDevCard(DevCardColour.PURPLE));
        this.actionTokensDeque.add(new Move());
        this.actionTokensDeque.add(new MoveAndScrum());
        shuffleActionTokensDeque();
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


    //Activate Production Power's

    //List of production powers the player decides to activate between among his available Production Powers.
    private List<ProductionPower> listOfAffordableProductionPowers = new ArrayList<>();

    /**
     * The method allows the player to choose a Production power to use. The method also ensures the player can afford the Production Power.
     * If the method i successful the Production Power chosen is stored in the list listOfAffordableProductionPowers.
     * @param activePlayer the player who decided to activate a Production Power.
     * @param productionPowerChosen an int that indicates which Production Power the player wants to activate.
     * @return true if the player can buy the Production Power, false otherwise.
     */
    public boolean chooseProductionPower(Player activePlayer, int productionPowerChosen) {

        ProductionPower productionPower = activePlayer.getDevCardDashboard().getSingleProductionPower(productionPowerChosen);

        if(productionPower == null){
            return false;
        } else{
            if(canBuyProductionPower(activePlayer, productionPower)){
                return listOfAffordableProductionPowers.add(productionPower);
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

        Resource[] resourcesArray = new Resource[4];
        resourcesArray[0] = Resource.SHIELD;
        resourcesArray[1] = Resource.MONEY;
        resourcesArray[2] = Resource.SLAVE;
        resourcesArray[3] = Resource.STONE;

        int[] resourceToPayArray = new int[4];

        for (Resource resource : resourceToPay) {
            for(int j=0; j<4; j++){
                if (resource.equals(resourcesArray[j])) {
                    resourceToPayArray[j]++;
                }
            }
        }
        int i=0;
        for(Resource resourceType : resourcesArray){
            boolean chestContains = activePlayer.getChest().contains(resourceType, resourceToPayArray[i]);
            boolean warehouseContains = activePlayer.getWarehouse().contains(resourceToPayArray[i], resourceType);
            if(!(chestContains||warehouseContains)){
                return false;
            }
            i++;
        }

        return true;

    }

    /**
     * The method allows the player to pay the production power. If the player gives wrong coordinates nothing change in the Warehouse or Chest.
     * @param activePlayer is the player who wants to pay a production power.
     * @param coordinates are the coordinates of the resources that the player chooses.
     * @param productionPower is the Production Power the player wants to pay.
     * @return true if the player manage to pay the production power, false otherwise.
     */
    public boolean payProductionPower(Player activePlayer, List<List<Object>> coordinates, ProductionPower productionPower){

        Resource resource;
        boolean warehouse;
        int shelfLevel;

        //Correct resource control
        for(List<Object> coordinate : coordinates) {

            resource = (Resource) coordinate.get(0);
            warehouse = (boolean) coordinate.get(1);
            shelfLevel = (int) coordinate.get(2);

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
                    productionPower.addSingleCoordinate(resource, false, 0, activePlayer);
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
        listOfAffordableProductionPowers.clear();
        return true;
    }

    /**
     * @return the singleton instance.
     */
    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    /**
     * Search a nickname in the current Game.
     *
     * @param nickname the nickname of the player.
     * @return {@code true} if the nickname is found, {@code false} otherwise.
     */
    public boolean isNicknameTaken(String nickname) {
        return players.stream()
                .anyMatch(p -> nickname.equals(p.getNickname()));
    }


    /**
     * Adds a player to the game.
     * Notifies all the views if the playersNumber is already set.
     *
     * @param player the player to add to the game.
     */
    public void addPlayer(Player player) {
        players.add(player);
        if (playerNumbers != 0) {
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.playerNumbers));
        }
    }

    /**
     * Returns a list of player nicknames that are already in-game.
     *
     * @return a list with all nicknames in the Game
     */
    public List<String> getPlayersNicknames() {
        List<String> nicknames = new ArrayList<>();
        for (Player p : players) {
            nicknames.add(p.getNickname());
        }
        return nicknames;
    }

    /**
     * Returns the number of players chosen by the first player.
     *
     * @return the number of players chosen by the first player.
     */
    public int getChosenPlayersNumber() {
        return playerNumbers;
    }


    /**
     * Number of current players added in the game.
     *
     * @return the number of players.
     */
    public int getNumCurrentPlayers() {
        return players.size();
    }
    /**
     * Sets the max number of players chosen by the first player joining the game.
     *
     * @param chosenMaxPlayers the max players number. Value can be {@code 0 < x < MAX_PLAYERS}.
     * @return {@code true} if the argument value is {@code 0 < x < MAX_PLAYERS}, {@code false} otherwise.
     */
    public boolean setChosenMaxPlayers(int chosenMaxPlayers) {
        if (chosenMaxPlayers > 0 && chosenMaxPlayers <= MAX_PLAYERS) {
            this.playerNumbers = chosenMaxPlayers;
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.playerNumbers));
            return true;
        }
        return false;
    }

    /**
     * Removes a player from the game.
     * Notifies all the views if the notifyEnabled argument is set to {@code true}.
     *
     * @param nickname      the nickname of the player to remove from the game.
     * @param notifyEnabled set to {@code true} to enable a lobby disconnection message, {@code false} otherwise.
     * @return {@code true} if the player is removed, {@code false} otherwise.
     */
    public boolean removePlayerByNickname(String nickname, boolean notifyEnabled) {
        boolean result = players.remove(getPlayerByNickname(nickname));

        if (notifyEnabled) {
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.playerNumbers));
        }

        return result;
    }

    /**
     * Returns a player given his {@code nickname}.
     * Only the first occurrence is returned because
     * the player nickname is considered to be unique.
     * If no player is found {@code null} is returned.
     *
     * @param nickname the nickname of the player to be found.
     * @return Returns the player given his {@code nickname}, {@code null} otherwise.
     */
    public Player getPlayerByNickname(String nickname) {
        return players.stream()
                .filter(player -> nickname.equals(player.getNickname()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Resets the game instance. After this operations, all the game data is lost.
     */
    public static void resetInstance() {
        Game.instance = null;
    }
}