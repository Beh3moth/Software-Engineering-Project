package it.polimi.ngsw.view.gui;

import it.polimi.ngsw.model.*;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import it.polimi.ngsw.view.View;
import it.polimi.ngsw.view.gui.controller.*;
import javafx.application.Platform;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Gui extends ViewObservable implements View {

    GameController gameController = new GameController();
    LightModel lightModel = new LightModel();
    private int[] leaderCardStatus;
    private List<LeaderCard> leaderCardList;

    @Override
    public void askNickname() {
        Platform.runLater(() -> SceneController.changeScene(observers, "ask_nickname_scene.fxml"));
    }

    @Override
    public void askPlayersNumber() {
        Platform.runLater(() -> SceneController.changeScene(observers, "ask_players_number_scene.fxml"));
    }

    @Override
    public void askLeaderCard(List<LeaderCard> leaderCards) {
        this.leaderCardStatus = lightModel.getLeaderCardStatus();
        ChoseLeaderCardController controller = new ChoseLeaderCardController(leaderCards);
        controller.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeScene(controller, "chose_leader_scene.fxml"));
    }

    @Override
    public void showDisconnectionMessage(String nicknameDisconnected, String text) {
        Platform.runLater(() -> SceneController.showProblem("Info Message", nicknameDisconnected));
    }

    @Override
    public void showErrorAndExit(String error) {
        Platform.runLater(() -> SceneController.showProblem("Info Message", error));
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
        if (nicknameAccepted && connectionSuccessful) {
            Platform.runLater(() -> Platform.runLater(() -> SceneController.showProblem("Info Message", "Connected successfully as " + nickname)));
        } else if (connectionSuccessful) {
            askNickname();
        } else if (nicknameAccepted) {
            Platform.runLater(() -> {
                Platform.runLater(() -> SceneController.showProblem("Info Message", "Login error."));
                SceneController.changeScene(observers, "lobby_scene.fxml");
            });
        } else {
            Platform.runLater(() -> {
                Platform.runLater(() -> SceneController.showProblem("Info Message", "Invalid nickname"));
                SceneController.changeScene(observers, "ask_nickname_scene.fxml");
            });
        }
    }

    @Override
    public void askFirstPlayer(List<String> nicknameList) {
        Platform.runLater(() -> SceneController.changeScene(observers, "chose_first_player_scene.fxml"));
    }

    @Override
    public void distributeInitialResources(int resourceNumber) {
        distribute_initial_resources_controller dir = new distribute_initial_resources_controller();
        dir.setResourceNumber(resourceNumber);
        dir.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeScene(dir, "distribute_initial_resources_scene.fxml"));
    }

    @Override
    public void showWinMessage(String winner) {
    }

    @Override
    public void startTurnMessage(List<LeaderCard> leaderCardList, Marble singleMarble, Marble[] firstRow, Marble[] secondRow, Marble[] thirdRow, List<ProductionPower> leaderProductionPowerList, Map<Integer, DevCard> activeDevCardMap, ProductionPower baseProductionPower, DevCard[][] devCardMarket, Resource firstShelf, Resource secondShelf, int secondShelfNumber, Resource thirdShelf, int thirdShelfNumber, Map<Resource, Integer> chest, int crossPosition, int victoryPoints, boolean papalCardOne, boolean papalCardTwo, boolean papalCardThree, Resource firstSpecialResource, int firstSpecialNumber,Resource secondSpecialResource,int secondSpecialNumber) {
        this.leaderCardStatus = lightModel.getLeaderCardStatus();
        lightModel.setSingleMarble(singleMarble);
        lightModel.setFirstRow(firstRow);
        lightModel.setSecondRow(secondRow);
        lightModel.setThirdRow(thirdRow);
        lightModel.setDevCardMarket(devCardMarket);
        lightModel.setLeaderProductionPowerList(leaderProductionPowerList);
        lightModel.setActiveDevCardMap(activeDevCardMap);
        lightModel.setFirstShelf(firstShelf);
        lightModel.setSecondShelf(secondShelf);
        lightModel.setThirdShelf(thirdShelf);
        lightModel.setSecondShelfNumber(secondShelfNumber);
        lightModel.setThirdShelfNumber(thirdShelfNumber);
        lightModel.setBaseProductionPower(baseProductionPower);
        lightModel.setChest(chest);
        lightModel.setCrossPosition(crossPosition);
        lightModel.setVictoryPoints(victoryPoints);
        lightModel.setPapalCardOne(papalCardOne);
        lightModel.setPapalCardTwo(papalCardTwo);
        lightModel.setPapalCardThree(papalCardThree);
        lightModel.setFsr(firstSpecialResource);
        lightModel.setSsr(secondSpecialResource);
        lightModel.setFsn(firstSpecialNumber);
        lightModel.setSsn(secondSpecialNumber);
        lightModel.setLeaderCardList(leaderCardList);
        this.gameController.setLightModel(lightModel);
        if (this.leaderCardStatus[0] == 1 || this.leaderCardStatus[1] == 1) {
            LeaderActionController controller = new LeaderActionController(leaderCardList, lightModel, 1);
            controller.addAllObservers(observers);
            Platform.runLater(() -> SceneController.changeScene(controller, "leader_action_scene.fxml"));
        } else {
            //mainMove();
            gameController.addAllObservers(observers);
            gameController.setLightModel(lightModel);
            Platform.runLater(() -> SceneController.changeScene(gameController, "game_scene.fxml"));
        }
    }

    @Override
    public void continueTurn(int turnZone, int actionTypology, int goneRight, int whichCard, List<LeaderCard> leaderCardList) {
        if (turnZone == 1) { //inizio turno

            if (actionTypology == 1) { //1 vuol dire che era stata chiamata una leadercard request, 2 una discard card

                if (goneRight == 0) {  //0 vuol dire non attivata, quindi richiedi, 1 attivata
                    LeaderActionController controller = new LeaderActionController(leaderCardList, lightModel, turnZone);
                    controller.addAllObservers(observers);
                    Platform.runLater(() -> SceneController.changeScene(controller, "leader_action_scene.fxml"));
                }
                else if (goneRight == 1) {
                    gameController.addAllObservers(observers);
                    gameController.setLightModel(lightModel);
                    Platform.runLater(() -> SceneController.changeScene(gameController, "game_scene.fxml"));
                    this.leaderCardStatus[whichCard] = 2;
                    // mainMove();
                }

            }
            else if (actionTypology == 2) {
                gameController.addAllObservers(observers);
                gameController.setLightModel(lightModel);
                Platform.runLater(() -> SceneController.changeScene(gameController, "game_scene.fxml"));
                this.leaderCardStatus[whichCard] = 0;
                //mainMove();
            }
        }
        else if (turnZone == 2) {
            if (actionTypology == 1) {
                if (goneRight == 1) {
                    this.leaderCardStatus[whichCard] = 2;
                    if(lightModel.isGameFinished()){
                        notifyObserver(obs -> obs.onUpdateCalculatePVEndGame());
                    }
                    else notifyObserver(obs -> obs.onEndTurn());
                } else if (goneRight == 0) {
                    if(lightModel.isGameFinished()){
                        afterLastMainMove(1, leaderCardList);
                    }
                    else {
                        LeaderActionController controller = new LeaderActionController(leaderCardList, lightModel, turnZone);
                        controller.addAllObservers(observers);
                        Platform.runLater(() -> SceneController.changeScene(controller, "leader_action_scene.fxml"));
                    }
                }//leadercard choice. middle turn
            } else if (actionTypology == 2) {
                this.leaderCardStatus[whichCard] = 0;
                if(lightModel.isGameFinished()){
                    notifyObserver(obs -> obs.onUpdateCalculatePVEndGame());
                }
                else notifyObserver(obs -> obs.onEndTurn());
            }
            //fine turno
        }
        else {

        }
    }

    @Override
    public void buyMarketResource(List<Resource> resources, Resource firstWhite, Resource secondWhite) {
        ReorderWarehouseController controller = new ReorderWarehouseController();
        controller.setReorderWarehouseController(lightModel, lightModel.getFirstShelf(), lightModel.getSecondShelf(), lightModel.getSecondShelfNumber(), lightModel.getThirdShelf(), lightModel.getThirdShelfNumber(), lightModel.getFsr(), lightModel.getFsn(), lightModel.getSsr(), lightModel.getSsn(), resources, false);
        controller.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeScene(controller, "reorder_warehouse_scene.fxml"));
    }

    @Override
    public void reorderWarehouse(Map<Resource, Integer> mapResources, Resource firstLevel, Resource secondLevel, Boolean isIndependent) {

    }

    @Override
    public void showGenericMessage(String message) {
        Platform.runLater(() -> SceneController.showProblem("Info Message", message));
    }

    @Override
    public void afterReorder(int i, List<LeaderCard> leaderCardList) {
        if (i == 1) {
            if (this.leaderCardStatus[0] == 1 || this.leaderCardStatus[1] == 1) {
                LeaderActionController controller = new LeaderActionController(leaderCardList, lightModel, 2);
                controller.addAllObservers(observers);
                Platform.runLater(() -> SceneController.changeScene(controller, "leader_action_scene.fxml"));
            } else {
                if(lightModel.isGameFinished()) {
                    notifyObserver(obs -> obs.onUpdateCalculatePVEndGame());
                }
                else notifyObserver(obs -> obs.onEndTurn());
            }
        } else {
            gameController.addAllObservers(observers);
            gameController.setLightModel(lightModel);
            Platform.runLater(() -> SceneController.changeScene(gameController, "game_scene.fxml"));
        }
    }

    @Override
    public void devCardResponse(boolean response, String action, DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo) {

    }

    @Override
    public void devCard(DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo) {
        pay_devcard_controller pdc = new pay_devcard_controller(devCard, this.lightModel, slotToPut, discountPowerOne, discountPowerTwo);
        pdc.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeScene(pdc, "pay_devcard_scene.fxml"));
    }

    @Override
    public void viewOtherPlayer(String otherPlayer, Boolean goneRight, int crossPosition, Map<Resource, Integer> resourcesAsMap, List<DevCard> activeDevCards, int[] shelfResNumber, Resource[] shelfResType) {
        view_other_player_scene_controller vop = new view_other_player_scene_controller();
        vop.setView_other_player_scene_controller(crossPosition, resourcesAsMap, activeDevCards, shelfResNumber, shelfResType, this.lightModel);
        vop.addAllObservers(observers);
        Platform.runLater(() -> SceneController.changeScene(vop, "view_other_player_scene.fxml"));
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

    //Production power

    @Override
    public void productionPowerList(List<ProductionPower> productionPowerList, String action) {
        //empty
    }

    @Override
    public void productionPowerResponse(boolean response, String action, ProductionPower productionPower) {
        switch (action) {
            case "setBaseProductionPower":
                if (response) {
                    pay_production_power_controller ppp = new pay_production_power_controller(productionPower, this.lightModel);
                    ppp.addAllObservers(observers);
                    Platform.runLater(() -> SceneController.changeScene(ppp, "pay_production_power_scene.fxml"));
                } else {
                    //reset all
                    gameController.addAllObservers(observers);
                    gameController.setLightModel(lightModel);
                    Platform.runLater(() -> SceneController.changeScene(observers, "game_scene.fxml"));
                }
                break;
            case "productionPowerCheck":
                if (response) {
                    pay_production_power_controller ppp = new pay_production_power_controller(productionPower, this.lightModel);
                    ppp.addAllObservers(observers);
                    Platform.runLater(() -> SceneController.changeScene(ppp, "pay_production_power_scene.fxml"));
                } else {
                    //out.println("Production Power have been chosen, but you can't afford it.");
                    if(productionPower.isLeaderProductionPower()){
                        for(ProductionPower power : lightModel.getLeaderProductionPowerList()){
                            if(power.equals(productionPower)){
                                power.resetLeaderProductionPower();
                            }
                        }
                    }
                    lightModel.getChosenIntegerList().remove(lightModel.getChosenIntegerList().size() - 1);
                    gameController.addAllObservers(observers);
                    gameController.setLightModel(lightModel);
                    Platform.runLater(() -> SceneController.changeScene(observers, "game_scene.fxml"));
                }
                break;
            case "payProductionPower":
                if (response) {
                    //out.println("You have successfully paid the Production Power.");
                    lightModel.getPaidProductionPowerList().add(productionPower);
                    ProductionChoiceController controller = new ProductionChoiceController();
                    controller.setProductionChoiceController(lightModel);
                    controller.addAllObservers(observers);
                    Platform.runLater(() -> SceneController.changeScene(controller, "production_choice_scene.fxml"));
                } else {
                    pay_production_power_controller ppp = new pay_production_power_controller(productionPower, this.lightModel);
                    ppp.addAllObservers(observers);
                    Platform.runLater(() -> SceneController.changeScene(ppp, "pay_production_power_scene.fxml"));
                }
                break;
            case "setLeaderProductionPower":
                if (response) {
                    pay_production_power_controller ppp = new pay_production_power_controller(productionPower, this.lightModel);
                    ppp.addAllObservers(observers);
                    Platform.runLater(() -> SceneController.changeScene(ppp, "pay_production_power_scene.fxml"));
                }
                else {
                    for(ProductionPower productionPowers : lightModel.getLeaderProductionPowerList()){
                        if(productionPowers.equals(productionPower)){
                            productionPowers.resetLeaderProductionPower();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
    
}
