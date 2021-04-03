package it.polimi.ngsw.model;

import java.util.List;

public class WhiteMarbleAbility extends LeaderCardBaseDecorator{

    private final int PV;
    private final String abilityName = "white marble";
    private final List<DevCardColour> leaderCardCost;
    private final Resource resourceToObtain;

    public WhiteMarbleAbility(LeaderCard leaderCard, int PV, List<DevCardColour> leaderCardCost, Resource resourceToObtain) {
        super(leaderCard);
        this.PV = PV;
        this.leaderCardCost = leaderCardCost;
        this.resourceToObtain = resourceToObtain;
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
     * Method that activates the Leader Card's ability.
     * @param player is the one who wants to activate the ability.
     */
    @Override
    public void activateAbility(Player player) {
        activateLeaderAbility(player);
    }

    /**
     * Method that activate the white marble power, setting the right player's attribute.
     * @param player who wants to activate the ability.
     */
    public void activateLeaderAbility(Player player){
        if(!resourceToObtain.equals(Resource.EMPTY)){
            player.setWhiteMarblePower(resourceToObtain);
        }
    }

}
