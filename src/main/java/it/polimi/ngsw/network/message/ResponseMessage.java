package it.polimi.ngsw.network.message;

/**
 * A message of the response
 */
public class ResponseMessage extends Message {

    private boolean response;
    private String action;

    public ResponseMessage(String nickname, boolean response, String action, MessageType messageType) {
        super(nickname, messageType);
        this.response = response;
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public boolean isResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                '}';
    }

}
