package it.polimi.ngsw.model;

import java.io.Serializable;

public interface LeaderCard extends Serializable {


    void activateAbility(Player player);
    int getPV();
    String getAbilityName();
    boolean isLeaderCardCostSatisfied(Player player);
    Resource getInputResource();
    String[] getLeaderCardCostAsString();
    String[] getLeaderCardAbilityAsString();

}
