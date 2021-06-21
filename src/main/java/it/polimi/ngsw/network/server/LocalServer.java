package it.polimi.ngsw.network.server;

import it.polimi.ngsw.controller.TurnController;
import it.polimi.ngsw.model.*;
import it.polimi.ngsw.network.message.*;
import it.polimi.ngsw.view.VirtualView;
import it.polimi.ngsw.view.cli.LocalCli;

import java.util.List;
import java.util.Map;

/**
 * Fake server that is used if you want to play the game alone in a local way
 */
public class LocalServer {

    private Game game;
    private LocalCli cli;
    private GameState gameState;
    private String nickname;

    public LocalServer(LocalCli cli) {
        this.cli = cli;
        this.game = new Game();
        setGameState(GameState.LOGIN);

    }

    public void loginHandler(String nickname) {
        game.addPlayer(new Player(nickname));
        this.nickname = nickname;
        game.setChosenMaxPlayers(1);
        game.initLawrenceFaithPath();
        initSoloGame();
    }

    private void initSoloGame() {
        setGameState(GameState.INIT);
        cli.askLeaderCard(game.removeAndReturnTheLastFourLeaderCards());
    }

    public  void leaderCardHandler(List<LeaderCard> leaders){
        game.getPlayerByNickname(nickname).setLeaderCard(leaders);
        startGame();
    }


    private void startGame() {
        setGameState(GameState.IN_GAME);
        newTurn();
    }
    /**
     * Initialize a new Turn.
     */
    public void newTurn() {

        List<LeaderCard> Leaders = game.getPlayerByNickname(nickname).getLeaderCards();
        Marble singleMarble = game.getBoard().getSingleMarble();
        Marble[] firstRow = new Marble[4];
        Marble[] secondRow = new Marble[4];
        Marble[] thirdRow = new Marble[4];
        for(int i = 0; i < 4; i++){
            firstRow[i] = game.getBoard().getMarble(0,i);
            secondRow[i] = game.getBoard().getMarble(1,i);
            thirdRow[i] = game.getBoard().getMarble(2,i);
        }

        //Import DevCardDashboard active cards
        List<ProductionPower> leaderProductionPowerList = game.getPlayerByNickname(nickname).getDevCardDashboard().getLeaderProductionPowerList();
        ProductionPower baseProductionPower = game.getPlayerByNickname(nickname).getDevCardDashboard().getBaseProductionPower();
        Map<Integer, DevCard> activeDevCardMap = game.getPlayerByNickname(nickname).getDevCardDashboard().getActiveDevCardsAsMap();
        List<ProductionPower> productionPowerList = game.getPlayerByNickname(nickname).getDevCardDashboard().getActiveProductionPowerList();
        DevCard[][] devCardMarket = game.getBoard().getDevCardMarket();
        Resource firstShelf = game.getPlayerByNickname(nickname).getWarehouse().getShelf(1).getResourceType();
        Resource secondShelf = game.getPlayerByNickname(nickname).getWarehouse().getShelf(2).getResourceType();
        int secondShelfNumber = game.getPlayerByNickname(nickname).getWarehouse().getShelf(2).getResourceNumber();
        Resource thirdShelf = game.getPlayerByNickname(nickname).getWarehouse().getShelf(3).getResourceType();
        int thirdShelfNumber = game.getPlayerByNickname(nickname).getWarehouse().getShelf(3).getResourceNumber();
        Map<Resource, Integer> chest = game.getPlayerByNickname(nickname).getChest().getResourcesAsMap();
        int crossPosition = game.getPlayerByNickname(nickname).getFaithPath().getCrossPosition();
        int victoryPoints = game.getPlayerByNickname(nickname).getPV();
        boolean papalCardOne = game.getPlayerByNickname(nickname).getFaithPath().getPapalCardOne();
        boolean papalCardTwo = game.getPlayerByNickname(nickname).getFaithPath().getPapalCardTwo();
        boolean papalCardThree = game.getPlayerByNickname(nickname).getFaithPath().getPapalCardThree();
        cli.startTurnMessage(Leaders,singleMarble, firstRow, secondRow, thirdRow, leaderProductionPowerList, activeDevCardMap, baseProductionPower, devCardMarket, firstShelf, secondShelf, secondShelfNumber, thirdShelf, thirdShelfNumber, chest, crossPosition, victoryPoints, papalCardOne, papalCardTwo, papalCardThree);

    }

    public void activateLeaderCard(int wich, int turnZone){
        if(game.getPlayerByNickname(nickname).activeLeaderCard(wich)){
            if(turnZone == 1){
                cli.continueTurn(1,1,1, wich, game.getPlayerByNickname(nickname).getLeaderCards());   //inizio turno, tipo leadercard, andato a segno
            }
            else if(turnZone == 2){
                cli.continueTurn(2,1,1, wich, game.getPlayerByNickname(nickname).getLeaderCards());   //fine turno, tipo leadercard, andato a segno
            }
        }
        else{
            if(turnZone == 1)cli.continueTurn(1,1,0, wich, game.getPlayerByNickname(nickname).getLeaderCards());   //inizio turno, tipo leadercard, non andato a segno
            else if(turnZone == 2)cli.continueTurn(2,1,0, wich, game.getPlayerByNickname(nickname).getLeaderCards());   //fine turno, tipo leadercard, non andato a segno
        }
    }

