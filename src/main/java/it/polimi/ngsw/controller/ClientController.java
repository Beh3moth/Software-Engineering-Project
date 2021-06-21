package it.polimi.ngsw.controller;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.ProductionPower;
import it.polimi.ngsw.network.client.Client;
import it.polimi.ngsw.network.client.SocketClient;
import it.polimi.ngsw.network.message.*;
import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.Resource;
import it.polimi.ngsw.observer.Observer;
import it.polimi.ngsw.observer.ViewObserver;
import it.polimi.ngsw.view.View;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is part of the client side.
 * It is an interpreter between the network and a generic view (which in this case can be CLI or GUI).
 * It receives the messages, wraps/unwraps and pass them to the network/view.
 */
public class ClientController implements ViewObserver, Observer{

    private final View view;

    private Client client;
    private String nickname;

    private final ExecutorService taskQueue;

    /**
     * Constructs Client Controller.
     *
     * @param view the view to be controlled.
     */
    public ClientController(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
    }

    /**
     * Validates the given IPv4 address by using a regex.
     *
     * @param ip the string of the ip address to be validated
     * @return {@code true} if the ip is valid, {@code false} otherwise.
     */
    public static boolean isValidIpAddress(String ip) {
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(regex);
    }

    /**
     * Checks if the given port string is in the range of allowed ports.
     *
     * @param portStr the ports to be checked.
     * @return {@code true} if the port is valid, {@code false} otherwise.
     */
    public static boolean isValidPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Create a new Socket Connection to the server with the updated info.
     * An error view is shown if connection cannot be established.
     *
     * @param serverInfo a map of server address and server port.
     */
    @Override
    public void onUpdateServerInfo(Map<String, String> serverInfo) {
        try {
            client = new SocketClient(serverInfo.get("address"), Integer.parseInt(serverInfo.get("port")));
            client.addObserver(this);
            client.readMessage(); // Starts an asynchronous reading from the server.
            client.enablePinger(true);
            taskQueue.execute(view::askNickname);
        } catch (IOException e) {
            taskQueue.execute(() -> view.showLoginResult(false, false, this.nickname));
        }
    }

    /**
     * Sends a message to the server with the player number chosen by the user.
     *
     * @param playersNumber the number of players.
     */
    @Override
    public void onUpdatePlayersNumber(int playersNumber) {
        client.sendMessage(new PlayerNumberReply(this.nickname, playersNumber));
    }

    /**
     * Sends a message to the server with the chosen cards of the player.
     *
     * @param chosenCard a list of two chosen cards
     */
    @Override
    public void onUpdateLeaderCard(List<LeaderCard> chosenCard){
        client.sendMessage(new LeaderCardListMessage(this.nickname, chosenCard));
    }

    /**
     * Sends a message to the server with the nickname of the first player chosen by the user.
     *
     * @param nickname the nickname of the first player.
     */
    @Override
    public void onUpdateFirstPlayer(String nickname) {
        client.sendMessage(new FirstPlayerMessage(this.nickname, MessageType.PICK_FIRST_PLAYER, null,  nickname));
    }

    /**
     * Disconnects the client from the network.
     */
    @Override
    public void onDisconnection() {
        client.disconnect();
    }

    /**
     * Sends to the server a message with the new warehouse of the player
     * @param newFirstShelf  the new first shelf
     * @param newSecondShelf  the new second shelf
     * @param newThirdShelf  the new third shelf
     * @param newFirstSpecialShelf the new special first shelf
     * @param newSecondSpecialShelf the new special second shelf
     * @param discardList  the list of recources to discard
     * @param isIndependent if 1 is a reorderwarehose, if 0 it is after buy market
     */
    @Override
    public void onUpdateNewWarehouse(Resource newFirstShelf, List<Resource> newSecondShelf, List<Resource> newThirdShelf, List<Resource> newFirstSpecialShelf, List<Resource> newSecondSpecialShelf, List<Resource> discardList, Boolean isIndependent) {
        client.sendMessage(new NewWarehouseMessage(this.nickname, newFirstShelf, newSecondShelf, newThirdShelf, newFirstSpecialShelf, newSecondSpecialShelf, discardList, isIndependent));

    }

    /**
     * Send the end of the turn message
     */
    @Override
    public void onEndTurn() {
        client.sendMessage(new EndTurnMessage(this.nickname));
    }

