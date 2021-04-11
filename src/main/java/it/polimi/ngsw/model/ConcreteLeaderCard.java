package it.polimi.ngsw.model;

public class ConcreteLeaderCard implements LeaderCard {

    @Override
    public void activateAbility(Player player) {
    }

    @Override
    public int getPV() {
        return 0;
    }

    @Override
    public String getAbilityName() {
        return null;
    }

    @Override
    public boolean isLeaderCardCostSatisfied(Player player) {
        return false;
    }

}
