package it.polimi.ngsw.network.message;


/**
 * This enum contains all the message type available and used by the server and clients.
 */
public enum MessageType {
    LOGIN_REQUEST, LOGIN_REPLY,
    PLAYERNUMBER_REQUEST, PLAYERNUMBER_REPLY,
    LOBBY,
    LEADERCARDREQUEST,
    PICK_FIRST_PLAYER,
    PICK_INITIAL_RESOURCES,
    START_TURN,
    LEADER_CARD_RESPONSE,
    CONTINUE_TURN,
    BUY_MARKET,
    DISCARD_CARD,
    DISCONNECTION,
    NEWRESOURCE,
    REORDER_WAREHOUSE,
    GENERIC_MESSAGE,
    PING,
    ERROR,
   NEW_WAREHOUSE;
}