    /**
     * Sends a message to the server with the updated nickname.
     * The nickname is also stored locally for later usages.
     *
     * @param nickname the nickname to be sent.
     */
    @Override
    public void onUpdateNickname(String nickname) {
        this.nickname = nickname;
        client.sendMessage(new LoginRequest(this.nickname));
    }

    /**
     * Send a message to the server of the picked resources
     * @param number the number
     * @param resourceOne first resource
     * @param resourceTwo second resource
     * @param firstPos the first position
     * @param secondPos the second position
     */
    @Override
    public void onUpdatePickedResources(int number, Resource resourceOne, Resource resourceTwo, int firstPos, int secondPos){
        client.sendMessage(new DistribuiteInitialResourcesMessage(this.nickname, MessageType.PICK_INITIAL_RESOURCES, number, resourceOne, resourceTwo, firstPos, secondPos));
    }

    /**
     * Send a message to the server of the activation of a leadercard
     * @param chosenCard the chosen card
     * @param turnZone in wich part of the turno, if 1 == start, if 2 == end of the turn
     */
    @Override
    public void onUpdateLeaderCardActivation(int chosenCard, int turnZone){
        client.sendMessage(new LeaderCardActivationMessage(this.nickname, chosenCard, turnZone));
    }

    /**
     * Send a message to the server of the discard of a leadercard
     * @param wichCard the card chosen
     * @param turnZone in wich part of the turno, if 1 == start, if 2 == end of the turn
     */
    @Override
    public void onUpdateDiscardLeaderCard(int wichCard, int turnZone) {
        client.sendMessage(new DiscardLeaderMessage(this.nickname, wichCard, turnZone));
    }

    /**
     * Send a message to the server of the action "Buy from market"
     * @param rowOrColumn if 1 == column, if 2 == row
     * @param wichOne wich column/row
     */
    @Override
    public void onUpdateBuyFromMarket(int rowOrColumn, int wichOne){
        client.sendMessage(new BuyFromMarketMessage(this.nickname, rowOrColumn, wichOne));
    }

    /**
     * Send q message of the action "reorder warehouse"
     * @param isIndipendent if it is indipendent
     */
    @Override
    public void onUpdateReorderWarehouse(boolean isIndipendent) {
        client.sendMessage(new ReorderWarehouseMessage(this.nickname, null, null, null, isIndipendent));
    }

    /**
     * Send a message with an integer and a string
     * @param integerChosen the integer
     * @param action the string
     */
    @Override
    public void onUpdateIntegerChosen(int integerChosen, String action) {
        client.sendMessage(new IntegerMessage(this.nickname, integerChosen, action));
    }

    /**
     * Send a message to the server with the resources to pay and to receive
     * @param resourcesToPay resource that the player has to pay
     * @param resourcesToReceive the resources that the player will receive
     * @param action a string of the action
     */
    @Override
    public void onUpdateTwoResourceList(List<Resource> resourcesToPay, List<Resource> resourcesToReceive, String action) {
        client.sendMessage(new TwoResourceListMessage(this.nickname, resourcesToPay, resourcesToReceive, action));
    }

    /**
     * Send a message of the payment of the production power
     * @param isWarehouse if is from warehouse
     * @param shelfLevel wich shelf
     * @param resourceType type of the resources
     * @param productionPower wich production power
     */
    @Override
    public void onUpdatePayProductionPower(Boolean[] isWarehouse, Integer[] shelfLevel, Resource[] resourceType, ProductionPower productionPower) {
        client.sendMessage(new ProductionPowerCoordinatesMessage(this.nickname, isWarehouse, shelfLevel, resourceType, productionPower));
    }

    /**
     * Send a message of all the production powers
     * @param productionPowerList the list of the production power
     * @param action string of the action
     */
    @Override
    public void onUpdateProductionPowerList(List<ProductionPower> productionPowerList, String action){
        client.sendMessage(new ProductionPowerListMessage(this.nickname, productionPowerList, action));
    }

    /**
     * Send a message of the resources of the production power
     * @param resource the resource
     * @param productionPower the production power
     */
    @Override
    public void onUpdateProductionPowerResource(Resource resource, ProductionPower productionPower) {
        client.sendMessage(new ProductionPowerResourceMessage(this.nickname, resource, productionPower));
    }

    /**
     * Send a message of the chosen dev card to buy
     * @param level  wich level ot the dashboard
     * @param column wich color
     * @param slotToPut where to put it
     */
    @Override
    public void onUpdateChooseDevCard(int level, int column, int slotToPut){
        client.sendMessage(new ChosenDevCardMessage(this.nickname, level, column, slotToPut));
    }

