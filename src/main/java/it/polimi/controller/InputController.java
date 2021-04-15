package it.polimi.controller;
import java.io.Serializable;
import it.polimi.ngsw.model.*;
import it.polimi.network.message.*;
import it.polimi.network.server.*;
import it.polimi.view.View;
import it.polimi.view.VirtualView;
import java.util.List;
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
    public InputController(Map<String, VirtualView> virtualViewMap, GameController gameController) {
        this.game = Game.getInstance();
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
            case BOARD: // server doesn't never receive a BOARD.
                return false;
           /* case BUILD:
                return buildCheck(message);
            case INIT_COLORS:
                return colorCheck(message);
            case GENERIC_MESSAGE: // server doesn't receive a GENERIC_MESSAGE.
                return false;
            case GODLIST:
                return godListCheck(message);
            case LOGIN_REPLY: // server doesn't receive a LOGIN_REPLY.
                return false;
            case MOVE:
                return moveCheck(message);
            case PICK_MOVING_WORKER:
                return pickMovingCheck(message);
            case PLAYERNUMBER_REPLY:
                return playerNumberReplyCheck(message);
            case PLAYERNUMBER_REQUEST: // server doesn't receive a GenericErrorMessage.
                return false;
            case INIT_WORKERSPOSITIONS:
                return workerPositionsCheck(message);*/
            default: // Never should reach this statement.
                return false;
        }

    }
}
