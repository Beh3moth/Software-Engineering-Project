package it.polimi.ngsw.view;

import it.polimi.ngsw.model.*;
import it.polimi.ngsw.network.message.*;
import it.polimi.ngsw.network.server.ClientHandler;
import it.polimi.ngsw.observer.Observer;

import java.util.List;
import java.util.Map;

/**
 * Hides the network implementation from the controller.
 * The controller calls methods from this class as if it was a normal view.
 * Instead, a network protocol is used to communicate with the real view on the client side.
 */
public class VirtualView implements View, Observer {

    private final ClientHandler clientHandler;

    /**
     * Default constructor.
     *
     * @param clientHandler the client handler the virtual view must send messages to.
     */
    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Returns the client handler associated to a client.
     *
     * @return client handler.
     */
    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    /**
     * Ask the player to give his nickname
     */
    @Override
    public void askNickname() {
        clientHandler.sendMessage(new LoginReply(false, true));
    }

    /**
     * Ask the player to chose two leadercards.
     * @param leaderCards    the list of the available LeaderCards.
     */
    @Override
    public void askLeaderCard(List<LeaderCard> leaderCards) {
        clientHandler.sendMessage(new LeaderCardListMessage(Game.SERVER_NICKNAME, leaderCards));
    }

    /**
     * Show the result of the login to the player.
     * @param nicknameAccepted     indicates if the chosen nickname has been accepted.
     * @param connectionSuccessful indicates if the connection has been successful.
     * @param nickname             the nickname of the player to be greeted.
     */
    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        clientHandler.sendMessage(new LoginReply(nicknameAccepted, connectionSuccessful));
    }

    /**
     * Show a generic message.
     * @param genericMessage the generic message to be shown.
     */
    @Override
    public void showGenericMessage(String genericMessage) {
        clientHandler.sendMessage(new GenericMessage(genericMessage));
    }

    /**
     * Message sent after reordering the warehouse
     * @param i if is independent
     * @param Leaders the leader cards
     */
    @Override
    public void afterReorder(int i, List<LeaderCard> Leaders) {
        clientHandler.sendMessage(new AfterReorderMessage(Game.SERVER_NICKNAME, i, Leaders));
    }

    /**
     * Ask the number of the players
     */
    @Override
    public void askPlayersNumber() {
        clientHandler.sendMessage(new PlayerNumberRequest());
    }

    /**
     * Ask who is the first player that will start the game
     * @param players
     */
    @Override
    public void askFirstPlayer(List<String> players) {
        clientHandler.sendMessage(new FirstPlayerMessage(Game.SERVER_NICKNAME, MessageType.PICK_FIRST_PLAYER, players, null ));
    }

    /**
     * Distribuite the resources at the begging of the game
     * @param resourcesNumber how many resources
     */
    @Override
    public void distributeInitialResources(int resourcesNumber){
        clientHandler.sendMessage(new DistribuiteInitialResourcesMessage(Game.SERVER_NICKNAME, MessageType.PICK_INITIAL_RESOURCES, resourcesNumber, null, null , 0, 0));
    }

    /**
     * Show the win message.
     * @param winner the nickname of the winner.
     */
    @Override
    public void showWinMessage(String winner) {
    }

    /**
     * Show all the players.
     * @param nicknameList list of players.
     * @param numPlayers   number of players.
     */
    @Override
    public void showLobby(List<String> nicknameList, int numPlayers) {
        clientHandler.sendMessage(new LobbyMessage(nicknameList, numPlayers));
    }

    /**
     * Show a message of disconnection.
     * @param nicknameDisconnected the nickname of the player who has disconnected.
     * @param text                 a generic info text message.
     */
    @Override
    public void showDisconnectionMessage(String nicknameDisconnected, String text) {
        clientHandler.sendMessage(new DisconnectionMessage(nicknameDisconnected, text));
    }

    /**
     * Show an error.
     * @param error the error message to be shown.
     */
    @Override
    public void showErrorAndExit(String error) {
        clientHandler.sendMessage(new ErrorMessage(Game.SERVER_NICKNAME, error));
    }

    /**
     * Permit the player to start his turn.
     */
    @Override
    public void startTurnMessage (List<LeaderCard> Leaders, Marble singleMarble, Marble[] firstRow, Marble[] secondRow, Marble[] thirdRow, List<ProductionPower> leaderProductionPowerList, Map<Integer, DevCard> activeDevCardMap, ProductionPower baseProductionPower, DevCard[][] devCardMarket,Resource firstShelf,Resource secondShelf,int secondShelfNumber,Resource thirdShelf,int thirdShelfNumber, Map<Resource, Integer> chest, int crossPosition, int victoryPoints, boolean papalCardOne, boolean papalCardTwo, boolean papalCardThree, Resource firstSpecialResource, int firstSpecialNumber,Resource secondSpecialResource,int secondSpecialNumber, Integer lawrencePosition) {
        clientHandler.sendMessage(new StartTurnMessage(Game.SERVER_NICKNAME, Leaders, singleMarble, firstRow, secondRow, thirdRow, leaderProductionPowerList, activeDevCardMap, baseProductionPower, devCardMarket,  firstShelf, secondShelf, secondShelfNumber, thirdShelf, thirdShelfNumber, chest, crossPosition, victoryPoints, papalCardOne, papalCardTwo, papalCardThree, firstSpecialResource, firstSpecialNumber, secondSpecialResource, secondSpecialNumber, lawrencePosition));
    }

    /**
     * Send a message to continue his turn.
     */
    @Override
    public void continueTurn(int turnZone, int actionTypology, int goneRight, int wichCard, List<LeaderCard> Leaders){
        clientHandler.sendMessage(new ContinueTurnMessage(Game.SERVER_NICKNAME, turnZone, actionTypology, goneRight, wichCard, Leaders));
    }

    /**
     * Sends a message to buy the resources in the market.
     */
    @Override
    public void buyMarketResource(List<Resource> resources, Resource firstWhite, Resource secondWhite){
        clientHandler.sendMessage(new NewResourcesMessage(Game.SERVER_NICKNAME, resources, firstWhite, secondWhite));
    }

    /**
     * Permit to reorder the warehouse.
     */
    @Override
    public void reorderWarehouse(Map<Resource, Integer> mapResources, Resource firstLevel, Resource secondLevel, Boolean isIndipendent) {
        clientHandler.sendMessage(new ReorderWarehouseMessage(Game.SERVER_NICKNAME, mapResources, firstLevel, secondLevel, isIndipendent));
    }

    //Activate Production Power Methods

    @Override
    public void productionPowerList(List<ProductionPower> productionPowerList, String action) {
        clientHandler.sendMessage(new ProductionPowerListMessage(Game.SERVER_NICKNAME, productionPowerList, action));
    }

    @Override
    public void productionPowerResponse(boolean response, String action, ProductionPower baseProductionPower) {
        clientHandler.sendMessage(new ProductionPowerResponseMessage(Game.SERVER_NICKNAME, response, action, baseProductionPower));
    }

    @Override
    public void devCardResponse(boolean response, String action, DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo){
        clientHandler.sendMessage(new DevCardResponseMessage(Game.SERVER_NICKNAME, response, action, devCard, slotToPut, discountPowerOne, discountPowerTwo));
    }

    @Override
    public void devCard(DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo) {
        clientHandler.sendMessage(new DevCardMessage(Game.SERVER_NICKNAME, devCard, slotToPut, discountPowerOne, discountPowerTwo));
    }

    @Override
    public void viewOtherPlayer(String otherNickname, Boolean goneRight, int crossPosition, Map<Resource, Integer> resourcesAsMap, List<DevCard> activeDevCards, int[] shelfResNumber, Resource[] shelfResType) {
        clientHandler.sendMessage(new WatchOtherPlayerInfoMessage(Game.SERVER_NICKNAME, otherNickname, goneRight, crossPosition, resourcesAsMap, activeDevCards, shelfResNumber, shelfResType));

    }

    @Override
    public void afterLastMainMove(int i, List<LeaderCard> leaders) {
        clientHandler.sendMessage(new AfterLastMainMessage(Game.SERVER_NICKNAME, i, leaders));
    }

    @Override
    public void faithPathResponse(int crossPosition, int victoryPoints, boolean papalCardOne, boolean papalCardTwo, boolean papalCardThree) {
        clientHandler.sendMessage(new FaithPathMessage(Game.SERVER_NICKNAME, crossPosition, victoryPoints, papalCardOne, papalCardTwo, papalCardThree));
    }

    @Override
    public void endGameSinglePlayer(int playerVictoryPoints, int lawrenceCrossPosition, boolean winner) {
        clientHandler.sendMessage(new EndGameSinglePlayerMessage(Game.SERVER_NICKNAME, playerVictoryPoints, lawrenceCrossPosition, winner));
    }

    /**
     * Receives an update message from the model.
     * The message is sent over the network to the client.
     * The proper action based on the message type will be taken by the real view on the client.
     * @param message the update message.
     */
    @Override
    public void update(Message message) {
        clientHandler.sendMessage(message);
    }

}
