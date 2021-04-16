package it.polimi.controller;

import it.polimi.network.server.Server;
import it.polimi.ngsw.model.*;
import it.polimi.view.*;
import it.polimi.ngsw.model.GameState;
import it.polimi.network.message.*;
import it.polimi.observer.Observer;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

import static it.polimi.network.message.MessageType.PLAYERNUMBER_REPLY;

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
     * Change gameState into INIT. Initialize TurnController and asks a player to pick the gods
     */
    private void initGame() {
        setGameState(GameState.INIT);

        turnController = new TurnController(virtualViewMap, this);
        broadcastGenericMessage("All Players are connected. " + turnController.getActivePlayer()
                + " is choosing two leadercards ");

        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        for(int i = 0; i < game.getChosenPlayersNumber(); i++) {
            virtualView.askLeaderCard(game.removeAndReturnTheLastFourLeaderCards());
            turnController.next();
            virtualView = virtualViewMap.get(turnController.getActivePlayer());
        }
        //turnController.next();
        //virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.askFirstPlayer(turnController.getNicknameQueue());
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
                if (inputController.checkUser(receivedMessage)) {
                    initState(receivedMessage, virtualView);
                }
                break;
            case IN_GAME:
                if (inputController.checkUser(receivedMessage)) {
                    //inGameState(receivedMessage);
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
            if (inputController.verifyReceivedData(receivedMessage)) {
                game.setChosenMaxPlayers(((PlayerNumberReply) receivedMessage).getPlayerNumber());
                broadcastGenericMessage("Waiting for other Players . . .");
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
                    leaderCardHandler((LeaderCardListMessage) receivedMessage, virtualView);
                break;
            case PICK_FIRST_PLAYER:
                    pickFirstPlayerHandler(((FirstPlayerMessage) receivedMessage).getActivePlayerNickname());
                break;
            case PICK_INITIAL_RESOURCES:
                    distribuiteResourceHandler((DistribuiteInitialResourcesMessage) receivedMessage);
            default:
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }


    private  void leaderCardHandler(LeaderCardListMessage receivedMessage,VirtualView virtualview){
        game.getPlayerByNickname(turnController.getActivePlayer()).setLeaderCard(receivedMessage.getLeaderCardList());
    };

    /**
     * Handles the Challenger's choice for the first player.
     *
     * @param firstPlayerNick first player choosen by Challenger.
     */
    private void pickFirstPlayerHandler(String firstPlayerNick) {
        turnController.setActivePlayer(firstPlayerNick);
        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " ", turnController.getActivePlayer());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.showGenericMessage("");
        for(int i = 0; i < game.getChosenPlayersNumber(); i++){
            if(i == 1) {
                virtualView.distribuiteInitialResources(1);
            }
            else if(i == 2){
                virtualView.distribuiteInitialResources(1);
                game.getPlayerByNickname(turnController.getActivePlayer()).getFaithPath().increaseCrossPosition();
            } else if(i == 3){
                virtualView.distribuiteInitialResources(2);
                game.getPlayerByNickname(turnController.getActivePlayer()).getFaithPath().increaseCrossPosition();
            }
            turnController.next();
            virtualView = virtualViewMap.get(turnController.getActivePlayer());
        }
        startGame();
    }

    private void distribuiteResourceHandler(DistribuiteInitialResourcesMessage message){
        Player player = game.getPlayerByNickname(message.getNickname());
        player.getWarehouse().addResourceToWarehouse(message.getFirstPosition(), message.getFirstResource());
        if(message.getSecondPosition() > 0){
            player.getWarehouse().addResourceToWarehouse(message.getSecondPosition(), message.getSecondResource());
        }
    }
    /**
     * Initializes the game after all Clients are connected and all Gods, Workers and Colors are setted up.
     */
    private void startGame() {
        setGameState(GameState.IN_GAME);
        broadcastGenericMessage("Game Started!");   //aggiungi su client controller nickname scelto

        //turnController.broadcastMatchInfo();
        //turnController.newTurn();
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
}
