package it.polimi.ngsw.network.message;

public class IntegerMessage extends Message{

    private int integer;

    IntegerMessage(String nickname, MessageType messageType, int integer) {
        super(nickname, messageType);
        this.integer = integer;
    }

    public int getInteger() {
        return integer;
    }
}
