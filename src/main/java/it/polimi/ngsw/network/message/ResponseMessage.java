package it.polimi.ngsw.network.message;

public class ResponseMessage extends Message {

    private boolean response;
    private String action;

    public ResponseMessage(String nickname, boolean response, String action) {
        super(nickname, MessageType.PRODUCTION_POWER_RESPONSE_MESSAGE);
        this.response = response;
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public boolean isResponse() {
        return response;
    }
}
