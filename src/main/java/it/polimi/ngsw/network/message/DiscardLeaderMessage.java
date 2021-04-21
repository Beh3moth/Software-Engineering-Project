package it.polimi.ngsw.network.message;

public class DiscardLeaderMessage extends Message{
    private static final long serialVersionUID = 130412160057182598L;
    private int leaderCardChosen;
    private int turnZone;
    public DiscardLeaderMessage(String nickname,  int chosen, int turnZone) {
        super(nickname, MessageType.DISCARD_CARD);
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
        return " Discard " + getCardChosen() + "° LeaderCard ";
    }
}
