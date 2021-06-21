package it.polimi.ngsw.network.message;

/**
 * Message to activate a production power
 */
public class ActivateProductionPowersMessage extends Message {

    public ActivateProductionPowersMessage(String nickname) {
        super(nickname, MessageType.ACTIVATE_PRODUCTION_POWERS);
    }

    @Override
    public String toString() {
        return "Player " + this.getNickname() + " active a production power ";
    }
}