    /**
     * Send a message to the server with the activation of the production powers
     */
    @Override
    public void onUpdateProductionPowerActivation() {
        client.sendMessage(new ActivateProductionPowersMessage(this.nickname));
    }

    /**
     * Send a message to the server with the payment of the devCard
     * @param isWarehouse if is warehouse
     * @param shelfLevel wich shelf
     * @param resourceType the type of the resources
     * @param devCard the devacard
     * @param slotToPut where to put the card
     * @param discountPowerOne the discount one
     * @param discountPowerTwo the discount two
     */
    @Override
    public void onUpdatePayDevCard(Boolean[] isWarehouse, Integer[] shelfLevel, Resource[] resourceType, DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo) {
        client.sendMessage(new DevCardCoordinatesMessage (this.nickname, isWarehouse, shelfLevel, resourceType, devCard, slotToPut, discountPowerOne, discountPowerTwo));
    }

    /**
     * Send a message to the server to watch the info of the other player
     * @param nicknameOtherPlayer the nickname of the player
     */
    @Override
    public void onUpdateWatchInfo(String nicknameOtherPlayer) {
        client.sendMessage(new WatchOtherPlayerInfoMessage(this.nickname, nicknameOtherPlayer, null, 0, null, null, null,null ));
    }

    /**
     * Send a message to the server to calculate the PV of the players
     */
    @Override
    public void onUpdateCalculatePVEndGame() {
        client.sendMessage(new CalculatePVEndGame(this.nickname));
    }

    /**
     * Send a message to ask for the faithpath of the player
     */
    @Override
    public void onUpdateAskForFaithPath() {
        client.sendMessage(new AskForFaithPathMessage(this.nickname));
    }

