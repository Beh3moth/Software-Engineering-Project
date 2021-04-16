package it.polimi.view;

import it.polimi.ngsw.model.Game;
import it.polimi.ngsw.model.LeaderCard;

import java.util.List;

public interface View {

    /**
     * Asks the user to choose a Nickname.
     */
    void askNickname();

    /**
     * Asks the user how many players he wants to play with.
     */
    void askPlayersNumber();

    /**
     * Allows the user to choose his LeaderCards.
     *
     * @param gods    the list of the available LeaderCards.
     */

    void askLeaderCard(List<LeaderCard> gods);


    /**
     * Shows a disconnection message.
     *
     * @param nicknameDisconnected the nickname of the player who has disconnected.
     * @param text                 a generic info text message.
     */
    void showDisconnectionMessage(String nicknameDisconnected, String text);

    /**
     * Shows an error message.
     *
     * @param error the error message to be shown.
     */
    void showErrorAndExit(String error);

    /**
     * Shows the lobby with connected players.
     *
     * @param nicknameList list of players.
     * @param numPlayers   number of players.
     */
    void showLobby(List<String> nicknameList, int numPlayers);
    /**
     * Shows to the user if the Login succeeded.
     *
     * @param nicknameAccepted     indicates if the chosen nickname has been accepted.
     * @param connectionSuccessful indicates if the connection has been successful.
     * @param nickname             the nickname of the player to be greeted.
     */
    void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname);



    /**
     * Asks the "chosen player" who he wants the game to start from.
     *
     * @param nicknameList the list of nicknames of all the players.
     */
    void askFirstPlayer(List<String> nicknameList);


    void distribuiteInitialResources(int resourcesNumber);
    /**
     * Shows a win message.
     *
     * @param winner the nickname of the winner.
     */
    void showWinMessage(String winner);


    /**
     * Shows a generic message.
     *
     * @param genericMessage the generic message to be shown.
     */
    void showGenericMessage(String genericMessage);
}
