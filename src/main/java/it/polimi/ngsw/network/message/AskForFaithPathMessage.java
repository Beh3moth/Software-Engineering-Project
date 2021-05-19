package it.polimi.ngsw.network.message;

public class AskForFaithPathMessage extends Message{

    public AskForFaithPathMessage(String nickname) {
        super(nickname, MessageType.ASK_FOR_FAITH_PATH);
    }
    @Override
    public String toString() {
        return "asking the faithpath";
    }
}
