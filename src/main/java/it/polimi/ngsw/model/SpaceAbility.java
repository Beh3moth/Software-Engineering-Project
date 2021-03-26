package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.List;

public class SpaceAbility extends LeaderCardBaseDecorator{

    private int PV;
    private final String abilityName = "space";
    private List<Resource> leaderCardCost;
    private Resource resourceToIncrease;

    public SpaceAbility(LeaderCard leaderCard, int PV, List<Resource> leaderCardCost, Resource resourceToIncrease) {
        super(leaderCard);
        this.PV = PV;
        this.leaderCardCost = leaderCardCost;
        this.resourceToIncrease = resourceToIncrease;
    }

    /**
     * Method that returns the number of victory points of the leader card.
     * @return an int containing the number of victory points of the leader card.
     */
    @Override
    public int getPV() {
        return PV;
    }

    /**
     * Method that returns the name of the ability of a Leader Card.
     * @return a String of the ability of the Leader Card's name.
     */
    @Override
    public String getAbilityName() {
        return abilityName;
    }

    /**
     * Method that return the leader card's cost.
     * @return a list of resources List<Resource>
     */
    public List<Resource> getLeaderCardCost() {
        return leaderCardCost;
    }

    /**
     * Method that activates the Leader Card's ability.
     * @param player is the one who wants to activate the ability.
     * @param resource isn't used. Use EMPTY.
     * @param resourceNumber isn't used. Use 0.
     */
    @Override
    public void activateAbility(Player player, Resource resource, int resourceNumber) {
        activateLeaderAbility(player);
    }

    /**
     * Method that calls the Warehouse's method to activate the additional shelf of a certain resource.
     * @param player who wants to activate the ability.
     */
    public void activateLeaderAbility(Player player){
        if(!resourceToIncrease.equals(Resource.EMPTY)){
            player.getWarehouse().unlockLeaderLevel(resourceToIncrease);
        }
    }

}
