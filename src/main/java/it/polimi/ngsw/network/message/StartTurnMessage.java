package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.LeaderCard;

import java.util.List;

public class StartTurnMessage extends Message{
    private static final long serialVersionUID = -1157260256539543173L;
    private List<LeaderCard> leaderCards;
    public StartTurnMessage(String nickname, List<LeaderCard> Leaders) {
        super(nickname, MessageType.START_TURN);
        this.leaderCards = Leaders;
    }

    public List<LeaderCard> getLeaders(){
        return this.leaderCards;
    }

    @Override
    public String toString() {
        return " Turn starter ";
    }

}
