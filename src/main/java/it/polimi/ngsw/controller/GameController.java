package it.polimi.ngsw.controller;

import it.polimi.ngsw.network.server.Server;
import it.polimi.ngsw.model.*;
import it.polimi.ngsw.view.*;
import it.polimi.ngsw.model.GameState;
import it.polimi.ngsw.network.message.*;
import it.polimi.ngsw.observer.Observer;

import java.io.Serializable;
import java.util.*;

import static it.polimi.ngsw.network.message.MessageType.PLAYERNUMBER_REPLY;

/**
 * This class controls the evolution of the {@link Game}.
 * Messages are read and responses are elaborated.
 */
public class GameController implements Observer, Serializable {
    private static final long serialVersionUID = 4951303731052728724L;
    private Game game;
    private transient Map<String, VirtualView> virtualViewMap;
    private GameState gameState;
    private TurnController turnController;
    private static final String STR_INVALID_STATE = "Invalid game state!";
    private int contSituation;
    private int firstPlayerPosition;
    private int remainingTurn;
    private boolean isGameEnded;

    /**
     * Controller of the Game.
     */
    public GameController() {
        initGameController();
    }

    /**
     * Initialize Game Controller.
     */
    public void initGameController() {
        this.game = new Game();
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
        this.contSituation = 0;
        this.firstPlayerPosition = 0;
        this.isGameEnded = false;
        setGameState(GameState.LOGIN);
    }

    /**
     * Set the State of the current Game.
     *
     * @param gameState State of the current Game.
     */
    private void setGameState(GameState gameState) {
        this.gameState = gameState;
    }




    /**
     * Check if a nickname is valid or not.
     *
     * @param nickname new client's nickname.
     * @param view     view for active client.
     * @return {code @true} if it's a valid nickname {code @false} otherwise.
     */
    public boolean checkLoginNickname(String nickname, View view) {
        if (nickname.isEmpty() || nickname.equalsIgnoreCase(Game.SERVER_NICKNAME)) {
            view.showGenericMessage("Forbidden name.");
            view.showLoginResult(false, true, null);
            return false;
        } else if (game.isNicknameTaken(nickname)) {
            view.showGenericMessage("Nickname already taken.");
            view.showLoginResult(false, true, null);
            return false;
        }
        return true;
    }

    /**
     * Checks if the game is already started, then no more players can connect.
     *
     * @return {@code true} if the game isn't started yet, {@code false} otherwise.
     */
    public boolean isGameStarted() {
        return this.gameState != GameState.LOGIN;
    }

    /**
     * Return Turn Controller of the Game.
     *
     * @return Turn Controller of the Game.
     */
    public TurnController getTurnController() {
        return turnController;
    }



    /**
     * Handles the login of a player. If the player is new, his VirtualView is saved, otherwise it's discarded
     * and the player is notified.
     * If it's the first Player then ask number of Players he wants, add Player to the Game otherwise change the GameState.
     *
     * @param nickname    the nickname of the player.
     * @param virtualView the virtualview of the player.
     */
    public void loginHandler(String nickname, VirtualView virtualView) {

        if (virtualViewMap.isEmpty()) { // First player logged. Ask number of players.
            addVirtualView(nickname, virtualView);
            game.addPlayer(new Player(nickname));
            virtualView.showLoginResult(true, true, Game.SERVER_NICKNAME);
            virtualView.askPlayersNumber();
        } else if (virtualViewMap.size() < game.getChosenPlayersNumber() && game.getChosenPlayersNumber() > 1) {
            addVirtualView(nickname, virtualView);
            game.addPlayer(new Player(nickname));
            virtualView.showLoginResult(true, true, Game.SERVER_NICKNAME);
            if (game.getNumCurrentPlayers() == game.getChosenPlayersNumber()) { // If all players logged
                    initGame();
            }
        }else {
            virtualView.showLoginResult(true, false, Game.SERVER_NICKNAME);
        }

    }

    /**
     * This method start the solo game
     */
    private void initSoloGame(){
        setGameState(GameState.INIT);
        turnController = new TurnController(virtualViewMap, this, game);
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.askLeaderCard(game.removeAndReturnTheLastFourLeaderCards());
    }

    /**
     * Check if message is sent from active player.
     *
     * @param receivedMessage message from client.
     * @return {@code true} if correct {@code false} otherwise.
     */
    public boolean checkUser(Message receivedMessage) {
        return receivedMessage.getNickname().equals(getTurnController().getActivePlayer());
    }

    /**
     * Change gameState into INIT. Initialize TurnController and asks a player to pick the leadercards
     */
    private void initGame() {
        setGameState(GameState.INIT);

        turnController = new TurnController(virtualViewMap, this, game);
        broadcastGenericMessage("All Players are connected. " + turnController.getActivePlayer()
                + " is choosing two leadercards ");
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        this.contSituation++;
        virtualView.askLeaderCard(game.removeAndReturnTheLastFourLeaderCards());
    }

