package it.polimi.network.message;


/**
 * This enum contains all the message type available and used by the server and clients.
 */
public enum MessageType {
    LOGIN_REQUEST, LOGIN_REPLY,
    PLAYERNUMBER_REQUEST, PLAYERNUMBER_REPLY,
    LOBBY,
    LEADERCARDREQUEST,
    GODLIST,
    PICK_FIRST_PLAYER,
    PICK_INITIAL_RESOURCES,
    WIN,
    WIN_FX,
    LOSE,

    //utility:
    GAME_LOAD,
    MATCH_INFO,
    DISCONNECTION,
    GENERIC_MESSAGE,
    PING,
    ERROR,
    ENABLE_EFFECT,
    APPLY_EFFECT,
    PERSISTENCE
}
