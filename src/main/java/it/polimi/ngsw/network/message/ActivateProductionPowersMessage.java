package it.polimi.ngsw.network.message;

public class ActivateProductionPowersMessage extends Message {

    public ActivateProductionPowersMessage(String nickname) {
        super(nickname, MessageType.ACTIVATE_PRODUCTION_POWERS);
    }
}