    /**
     * Switch on Game State.
     *
     * @param receivedMessage Message from Active Player.
     */
    public void onMessageReceived(Message receivedMessage) {

        VirtualView virtualView = virtualViewMap.get(receivedMessage.getNickname());
        switch (gameState) {
            case LOGIN:
                loginState(receivedMessage);
                break;
            case INIT:
                initState(receivedMessage, virtualView);
                break;
            case IN_GAME:
                if (this.checkUser(receivedMessage)) {
                    inGameState(receivedMessage);
                }
                break;
            default: // Should never reach this condition
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }

    /**
     * Switch on Login Messages' Types.
     *
     * @param receivedMessage Message from Active Player.
     */
    private void loginState(Message receivedMessage) {
        if (receivedMessage.getMessageType() == PLAYERNUMBER_REPLY) {
            game.setChosenMaxPlayers(((PlayerNumberReply) receivedMessage).getPlayerNumber());
            if(((PlayerNumberReply) receivedMessage).getPlayerNumber()>1){
                broadcastGenericMessage("Waiting for other Players . . .");
            }
            else if (((PlayerNumberReply) receivedMessage).getPlayerNumber()==1){
                broadcastGenericMessage("Solo mode.");
            }
            if(game.getChosenPlayersNumber()==1) {
                game.initLawrenceFaithPath();
                initSoloGame();
            }
        } else {
            Server.LOGGER.warning("Wrong message received from client.");
        }
    }


    /**
     * Switch on initialization's Messages' Types.
     *
     * @param receivedMessage Message from Active Player.
     * @param virtualView     Virtual View the Active PLayer.
     */
    private void initState(Message receivedMessage, VirtualView virtualView) {
        switch (receivedMessage.getMessageType()) {
            case LEADERCARDREQUEST:
                    leaderCardHandler((LeaderCardListMessage) receivedMessage);
                break;
            case PICK_FIRST_PLAYER:
                    pickFirstPlayerHandler(((FirstPlayerMessage) receivedMessage).getActivePlayerNickname());
                break;
            case PICK_INITIAL_RESOURCES:
                    distribuiteResourceHandler((DistribuiteInitialResourcesMessage) receivedMessage);
                break;
            default:
                Server.LOGGER.warning("Noooot Valid");
                break;
        }
    }

    /**
     * Manage the two leader card chosen from the plkayer
     * @param receivedMessage the message with the two leadercard
     */
    private  void leaderCardHandler(LeaderCardListMessage receivedMessage){
        game.getPlayerByNickname(receivedMessage.getNickname()).setLeaderCard(receivedMessage.getLeaderCardList());
        turnController.next();
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        this.contSituation++; // parte da 1, passa a 2 e chiede al secondo le leadercard, diventa tre e chiede al terzo
        if(contSituation <= game.getChosenPlayersNumber() && game.getChosenPlayersNumber() > 1){
            broadcastGenericMessage(turnController.getActivePlayer()
                    + " is choosing two leadercards \n");
            virtualView.askLeaderCard(game.removeAndReturnTheLastFourLeaderCards());
        }
        if(contSituation == game.getChosenPlayersNumber() + 1 && game.getChosenPlayersNumber() > 1){
            this.contSituation = 0;
            broadcastGenericMessage(turnController.getActivePlayer()
                    + " is choosing the first player that will start \n");
            virtualView.askFirstPlayer(turnController.getNicknameQueue());
        }
        else if(game.getChosenPlayersNumber() == 1){
            startGame();
        }
    }

    /**
     * Handles the first player.
     *
     * @param firstPlayerNick first player choosen by the starter player.
     */
    private void pickFirstPlayerHandler(String firstPlayerNick) {
        turnController.setActivePlayer(firstPlayerNick);
        for(int i=0; i < game.getChosenPlayersNumber(); i++){
            if(firstPlayerNick == turnController.getNicknameQueue().get(i)){
                this.firstPlayerPosition = i+1; //questo prende la posizione del primo giocatore
            }
        }
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is the first player", turnController.getActivePlayer());
        VirtualView virtualView;
        this.contSituation = 0;
        contSituation++; // va a 1
        if(game.getChosenPlayersNumber() > 1){
            contSituation++; // va a 2
            turnController.next();
            broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is chosing his initial resources", turnController.getActivePlayer());
            virtualView = virtualViewMap.get(turnController.getActivePlayer());
            virtualView.distributeInitialResources(1);
        }// caso multiplayer
        else{contSituation = 0;}
    }

    /**
     * Distribuite the resources of the first part before the game
     * @param message the message with the resources
     */
    private void distribuiteResourceHandler(DistribuiteInitialResourcesMessage message){
        Player player = game.getPlayerByNickname(message.getNickname());
        player.getWarehouse().addResourceToWarehouse(message.getFirstPosition(), message.getFirstResource()); //contSituation arriva prima volta con valore 2
        if(message.getSecondPosition() > 0){
            player.getWarehouse().addResourceToWarehouse(message.getSecondPosition(), message.getSecondResource());
        }
        turnController.next();
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        this.contSituation++;
        if(this.contSituation <= game.getChosenPlayersNumber() && this.contSituation == 3){
            broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is chosing his initial resources", turnController.getActivePlayer());
            virtualView.distributeInitialResources(1);
            game.getPlayerByNickname(turnController.getActivePlayer()).getFaithPath().increaseCrossPosition();
        }
        else if(contSituation == game.getChosenPlayersNumber() && contSituation == 4){
            broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is chosing his initial resources", turnController.getActivePlayer());
            virtualView.distributeInitialResources(2);
            game.getPlayerByNickname(turnController.getActivePlayer()).getFaithPath().increaseCrossPosition();
        }
        if(contSituation == game.getChosenPlayersNumber() + 1){
            contSituation = 0;
            startGame();
        }
    }
    /**
     * Start the game
     */
    private void startGame() {
        setGameState(GameState.IN_GAME);
        broadcastGenericMessage("Game Started!");   //aggiungi su client controller nickname scelto
        turnController.newTurn();
    }

    /**
     * Continue the game, so pass the action to the next player
     */
    private void continueGame(){
        if(game.getChosenPlayersNumber()==1){
            boolean bool = false;
            VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
            Player player =  game.getPlayerByNickname(turnController.getActivePlayer());
            if(game.lawrenceIsTheWinner()){
                virtualView.endGameSinglePlayer(player.getPV(), game.getLawrenceFaithPath().getCrossPosition(), false);
                bool = true;
                endGame();
            }
            else if(game.SinglePlayerIsTheWinner()){
                virtualView.endGameSinglePlayer(player.getPV(), game.getLawrenceFaithPath().getCrossPosition(), true);
                bool = true;
                endGame();
            }if(bool == false){
            broadcastGenericMessage("\nLawrence Turn");
            broadcastGenericMessage(game.drawActionToken());
            broadcastGenericMessage("Lawrence cross position: " + (game.getLawrenceFaithPath().getCrossPosition()));
            }
        }
        turnController.next();
        turnController.newTurn();
    }

    /**
     * Switch on Game Messages' Types.
     *
     * @param receivedMessage Message from Active Player.
     */
    private void inGameState(Message receivedMessage) {
        switch (receivedMessage.getMessageType()) {
            case LEADER_CARD_RESPONSE:
                activateLeaderCard(receivedMessage);
                break;
            case DISCARD_CARD:
                discardCard(receivedMessage);
                break;
            case BUY_MARKET:
                buyFromMarket(receivedMessage);
                break;
            case REORDER_WAREHOUSE:
                reorderWarehouseGetter(receivedMessage);
                break;
            case NEW_WAREHOUSE:
                newWarehouse(receivedMessage);
                break;
            case END_TURN:
                continueGame();
                break;
            case INTEGER:
                integerChosenAction((IntegerMessage) receivedMessage);
                break;
            case TWO_LIST_OF_RESOURCES:
                twoResourceListAction((TwoResourceListMessage) receivedMessage);
                break;
            case PRODUCTION_POWER_LIST:
                productionPowerListAction((ProductionPowerListMessage) receivedMessage);
                break;
            case PRODUCTION_POWER_COORDINATES_MESSAGE:
                payProductionPower((ProductionPowerCoordinatesMessage) receivedMessage);
                break;
            case DEVCARD_COORDINATES_MESSAGE:
                payDevCard((DevCardCoordinatesMessage) receivedMessage);
                break;
            case PRODUCTION_POWER_RESOURCE:
                setLeaderProductionPower((ProductionPowerResourceMessage) receivedMessage);
                break;
            case PRODUCTION_POWER_RESPONSE_MESSAGE:
                break;
            case ACTIVATE_PRODUCTION_POWERS:
                activateProductionPowers((ActivateProductionPowersMessage) receivedMessage);
                break;
            case CHOSENDEVCARD:
                chooseDevCardToPay((ChosenDevCardMessage) receivedMessage);
                break;
            case WATCH_OTHER_PLAYER:
                watchOtherInfo((WatchOtherPlayerInfoMessage) receivedMessage);
                break;
            case CALCULATE_PV_WIN:
                calculatePVWin();
                break;
            case ASK_FOR_FAITH_PATH:
                askForFaithPath((AskForFaithPathMessage) receivedMessage);
                break;
            default:
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }

    /**
     * Calculate the PV of the players and return the winner
     */
    private void calculatePVWin() {
        int PVwinner = -1;
        String NameWinner = "none";
        for(int i=0; i<game.getChosenPlayersNumber(); i++){
            if(game.getPlayerByNickname(turnController.getNicknameQueue().get(i)).getFaithPath().getPV() > PVwinner) {
                PVwinner =  game.getPlayerByNickname(turnController.getNicknameQueue().get(i)).getFaithPath().getPV();
                NameWinner = turnController.getNicknameQueue().get(i);
            }
        }
        broadcastGenericMessage("The winner is : " + NameWinner + " with " + PVwinner + " PV ");

        Server.LOGGER.info("Game finished. Server ready for a new Game.");
    }

    /**
     * Take all the info of the other player that the activeplayer wants to know about
     * @param receivedMessage the message
     */
    private void watchOtherInfo(Message receivedMessage) {
        List<String> players = turnController.getNicknameQueue();
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        boolean goneRight = false;
        Player otherPlayer;
        if (players.contains(((WatchOtherPlayerInfoMessage) receivedMessage).getNicknameOtherPlayer())) {
            goneRight = true;
        }
        if(goneRight == true){
            otherPlayer = game.getPlayerByNickname(((WatchOtherPlayerInfoMessage) receivedMessage).getNicknameOtherPlayer());
            int[] ShelfResNumber = new int []{-1,-1,-1,-1,-1};
            Resource[] ShelfResType = new Resource []{Resource.EMPTY,Resource.EMPTY,Resource.EMPTY,Resource.EMPTY,Resource.EMPTY };
            ShelfResNumber[0] = otherPlayer.getWarehouse().getShelf(1).getResourceNumber();ShelfResType[0] = otherPlayer.getWarehouse().getShelf(1).getResourceType();
            ShelfResNumber[1] = otherPlayer.getWarehouse().getShelf(2).getResourceNumber();ShelfResType[1] = otherPlayer.getWarehouse().getShelf(2).getResourceType();
            ShelfResNumber[2] = otherPlayer.getWarehouse().getShelf(3).getResourceNumber();ShelfResType[2] = otherPlayer.getWarehouse().getShelf(3).getResourceType();
            if(otherPlayer.getWarehouse().getShelf(4) != null){ShelfResNumber[3] = otherPlayer.getWarehouse().getShelf(4).getResourceNumber();ShelfResType[3] = otherPlayer.getWarehouse().getShelf(4).getResourceType();}
            if(otherPlayer.getWarehouse().getShelf(5) != null){ShelfResNumber[4] = otherPlayer.getWarehouse().getShelf(5).getResourceNumber();ShelfResType[4] = otherPlayer.getWarehouse().getShelf(5).getResourceType();}
            virtualView.viewOtherPlayer(((WatchOtherPlayerInfoMessage) receivedMessage).getNicknameOtherPlayer(), goneRight, otherPlayer.getFaithPath().getCrossPosition(), otherPlayer.getChest().getResourcesAsMap(),otherPlayer.getDevCardDashboard().getActiveDevCards(), ShelfResNumber, ShelfResType);
        }
        else virtualView.viewOtherPlayer(((WatchOtherPlayerInfoMessage) receivedMessage).getNicknameOtherPlayer(), goneRight, 0, null,null, null, null);
    }

    /**
     * This method manage the creation of the new warehoouse of the player
     * @param receivedMessage the message
     */
    private void newWarehouse(Message receivedMessage) {
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " has formed his new warehouse ", turnController.getActivePlayer());
        if(((NewWarehouseMessage) receivedMessage).getDiscardList() != null) {
            for (int i = 0; i < ((NewWarehouseMessage) receivedMessage).getDiscardList().size(); i++) {
                game.increaseOtherFaithPoints(game.getPlayerByNickname(turnController.getActivePlayer()), 1);
            }
            broadcastGenericMessage("You received " + ((NewWarehouseMessage) receivedMessage).getDiscardList().size() + " faith point", turnController.getActivePlayer());
        }
        game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().newFirstShelf(((NewWarehouseMessage) receivedMessage).getNewFirstShelf());

        if (((NewWarehouseMessage) receivedMessage).getNewSecondShelf() != null && !((NewWarehouseMessage) receivedMessage).getNewSecondShelf().isEmpty()) {
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().newSecondShelf(((NewWarehouseMessage) receivedMessage).getNewSecondShelf());
        }

        if (((NewWarehouseMessage) receivedMessage).getNewSecondShelf() == null || ((NewWarehouseMessage) receivedMessage).getNewSecondShelf().isEmpty()) {
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getShelf(2).setResourceType(Resource.EMPTY);
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getShelf(2).setResourceNumber(0);
        }


        if (((NewWarehouseMessage) receivedMessage).getNewThirdShelf() != null && !((NewWarehouseMessage) receivedMessage).getNewThirdShelf().isEmpty()) {
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().newThirdShelf(((NewWarehouseMessage) receivedMessage).getNewThirdShelf()); }

        if (((NewWarehouseMessage) receivedMessage).getNewThirdShelf() == null || ((NewWarehouseMessage) receivedMessage).getNewThirdShelf().isEmpty()) {
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getShelf(3).setResourceType(Resource.EMPTY);
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getShelf(3).setResourceNumber(0);
        }

        if (((NewWarehouseMessage) receivedMessage).getNewFirstSpecialShelf() != null && !((NewWarehouseMessage) receivedMessage).getNewFirstSpecialShelf().isEmpty()) {
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().newFirstSpecialShelf(((NewWarehouseMessage) receivedMessage).getNewFirstSpecialShelf());
        }

        if ((game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getShelf(4) != null) && (((NewWarehouseMessage) receivedMessage).getNewFirstSpecialShelf() == null || ((NewWarehouseMessage) receivedMessage).getNewFirstSpecialShelf().isEmpty())) {
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getShelf(4).setResourceNumber(0);
        }

        if (((NewWarehouseMessage) receivedMessage).getNewSecondSpecialShelf() != null && !((NewWarehouseMessage) receivedMessage).getNewSecondSpecialShelf().isEmpty()) {
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().newSecondSpecialShelf(((NewWarehouseMessage) receivedMessage).getNewSecondSpecialShelf()); }

        if ((game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getShelf(5) != null )&&(((NewWarehouseMessage) receivedMessage).getNewSecondSpecialShelf() == null || ((NewWarehouseMessage) receivedMessage).getNewSecondSpecialShelf().isEmpty())) {
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getShelf(5).setResourceNumber(0);
        }

        game.getPlayerByNickname(receivedMessage.getNickname()).getWarehouse().removeAllStock();

        List<LeaderCard> Leaders = game.getPlayerByNickname(turnController.getActivePlayer()).getLeaderCards();

        if(((NewWarehouseMessage) receivedMessage).getIsIndependent() == true){ //se io l'ho chiamata all'inizio, prima di qualsiasi mossa (buy market)
            virtualView.afterReorder(0, Leaders);
        }
        else{
            if(this.isGameEnded == true){
                this.remainingTurn--; //abbassa i turni rimanenti di uno
                if(this.remainingTurn == 0){
                    virtualView.afterLastMainMove(1, Leaders); //se sono l'ultimo giocatore, allora finisco la parte leader card
                }else{
                    virtualView.afterReorder(1, Leaders);  // se non sono l'ultimo
                }
            }
            else if(game.isGameEndedMultiPlayers() == true && this.isGameEnded == false ){  //è il primo ad arrivarci, mette il fatto che il gioco sia in fase di ended
                int thisPlayerPosition = -1;
                this.isGameEnded = true;
                broadcastGenericMessage("THE GAME IS ALMOST FINISHED ");
                for(int i=0; i < game.getChosenPlayersNumber(); i++){
                    if(turnController.getActivePlayer() == turnController.getNicknameQueue().get(i)) thisPlayerPosition = i +1;
                }
                if(this.firstPlayerPosition <= thisPlayerPosition){
                    this.remainingTurn = this.firstPlayerPosition - thisPlayerPosition + (game.getChosenPlayersNumber() - 1);
                }else{
                    this.remainingTurn = this.firstPlayerPosition - thisPlayerPosition - 1;
                }
                if(this.remainingTurn == 0){virtualView.afterLastMainMove(1,Leaders);} //vuol dire che subito il gioco termina perchè dopo di me c'è il primo giocatore
                else {
                    virtualView.afterReorder(1, Leaders);  //l'ho chiamata dopo buy market, quindi finita la main move
                }
            }
            else{
                virtualView.afterReorder(1, Leaders);  //l'ho chiamata dopo buy market, quindi finita la main move
            }
        }
    }

    /**
     * Receive the action of the reordering of the warehouse
     * @param receivedMessage
     */
    private void reorderWarehouseGetter(Message receivedMessage) {
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is reordering his warehouse", turnController.getActivePlayer());
        game.getPlayerByNickname(receivedMessage.getNickname()).getWarehouse().removeAllStock();
        virtualView.reorderWarehouse(game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getResourcesAsMap(), game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getLeaderLevelType(1), game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getLeaderLevelType(2), ((ReorderWarehouseMessage) receivedMessage).getIsIndependent());
    }

    /**
     * Activates the chosen leadercard, it returns if the action went well or not
     * @param received the received message
     */
    public void activateLeaderCard(Message received){
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        if(game.getPlayerByNickname(turnController.getActivePlayer()).activeLeaderCard(((LeaderCardActivationMessage) received).getCardChosen())){
            if(((LeaderCardActivationMessage) received).getTurnZone() == 1){
                broadcastGenericMessage("The player " + turnController.getActivePlayer() + " has activated a leadercard", turnController.getActivePlayer());
                virtualView.continueTurn(1,1,1, ((LeaderCardActivationMessage) received).getCardChosen(), game.getPlayerByNickname(turnController.getActivePlayer()).getLeaderCards());   //inizio turno, tipo leadercard, andato a segno
            }
            else if(((LeaderCardActivationMessage) received).getTurnZone() == 2){
                broadcastGenericMessage("The player " + turnController.getActivePlayer() + " has activated a leadercard", turnController.getActivePlayer());
                virtualView.continueTurn(2,1,1, ((LeaderCardActivationMessage) received).getCardChosen(), game.getPlayerByNickname(turnController.getActivePlayer()).getLeaderCards());   //fine turno, tipo leadercard, andato a segno
            }
        }
        else{
            if(((LeaderCardActivationMessage) received).getTurnZone() == 1)virtualView.continueTurn(1,1,0, ((LeaderCardActivationMessage) received).getCardChosen(), game.getPlayerByNickname(turnController.getActivePlayer()).getLeaderCards());   //inizio turno, tipo leadercard, non andato a segno
            else if(((LeaderCardActivationMessage) received).getTurnZone() == 2)virtualView.continueTurn(2,1,0, ((LeaderCardActivationMessage) received).getCardChosen(), game.getPlayerByNickname(turnController.getActivePlayer()).getLeaderCards());   //fine turno, tipo leadercard, non andato a segno
        }
    }

    /**
     * Discard the card, it goes well always
     * @param received the message
     */
    public void discardCard(Message received){
        game.getPlayerByNickname(received.getNickname()).getFaithPath().increaseCrossPosition();
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " has discarded a leadercard, he gained a faith point", turnController.getActivePlayer());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.continueTurn(((DiscardLeaderMessage) received).getTurnZone(),2,1, ((DiscardLeaderMessage) received).getCardChosen(), null);
    }

