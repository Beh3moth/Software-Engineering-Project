package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.*;

import java.util.List;

/**
 * Message for the new resources
 */
public class NewResourcesMessage extends Message{

    private static final long serialVersionUID = -345102443683001469L;
    private List<Resource> resources;
    private Resource firstResource;
    private Resource secondResource;
    public NewResourcesMessage(String nickname, List<Resource> resources, Resource firstResource, Resource secondResource){
        super(nickname, MessageType.NEWRESOURCE);
        this.resources = resources;
        this.firstResource = firstResource;
        this.secondResource =secondResource;
    }
    public List<Resource> getResources()
    {
        return this.resources;
    }
    public Resource getFirstResource(){
        return firstResource;
    }
    public Resource getSecondResource(){
        return secondResource;
    }


    @Override
    public String toString() {
        return "NewResourcesMessage{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                '}';
    }
}
