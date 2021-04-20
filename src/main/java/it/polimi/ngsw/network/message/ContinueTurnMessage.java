package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.Resource;

import java.util.List;

public class ContinueTurnMessage extends Message{
    private int turnZone;
    private int actionTypology;
    private int goneRight;
    private int wichCard;
    private List<LeaderCard> Leaders;
    public ContinueTurnMessage(String senderNickname,  int turnZone, int actionTypology, int goneRight, int wichCard, List<LeaderCard> Leaders ) {
        super(senderNickname, MessageType.CONTINUE_TURN);
        this.turnZone = turnZone;
        this.actionTypology = actionTypology;
        this.goneRight = goneRight;
        this.wichCard = wichCard;
        this.Leaders = Leaders;

    }

    public int getTurnZone(){
        return turnZone;
    }
    public int getActionTypology(){
        return actionTypology;
    }
    public int getGoneRight(){
        return goneRight;
    }

    public int getCard(){
        return wichCard;
    }
    public List<LeaderCard> getLeaders(){
        return this.Leaders;
    }
        @Override
        public String toString() {
            return "the turn continue";
        }
}
