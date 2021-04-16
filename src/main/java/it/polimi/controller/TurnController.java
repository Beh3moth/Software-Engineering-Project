package it.polimi.controller;

import it.polimi.network.message.Message;
import it.polimi.ngsw.model.Game;
import it.polimi.ngsw.model.PhaseType;
import it.polimi.view.VirtualView;

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
     * Returns a list of Players' nicknames.
     *
     * @return a list of String.
     */
    public List<String> getNicknameQueue() {
        return nicknameQueue;
    }
}
