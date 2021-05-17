package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.*;

import java.util.List;
import java.util.Map;

public class StartTurnMessage extends Message{

    private static final long serialVersionUID = -1157260256539543173L;
    private List<LeaderCard> leaderCards;
    private Marble singleMarble;
    private Marble[] firstRow;
    private Marble[] secondRow;
    private Marble[] thirdRow;
    private List<ProductionPower> leaderProductionPowerList;
    private List<DevCard> activeDevCardList;
    private ProductionPower baseProductionPower;
    private DevCard[][] devCardMarket;
    private Resource firstShelf;
    private Resource secondShelf;
    private int secondShelfNumber;
    private Resource thirdShelf;
    private int thirdShelfNumber;
    private Map<Resource, Integer> chest;

    public StartTurnMessage (String nickname, List<LeaderCard> Leaders, Marble singleMarble, Marble[] firstRow, Marble[] secondRow, Marble[] thirdRow, List<ProductionPower> leaderProductionPowerList, List<DevCard> activeDevCardList, ProductionPower baseProductionPower, DevCard[][] devCardMarket, Resource firstShelf, Resource secondShelf, int secondShelfNumber, Resource thirdShelf, int thirdShelfNumber, Map<Resource, Integer> chest) {
        super(nickname, MessageType.START_TURN);
        this.leaderCards = Leaders;
        this.singleMarble = singleMarble;
        this.firstRow = firstRow;
        this.secondRow = secondRow;
        this.thirdRow = thirdRow;
        this.leaderProductionPowerList = leaderProductionPowerList;
        this.activeDevCardList = activeDevCardList;
        this.baseProductionPower = baseProductionPower;
        this.devCardMarket = devCardMarket;
        this.firstShelf = firstShelf;
        this.secondShelf =secondShelf;
        this.secondShelfNumber = secondShelfNumber;
        this.thirdShelf = thirdShelf;
        this.thirdShelfNumber = thirdShelfNumber;
        this.chest = chest;
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

    public DevCard[][] getDevCardMarket() {
        return devCardMarket;
    }

    public Resource getFirstShelf(){return firstShelf;    }

    public Resource getSecondShelf(){return secondShelf;}

    public Resource getThirdShelf(){return thirdShelf;}

    public int getSecondShelfNumber(){return secondShelfNumber;}

    public int getThirdShelfNumber(){return thirdShelfNumber;}

    public ProductionPower getBaseProductionPower() {
        return baseProductionPower;
    }

    public Map<Resource, Integer> getChest() {
        return chest;
    }

}
