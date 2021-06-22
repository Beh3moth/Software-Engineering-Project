package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.Resource;

import java.util.List;

/**
 * Message after reordering the warehouse
 */
public class AfterReorderMessage extends Message{

    private int isIndependent;
    private List<LeaderCard> Leaders;

    public AfterReorderMessage(String nickname, int isIndependent, List<LeaderCard> Leaders){
        super(nickname, MessageType.AFTER_REORDER);
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
        return "Continue turn after having reordered the warehouse";
    }
}
