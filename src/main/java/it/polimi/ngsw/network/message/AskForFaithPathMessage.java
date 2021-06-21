package it.polimi.ngsw.network.message;

/**
 * Method that ask for the faith path of the plyer
 */
public class AskForFaithPathMessage extends Message{

    public AskForFaithPathMessage(String nickname) {
        super(nickname, MessageType.ASK_FOR_FAITH_PATH);
    }
    @Override
    public String toString() {
        return "Asking for the faithpath";
    }
}
