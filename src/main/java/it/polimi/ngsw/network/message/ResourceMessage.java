package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.Resource;

public class ResourceMessage extends  Message {

    private Resource resource;

    public ResourceMessage(String nickname, Resource resource, MessageType messageType) {
        super(nickname, messageType);
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return "send the resource of the warehouse";
    }

}
