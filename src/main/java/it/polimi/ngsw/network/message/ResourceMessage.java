package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.Resource;

public class ResourceMessage extends  Message {

    private Resource resource;

    public ResourceMessage(String nickname, Resource resource) {
        super(nickname, MessageType.RESOURCE);
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

}