    /**
     * This method receive all the message from the server and send them to the right method of the cli/gui
     * @param message the message
     */
    public void update(Message message) {

        switch (message.getMessageType()) {
            case GENERIC_MESSAGE:
                taskQueue.execute(() -> view.showGenericMessage(((GenericMessage) message).getMessage()));
                break;
            case PICK_INITIAL_RESOURCES:
                DistribuiteInitialResourcesMessage resourceinitialMessage = (DistribuiteInitialResourcesMessage) message;
                taskQueue.execute(() -> view.distributeInitialResources(resourceinitialMessage.getNumber()));
                break;
            case DISCONNECTION:
                DisconnectionMessage dm = (DisconnectionMessage) message;
                client.disconnect();
                view.showDisconnectionMessage(dm.getNicknameDisconnected(), dm.getMessageStr());
                break;
            case ERROR:
                ErrorMessage em = (ErrorMessage) message;
                view.showErrorAndExit(em.getError());
                break;
            case PLAYERNUMBER_REQUEST:
                taskQueue.execute(view::askPlayersNumber);
                break;
            case PICK_FIRST_PLAYER:
                FirstPlayerMessage playersMessage = (FirstPlayerMessage) message;
                taskQueue.execute(() -> view.askFirstPlayer(playersMessage.getActivePlayers()));
                break;
            case LEADERCARDREQUEST:
                LeaderCardListMessage leaderCardMessage = (LeaderCardListMessage) message;
                taskQueue.execute(() -> view.askLeaderCard(leaderCardMessage.getLeaderCardList()));
                break;
            case LOGIN_REPLY:
                LoginReply loginReply = (LoginReply) message;
                taskQueue.execute(() -> view.showLoginResult(loginReply.isNicknameAccepted(), loginReply.isConnectionSuccessful(), this.nickname));
                break;
            case LOBBY:
                LobbyMessage lobbyMessage = (LobbyMessage) message;
                taskQueue.execute(() -> view.showLobby(lobbyMessage.getNicknameList(), lobbyMessage.getMaxPlayers()));
                break;
            case START_TURN:
                StartTurnMessage start = (StartTurnMessage) message;
                taskQueue.execute(() -> view.startTurnMessage(start.getLeaders(), start.getSingleMarble(), start.getFirstRow(), start.getSecondRow(), start.getThirdRow(), start.getLeaderProductionPowerList(), start.getActiveDevCardMap(), start.getBaseProductionPower(), start.getDevCardMarket(), start.getFirstShelf(), start.getSecondShelf(), start.getSecondShelfNumber(), start.getThirdShelf(), start.getThirdShelfNumber(), start.getChest(), start.getCrossPosition(), start.getVictoryPoints(), start.isPapalCardOne(), start.isPapalCardTwo(), start.isPapalCardThree(), start.getFsr(), start.getFsn(), start.getSsr(), start.getSsn()));
                break;
            case CONTINUE_TURN:
                ContinueTurnMessage continueMessage = (ContinueTurnMessage) message;
                taskQueue.execute(() -> view.continueTurn(continueMessage.getTurnZone(), continueMessage.getActionTypology(), continueMessage.getGoneRight(), continueMessage.getCard(), continueMessage.getLeaders()));
                break;
            case NEWRESOURCE:
                NewResourcesMessage resourceMessage = (NewResourcesMessage) message;
                taskQueue.execute(() -> view.buyMarketResource(resourceMessage.getResources(), resourceMessage.getFirstResource(), resourceMessage.getSecondResource()));
                break;
            case REORDER_WAREHOUSE:
                ReorderWarehouseMessage reorderWarehouseMessage = (ReorderWarehouseMessage) message;
                taskQueue.execute(() -> view.reorderWarehouse(reorderWarehouseMessage.getMapResources(),reorderWarehouseMessage.getFirstLevel(), reorderWarehouseMessage.getSecondLevel(), reorderWarehouseMessage.getIsIndependent()));
                break;
            case AFTER_REORDER:
                AfterReorderMessage received = (AfterReorderMessage) message;
                taskQueue.execute(() -> view.afterReorder(received.getIsIndependent(), received.getLeaders()));
                break;
            case PRODUCTION_POWER_LIST:
                ProductionPowerListMessage productionPowerListMessage = (ProductionPowerListMessage) message;
                taskQueue.execute(() -> view.productionPowerList(productionPowerListMessage.getProductionPowerList(), productionPowerListMessage.getAction()));
                break;
            case PRODUCTION_POWER_RESPONSE_MESSAGE:
                ProductionPowerResponseMessage productionPowerResponse = (ProductionPowerResponseMessage) message;
                taskQueue.execute(() -> view.productionPowerResponse(productionPowerResponse.isResponse(), productionPowerResponse.getAction(), productionPowerResponse.getProductionPower()));
                break;
            case DEVCARD_RESPONSE_MESSAGE:
                DevCardResponseMessage devCardResponse = (DevCardResponseMessage) message;
                taskQueue.execute(() -> view.devCardResponse(devCardResponse.isResponse(), devCardResponse.getAction(), devCardResponse.getDevCard(), devCardResponse.getSlotToPut(), devCardResponse.getDiscountPowerOne(), devCardResponse.getDiscountPowerTwo()));
                break;
            case DEVCARD:
                DevCardMessage devCardMessage = (DevCardMessage) message;
                taskQueue.execute(() -> view.devCard(devCardMessage.getDevCard(), devCardMessage.getSlotToPut(), devCardMessage.getDiscountPowerOne(), devCardMessage.getDiscountPowerTwo()));
                break;
            case WATCH_OTHER_PLAYER:
                WatchOtherPlayerInfoMessage Message = (WatchOtherPlayerInfoMessage) message;
                taskQueue.execute(() -> view.viewOtherPlayer(Message.getNicknameOtherPlayer(), Message.getGoneRight(), Message.getCrossPosition(), Message.getResourceAsMap(),  Message.getActiveDevCards(), Message.getShlefNumber(), Message.getShelfResource()));
                break;
            case FAITH_PATH_MESSAGE:
                FaithPathMessage faithPath = (FaithPathMessage) message;
                taskQueue.execute(() -> view.faithPathResponse(faithPath.getCrossPosition(), faithPath.getVictoryPoints(), faithPath.isPapalCardOneActive(), faithPath.isPapalCardTwoActive(), faithPath.isPapalCardThreeActive()));
                break;
            case AFTER_LAST_MAIN:
                AfterLastMainMessage afterLast = (AfterLastMainMessage) message;
                taskQueue.execute(() -> view.afterLastMainMove(afterLast.getIsIndependent(), afterLast.getLeaders()));
                break;
            case END_GAME_SINGLE_PLAYER:
                EndGameSinglePlayerMessage endGameSinglePlayerMessage = (EndGameSinglePlayerMessage) message;
                taskQueue.execute(() -> view.endGameSinglePlayer(endGameSinglePlayerMessage.getPlayerVictoryPoints(), endGameSinglePlayerMessage.getLawrenceCrossPosition(), endGameSinglePlayerMessage.isWinner()));
                break;
            default:
                break;
        }
    }

}
