package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.Resource;

import java.util.List;
import java.util.Map;

public class WatchOtherPlayerInfoMessage extends Message{

    private String nicknameOtherPlayer;
    private Boolean goneRight;
    private int crossPosition;
    private Map<Resource, Integer> resourceAsMap;
    private List<DevCard> activeDevCards;
    private int[] shelfNumber;
    private Resource[] shelfResource;
    public WatchOtherPlayerInfoMessage(String nickname, String nicknameOtherPlayer, Boolean goneRight, int crossPosition, Map<Resource, Integer> resourcesAsMap, List<DevCard> activeDevCards, int[] shelfResNumber, Resource[] shelfResType) {
        super(nickname, MessageType.WATCH_OTHER_PLAYER);
        this.nicknameOtherPlayer = nicknameOtherPlayer;
        this.goneRight = goneRight;
        this.crossPosition = crossPosition;
        this.resourceAsMap = resourcesAsMap;
        this.activeDevCards = activeDevCards;
        this.shelfNumber = shelfResNumber;
        this.shelfResource = shelfResType;
    }
    public Boolean getGoneRight(){return goneRight;}
    public int getCrossPosition(){return crossPosition;}
    public Map<Resource, Integer> getResourceAsMap(){return resourceAsMap;}
    public List<DevCard> getActiveDevCards(){return activeDevCards;}
    public int[] getShlefNumber(){return shelfNumber;}
    public Resource[] getShelfResource(){return shelfResource;}
    public String getNicknameOtherPlayer(){
        return nicknameOtherPlayer;
    }
    @Override
    public String toString() {
        return "WatchOtherPlayerMessage " +
                "nickname = " + getNickname();
    }
}
