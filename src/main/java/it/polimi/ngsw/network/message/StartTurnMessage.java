package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.Marble;
import it.polimi.ngsw.model.ProductionPower;

import java.util.List;

public class StartTurnMessage extends Message{
    private static final long serialVersionUID = -1157260256539543173L;
    private List<LeaderCard> leaderCards;
    private Marble singleMarble;
    private Marble[] firstRow;
    private Marble[] secondRow;
    private Marble[] thirdRow;

    //DevCardDashboard attributes
    private List<ProductionPower> leaderProductionPowerList;
    private List<DevCard> activeDevCardList;

    public StartTurnMessage (String nickname, List<LeaderCard> Leaders, Marble singleMarble, Marble[] firstRow, Marble[] secondRow, Marble[] thirdRow, List<ProductionPower> leaderProductionPowerList, List<DevCard> activeDevCardList) {
        super(nickname, MessageType.START_TURN);
        this.leaderCards = Leaders;
        this.singleMarble = singleMarble;
        this.firstRow = firstRow;
        this.secondRow = secondRow;
        this.thirdRow = thirdRow;
        this.leaderProductionPowerList = leaderProductionPowerList;
        this.activeDevCardList = activeDevCardList;
    }

    public List<LeaderCard> getLeaders(){
        return this.leaderCards;
    }

    public Marble getSingleMarble(){
        return this.singleMarble;
    }

    public Marble[] getFirstRow(){
        return this.firstRow;
    }

    public Marble[] getSecondRow(){
        return this.secondRow;
    }
    public Marble[] getThirdRow(){
        return this.thirdRow;
    }

    public List<ProductionPower> getLeaderProductionPowerList() {
        return leaderProductionPowerList;
    }

    public List<DevCard> getActiveDevCardList() {
        return activeDevCardList;
    }

    @Override
    public String toString() {
        return " Turn starter ";
    }
}
