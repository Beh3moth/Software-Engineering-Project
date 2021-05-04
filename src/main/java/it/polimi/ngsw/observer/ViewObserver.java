package it.polimi.ngsw.observer;


import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.Resource;
import java.util.List;
import java.util.Map;

/**
 * Custom observer interface for views. It supports different types of notification.
 */
public interface ViewObserver {

    /**
     * Create a new connection to the server with the updated info.
     *
     * @param serverInfo a map of server address and server port.
     */
    void onUpdateServerInfo(Map<String, String> serverInfo);

    /**
     * Sends a message to the server with the updated nickname.
     *
     * @param nickname the nickname to be sent.
     */
    void onUpdateNickname(String nickname);

    /**
     * Sends a message to the server with the player number chosen by the user.
     *
     * @param playersNumber the number of players.
     */
    void onUpdatePlayersNumber(int playersNumber);

    void onUpdateLeaderCard(List<LeaderCard> chosenCard);

    void onUpdatePickedResources(int number, Resource resourceOne, Resource resourceTwo, int FirstPos, int SecondPos);

    /**
     * Sends a message to the server with the nickname of the first player chosen by the user.
     *
     * @param nickname the nickname of the first player.
     */
    void onUpdateFirstPlayer(String nickname);


    void onUpdateLeaderCardActivation(int chosenCard, int turnZone);

    void onUpdateDiscardCard(int wichCard,int turnZone);

    void onUpdateBuyFromMarket(int rowOrColumn, int wichOne);

    void onUpdateReorderWarehouse(boolean isIndipendent);
    /**
     * Handles a disconnection wanted by the user.
     * (e.g. a click on the back button into the GUI).
     */
    void onDisconnection();

    void onUpdateNewWarehouse(Resource newFirstShelf, List<Resource> newSecondShelf, List<Resource> newThirdShelf, List<Resource> newFirstSpecialShelf, List<Resource> newSecondSpecialShelf, List<Resource> discardList, Boolean isIndependent);

    void onEndTurn();

    //Production power methods

}