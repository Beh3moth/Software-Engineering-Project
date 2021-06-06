package it.polimi.ngsw.controller;

import it.polimi.ngsw.model.*;
import it.polimi.ngsw.model.PhaseType;
import it.polimi.ngsw.view.VirtualView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This Class contains all methods used to manage every single turn of the match.
 */
public class TurnController implements Serializable {

    private static final long serialVersionUID = -5987205913389392005L;
    private final Game game;
    private final List<String> nicknameQueue;
    private String activePlayer;


    transient Map<String, VirtualView> virtualViewMap;
    private PhaseType phaseType;

    private final GameController gameController;

    /**
     * Constructor of the Turn Controller.
     *
     * @param virtualViewMap Virtual View Map of all Clients.
     * @param gameController Game Controller.
     */
    public TurnController(Map<String, VirtualView> virtualViewMap, GameController gameController) {
        this.game = Game.getInstance();
        this.nicknameQueue = new ArrayList<>(game.getPlayersNicknames());

        this.activePlayer = nicknameQueue.get(0); // set first active player
        this.virtualViewMap = virtualViewMap;
        this.gameController = gameController;
    }



    /**
     * @return the nickname of the active player.
     */
    public String getActivePlayer() {
        return activePlayer;
    }

    /**
     * Set the active player.
     *
     * @param activePlayer the active Player to be set.
     */
    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * Set next active player.
     */
    public void next() {
        int currentActive = nicknameQueue.indexOf(activePlayer);
        if (currentActive + 1 < game.getNumCurrentPlayers()) {
            currentActive = currentActive + 1;
        } else {
            currentActive = 0;
        }
        activePlayer = nicknameQueue.get(currentActive);
    }

    /**
     * Initialize a new Turn.
     */
    public void newTurn() {

        turnControllerNotify("Turn of " + activePlayer, activePlayer);
        VirtualView vv = virtualViewMap.get(getActivePlayer());
        setPhaseType(PhaseType.START_TURN);
        List<LeaderCard> Leaders = game.getPlayerByNickname(getActivePlayer()).getLeaderCards();
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
        List<ProductionPower> leaderProductionPowerList = game.getPlayerByNickname(getActivePlayer()).getDevCardDashboard().getLeaderProductionPowerList();
        ProductionPower baseProductionPower = game.getPlayerByNickname(getActivePlayer()).getDevCardDashboard().getBaseProductionPower();
        Map<Integer, DevCard> activeDevCardMap = game.getPlayerByNickname(getActivePlayer()).getDevCardDashboard().getActiveDevCardsAsMap();
        List<ProductionPower> productionPowerList = game.getPlayerByNickname(getActivePlayer()).getDevCardDashboard().getActiveProductionPowerList();
        DevCard[][] devCardMarket = game.getBoard().getDevCardMarket();
        Resource firstShelf = game.getPlayerByNickname(getActivePlayer()).getWarehouse().getShelf(1).getResourceType();
        Resource secondShelf = game.getPlayerByNickname(getActivePlayer()).getWarehouse().getShelf(2).getResourceType();
        int secondShelfNumber = game.getPlayerByNickname(getActivePlayer()).getWarehouse().getShelf(2).getResourceNumber();
        Resource thirdShelf = game.getPlayerByNickname(getActivePlayer()).getWarehouse().getShelf(3).getResourceType();
        int thirdShelfNumber = game.getPlayerByNickname(getActivePlayer()).getWarehouse().getShelf(3).getResourceNumber();
        Map<Resource, Integer> chest = game.getPlayerByNickname(getActivePlayer()).getChest().getResourcesAsMap();
        int crossPosition = game.getPlayerByNickname(getActivePlayer()).getFaithPath().getCrossPosition();
        int victoryPoints = game.getPlayerByNickname(getActivePlayer()).getPV();
        boolean papalCardOne = game.getPlayerByNickname(getActivePlayer()).getFaithPath().getPapalCardOne();
        boolean papalCardTwo = game.getPlayerByNickname(getActivePlayer()).getFaithPath().getPapalCardTwo();
        boolean papalCardThree = game.getPlayerByNickname(getActivePlayer()).getFaithPath().getPapalCardThree();
        vv.startTurnMessage(Leaders,singleMarble, firstRow, secondRow, thirdRow, leaderProductionPowerList, activeDevCardMap, baseProductionPower, devCardMarket, firstShelf, secondShelf, secondShelfNumber, thirdShelf, thirdShelfNumber, chest, crossPosition, victoryPoints, papalCardOne, papalCardTwo, papalCardThree);

    }
    /**
     * Sends a Match Info Message to all the players.
     * Sends a Generic Message which contains Turn Information to all players but the one specified in the second argument.
     *
     * @param messageToNotify Message to send.
     * @param excludeNickname name of the player to be excluded from the broadcast.
     */
    public void turnControllerNotify(String messageToNotify, String excludeNickname) {
        virtualViewMap.entrySet().stream()
                .filter(entry -> !excludeNickname.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(vv -> vv.showGenericMessage(messageToNotify));
    }


    /**
     * Set the current Phase Type.
     *
     * @param turnPhaseType Phase Type.
     */
    public void setPhaseType(PhaseType turnPhaseType) {
        this.phaseType = turnPhaseType;
    }
    /**
     * Returns a list of Players' nicknames.
     *
     * @return a list of String.
     */
    public List<String> getNicknameQueue() {
        return nicknameQueue;
    }
}
