package it.polimi.ngsw.model;

public class ConcreteLeaderCard implements LeaderCard {

    @Override
    public String getLeaderCardId() {
        return null;
    }

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

    @Override
    public Resource getInputResource() {
        return null;
    }

    @Override
    public String[] getLeaderCardCostAsString() {
        return null;
    }

    @Override
    public String[] getLeaderCardAbilityAsString() {
        return new String[0];
    }

    @Override
    public boolean isActive() {
        return false;
    }

}