    public void discardCard(int chose, int TurnZone){
        game.getPlayerByNickname(nickname).getFaithPath().increaseCrossPosition();
        cli.continueTurn(TurnZone,2,1, chose, null);
    }

    public void buyFromMarket(int RowOrColumn, int WichOne){
        Player activePlayer = game.getPlayerByNickname(nickname);
        game.buyFromMarket(RowOrColumn,WichOne, activePlayer);
        game.manageWhiteResources(activePlayer);
        cli.buyMarketResource(game.getPlayerByNickname(nickname).getWarehouse().getWarehouseStock(),game.getPlayerByNickname(nickname).getWhiteMarblePowerOne(), game.getPlayerByNickname(nickname).getWhiteMarblePowerTwo() );
    }

    /**
     * Set the State of the current Game.
     *
     * @param gameState State of the current Game.
     */
    private void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void ReorderWarehouse(boolean b) {
        game.getPlayerByNickname(nickname).getWarehouse().removeAllStock();
        cli.reorderWarehouse(game.getPlayerByNickname(nickname).getWarehouse().getResourcesAsMap(), game.getPlayerByNickname(nickname).getWarehouse().getLeaderLevelType(1), game.getPlayerByNickname(nickname).getWarehouse().getLeaderLevelType(2), b);
    }

    public void NewWarehouse(Resource newFirstShelf, List<Resource> newSecondShelf, List<Resource> newThirdShelf, List<Resource> newFirstSpecialShelf, List<Resource> newSecondSpecialShelf, List<Resource> supportDiscard, Boolean isIndipendent) {
        if(supportDiscard != null) {
            for (int i = 0; i < supportDiscard.size(); i++) {
                game.increaseOtherFaithPoints(game.getPlayerByNickname(nickname), 1);
            }
        }
        game.getPlayerByNickname(nickname).getWarehouse().newFirstShelf(newFirstShelf);

        if (newSecondShelf != null && !newSecondShelf.isEmpty()) {
            game.getPlayerByNickname(nickname).getWarehouse().newSecondShelf(newSecondShelf); }

        if (newSecondShelf == null || newSecondShelf.isEmpty()) {
            game.getPlayerByNickname(nickname).getWarehouse().getShelf(2).setResourceType(Resource.EMPTY);
            game.getPlayerByNickname(nickname).getWarehouse().getShelf(2).setResourceNumber(0);
        }


        if (newThirdShelf != null && !newThirdShelf.isEmpty()) {
            game.getPlayerByNickname(nickname).getWarehouse().newThirdShelf(newThirdShelf); }

        if (newThirdShelf == null || newThirdShelf.isEmpty()) {
            game.getPlayerByNickname(nickname).getWarehouse().getShelf(3).setResourceType(Resource.EMPTY);
            game.getPlayerByNickname(nickname).getWarehouse().getShelf(3).setResourceNumber(0);
        }

        if (newFirstSpecialShelf != null && !newFirstSpecialShelf.isEmpty()) {
            game.getPlayerByNickname(nickname).getWarehouse().newFirstSpecialShelf(newFirstSpecialShelf); }

        if ((game.getPlayerByNickname(nickname).getWarehouse().getShelf(4) != null) && (newFirstSpecialShelf == null || newFirstSpecialShelf.isEmpty())) {
            game.getPlayerByNickname(nickname).getWarehouse().getShelf(4).setResourceNumber(0);
        }

        if (newSecondSpecialShelf != null && !newSecondSpecialShelf.isEmpty()) {
            game.getPlayerByNickname(nickname).getWarehouse().newSecondSpecialShelf(newSecondSpecialShelf); }

        if ((game.getPlayerByNickname(nickname).getWarehouse().getShelf(5) != null )&&(newSecondSpecialShelf == null || newSecondSpecialShelf.isEmpty())) {
            game.getPlayerByNickname(nickname).getWarehouse().getShelf(5).setResourceNumber(0);
        }

        List<LeaderCard> Leaders = game.getPlayerByNickname(nickname).getLeaderCards();

        if(isIndipendent == true){ //se io l'ho chiamata all'inizio, prima di qualsiasi mossa (buy market)
            cli.afterReorder(0, Leaders);
        }
        else{
            cli.afterReorder(1, Leaders);  //l'ho chiamata dopo buy market, quindi finita la main move
        }

    }

    public void ChooseDevCard(int finalLevel, int finalColumn, int finalSlotToPut) {
        DevCard devCardChosen;
        DevCardColour devCardColour = DevCardColour.EMPTY;
        switch(finalColumn){
            case 1: {devCardColour = DevCardColour.GREEN;break;}
            case 2: {devCardColour = DevCardColour.BLUE;break;}
            case 3: {devCardColour = DevCardColour.YELLOW;break;}
            case 4: {devCardColour = DevCardColour.PURPLE;break;}
        }
        Player player = game.getPlayerByNickname(nickname);
        devCardChosen = player.chooseDevCard(game.getBoard(), finalLevel, devCardColour, finalSlotToPut);
        Resource discountPowerOne = player.getDiscountPowerOne();
        Resource discountPowerTwo = player.getDiscountPowerTwo();
        cli.devCard(devCardChosen, finalSlotToPut, discountPowerOne, discountPowerTwo);
    }

