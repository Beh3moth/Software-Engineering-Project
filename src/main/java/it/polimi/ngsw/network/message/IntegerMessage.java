package it.polimi.ngsw.network.message;

public class IntegerMessage extends Message{

    private int integer;
    private String action;

    public IntegerMessage(String nickname, int integer, String action) {
        super(nickname, MessageType.INTEGER);
        this.integer = integer;
        this.action = action;
    }

    public int getInteger() {
        return integer;
    }


    @Override
    public String toString() {
        return " The player has chosen " + getInteger();
    }

    public String getAction() {
        return action;
    }
}
