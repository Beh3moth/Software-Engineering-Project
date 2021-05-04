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
    private InputController inputController;
    private static final String STR_INVALID_STATE = "Invalid game state!";
    private int contSituation;
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
        this.game = Game.getInstance();
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
        this.inputController = new InputController(virtualViewMap, this);
        this.contSituation = 0;
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
     * Verifies the nickname through the InputController.
     *
     * @param nickname the nickname to be checked.
     * @param view     the view of the player who is logging in.
     * @return see {@link InputController#checkLoginNickname(String, View)}
     */
    public boolean checkLoginNickname(String nickname, View view) {
        return inputController.checkLoginNickname(nickname, view);
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
        } else if (virtualViewMap.size() < game.getChosenPlayersNumber()) {
            addVirtualView(nickname, virtualView);
            game.addPlayer(new Player(nickname));
            virtualView.showLoginResult(true, true, Game.SERVER_NICKNAME);

            if (game.getNumCurrentPlayers() == game.getChosenPlayersNumber()) { // If all players logged
                    initGame();
            }
        } else {
            virtualView.showLoginResult(true, false, Game.SERVER_NICKNAME);
        }
    }


    /**
     * Change gameState into INIT. Initialize TurnController and asks a player to pick the leadercards
     */
    private void initGame() {
        setGameState(GameState.INIT);

        turnController = new TurnController(virtualViewMap, this);
        broadcastGenericMessage("All Players are connected. " + turnController.getActivePlayer()
                + " is choosing two leadercards ");

        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        //for(int i = 0; i < game.getChosenPlayersNumber(); i++) {
        this.contSituation++;
        virtualView.askLeaderCard(game.removeAndReturnTheLastFourLeaderCards());
            //turnController.next();
           // virtualView = virtualViewMap.get(turnController.getActivePlayer());
        //}
        //turnController.next();
        //virtualView = virtualViewMap.get(turnController.getActivePlayer());
        //virtualView.askFirstPlayer(turnController.getNicknameQueue());
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
                if (inputController.checkUser(receivedMessage)) {
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
                broadcastGenericMessage("Waiting for other Players . . .");
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


    private  void leaderCardHandler(LeaderCardListMessage receivedMessage){
        game.getPlayerByNickname(receivedMessage.getNickname()).setLeaderCard(receivedMessage.getLeaderCardList());
        turnController.next();
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        this.contSituation++; // parte da 1, passa a 2 e chiede al secondo le leadercard, diventa tre e chiede al terzo
        if(contSituation <= game.getChosenPlayersNumber()){
            virtualView.askLeaderCard(game.removeAndReturnTheLastFourLeaderCards());
        }
        if(contSituation == game.getChosenPlayersNumber() + 1){
            this.contSituation = 0;
            virtualView.askFirstPlayer(turnController.getNicknameQueue());
        }
    };

    /**
     * Handles the Challenger's choice for the first player.
     *
     * @param firstPlayerNick first player choosen by Challenger.
     */
    private void pickFirstPlayerHandler(String firstPlayerNick) {
        turnController.setActivePlayer(firstPlayerNick);
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " ", turnController.getActivePlayer());
        VirtualView virtualView;
        this.contSituation = 0;
        contSituation++; // va a 1
        if(game.getChosenPlayersNumber() > 1){
            contSituation++; // va a 2
            turnController.next();
            virtualView = virtualViewMap.get(turnController.getActivePlayer());
            virtualView.distribuiteInitialResources(1);
        }// caso multiplayer
        else{contSituation = 0;}
    }


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
            virtualView.distribuiteInitialResources(1);
            game.getPlayerByNickname(turnController.getActivePlayer()).getFaithPath().increaseCrossPosition();
        }
        else if(contSituation == game.getChosenPlayersNumber() && contSituation == 4){
            virtualView.distribuiteInitialResources(2);
            game.getPlayerByNickname(turnController.getActivePlayer()).getFaithPath().increaseCrossPosition();
        }
        if(contSituation == game.getChosenPlayersNumber() + 1){
            contSituation = 0;
            startGame();
        }
    }
    /**
     * Initializes the game after all Clients are connected and all Gods, Workers and Colors are setted up.
     */
    private void startGame() {
        setGameState(GameState.IN_GAME);
        broadcastGenericMessage("Game Started!");   //aggiungi su client controller nickname scelto
        //turnController.broadcastMatchInfo();
        turnController.newTurn();
    }

    private void continueGame(){
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
            default:
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }

    private void newWarehouse(Message receivedMessage) {
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        if(((NewWarehouseMessage) receivedMessage).getDiscardList() != null) {
            for (int i = 0; i < ((NewWarehouseMessage) receivedMessage).getDiscardList().size(); i++) {
                //game.increaseOtherFaithPoints(turnController.getActivePlayer(), 1);
            }
        }
        if(((NewWarehouseMessage) receivedMessage).getNewFirstShelf() != Resource.EMPTY){
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().newFirstShelf(((NewWarehouseMessage) receivedMessage).getNewFirstShelf()); }
        if (((NewWarehouseMessage) receivedMessage).getNewSecondShelf() != null && !((NewWarehouseMessage) receivedMessage).getNewSecondShelf().isEmpty()) {
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().newSecondShelf(((NewWarehouseMessage) receivedMessage).getNewSecondShelf()); }
        if (((NewWarehouseMessage) receivedMessage).getNewThirdShelf() != null && !((NewWarehouseMessage) receivedMessage).getNewThirdShelf().isEmpty()) {
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().newThirdShelf(((NewWarehouseMessage) receivedMessage).getNewThirdShelf()); }
        if (((NewWarehouseMessage) receivedMessage).getNewFirstSpecialShelf() != null && !((NewWarehouseMessage) receivedMessage).getNewFirstSpecialShelf().isEmpty()) {
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().newFirstSpecialShelf(((NewWarehouseMessage) receivedMessage).getNewFirstSpecialShelf()); }
        if (((NewWarehouseMessage) receivedMessage).getNewSecondSpecialShelf() != null && !((NewWarehouseMessage) receivedMessage).getNewSecondSpecialShelf().isEmpty()) {
            game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().newSecondSpecialShelf(((NewWarehouseMessage) receivedMessage).getNewSecondSpecialShelf()); }
        List<LeaderCard> Leaders = game.getPlayerByNickname(turnController.getActivePlayer()).getLeaderCards();
        if(((NewWarehouseMessage) receivedMessage).getIsIndependent() == true){ //se io l'ho chiamata all'inizio, prima di qualsiasi mossa (buy market)
            virtualView.afterReorder(0, Leaders);
        }
        else{
            virtualView.afterReorder(1, Leaders);  //l'ho chiamata dopo buy market, quindi finita la main move
        }
    }

    private void reorderWarehouseGetter(Message receivedMessage) {
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.reorderWarehouse(game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getResourcesAsMap(), game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getLeaderLevelType(1), game.getPlayerByNickname(turnController.getActivePlayer()).getWarehouse().getLeaderLevelType(2), ((ReorderWarehouseMessage) receivedMessage).getIsIndependent());
    }

    public void activateLeaderCard(Message received){
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        if(game.getPlayerByNickname(turnController.getActivePlayer()).activeLeaderCard(((LeaderCardActivationMessage) received).getCardChosen()) == true){
            if(((LeaderCardActivationMessage) received).getTurnZone() == 1)virtualView.continueTurn(1,1,1, ((LeaderCardActivationMessage) received).getCardChosen(), game.getPlayerByNickname(turnController.getActivePlayer()).getLeaderCards());   //inizio turno, tipo leadercard, andato a segno
            else if(((LeaderCardActivationMessage) received).getTurnZone() == 2)virtualView.continueTurn(2,1,1, ((LeaderCardActivationMessage) received).getCardChosen(), game.getPlayerByNickname(turnController.getActivePlayer()).getLeaderCards());   //fine turno, tipo leadercard, andato a segno
        }
        else{
            if(((LeaderCardActivationMessage) received).getTurnZone() == 1)virtualView.continueTurn(1,1,0, ((LeaderCardActivationMessage) received).getCardChosen(), game.getPlayerByNickname(turnController.getActivePlayer()).getLeaderCards());   //inizio turno, tipo leadercard, non andato a segno
            else if(((LeaderCardActivationMessage) received).getTurnZone() == 2)virtualView.continueTurn(2,1,0, ((LeaderCardActivationMessage) received).getCardChosen(), game.getPlayerByNickname(turnController.getActivePlayer()).getLeaderCards());   //fine turno, tipo leadercard, non andato a segno
        }
    }

    public void discardCard(Message received){
        game.getPlayerByNickname(received.getNickname()).getFaithPath().increaseCrossPosition();
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.continueTurn(((DiscardLeaderMessage) received).getTurnZone(),2,1, ((DiscardLeaderMessage) received).getCardChosen(), null);
    }

    public void buyFromMarket(Message received){
        Player activePlayer = game.getPlayerByNickname(received.getNickname());
        game.buyFromMarket(((BuyFromMarketMessage) received).getRowOrColumn(),((BuyFromMarketMessage) received).getWichOne(), activePlayer);
        game.manageWhiteResources(activePlayer);
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
        //game.getBoard().removeObserver(vv);
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
                Server.LOGGER.warning("Invalid effect request!");
                break;
        }

    }
    /**
     * Reset the Game Instance and re-initialize GameController Class.
     */
    public void endGame() {
        Game.resetInstance();

        //StorageData storageData = new StorageData();
        //storageData.delete();

        initGameController();
        Server.LOGGER.info("Game finished. Server ready for a new Game.");
    }

    //Production Power methods

}
