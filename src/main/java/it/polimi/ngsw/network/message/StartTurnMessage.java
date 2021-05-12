package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.*;

import java.util.List;

public class StartTurnMessage extends Message{
    private static final long serialVersionUID = -1157260256539543173L;
    private List<LeaderCard> leaderCards;
    private Marble singleMarble;
    private Marble[] firstRow;
    private Marble[] secondRow;
    private Marble[] thirdRow;
    private List<ProductionPower> leaderProductionPowerList;
    private List<DevCard> activeDevCardList;
    private List<ProductionPower> productionPowerList;
    private DevCard[][] devCardMarket;
    private Resource firstShelf;
    private Resource secondShelf;
    private int secondShelfNumber;
    private Resource thirdShelf;
    private int thirdShelfNumber;

    public StartTurnMessage (String nickname, List<LeaderCard> Leaders, Marble singleMarble, Marble[] firstRow, Marble[] secondRow, Marble[] thirdRow, List<ProductionPower> leaderProductionPowerList, List<DevCard> activeDevCardList, List<ProductionPower> productionPowerList, DevCard[][] devCardMarket, Resource firstShelf, Resource secondShelf, int secondShelfNumber, Resource thirdShelf, int thirdShelfNumber) {
        super(nickname, MessageType.START_TURN);
        this.leaderCards = Leaders;
        this.singleMarble = singleMarble;
        this.firstRow = firstRow;
        this.secondRow = secondRow;
        this.thirdRow = thirdRow;
        this.leaderProductionPowerList = leaderProductionPowerList;
        this.activeDevCardList = activeDevCardList;
        this.productionPowerList = productionPowerList;
        this.devCardMarket = devCardMarket;
        this.firstShelf = firstShelf;
        this.secondShelf =secondShelf;
        this.secondShelfNumber = secondShelfNumber;
        this.thirdShelf = thirdShelf;
        this.thirdShelfNumber = thirdShelfNumber;
    }

    @Override
    public String toString() {
        return " Turn starter ";
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

    public List<ProductionPower> getProductionPowerList() {
        return productionPowerList;
    }

    public DevCard[][] getDevCardMarket() {
        return devCardMarket;
    }

    public Resource getFirstShelf(){return firstShelf;    }
    public Resource getSecondShelf(){return secondShelf;}
    public Resource getThirdShelf(){return thirdShelf;}
    public int getSecondShelfNumber(){return secondShelfNumber;}
    public int getThirdShelfNumber(){return thirdShelfNumber;}
}
