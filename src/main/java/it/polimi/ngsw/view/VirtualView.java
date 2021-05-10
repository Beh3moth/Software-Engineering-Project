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

    @Override
    public void askNickname() {
        clientHandler.sendMessage(new LoginReply(false, true));
    }


    @Override
    public void askLeaderCard(List<LeaderCard> leaderCards) {
        clientHandler.sendMessage(new LeaderCardListMessage(Game.SERVER_NICKNAME, leaderCards));
    }


    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        clientHandler.sendMessage(new LoginReply(nicknameAccepted, connectionSuccessful));
    }

    @Override
    public void showGenericMessage(String genericMessage) {
        clientHandler.sendMessage(new GenericMessage(genericMessage));
    }

    @Override
    public void afterReorder(int i, List<LeaderCard> Leaders) {
        clientHandler.sendMessage(new AfterReorderMessage(Game.SERVER_NICKNAME, i, Leaders));
    }

    @Override
    public void askPlayersNumber() {
        clientHandler.sendMessage(new PlayerNumberRequest());
    }

    @Override
    public void askFirstPlayer(List<String> players) {
        clientHandler.sendMessage(new FirstPlayerMessage(Game.SERVER_NICKNAME, MessageType.PICK_FIRST_PLAYER, players, null ));
    }

    @Override
    public void distribuiteInitialResources(int resourcesNumber){
        clientHandler.sendMessage(new DistribuiteInitialResourcesMessage(Game.SERVER_NICKNAME, MessageType.PICK_INITIAL_RESOURCES, resourcesNumber, null, null , 0, 0));
    }

    @Override
    public void showWinMessage(String winner) {

    }

    @Override
    public void showLobby(List<String> nicknameList, int numPlayers) {
        clientHandler.sendMessage(new LobbyMessage(nicknameList, numPlayers));
    }
    @Override
    public void showDisconnectionMessage(String nicknameDisconnected, String text) {
        clientHandler.sendMessage(new DisconnectionMessage(nicknameDisconnected, text));
    }
    @Override
    public void showErrorAndExit(String error) {
        clientHandler.sendMessage(new ErrorMessage(Game.SERVER_NICKNAME, error));
    }

    @Override
    public void startTurnMessage (List<LeaderCard> Leaders, Marble singleMarble, Marble[] firstRow, Marble[] secondRow, Marble[] thirdRow, List<ProductionPower> leaderProductionPowerList, List<DevCard> activeDevCardList, List<ProductionPower> productionPowerList, DevCard[][] devCardMarket) {
        clientHandler.sendMessage(new StartTurnMessage(Game.SERVER_NICKNAME, Leaders, singleMarble, firstRow, secondRow, thirdRow, leaderProductionPowerList, activeDevCardList, productionPowerList, devCardMarket));
    }

    @Override
    public void continueTurn(int turnZone, int actionTypology, int goneRight, int wichCard, List<LeaderCard> Leaders){
        clientHandler.sendMessage(new ContinueTurnMessage(Game.SERVER_NICKNAME, turnZone, actionTypology, goneRight, wichCard, Leaders));
    }

    @Override
    public void buyMarketResource(List<Resource> resources, Resource firstWhite, Resource secondWhite){
        clientHandler.sendMessage(new NewResourcesMessage(Game.SERVER_NICKNAME, resources, firstWhite, secondWhite));
    }

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
    public void devCardResponse(boolean response, String action, DevCard devCard, int slotToPut){
        clientHandler.sendMessage(new DevCardResponseMessage(Game.SERVER_NICKNAME, response, action, devCard, slotToPut));
    }

    @Override
    public void devCard(DevCard devCard, int slotToPut) {
        clientHandler.sendMessage(new DevCardMessage(Game.SERVER_NICKNAME, devCard, slotToPut));
    }

    /**
     * Receives an update message from the model.
     * The message is sent over the network to the client.
     * The proper action based on the message type will be taken by the real view on the client.
     *
     * @param message the update message.
     */
    @Override
    public void update(Message message) {
        clientHandler.sendMessage(message);
    }

}
