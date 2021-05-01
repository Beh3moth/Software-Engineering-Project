package it.polimi.ngsw.view;

import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.Marble;
import it.polimi.ngsw.model.Resource;

import java.util.List;
import java.util.Map;

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
     * @param leaderCards    the list of the available LeaderCards.
     */

    void askLeaderCard(List<LeaderCard> leaderCards);



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


    void startTurnMessage(List<LeaderCard> Leaders, Marble singleMarble, Marble[] firstRow, Marble[] secondRow, Marble[] thirdRow );

    void continueTurn(int turnZone, int actionTypology, int goneRight, int wichCard, List<LeaderCard> Leaders);

    void buyMarketResource(List<Resource> resources, Resource firstWhite, Resource secondWhite);
    void reorderWarehouse(Map<Resource, Integer> mapResources, Resource firstLevel, Resource secondLevel);
    /**
     * Shows a generic message.
     *
     * @param genericMessage the generic message to be shown.
     */
    void showGenericMessage(String genericMessage);
}
