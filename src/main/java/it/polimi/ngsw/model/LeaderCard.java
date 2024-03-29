package it.polimi.ngsw.model;

import java.io.Serializable;

/**
 * The interface of the leadercards
 */
public interface LeaderCard extends Serializable {

    String getLeaderCardId();
    void activateAbility(Player player);
    int getPV();
    String getAbilityName();
    boolean isLeaderCardCostSatisfied(Player player);
    Resource getInputResource();
    String[] getLeaderCardCostAsString();
    String[] getLeaderCardAbilityAsString();
    boolean isActive();

}
