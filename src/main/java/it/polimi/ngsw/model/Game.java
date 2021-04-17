package it.polimi.ngsw.model;

import java.io.FileNotFoundException;
import java.util.*;
import it.polimi.ngsw.network.message.LobbyMessage;
import it.polimi.ngsw.observer.Observable;

public class Game extends Observable implements FaithPathListener{
    private static Game instance;
    public static final int MAX_PLAYERS = 4;
    public static final String SERVER_NICKNAME = "server";


    private Board board = new Board();
    private Player activePlayer;
    private List<Player> players;
    private int playerNumbers;
    private FaithPath lawrenceFaithPath = null;
    private Deque<ActionToken> actionTokensDeque = new ArrayDeque<>(6);

    private List<LeaderCard> leaderCards = new ArrayList<>();
    private LeaderCardParser leaderCardParser = new LeaderCardParser();
    
    public Game(){
        players = new ArrayList<>(0);
        initActionTokensDeque();
        this.leaderCards = initLeaderCards();
    }

    public Board getBoard(){
        return this.board;
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
     * Returns the number of players chosen by the first player.
     *
     * @return the number of players chosen by the first player.
     */
    public int getChosenPlayersNumber() {
        return playerNumbers;
    }

    //Init game

    /**
     * this method allows you to set the number of players
     * @param numberOfPlayers
     * @return true if the number of players is allowed
     */
    public boolean setNumberOfPlayers(int numberOfPlayers){
        if(numberOfPlayers < 1 || numberOfPlayers > 4) return false;
        else{
            this.playerNumbers = numberOfPlayers;
            return true;
        }
    }

    /**
     * this method create the player
     */
    public void createPlayers(){
        for(int i = 0; i < this.playerNumbers; i++){
            Player newPlayer = new Player("jhon");
            players.add(newPlayer);
            makeGameListenerOfPlayerFaithPath(players.get(i));
        }
        if(players.size()==1){
            Player newPlayer = new Player("john");
            players.add(newPlayer);
            makeGameListenerOfPlayerFaithPath(players.get(0));
            initLawrenceFaithPath();
            makeGameListenerOfLawrenceFaithPath();
        }
    }

    /**
     * this method initialize the Lawrence's FaithPath
     */
    public void initLawrenceFaithPath(){
        this.lawrenceFaithPath = new FaithPath();
    }

    /**
     * this method checks that the multiplayers' game is over
     * @return true if the multiplayers' game is ended, false otherwise
     */
    public boolean isGameEndedMultiPlayers(){
        for(int i = 0; i < playerNumbers; i++){
            if((players.get(i).getFaithPath().getCrossPosition() == 20) ||
                    (players.get(i).getDevCardDashboard().getDevCardNumber() == 7))return true;
        }
        return false;
    }

    /**
     * this method checks that the singleplayer's game is over
     * @return true if the singleplayer's game is ended, false otherwise
     */
    public boolean isGameEndedSinglePlayer(){
        for(int i = 0; i < 4; i++){
            if(this.board.getDevCardSpace(0,i).getNumberOfCards() == 0 &&
                    this.board.getDevCardSpace(1,i).getNumberOfCards() == 0 &&
                    this.board.getDevCardSpace(2,i).getNumberOfCards() == 0
            )return true;
        }

        if(this.lawrenceFaithPath.getCrossPosition() == 20)return true;

        if(this.players.get(0).getFaithPath().getCrossPosition() == 20)return true;

        if(this.players.get(0).getDevCardDashboard().getDevCardNumber() == 7)return true;

        return false;
    }

    public Player getPlayerFromList(int indexNumber){
        return this.players.get(indexNumber);
    }

    //Buy DevCard methods

    /**
     *this method allows the player to choose a card, after having done the checks he removes it
     * @param activePlayer
     * @param level
     * @param colour
     * @param slotToPut slot in dashboard where player want to put a devCard
     * @return devCard if it is legal, null otherwise
     */
    public DevCard chooseDevCard(Player activePlayer, int level, DevCardColour colour, int slotToPut){
        int devColumn = -1;
        Map<Resource, Integer> Cost = new HashMap<>();
        DevCard devCard;
        switch(colour){
            case GREEN: devColumn = 0;
            case BLUE: devColumn = 1;
            case YELLOW: devColumn = 2;
            case PURPLE: devColumn = 3;
            case EMPTY: return null;
        }

        if(this.board.getDevCardSpace(level - 1, devColumn).getNumberOfCards() == 0){
            return null;
        }

        else{
            if(level != activePlayer.getDevCardDashboard().getLevel(slotToPut) + 1)return null;
            Cost = this.board.getDevCardSpace(level-1, devColumn).firstDevCard().getDevCostAsMap();
            if(!canBuyDevCard(activePlayer,Cost))return null;
            devCard = this.board.getDevCardSpace(level - 1,devColumn).firstDevCard();
            this.board.getDevCardSpace(level, devColumn).removeFirstCard();
            return devCard;
        }
    }

    /**
     * this method removes the resources to be paid and adds the devCard to the player's dashboard
     * @param activePlayer
     * @param devCard
     * @param coordinates
     * @param slotToPut
     * @return true if t
     */
    public boolean buyDevCard(Player activePlayer, DevCard devCard, List<List<Object>> coordinates, int slotToPut){
        if(!canBuyDevCardWhithList(activePlayer, coordinates))return false;
        Resource resource;
        boolean warehouse;
        int shelfLevel;
        for(List<Object> coordinate : coordinates){

            resource = (Resource) coordinate.get(0);
            warehouse = (boolean) coordinate.get(1);

            if(warehouse) {
                shelfLevel = (int) coordinate.get(2);
                activePlayer.getWarehouse().removeResourceWarehouse(shelfLevel);
            }

            else{
                activePlayer.getChest().removeResource(resource, 1);
            }
        }
        activePlayer.getDevCardDashboard().putDevCardIn(slotToPut, devCard);
        return true;
    }

    /**
     *
     * @param activePlayer
     * @param coordinates
     * @return
     */
    public boolean canBuyDevCardWhithList(Player activePlayer, List<List<Object>> coordinates ){
        Resource resource;
        boolean warehouse;
        int shieldNumberWarehouse = 0;
        int stoneNumberWarehouse = 0;
        int slaveNumberWarehouse = 0;
        int moneyNumberWarehouse = 0;
        int shieldNumberChest = 0;
        int stoneNumberChest = 0;
        int slaveNumberChest = 0;
        int moneyNumberChest = 0;

        for(List<Object> coordinate : coordinates) {

            resource = (Resource) coordinate.get(0);
            warehouse = (boolean) coordinate.get(1);

            if(warehouse){
                switch(resource){
                    case SHIELD:
                        shieldNumberWarehouse++;
                        if(!activePlayer.getWarehouse().contains(shieldNumberWarehouse, resource))return false;
                    case STONE:
                        stoneNumberWarehouse++;
                        if(!activePlayer.getWarehouse().contains(stoneNumberWarehouse, resource))return false;
                    case SLAVE:
                        slaveNumberWarehouse++;
                        if(!activePlayer.getWarehouse().contains(slaveNumberWarehouse, resource))return false;
                    case MONEY:
                        moneyNumberWarehouse++;
                        if(!activePlayer.getWarehouse().contains(moneyNumberWarehouse, resource))return false;
                }
            }
            else{
                switch(resource){
                    case SHIELD:
                        shieldNumberChest++;
                        if(!activePlayer.getChest().contains(resource, shieldNumberChest))return false;
                    case STONE:
                        stoneNumberChest++;
                        if(!activePlayer.getChest().contains(resource, stoneNumberChest))return false;
                    case SLAVE:
                        slaveNumberChest++;
                        if(!activePlayer.getChest().contains(resource, slaveNumberChest))return false;
                    case MONEY:
                        moneyNumberChest++;
                        if(!activePlayer.getChest().contains(resource, moneyNumberChest))return false;
                }
            }

        }
        return true;
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
        if(cost.get(Resource.SHIELD) != null)shieldsToPay = cost.get(Resource.SHIELD);
        if(cost.get(Resource.SLAVE) != null)slavesToPay = cost.get(Resource.SLAVE);
        if(cost.get(Resource.MONEY) != null)moneyToPay = cost.get(Resource.MONEY);
        if(cost.get(Resource.STONE) != null)stoneToPay = cost.get(Resource.STONE);

        if(shieldsToPay > 0){
            int nShield = 0;
            if(activePlayer.getWarehouse().getLevel(Resource.SHIELD) != 0) {
                nShield = activePlayer.getWarehouse().getShelf(activePlayer.getWarehouse().getLevel(Resource.SHIELD)).getResourceNumber();
            }
            if(activePlayer.getWarehouse().getLeaderLevelType(1) == Resource.SHIELD){
                nShield += activePlayer.getWarehouse().getLeaderShelf(1).getResourceNumber();
            }
            if(activePlayer.getWarehouse().getLeaderLevelType(2) == Resource.SHIELD){
                nShield += activePlayer.getWarehouse().getLeaderShelf(2).getResourceNumber();
            }
            nShield += activePlayer.getChest().getShield();
            if(shieldsToPay > nShield)return false;
        }

        if(slavesToPay > 0){
            int nSlave = 0;
            if(activePlayer.getWarehouse().getLevel(Resource.SLAVE) != 0) {
                nSlave = activePlayer.getWarehouse().getShelf(activePlayer.getWarehouse().getLevel(Resource.SLAVE)).getResourceNumber();
            }
            if(activePlayer.getWarehouse().getLeaderLevelType(1) == Resource.SLAVE){
                nSlave += activePlayer.getWarehouse().getLeaderShelf(1).getResourceNumber();
            }
            if(activePlayer.getWarehouse().getLeaderLevelType(2) == Resource.SLAVE){
                nSlave += activePlayer.getWarehouse().getLeaderShelf(2).getResourceNumber();
            }
            nSlave += activePlayer.getChest().getSlave();
            if(slavesToPay > nSlave)return false;
        }

        if(moneyToPay > 0){
            int nMoney = 0;
            if(activePlayer.getWarehouse().getLevel(Resource.MONEY) != 0) {
                nMoney = activePlayer.getWarehouse().getShelf(activePlayer.getWarehouse().getLevel(Resource.MONEY)).getResourceNumber();
            }
            if(activePlayer.getWarehouse().getLeaderLevelType(1) == Resource.MONEY){
                nMoney += activePlayer.getWarehouse().getLeaderShelf(1).getResourceNumber();
            }
            if(activePlayer.getWarehouse().getLeaderLevelType(2) == Resource.MONEY){
                nMoney += activePlayer.getWarehouse().getLeaderShelf(2).getResourceNumber();
            }
            nMoney += activePlayer.getChest().getMoney();
            if(moneyToPay > nMoney)return false;
        }

        if(stoneToPay > 0){
            int nStone = 0;
            if(activePlayer.getWarehouse().getLevel(Resource.STONE) != 0) {
                nStone = activePlayer.getWarehouse().getShelf(activePlayer.getWarehouse().getLevel(Resource.STONE)).getResourceNumber();
            }
            if(activePlayer.getWarehouse().getLeaderLevelType(1) == Resource.STONE){
                nStone += activePlayer.getWarehouse().getLeaderShelf(1).getResourceNumber();
            }
            if(activePlayer.getWarehouse().getLeaderLevelType(2) == Resource.STONE){
                nStone += activePlayer.getWarehouse().getLeaderShelf(2).getResourceNumber();
            }
            nStone += activePlayer.getChest().getStone();
            if(stoneToPay > nStone)return false;
        }

        return true;
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
     * The method returns a list of Leader Cards.
     */
    public List<LeaderCard> getLeaderCards(){
        return leaderCards;
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


    //Activate Production Power

    private List<ProductionPower> listOfPaidProductionPowers = new ArrayList<>();

    public void addProductionPowerToPaidList(ProductionPower productionPower){
        listOfPaidProductionPowers.add(productionPower);
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
     * The method allows the player to choose a Production power to use.
     * @param activePlayer the player who decided to activate a Production Power.
     * @param productionPowerChosen an int that indicates which Production Power the player wants to activate.
     * @return the Production Power chosen if it exists, null if it doesn't.
     */
    public ProductionPower chooseProductionPower(Player activePlayer, int productionPowerChosen) {
        if(productionPowerChosen>=0 && productionPowerChosen<=5 && activePlayer!=null){
            return activePlayer.getDevCardDashboard().getProductionPower(productionPowerChosen);
        }
        else return null;
    }

    /**
     * The method allows a player to set the resources to pay and the ones to receive.
     * @param resourceToPay is a List of Resources.
     * @param resourceToReceive is a List of Resources.
     * @param baseProductionPower is the Base Production Power.
     * @return return false if one of the parameters is null or if the procedure fails, true otherwise.
     */
    public boolean setBaseProductionPowerResourceLists(List<Resource> resourceToPay, List<Resource> resourceToReceive, ProductionPower baseProductionPower){
        if(baseProductionPower!=null && resourceToPay!=null && resourceToReceive!=null){
            return baseProductionPower.setBaseProductionPowerLists(resourceToPay, resourceToReceive);
        }
        else return false;
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

        int[] numberOfResourcesToPay = new int[4];

        for (Resource resource : resourceToPay) {
            for(int j=0; j<4; j++){
                if (resource.equals(resourcesArray[j])) {
                    numberOfResourcesToPay[j]++;
                }
            }
        }

        int i=0;
        for(Resource resourceType : resourcesArray){
            boolean chestContains = activePlayer.getChest().contains(resourceType, numberOfResourcesToPay[i]);
            boolean warehouseContains = activePlayer.getWarehouse().contains(numberOfResourcesToPay[i], resourceType);
            if(!chestContains && !warehouseContains){
                if(productionPower.isBaseProductionPower()){
                    productionPower.removeBaseProductionPowerLists();
                }
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

        for(List<Object> coordinate : coordinates) {

            resource = (Resource) coordinate.get(0);
            warehouse = (boolean) coordinate.get(1);
            shelfLevel = (int) coordinate.get(2);

            if(warehouse){
                if (!activePlayer.getWarehouse().hasResource(shelfLevel, resource)) {
                    rejectProductionPower(activePlayer, productionPower);
                    return false;
                }
                else {
                    productionPower.addSingleCoordinate(resource, true, shelfLevel, activePlayer);
                    activePlayer.getWarehouse().removeResourceWarehouse(shelfLevel);
                }
            } else {
                if (!activePlayer.getChest().contains(resource, 1)) {
                    rejectProductionPower(activePlayer, productionPower);
                    return false;
                }
                else {
                    productionPower.addSingleCoordinate(resource, false, 0, activePlayer);
                    activePlayer.getChest().removeResource(resource, 1);
                }
            }

        }

        listOfPaidProductionPowers.add(productionPower);
        return true;

    }

    /**
     * The method puts the resources of a production power back in their origin and deletes the Production Power.
     * @param activePlayer is the Player who has the Production Power.
     * @param productionPower is the Production Power that has the coordinates of the resources.
     */
    public void rejectProductionPower(Player activePlayer, ProductionPower productionPower){

        if(productionPower.getCoordinates()!=null){
            productionPower.moveResourcesToOrigin(activePlayer);
        }

        listOfPaidProductionPowers.remove(productionPower);

        if(productionPower.isBaseProductionPower()){
            productionPower.removeBaseProductionPowerLists();
        }

    }

    /**
     * The method returns a list of Leader Production Powers and removes them from ListOfPaidProductionPower.
     * @return a list of Leader Production Powers.
     */
    public List<ProductionPower> checkForLeaderProductionPowerAbility(){

        if(!listOfPaidProductionPowers.isEmpty()){

            List<ProductionPower> productionPowerAbilityList = new ArrayList<>();

            for(ProductionPower productionPower : listOfPaidProductionPowers){
                for(Resource resource : productionPower.getResourceToReceive()){
                    if(resource.equals(Resource.EMPTY)){
                        productionPowerAbilityList.add(productionPower);
                    }
                }
            }

            for(ProductionPower productionPower : productionPowerAbilityList){
                listOfPaidProductionPowers.remove(productionPower);
            }

            return productionPowerAbilityList;

        }
        else return null;

    }


    /**
     * The method sets the resource to receive from leader production power. It also give a faith point to the player.
     * @param activePlayer is the player who has to choose the resource to receive.
     * @param resource is the resource chosen by the player.
     * @param leaderProductionPower is the Leader Production Power.
     * @return true if successful, false otherwise.
     */
    public boolean setResourceToReceiveFromLeaderProductionPowerAbility(Player activePlayer, Resource resource, ProductionPower leaderProductionPower){
        activePlayer.addPV(1);
        listOfPaidProductionPowers.add(leaderProductionPower);
        return leaderProductionPower.setLeaderProductionPowerResourceToReceive(resource);
    }

    /**
     * The method activates every production power the player chose. It also cleans the coordinates.
     * @return true if successful, false otherwise.
     */
    public boolean activateProductionPowers(Player player){
        for(ProductionPower productionPower : listOfPaidProductionPowers){
            for(Resource resource : productionPower.getResourceToPay()){
                player.getChest().addResource(resource, 1);
            }
            productionPower.cleanCoordinates();
        }
        listOfPaidProductionPowers.clear();
        return true;
    }


    //Vatican Report management

    /**
     * The method makes the class Game listener of a player FaithPath.
     */
    public void makeGameListenerOfPlayerFaithPath(Player player){
        player.getFaithPath().events.subscribe(this);
    }

    /**
     * The method makes the class Game listener of Lawrence's FaithPath.
     */
    public void makeGameListenerOfLawrenceFaithPath(){
        lawrenceFaithPath.events.subscribe(this);
    }

    /**
     * The method creates a list of every FaithPath in the game: both of players and Lawrence The Magnificent.
     * @return a list of every FaithPath in the game: both of players and Lawrence The Magnificent.
     */
    private List<FaithPath> createFaithPathList(){
        List<FaithPath> faithPathList = new ArrayList<>();
        for(Player player : players){
            faithPathList.add(player.getFaithPath());
        }
        if(lawrenceFaithPath != null){
            faithPathList.add(lawrenceFaithPath);
        }
        return faithPathList;
    }

    /**
     * The method receives the cross position upgraded, verify if the position requires a Vatican Report and in case activates the Papal Cards of the players in the right range.
     */
    @Override
    public void update(int crossPosition){

        List<FaithPath> faithPathList = createFaithPathList();

        if(crossPosition>=8 && crossPosition<=15){
            if(isVaticanReportOne(faithPathList)){
                for(FaithPath faithPath : faithPathList){
                    if(faithPath.getCrossPosition()>=5 && faithPath.getCrossPosition()<=8){
                        faithPath.activatePapalCardOne();
                    }
                }
            }
        }

        else if(crossPosition>=16 && crossPosition<=23){
            if(isVaticanReportTwo(faithPathList)){
                for(FaithPath faithPath : faithPathList){
                    if(faithPath.getCrossPosition()>=12 && faithPath.getCrossPosition()<=16){
                        faithPath.activatePapalCardTwo();
                    }
                }
            }
        }

        else if(crossPosition>=24){
            if(isVaticanReportThree(faithPathList)){
                for(FaithPath faithPath : faithPathList){
                    if(faithPath.getCrossPosition()>=19){
                        faithPath.activatePapalCardThree();
                    }
                }
            }
        }

    }

    /**
     * The method verifies if the cross position requires a Vatican Report.
     * @param faithPathList is a list of every active FaithPath in the game.
     * @return In case the Vatican Report is required the method returns true, false otherwise.
     */
    private boolean isVaticanReportOne(List<FaithPath> faithPathList){
        for(FaithPath faithpath : faithPathList){
            if(faithpath.getPapalCardOne()){
                return false;
            }
        }
        return true;
    }

    /**
     * The method verifies if the cross position requires a Vatican Report.
     * @param faithPathList is a list of every active FaithPath in the game.
     * @return In case the Vatican Report is required the method returns true, false otherwise.
     */
    private boolean isVaticanReportTwo(List<FaithPath> faithPathList){
        for(FaithPath faithpath : faithPathList){
            if(faithpath.getPapalCardTwo()){
                return false;
            }
        }
        return true;
    }

    /**
     * The method verifies if the cross position requires a Vatican Report.
     * @param faithPathList is a list of every active FaithPath in the game.
     * @return In case the Vatican Report is required the method returns true, false otherwise.
     */
    private boolean isVaticanReportThree(List<FaithPath> faithPathList){
        for(FaithPath faithpath : faithPathList){
            if(faithpath.getPapalCardThree()){
                return false;
            }
        }
        return true;
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
     * Resets the game instance. After this operations, all the game data is lost.
     */
    public static void resetInstance() {
        Game.instance = null;
    }
}