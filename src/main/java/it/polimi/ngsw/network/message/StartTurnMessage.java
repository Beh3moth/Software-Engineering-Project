package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.*;

import java.util.List;
import java.util.Map;

/**
 * Message of the begining of the turn, it transport all the info needed by the player to play the game
 */
public class StartTurnMessage extends Message{

    private static final long serialVersionUID = -1157260256539543173L;
    private List<LeaderCard> leaderCards;
    private Marble singleMarble;
    private Marble[] firstRow;
    private Marble[] secondRow;
    private Marble[] thirdRow;
    private List<ProductionPower> leaderProductionPowerList;
    private Map<Integer, DevCard> activeDevCardMap;
    private ProductionPower baseProductionPower;
    private DevCard[][] devCardMarket;
    private Resource firstShelf;
    private Resource secondShelf;
    private int secondShelfNumber;
    private Resource thirdShelf;
    private int thirdShelfNumber;
    private Map<Resource, Integer> chest;
    private int crossPosition;
    private int victoryPoints;
    private boolean papalCardOne;
    private boolean papalCardTwo;
    private boolean papalCardThree;
    private Resource fsr;
    private int fsn;
    private Resource ssr;
    private int ssn;

    public StartTurnMessage (String nickname, List<LeaderCard> Leaders, Marble singleMarble, Marble[] firstRow, Marble[] secondRow, Marble[] thirdRow, List<ProductionPower> leaderProductionPowerList, Map<Integer, DevCard> activeDevCardMap, ProductionPower baseProductionPower, DevCard[][] devCardMarket, Resource firstShelf, Resource secondShelf, int secondShelfNumber, Resource thirdShelf, int thirdShelfNumber, Map<Resource, Integer> chest, int crossPosition, int victoryPoints, boolean papalCardOne, boolean papalCardTwo, boolean papalCardThree, Resource fsr, int fsn, Resource ssr, int ssn) {
        super(nickname, MessageType.START_TURN);
        this.leaderCards = Leaders;
        this.singleMarble = singleMarble;
        this.firstRow = firstRow;
        this.secondRow = secondRow;
        this.thirdRow = thirdRow;
        this.leaderProductionPowerList = leaderProductionPowerList;
        this.activeDevCardMap = activeDevCardMap;
        this.baseProductionPower = baseProductionPower;
        this.devCardMarket = devCardMarket;
        this.firstShelf = firstShelf;
        this.secondShelf =secondShelf;
        this.secondShelfNumber = secondShelfNumber;
        this.thirdShelf = thirdShelf;
        this.thirdShelfNumber = thirdShelfNumber;
        this.chest = chest;
        this.crossPosition = crossPosition;
        this.victoryPoints = victoryPoints;
        this.papalCardOne = papalCardOne;
        this.papalCardTwo = papalCardTwo;
        this.papalCardThree = papalCardThree;
        this.fsr = fsr;
        this.fsn = fsn;
        this.ssr = ssr;
        this.ssn = ssn;
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

    public Map<Integer, DevCard> getActiveDevCardMap() {
        return activeDevCardMap;
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

    @Override
    public String toString() {
        return " Turn has started ";
    }

    public int getCrossPosition() {
        return crossPosition;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public boolean isPapalCardOne() {
        return papalCardOne;
    }

    public boolean isPapalCardTwo() {
        return papalCardTwo;
    }

    public boolean isPapalCardThree() {
        return papalCardThree;
    }

    public Resource getFsr(){
        return fsr;
    }
    public Resource getSsr(){
        return ssr;
    }
    public int getFsn(){
        return fsn;
    }
    public int getSsn(){
        return ssn;
    }
}
