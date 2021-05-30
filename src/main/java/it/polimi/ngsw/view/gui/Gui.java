package it.polimi.ngsw.view.gui;

import it.polimi.ngsw.model.*;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.View;
import it.polimi.ngsw.view.gui.controller.LobbyController;
import javafx.application.Platform;

import java.util.List;
import java.util.Map;

public class Gui extends ViewObservable implements View {
    
    @Override
    public void askNickname() {
        Platform.runLater(() -> SceneController.changeScene(observers, "ask_nickname_scene.fxml"));
    }

    @Override
    public void askPlayersNumber() {

    }

    @Override
    public void askLeaderCard(List<LeaderCard> leaderCards) {

    }

    @Override
    public void showDisconnectionMessage(String nicknameDisconnected, String text) {

    }

    @Override
    public void showErrorAndExit(String error) {
        Platform.runLater(() -> SceneController.changeScene(observers, "logo_scene.fxml"));
    }

    @Override
    public void showLobby(List<String> nicknameList, int numPlayers) {
        LobbyController lobby;
        try {
            lobby = (LobbyController) SceneController.getActiveController();
            lobby.setPlayersNicknames(nicknameList);
            lobby.setPlayersNumber(numPlayers);
            Platform.runLater(lobby::upDateValues);
        }
        catch (ClassCastException e) {
            lobby = new LobbyController();
            lobby.addAllObservers(observers);
            lobby.setPlayersNicknames(nicknameList);
            lobby.setPlayersNumber(numPlayers);
            LobbyController finalLobby = lobby;
            Platform.runLater(() -> SceneController.changeScene(finalLobby, "lobby_scene.fxml"));
        }
    }

    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {

    }

    @Override
    public void askFirstPlayer(List<String> nicknameList) {

    }

    @Override
    public void distributeInitialResources(int resourcesNumber) {

    }

    @Override
    public void showWinMessage(String winner) {

    }

    @Override
    public void startTurnMessage(List<LeaderCard> Leaders, Marble singleMarble, Marble[] firstRow, Marble[] secondRow, Marble[] thirdRow, List<ProductionPower> leaderProductionPowerList, List<DevCard> activeDevCardList, ProductionPower baseProductionPower, DevCard[][] devCardMarket, Resource firstShelf, Resource secondShelf, int secondShelfNumber, Resource thirdShelf, int thirdShelfNumber, Map<Resource, Integer> chest, int crossPosition, int victoryPoints, boolean papalCardOne, boolean papalCardTwo, boolean papalCardThree) {

    }

    @Override
    public void continueTurn(int turnZone, int actionTypology, int goneRight, int wichCard, List<LeaderCard> Leaders) {

    }

    @Override
    public void buyMarketResource(List<Resource> resources, Resource firstWhite, Resource secondWhite) {

    }

    @Override
    public void reorderWarehouse(Map<Resource, Integer> mapResources, Resource firstLevel, Resource secondLevel, Boolean isIndipendent) {

    }

    @Override
    public void showGenericMessage(String genericMessage) {

    }

    @Override
    public void afterReorder(int i, List<LeaderCard> Leaders) {

    }

    @Override
    public void productionPowerList(List<ProductionPower> productionPowerList, String action) {

    }

    @Override
    public void productionPowerResponse(boolean response, String action, ProductionPower baseProductionPower) {

    }

    @Override
    public void devCardResponse(boolean response, String action, DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo) {

    }

    @Override
    public void devCard(DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo) {

    }

    @Override
    public void viewOtherPlayer(String otherPlayer, Boolean goneRight, int crossPosition, Map<Resource, Integer> resourcesAsMap, List<DevCard> activeDevCards, int[] shelfResNumber, Resource[] shelfResType) {

    }

    @Override
    public void faithPathResponse(int crossPosition, int victoryPoints, boolean papalCardOne, boolean papalCardTwo, boolean papalCardThree) {

    }

    @Override
    public void afterLastMainMove(int i, List<LeaderCard> leaders) {

    }

    @Override
    public void endGameSinglePlayer(int playerVictoryPoints, int lawrenceCrossPosition, boolean winner) {

    }
}
