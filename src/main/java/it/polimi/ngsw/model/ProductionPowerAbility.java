package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.List;

public class ProductionPowerAbility extends LeaderCardBaseDecorator{

    private int PV;
    private final String abilityName = "production power";
    private List<DevCard> leaderCardCost;
    private Resource inputResource;

    public ProductionPowerAbility(LeaderCard leaderCard, int PV, List<DevCard> leaderCardCost, Resource inputResource) {
        super(leaderCard);
        this.PV = PV;
        this.leaderCardCost = leaderCardCost;
        this.inputResource = inputResource;
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
    public List<DevCard> getLeaderCardCost() {
        return leaderCardCost;
    }

    @Override
    public void activateAbility(Player player, Resource resource, int resourceNumber) {
        activateLeaderAbility(player);
    }

    public void activateLeaderAbility(Player player){
        player.getDevCardDashboard().activateLeaderProduction(inputResource, player);
    }

}
