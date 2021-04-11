package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.List;

public class DiscountAbility extends LeaderCardBaseDecorator {

    private final int PV;
    private final String abilityName = "discount";
    private final List<DevCardColour> leaderCardCost;
    private final Resource discountResource;

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

    /**
     * The method returns true if the Player satisfies the Leader Card Cost, otherwise it returns false.
     * @param player is the Player who wants to check if he can activate a Leader Card.
     * @return true if the Player satisfies the Leader Card Cost, otherwise it returns false.
     */
    @Override
    public boolean isLeaderCardCostSatisfied(Player player){
        List<DevCardColour> devCardColourPlayerList = new ArrayList<>();
        List<DevCard> devCardList = player.getDevCardDashboard().getActiveDevCards();

        for(DevCard devCard : devCardList){
            devCardColourPlayerList.add(devCard.getCardColour());
        }

        for(DevCardColour devCardColour : leaderCardCost){
            if(!devCardColourPlayerList.contains(devCardColour)){
                return false;
            }
        }

        return true;
    }


}
