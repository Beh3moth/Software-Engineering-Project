package it.polimi.ngsw.view;

import it.polimi.ngsw.model.DevCard;
import it.polimi.ngsw.model.Marble;
import it.polimi.ngsw.model.ProductionPower;
import it.polimi.ngsw.model.Resource;

import java.util.List;
import java.util.Map;

public class LightModel {

    private List<ProductionPower> leaderProductionPowerList;
    private List<DevCard> activeDevCardList;
    private ProductionPower baseProductionPower;
    private Map<Resource, Integer> chest;
    private int crossPosition;
    private int victoryPoints;
    private boolean papalCardOne;
    private boolean papalCardTwo;
    private boolean papalCardThree;
    //samu
    private Marble singleMarble;
    private Marble[] firstRow;
    private Marble[] secondRow;
    private Marble[] thirdRow;
    private DevCard[][] devCardMarket;
    private boolean gameFinished;

    //aaron
    private Resource firstShelf;
    private Resource secondShelf;
    private int secondShelfNumber;
    private Resource thirdShelf;
    private int thirdShelfNumber;

    public Resource getFirstShelf(){
        return firstShelf;
    }
    public void setFirstShelf(Resource first){
        this.firstShelf = first;
    }
    public void setSecondShelfNumber(int second){
        this.secondShelfNumber = second;
    }
    public void setSecondShelf(Resource second){
        this.secondShelf = second;
    }
    public void setThirdShelf(Resource third){
        this.thirdShelf = third;
    }
    public void setThirdShelfNumber(int third){
        this.thirdShelfNumber = third;
    }

    public Resource getSecondShelf(){
        return secondShelf;
    }
    public Resource getThirdShelf(){
        return thirdShelf;
    }
    public int getSecondShelfNumber(){
        return secondShelfNumber;
    }
    public int getThirdShelfNumber(){
        return thirdShelfNumber;
    }

    public LightModel(){
        this.gameFinished = false;
    }
    public List<ProductionPower> getLeaderProductionPowerList() {
        return leaderProductionPowerList;
    }

    public void setLeaderProductionPowerList(List<ProductionPower> leaderProductionPowerList) {
        this.leaderProductionPowerList = leaderProductionPowerList;
    }

    public List<DevCard> getActiveDevCardList() {
        return activeDevCardList;
    }

    public void setActiveDevCardList(List<DevCard> activeDevCardList) {
        this.activeDevCardList = activeDevCardList;
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

    public Marble getSingleMarble(){return this.singleMarble;}

    public void setSingleMarble(Marble singleMarble){this.singleMarble = singleMarble;}

    public Marble[] getFirstRow() {return this.firstRow;}

    public Marble[] getSecondRow() {return this.secondRow;}

    public Marble[] getThirdRow() {return this.thirdRow;}

    public void setFirstRow(Marble[] firstRow) {
        this.firstRow = firstRow;
    }

    public void setSecondRow(Marble[] secondRow) {
        this.secondRow = secondRow;
    }

    public void setThirdRow(Marble[] thirdRow) {
        this.thirdRow = thirdRow;
    }

    public void setMarbleInFirstRow(int i, Marble marble){
        this.firstRow[i] = marble;
    }

    public void setMarbleInSecondRow(int i, Marble marble) {
        this.secondRow[i] = marble;
    }

    public void setMarbleInThirdRow(int i, Marble marble){
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
}
