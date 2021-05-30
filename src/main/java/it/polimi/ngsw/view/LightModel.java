package it.polimi.ngsw.view;

import it.polimi.ngsw.model.DevCard;
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
}
