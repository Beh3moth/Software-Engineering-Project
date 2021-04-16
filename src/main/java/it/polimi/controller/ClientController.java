package it.polimi.controller;

import it.polimi.network.client.Client;
import it.polimi.network.client.SocketClient;
import it.polimi.network.message.*;
import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.Resource;
import it.polimi.observer.Observer;
import it.polimi.observer.ViewObserver;
import it.polimi.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is part of the client side.
 * It is an interpreter between the network and a generic view (which in this case can be CLI or GUI).
 * It receives the messages, wraps/unwraps and pass them to the network/view.
 */
public class ClientController implements ViewObserver, Observer{

    private final View view;

    private Client client;
    private String nickname;

    private final ExecutorService taskQueue;

    /**
     * Constructs Client Controller.
     *
     * @param view the view to be controlled.
     */
    public ClientController(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
    }

    /**
     * Validates the given IPv4 address by using a regex.
     *
     * @param ip the string of the ip address to be validated
     * @return {@code true} if the ip is valid, {@code false} otherwise.
     */
    public static boolean isValidIpAddress(String ip) {
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(regex);
    }

    /**
     * Checks if the given port string is in the range of allowed ports.
     *
     * @param portStr the ports to be checked.
     * @return {@code true} if the port is valid, {@code false} otherwise.
     */
    public static boolean isValidPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Create a new Socket Connection to the server with the updated info.
     * An error view is shown if connection cannot be established.
     *
     * @param serverInfo a map of server address and server port.
     */
    @Override
    public void onUpdateServerInfo(Map<String, String> serverInfo) {
        try {
            client = new SocketClient(serverInfo.get("address"), Integer.parseInt(serverInfo.get("port")));
            client.addObserver(this);
            client.readMessage(); // Starts an asynchronous reading from the server.
            client.enablePinger(true);
            taskQueue.execute(view::askNickname);
        } catch (IOException e) {
            taskQueue.execute(() -> view.showLoginResult(false, false, this.nickname));
        }
    }

    /**
     * Sends a message to the server with the player number chosen by the user.
     *
     * @param playersNumber the number of players.
     */
    @Override
    public void onUpdatePlayersNumber(int playersNumber) {
        client.sendMessage(new PlayerNumberReply(this.nickname, playersNumber));
    }

    @Override
    public void onUpdateLeaderCard(List<LeaderCard> chosenCard){
        client.sendMessage(new LeaderCardListMessage(this.nickname, chosenCard));
    };

    /**
     * Sends a message to the server with the nickname of the first player chosen by the user.
     *
     * @param nickname the nickname of the first player.
     */
    @Override
    public void onUpdateFirstPlayer(String nickname) {
        client.sendMessage(new FirstPlayerMessage(this.nickname, MessageType.PICK_FIRST_PLAYER, null,  nickname));
    }

    /**
     * Disconnects the client from the network.
     */
    @Override
    public void onDisconnection() {
        client.disconnect();
    }

    /**
     * Sends a message to the server with the updated nickname.
     * The nickname is also stored locally for later usages.
     *
     * @param nickname the nickname to be sent.
     */
    @Override
    public void onUpdateNickname(String nickname) {
        this.nickname = nickname;
        client.sendMessage(new LoginRequest(this.nickname));
    }

    @Override
    public void onUpdatePickedResources(int number, Resource resourceOne, Resource resourceTwo, int firstPos, int secondPos){
        client.sendMessage(new DistribuiteInitialResourcesMessage(this.nickname, MessageType.PICK_INITIAL_RESOURCES, number, resourceOne, resourceTwo, firstPos, secondPos));
    };


    public void update(Message message) {

        switch (message.getMessageType()) {
            case GENERIC_MESSAGE:
                taskQueue.execute(() -> view.showGenericMessage(((GenericMessage) message).getMessage()));
                break;
            case PICK_INITIAL_RESOURCES:
                DistribuiteInitialResourcesMessage resourceinitialMessage = (DistribuiteInitialResourcesMessage) message;
                taskQueue.execute(() -> view.distribuiteInitialResources(resourceinitialMessage.getNumber()));
                break;
            case PICK_FIRST_PLAYER:
                FirstPlayerMessage playersMessage = (FirstPlayerMessage) message;
                taskQueue.execute(() -> view.askFirstPlayer(playersMessage.getActivePlayers()));
                break;
            case LEADERCARDREQUEST:
                LeaderCardListMessage leadercardMessage = (LeaderCardListMessage) message;
                taskQueue.execute(() -> view.askLeaderCard(leadercardMessage.getLeaderCardList()));
                break;
            case LOGIN_REPLY:
                LoginReply loginReply = (LoginReply) message;
                taskQueue.execute(() -> view.showLoginResult(loginReply.isNicknameAccepted(), loginReply.isConnectionSuccessful(), this.nickname));
                break;

            default: // Should never reach this condition
                break;
        }
    }

}
