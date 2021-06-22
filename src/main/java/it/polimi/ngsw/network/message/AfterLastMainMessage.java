package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.LeaderCard;

import java.util.List;

/**
 * Message that permit to finish the turn for the last time of the game
 */
public class AfterLastMainMessage extends Message{
    private int isIndependent;
    private List<LeaderCard> Leaders;

    public AfterLastMainMessage(String nickname, int isIndependent, List<LeaderCard> Leaders){
        super(nickname, MessageType.AFTER_LAST_MAIN);
        this.isIndependent = isIndependent;
        this.Leaders = Leaders;
    }

    public int getIsIndependent(){
        return isIndependent;
    }

    public List<LeaderCard> getLeaders(){
        return Leaders;
    }

    @Override
    public String toString() {
        return "Continue turn for the last time";
    }
}
