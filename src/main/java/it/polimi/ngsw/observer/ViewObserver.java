package it.polimi.ngsw.observer;


import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.ProductionPower;
import it.polimi.ngsw.model.Resource;
import it.polimi.ngsw.network.message.ProductionPowerResourceMessage;

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

    /**
     * Sends a message to the server with the chosen initial leader card
     * @param chosenCard the two leadercard
     */
    void onUpdateLeaderCard(List<LeaderCard> chosenCard);

    /**
     * Sends a message to the server with the chosen initial resources
     * @param number of picked resources
     * @param resourceOne the first resource
     * @param resourceTwo the second resource
     * @param FirstPos the first position of the first resource
     * @param SecondPos the second position of the second resource
     */
    void onUpdatePickedResources(int number, Resource resourceOne, Resource resourceTwo, int FirstPos, int SecondPos);

    /**
     * Sends a message to the server with the nickname of the first player chosen by the user.
     *
     * @param nickname the nickname of the first player.
     */
    void onUpdateFirstPlayer(String nickname);

    /**
     * Sends a message to the server with the activation of the leadercard
     * @param chosenCard the chosen card out of two
     * @param turnZone the turn zone, if before or after the main move
     */
    void onUpdateLeaderCardActivation(int chosenCard, int turnZone);

    /**
     * Sends a message to the server with the doscarded leaderCard
     * @param wichCard wich card
     * @param turnZone the turn zone
     */
    void onUpdateDiscardLeaderCard(int wichCard, int turnZone);

    /**
     * Sends a message to the server with the action "Buy from market"
     * @param rowOrColumn wich sector
     * @param wichOne wich one
     */
    void onUpdateBuyFromMarket(int rowOrColumn, int wichOne);

    /**
     * Sends a message to the server with the action "reorder warehouse"
     * @param isIndipendent if it is or not after the buy from market
     */
    void onUpdateReorderWarehouse(boolean isIndipendent);
    /**
     * Handles a disconnection wanted by the user.
     */
    void onDisconnection();

    /**
     * Sends a message to the server with the new warehouse
     * @param newFirstShelf new first shelf
     * @param newSecondShelf new second shelf
     * @param newThirdShelf new third shelf
     * @param newFirstSpecialShelf new first special shelf
     * @param newSecondSpecialShelf new special second shelf
     * @param discardList the discarded list of resources
     * @param isIndependent if it is or not after the buy from market
     */
    void onUpdateNewWarehouse(Resource newFirstShelf, List<Resource> newSecondShelf, List<Resource> newThirdShelf, List<Resource> newFirstSpecialShelf, List<Resource> newSecondSpecialShelf, List<Resource> discardList, Boolean isIndependent);

    /**
     * Message at the end of the turn
     */
    void onEndTurn();

    //Production power methods

    /**
     * Sends a message to the server with the list of production powers
     * @param productionPowerList the list
     * @param action string of the type of the action
     */
    void onUpdateProductionPowerList(List<ProductionPower> productionPowerList, String action);

    void onUpdateIntegerChosen(int productionPowerChosen, String action);

    /**
     * Sends a message to the server with two list of resources, one to pay, one to receive
     * @param resourcesToPay resource to pay
     * @param resourcesToReceive resources ti receive
     * @param action the type of action
     */
    void onUpdateTwoResourceList(List<Resource> resourcesToPay, List<Resource> resourcesToReceive, String action);

    /**
     * Sends a message to the server with the resources to pay
     * @param isWarehouse if it is from warehouse
     * @param shelfLevel the level of the shelf
     * @param resourceType the type of the resource
     * @param productionPower the production power
     */
    void onUpdatePayProductionPower(Boolean[] isWarehouse, Integer[] shelfLevel, Resource[] resourceType, ProductionPower productionPower);

    /**
     * Sends a message to the server with the resources of the production power
     * @param resource the resource
     * @param productionPower the production power
     */
    void onUpdateProductionPowerResource(Resource resource, ProductionPower productionPower);

    /**
     * Sends a message to the server with the activation of the production power
     */
    void onUpdateProductionPowerActivation();

    /**
     * Sends a message to the server with chosen devcard
     * @param level the level
     * @param column the column
     * @param slotToPut the slot to put the devcard
     */
    void onUpdateChooseDevCard(int level, int column, int slotToPut);

    //devCard methods

    /**
     * Sends a message to the server with the payment of the devcard
     * @param isWarehouse if is from warehouse
     * @param shelfLevel the level of the shelf
     * @param resourceType the type of the resource
     * @param devCard the devcard
     * @param slotToPut where to put the devcard
     * @param discountPowerOne the ability discount one
     * @param discountPowerTwo the ability discout two
     */
    void onUpdatePayDevCard(Boolean[] isWarehouse, Integer[] shelfLevel, Resource[] resourceType, DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo);

    /**
     * Sends a message to the server with the action "Watch other player info"
     * @param nicknameOtherPlayer the nickname of the player
     */
    void onUpdateWatchInfo(String nicknameOtherPlayer);

    //Faith path message

    void onUpdateAskForFaithPath();

    /**
     * Sends a message to the server with the PV of the players at the end of the game
     */
    void onUpdateCalculatePVEndGame();

}