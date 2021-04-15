package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.List;

public class DiscountAbility extends LeaderCardBaseDecorator {

    private int PV;
    private final String abilityName = "discount";
    private List<DevCardColour> leaderCardCost;
    private Resource discountResource;

    public DiscountAbility(LeaderCard leaderCard, int PV, List<DevCardColour> leaderCardCost, Resource discountResource) {
        super(leaderCard);
        this.PV = PV;
        this.leaderCardCost = leaderCardCost;
        this.discountResource = discountResource;
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
     * @return a list of Development Cards List<DevCard>
     */
    public List<DevCardColour> getLeaderCardCost() {
        return leaderCardCost;
    }

    /**
     * Method called by the player to activate the Leader Card's ability
     * @param player the player who's  activating the Leader Card. It will be used in this calls.
     */
    @Override
    public void activateAbility(Player player) {
        activateLeaderAbility(player);
    }

    /**
     * This method activate the discount leader card ability which affects directly the player.
     * @param player The player who decides to activate the leader card power.
     */
    public void activateLeaderAbility(Player player){
        if(!discountResource.equals(Resource.EMPTY)){
            player.setDiscountPower(discountResource);
        }
    }

}
