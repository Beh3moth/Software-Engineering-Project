package it.polimi.network.message;
import it.polimi.ngsw.model.Resource;
import java.util.List;
public class DistribuiteInitialResourcesMessage extends Message {
    private int resourcesNumber;
    private Resource firstRes;
    private Resource secondRes;
    private int firstPos;
    private int secondPos;
    public DistribuiteInitialResourcesMessage(String senderNickname, MessageType messageType, int resourcesNumber, Resource first, Resource second, int firstPosition, int secondPosition ){
        super(senderNickname, messageType);
        this.resourcesNumber = resourcesNumber;
        this.firstRes = first;
        this.secondRes = second;
        this.firstPos = firstPosition;
        this.secondPos = secondPosition;
    }

    public int getNumber(){
        return resourcesNumber;
    }

    public int getFirstPosition(){
        return firstPos;
    }
    public int getSecondPosition(){
        return secondPos;
    }
    public Resource getFirstResource(){
        return firstRes;
    }
    public Resource getSecondResource(){
        return secondRes;
    }
    @Override
    public String toString() {
        return "InitialDistribuitionMesssage{" +

                '}';
    }
}