    /**
     * Buy from the market given the sector and wich column/row
     * @param received the message with all the info
     */
    public void buyFromMarket(Message received){
        Player activePlayer = game.getPlayerByNickname(received.getNickname());
        game.buyFromMarket(((BuyFromMarketMessage) received).getRowOrColumn(),((BuyFromMarketMessage) received).getWichOne(), activePlayer);
        game.manageWhiteResources(activePlayer);
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " has bought from the market and now is managing his resources", turnController.getActivePlayer());
        VirtualView virtualView = virtualViewMap.get(received.getNickname()); //arrivo che ho una lista di risorse (anche white) nello stock, prima cosa è dare valore a tutte,
        virtualView.buyMarketResource(game.getPlayerByNickname(received.getNickname()).getWarehouse().getWarehouseStock(),game.getPlayerByNickname(received.getNickname()).getWhiteMarblePowerOne(), game.getPlayerByNickname(received.getNickname()).getWhiteMarblePowerTwo() );
    }
    /**
     * Adds a Player VirtualView to the controller if the first player max_players is not exceeded.
     * Then adds a controller observer to the view.
     * Adds the VirtualView to the game and board model observers.
     *
     * @param nickname    the player nickname to identify his associated VirtualView.
     * @param virtualView the VirtualView to be added.
     */
    public void addVirtualView(String nickname, VirtualView virtualView) {
        virtualViewMap.put(nickname, virtualView);
        game.addObserver(virtualView);
    }



    /**
     * Sends a Message which contains Game Information to all players but the one specified in the second argument.
     *
     * @param messageToNotify Message to send.
     * @param excludeNickname name of the player to be excluded from the broadcast.
     */
    public void broadcastGenericMessage(String messageToNotify, String excludeNickname) {
        virtualViewMap.entrySet().stream()
                .filter(entry -> !excludeNickname.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(vv -> vv.showGenericMessage(messageToNotify));
    }

    /**
     * Sends a Message which contains Game Information to every {@link Player} in Game.
     *
     * @param messageToNotify Message to send.
     */
    public void broadcastGenericMessage(String messageToNotify) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showGenericMessage(messageToNotify);
        }
    }

    /**
     * Removes a VirtualView from the controller.
     *
     * @param nickname      the nickname of the VirtualView associated.
     * @param notifyEnabled set to {@code true} to enable a lobby disconnection message, {@code false} otherwise.
     */
    public void removeVirtualView(String nickname, boolean notifyEnabled) {
        VirtualView vv = virtualViewMap.remove(nickname);

        game.removeObserver(vv);
        game.removePlayerByNickname(nickname, notifyEnabled);
    }

    /**
     * Sends a broadcast disconnection message.
     *
     * @param nicknameDisconnected user who had a connection drop.
     * @param text                 generic text.
     */
    public void broadcastDisconnectionMessage(String nicknameDisconnected, String text) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showDisconnectionMessage(nicknameDisconnected, text);
        }
    }


    /**
     * Receives an update message from the effect model.
     *
     * @param message the update message.
     */
    @Override
    public void update(Message message) {
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        switch (message.getMessageType()) {
            case ERROR:
                ErrorMessage errMsg = (ErrorMessage) message;
                Server.LOGGER.warning(errMsg.getError());
                break;
            default:
                Server.LOGGER.warning("Invalid!");
                break;
        }

    }
    /**
     * Reset the Game Instance and re-initialize GameController Class.
     */
    public void endGame() {
        initGameController();
        Server.LOGGER.info("Game finished. Server ready for a new Game.");
    }


    /**
     * This method activates all the production power selected
     * @param message the mesage with the production powers
     */
    public void activateProductionPowers(ActivateProductionPowersMessage message){
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        Player player =  game.getPlayerByNickname(message.getNickname());
        this.broadcastGenericMessage("The player activated his Production Powers:", message.getNickname());
        for(ProductionPower productionPower : player.getPaidList()){
            this.broadcastGenericMessage(productionPower.getResourceToPay().toString() + " -> " + productionPower.getResourceToReceive().toString(), message.getNickname());
        }
        List<LeaderCard> Leaders = game.getPlayerByNickname(turnController.getActivePlayer()).getLeaderCards();
        boolean success = player.activateProductionPowers();
        virtualView.productionPowerResponse(success, "activation", null);

        if(this.isGameEnded == true){
            this.remainingTurn--; //abbassa i turni rimanenti di uno
            if(this.remainingTurn == 0){
                virtualView.afterLastMainMove(1, Leaders); //se sono l'ultimo giocatore, allora finisco la parte leader card
            }else{
                virtualView.afterReorder(1, Leaders);  // se non sono l'ultimo
            }
        }
        else if(game.isGameEndedMultiPlayers() == true && this.isGameEnded == false){  //è il primo ad arrivarci, mette il fatto che il gioco sia in fase di ended
            int thisPlayerPosition = -1;
            this.isGameEnded = true;
            broadcastGenericMessage("THE GAME IS ALMOST FINISHED ");

            for(int i=0; i < game.getChosenPlayersNumber(); i++){
                if(turnController.getActivePlayer() == turnController.getNicknameQueue().get(i)) thisPlayerPosition = i +1;
            }
            if(this.firstPlayerPosition <= thisPlayerPosition){
                this.remainingTurn = this.firstPlayerPosition - thisPlayerPosition + (game.getChosenPlayersNumber() - 1);
            }else{
                this.remainingTurn = this.firstPlayerPosition - thisPlayerPosition - 1;
            }
            if(this.remainingTurn == 0){virtualView.afterLastMainMove(1,Leaders);} //vuol dire che subito il gioco termina perchè dopo di me c'è il primo giocatore
            else {
                virtualView.afterReorder(1, Leaders);  //l'ho chiamata dopo buy market, quindi finita la main move
            }
        }
        else{
            virtualView.afterReorder(1, Leaders);  //l'ho chiamata dopo buy market, quindi finita la main move
        }
    }

    /**
     * Manage the list of the of the production powers
     * @param receivedMessage the message with all the info needed
     */
    public void productionPowerListAction (ProductionPowerListMessage receivedMessage) {
        if(receivedMessage.getAction().equals("productionPowerChosen")){
            productionPowerCheck(receivedMessage.getProductionPowerList().get(0), receivedMessage.getNickname());
        }
    }

    /**
     * Makes a check if you can activate the production power
     * @param productionPower the production power to be checked
     * @param nickName the player
     */
    public void productionPowerCheck (ProductionPower productionPower, String nickName) {
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        Player player =  game.getPlayerByNickname(nickName);
        if( !player.canAfford(productionPower.getResourceToPayAsMap()) && productionPower.isLeaderProductionPower() ) {
            for (ProductionPower prodPower : player.getDevCardDashboard().getLeaderProductionPowerList()) {
                if(prodPower.equals(productionPower)){
                    prodPower.resetLeaderProductionPower();
                }
            }
        }
        virtualView.productionPowerResponse(player.canAfford(productionPower.getResourceToPayAsMap()), "productionPowerCheck", productionPower);
    }

    /**
     * A method created without sense
     * @param receivedMessage the message
     */
    public void integerChosenAction (IntegerMessage receivedMessage) {
    }

    /**
     * Receive two resources to be payed and two to receive
     * @param receivedMessage
     */
    public void twoResourceListAction (TwoResourceListMessage receivedMessage) {
        if (receivedMessage.getAction().equals("setBaseProductionPower")) {
            setBaseProductionPower(receivedMessage);
        }
    }

    /**
     * Set the production power of the leader skills
     * @param receivedMessage the message with all the info
     */
    public void setLeaderProductionPower(ProductionPowerResourceMessage receivedMessage){

        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        Player player =  game.getPlayerByNickname(receivedMessage.getNickname());
        for (ProductionPower productionPower : player.getDevCardDashboard().getLeaderProductionPowerList()) {
            if(productionPower.getResourceToPay().equals((receivedMessage.getProductionPower().getResourceToPay()))){
                if(productionPower.isLeaderProductionPower()){
                    boolean success = productionPower.setLeaderProductionPowerResourceToReceive(receivedMessage.getResource());
                    virtualView.productionPowerResponse(success, "setLeaderProductionPower", receivedMessage.getProductionPower());
                }
            }
        }


    }

    /**
     * Set the base production power of the player
     * @param receivedMessage the received message with all the info
     */
    public void setBaseProductionPower (TwoResourceListMessage receivedMessage) {

        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        Player player =  game.getPlayerByNickname(receivedMessage.getNickname());
        ProductionPower baseProductionPower = player.getDevCardDashboard().getProductionPower(0);
        List<Resource> resourceToPay = receivedMessage.getResourcesToPay();
        List<Resource> resourceToReceive = receivedMessage.getResourcesToReceive();

        if( ! baseProductionPower.setBaseProductionPowerLists(resourceToPay, resourceToReceive) ) {
            virtualView.productionPowerResponse(false, "setBaseProductionPower", baseProductionPower);
            baseProductionPower.resetLeaderProductionPower();
        }
        else {
            if (player.canAfford(baseProductionPower.getResourceToPayAsMap())) {
                virtualView.productionPowerResponse(true, "setBaseProductionPower", baseProductionPower);
            }
            else{
                virtualView.productionPowerResponse(false, "setBaseProductionPower", baseProductionPower);
                baseProductionPower.resetBaseProductionPower();
            }

        }

    }

    /**
     * This method manage the payment of the production power
     * @param receivedMessage the received message
     */
    public void payProductionPower(ProductionPowerCoordinatesMessage receivedMessage) {

        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        Player player =  game.getPlayerByNickname(receivedMessage.getNickname());

        boolean success = player.payProductionPower(receivedMessage.getProductionPower(), receivedMessage.getIsWarehouse(), receivedMessage.getShelfLevel(), receivedMessage.getResourceType());

        if (success) {
            virtualView.productionPowerResponse(true, "payProductionPower", receivedMessage.getProductionPower());
        }
        else {
            player.rejectProductionPower(receivedMessage.getProductionPower());
            virtualView.productionPowerResponse(false, "payProductionPower", receivedMessage.getProductionPower());
        }

    }

    /**
     * Chose the devcard to buy
     * @param receivedMessage the message with all the info
     */
    public void chooseDevCardToPay(ChosenDevCardMessage receivedMessage){
        DevCard devCardChosen;

        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        devCardChosen = player.chooseDevCard(game.getBoard(), receivedMessage.getLevel(), receivedMessage.getDevCardColour(), receivedMessage.getSlotToPut());
        Resource discountPowerOne = player.getDiscountPowerOne();
        Resource discountPowerTwo = player.getDiscountPowerTwo();
        virtualView.devCard(devCardChosen, receivedMessage.getSlotToPut(), discountPowerOne, discountPowerTwo);

    }

    /**
     * This method manage the payment of the devcard
     * @param receivedMessage the message with all the info
     */
    public void payDevCard(DevCardCoordinatesMessage receivedMessage){

        List<LeaderCard> Leaders = game.getPlayerByNickname(turnController.getActivePlayer()).getLeaderCards();

        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        Player player =  game.getPlayerByNickname(receivedMessage.getNickname());

        boolean success = player.buyDevCard(receivedMessage.getDevCard(), receivedMessage.getResourceType(),receivedMessage.getIsWarehouse(), receivedMessage.getShelfLevel(), receivedMessage.getSlotToPut());

        virtualView.devCardResponse(success, "payDevCard", receivedMessage.getDevCard(), receivedMessage.getSlotToPut(), receivedMessage.getDiscountPowerOne(), receivedMessage.getDiscountPowerTwo());

        String string = "the player bought a devCard: level : " + receivedMessage.getDevCard().getDevLevel() + ", colour : " + receivedMessage.getDevCard().getCardColour();
        this.broadcastGenericMessage(string, receivedMessage.getNickname());

        if(success) {
            if(this.isGameEnded == true){
                this.remainingTurn--; //abbassa i turni rimanenti di uno
                if(this.remainingTurn == 0){
                    virtualView.afterLastMainMove(1, Leaders); //se sono l'ultimo giocatore, allora finisco la parte leader card
                }else{
                    virtualView.afterReorder(1, Leaders);  // se non sono l'ultimo
                }
            }
            else if(game.isGameEndedMultiPlayers() == true && this.isGameEnded == false){  //è il primo ad arrivarci, mette il fatto che il gioco sia in fase di ended
                int thisPlayerPosition = -1;
                this.isGameEnded = true;
                broadcastGenericMessage("THE GAME IS ALMOST FINISHED ");
                for(int i=0; i < game.getChosenPlayersNumber(); i++){
                    if(turnController.getActivePlayer() == turnController.getNicknameQueue().get(i)) thisPlayerPosition = i +1;
                }
                if(this.firstPlayerPosition <= thisPlayerPosition){
                    this.remainingTurn = this.firstPlayerPosition - thisPlayerPosition + (game.getChosenPlayersNumber() - 1);
                }else{
                    this.remainingTurn = this.firstPlayerPosition - thisPlayerPosition - 1;
                }
                if(this.remainingTurn == 0){virtualView.afterLastMainMove(1,Leaders);} //vuol dire che subito il gioco termina perchè dopo di me c'è il primo giocatore
                else {
                    virtualView.afterReorder(1, Leaders);  //l'ho chiamata dopo buy market, quindi finita la main move
                }
            }
            else{
                virtualView.afterReorder(1, Leaders);  //l'ho chiamata dopo buy market, quindi finita la main move
            }
        }
    }

    /**
     * Return the game
     * @return the game
     */
    public Game getGame(){
        return game;
    }

    /**
     * method that takes all the info about the faith path
     * @param receivedMessage all the info by message
     */
    public void askForFaithPath(AskForFaithPathMessage receivedMessage){
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        Player player =  game.getPlayerByNickname(receivedMessage.getNickname());
        int crossPosition = player.getFaithPath().getCrossPosition();
        int victoryPoints = player.getFaithPath().getPV();
        boolean papalCardOne = player.getFaithPath().getPapalCardOne();
        boolean papalCardTwo = player.getFaithPath().getPapalCardTwo();
        boolean papalCardThree = player.getFaithPath().getPapalCardThree();
        virtualView.faithPathResponse(crossPosition, victoryPoints, papalCardOne, papalCardTwo, papalCardThree);
    }

}
