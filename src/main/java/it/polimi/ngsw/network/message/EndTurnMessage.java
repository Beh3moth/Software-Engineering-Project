package it.polimi.ngsw.network.message;

/**
 * End of the turn message
 */
public class EndTurnMessage extends Message{
    public EndTurnMessage(String nickname){
        super(nickname, MessageType.END_TURN);
    }
    @Override
    public String toString() {
        return "The player has finished his turn";
    }
}
