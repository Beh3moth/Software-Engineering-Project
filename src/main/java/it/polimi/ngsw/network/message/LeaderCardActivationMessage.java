package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.LeaderCard;

import java.util.List;

public class LeaderCardActivationMessage extends Message{
    private static final long serialVersionUID = -3735443375793802589L;
    private int leaderCardChosen;
    private int turnZone;
    public LeaderCardActivationMessage(String nickname,  int chosen, int turnZone) {
        super(nickname, MessageType.LEADER_CARD_RESPONSE);
        this.leaderCardChosen = chosen;
        this.turnZone = turnZone;
    }

    public int getCardChosen(){
        return leaderCardChosen;
    }

    public int getTurnZone(){
        return  turnZone;
    }

    @Override
    public String toString() {
        return " Activation " + getCardChosen() + "° LeaderCard ";
    }
}
