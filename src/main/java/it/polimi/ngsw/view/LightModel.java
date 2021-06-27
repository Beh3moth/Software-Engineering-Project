package it.polimi.ngsw.view;

import it.polimi.ngsw.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents the model saved in the client, it is made of getter and setter
 */
public class LightModel {

    private List<ProductionPower> leaderProductionPowerList;
    private List<LeaderCard> leaderCardList;
    private List<Integer> chosenIntegerList = new ArrayList<>();
    private List<ProductionPower> paidProductionPowerList = new ArrayList<>();
    //The possible Integers values are 0,1,2 and they represent the DevCard of a certain DevCardSpace.
    private Map<Integer, DevCard> activeDevCardMap;
    private ProductionPower baseProductionPower;
    private Map<Resource, Integer> chest;
    private int crossPosition;
    private int victoryPoints;
    private boolean papalCardOne;
    private boolean papalCardTwo;
    private boolean papalCardThree;
    private Marble singleMarble;
    private Marble[] firstRow;
    private Marble[] secondRow;
    private Marble[] thirdRow;
    private DevCard[][] devCardMarket;
    private boolean gameFinished;
    private Resource firstShelf;
    private Resource secondShelf;
    private int secondShelfNumber;
    private Resource thirdShelf;
    private int thirdShelfNumber;
    private Resource fsr;
    private int fsn;
    private Resource ssr;
    private int ssn;
    //1 means not activated but usable, 0 means discarded, 2 means activated
    private int[] leaderCardStatus = new int[]{1, 1};
    Integer lawrencePosition;

    public Resource getFirstShelf() {
        return firstShelf;
    }

    public void setFirstShelf(Resource first) {
        this.firstShelf = first;
    }

    public void setSecondShelfNumber(int second) {
        this.secondShelfNumber = second;
    }

    public void setSecondShelf(Resource second) {
        this.secondShelf = second;
    }

    public void setThirdShelf(Resource third) {
        this.thirdShelf = third;
    }

    public void setThirdShelfNumber(int third) {
        this.thirdShelfNumber = third;
    }

    public Resource getSecondShelf() {
        return secondShelf;
    }

    public Resource getThirdShelf() {
        return thirdShelf;
    }

    public int getSecondShelfNumber() {
        return secondShelfNumber;
    }

    public int getThirdShelfNumber() {
        return thirdShelfNumber;
    }

    public LightModel() {
        this.gameFinished = false;
    }

    public List<ProductionPower> getLeaderProductionPowerList() {
        return leaderProductionPowerList;
    }

    public void setLeaderProductionPowerList(List<ProductionPower> leaderProductionPowerList) {
        this.leaderProductionPowerList = leaderProductionPowerList;
    }

    public Map<Integer, DevCard> getActiveDevCardMap() {
        return activeDevCardMap;
    }

    public void setActiveDevCardMap(Map<Integer, DevCard> activeDevCardMap) {
        this.activeDevCardMap = activeDevCardMap;
    }

    public ProductionPower getBaseProductionPower() {
        return baseProductionPower;
    }

    public void setBaseProductionPower(ProductionPower baseProductionPower) {
        this.baseProductionPower = baseProductionPower;
    }

    public Map<Resource, Integer> getChest() {
        return chest;
    }

    public void setChest(Map<Resource, Integer> chest) {
        this.chest = chest;
    }

    public int getCrossPosition() {
        return crossPosition;
    }

    public void setCrossPosition(int crossPosition) {
        this.crossPosition = crossPosition;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public boolean isPapalCardOne() {
        return papalCardOne;
    }

    public void setPapalCardOne(boolean papalCardOne) {
        this.papalCardOne = papalCardOne;
    }

    public boolean isPapalCardTwo() {
        return papalCardTwo;
    }

    public void setPapalCardTwo(boolean papalCardTwo) {
        this.papalCardTwo = papalCardTwo;
    }

    public boolean isPapalCardThree() {
        return papalCardThree;
    }

    public void setPapalCardThree(boolean papalCardThree) {
        this.papalCardThree = papalCardThree;
    }

    //samu

    public Marble getSingleMarble() {
        return this.singleMarble;
    }

    public void setSingleMarble(Marble singleMarble) {
        this.singleMarble = singleMarble;
    }

    public Marble[] getFirstRow() {
        return this.firstRow;
    }

    public Marble[] getSecondRow() {
        return this.secondRow;
    }

    public Marble[] getThirdRow() {
        return this.thirdRow;
    }

    public void setFirstRow(Marble[] firstRow) {
        this.firstRow = firstRow;
    }

    public void setSecondRow(Marble[] secondRow) {
        this.secondRow = secondRow;
    }

    public void setThirdRow(Marble[] thirdRow) {
        this.thirdRow = thirdRow;
    }

    public void setMarbleInFirstRow(int i, Marble marble) {
        this.firstRow[i] = marble;
    }

    public void setMarbleInSecondRow(int i, Marble marble) {
        this.secondRow[i] = marble;
    }

    public void setMarbleInThirdRow(int i, Marble marble) {
        this.thirdRow[i] = marble;
    }

    public DevCard[][] getDevCardMarket() {
        return devCardMarket;
    }

    public void setDevCardMarket(DevCard[][] devCardMarket) {
        this.devCardMarket = devCardMarket;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public Resource getFsr() {
        return fsr;
    }

    public Resource getSsr() {
        return ssr;
    }

    public int getFsn() {
        return fsn;
    }

    public int getSsn() {
        return ssn;
    }

    public void setFsr(Resource fsr) {
        this.fsr = fsr;
    }

    public void setFsn(int fsn) {
        this.fsn = fsn;
    }

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    public void setSsr(Resource ssr) {
        this.ssr = ssr;
    }

    public int[] getLeaderCardStatus() {
        return leaderCardStatus;
    }

    public void setLeaderCardStatus(int index, int newState) {
        if (index == 0 || index == 1) {
            leaderCardStatus[index] = newState;
        }
    }

    public List<LeaderCard> getLeaderCardList() {
        return leaderCardList;
    }

    public void setLeaderCardList(List<LeaderCard> leaderCardList) {
        this.leaderCardList = leaderCardList;
    }

    public List<Integer> getChosenIntegerList() {
        return chosenIntegerList;
    }

    public void setChosenIntegerList(List<Integer> chosenIntegerList) {
        this.chosenIntegerList = chosenIntegerList;
    }

    public List<ProductionPower> getPaidProductionPowerList() {
        return paidProductionPowerList;
    }

    public void setPaidProductionPowerList(List<ProductionPower> paidProductionPowerList) {
        this.paidProductionPowerList = paidProductionPowerList;
    }

    public Integer getLawrencePosition() {
        return lawrencePosition;
    }

    public void setLawrencePosition(Integer lawrencePosition) {
        this.lawrencePosition = lawrencePosition;
    }

}
