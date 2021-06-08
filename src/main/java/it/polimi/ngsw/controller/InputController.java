package it.polimi.ngsw.controller;
import java.io.Serializable;
import it.polimi.ngsw.model.*;
import it.polimi.ngsw.network.message.*;
import it.polimi.ngsw.view.View;
import it.polimi.ngsw.view.VirtualView;

import java.util.Map;

/**
 * This Class verifies that all messages sent by client contain valid information.
 */
public class InputController implements Serializable {

    private static final long serialVersionUID = 7413156215358698632L;

    private final Game game;
    private transient Map<String, VirtualView> virtualViewMap;
    private final GameController gameController;

    /**
     * Constructor of the Input Controller Class.
     *
     * @param virtualViewMap Virtual View Map.
     * @param gameController Game Controller.
     */
    public InputController(Map<String, VirtualView> virtualViewMap, GameController gameController, Game game) {
        this.game = game;
        this.virtualViewMap = virtualViewMap;
        this.gameController = gameController;
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
     * Check if message is sent from active player.
     *
     * @param receivedMessage message from client.
     * @return {@code true} if correct {@code false} otherwise.
     */
    public boolean checkUser(Message receivedMessage) {
        return receivedMessage.getNickname().equals(gameController.getTurnController().getActivePlayer());
    }
    public boolean verifyReceivedData(Message message) {

        switch (message.getMessageType()) {
            case LOGIN_REPLY: // server doesn't receive a LOGIN_REPLY.
                return false;
            default: // Never should reach this statement.
                return false;
        }

    }
}
