package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.Resource;

import java.util.List;

/**
 * Message about a list of resources to pay, and a list of resources to receive
 */
public class TwoResourceListMessage extends Message {

    private List<Resource> resourcesToPay;
    private List<Resource> resourcesToReceive;
    private String action;

    public TwoResourceListMessage(String nickname, List<Resource> resourcesToPay, List<Resource> resourcesToReceive, String action) {
        super(nickname, MessageType.TWO_LIST_OF_RESOURCES);
        this.resourcesToPay = resourcesToPay;
        this.resourcesToReceive = resourcesToReceive;
        this.action = action;
    }

    public List<Resource> getResourcesToPay() {
        return resourcesToPay;
    }

    public List<Resource> getResourcesToReceive() {
        return resourcesToReceive;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "Player has chosen two lists of resources ";
    }
}