    public void PayDevCard(Boolean[] isWarehouse, Integer[] shelfLevel, Resource[] resourceType, DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo) {
        List<LeaderCard> Leaders = game.getPlayerByNickname(nickname).getLeaderCards();

        Player player =  game.getPlayerByNickname(nickname);

        boolean success = player.buyDevCard(devCard, resourceType,isWarehouse, shelfLevel, slotToPut);

        cli.devCardResponse(success, "payDevCard", devCard,slotToPut, discountPowerOne, discountPowerTwo);

        if(success) {
           cli.afterReorder(1, Leaders);  //l'ho chiamata dopo buy market, quindi finita la main move
        }
    }

    public void ProductionPowerActivation() {
        Player player =  game.getPlayerByNickname(nickname);
        List<LeaderCard> Leaders = game.getPlayerByNickname(nickname).getLeaderCards();
        boolean success = player.activateProductionPowers();
        cli.productionPowerResponse(success, "activation", null);
        cli.afterReorder(1, Leaders);
    }

    public void ProductionPowerList(List<ProductionPower> productionPower, String productionPowerChosen) {
        if(productionPowerChosen.equals("productionPowerChosen")){
            productionPowerCheck(productionPower.get(0), nickname);
        }
    }
    public void productionPowerCheck (ProductionPower productionPower, String nickName) {
        Player player =  game.getPlayerByNickname(nickName);
        if( !player.canAfford(productionPower.getResourceToPayAsMap()) && productionPower.isLeaderProductionPower() ) {
            for (ProductionPower prodPower : player.getDevCardDashboard().getLeaderProductionPowerList()) {
                if(prodPower.equals(productionPower)){
                    prodPower.resetLeaderProductionPower();
                }
            }
        }
        cli.productionPowerResponse(player.canAfford(productionPower.getResourceToPayAsMap()), "productionPowerCheck", productionPower);
    }

    public void PayProductionPower(Boolean[] isWarehouse, Integer[] shelfLevel, Resource[] resourceType, ProductionPower productionPower) {
        Player player =  game.getPlayerByNickname(nickname);
        boolean success = player.payProductionPower(productionPower, isWarehouse, shelfLevel, resourceType);

        if (success) {
            cli.productionPowerResponse(true, "payProductionPower", productionPower);
        }
        else {
            player.rejectProductionPower(productionPower);
            cli.productionPowerResponse(false, "payProductionPower", productionPower);
        }
    }

    public void ProductionPowerResource(Resource resourceChosen, ProductionPower ProductionPower) {
        Player player =  game.getPlayerByNickname(nickname);
        for (ProductionPower productionPower : player.getDevCardDashboard().getLeaderProductionPowerList()) {
            if(productionPower.getResourceToPay().equals((ProductionPower.getResourceToPay()))){
                if(productionPower.isLeaderProductionPower()){
                    boolean success = productionPower.setLeaderProductionPowerResourceToReceive(resourceChosen);
                    cli.productionPowerResponse(success, "setLeaderProductionPower", ProductionPower);
                }
            }
        }
    }

    public void TwoResourceList(List<Resource> resourcesToPayList, List<Resource> resourceToReceiveList, String setBaseProductionPower) {
        if (setBaseProductionPower.equals("setBaseProductionPower")) {
            Player player =  game.getPlayerByNickname(nickname);
            ProductionPower baseProductionPower = player.getDevCardDashboard().getProductionPower(0);
            List<Resource> resourceToPay = resourcesToPayList;
            List<Resource> resourceToReceive = resourceToReceiveList;

            if( ! baseProductionPower.setBaseProductionPowerLists(resourceToPay, resourceToReceive) ) {
                cli.productionPowerResponse(false, "setBaseProductionPower", baseProductionPower);
                baseProductionPower.resetLeaderProductionPower();
            }
            else {
                if (player.canAfford(baseProductionPower.getResourceToPayAsMap())) {
                    cli.productionPowerResponse(true, "setBaseProductionPower", baseProductionPower);
                }
                else{
                    cli.productionPowerResponse(false, "setBaseProductionPower", baseProductionPower);
                    baseProductionPower.resetBaseProductionPower();
                }

            }
        }
    }
    public void continueGame(){
            boolean bool = false;
            Player player =  game.getPlayerByNickname(nickname);
            if(game.lawrenceIsTheWinner()){
                cli.endGameSinglePlayer(player.getPV(), game.getLawrenceFaithPath().getCrossPosition(), false);
                bool = true;
            }
            else if(game.SinglePlayerIsTheWinner()){
                cli.endGameSinglePlayer(player.getPV(), game.getLawrenceFaithPath().getCrossPosition(), true);
                bool = true;
            }if(bool == false){
                cli.broadcastGenericMessage(game.drawActionToken());
            }
        newTurn();
    }
}
