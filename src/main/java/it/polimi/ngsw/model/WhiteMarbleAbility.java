package it.polimi.ngsw.model;

import java.util.ArrayList;
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

    /**
     * The method returns true if the Player satisfies the Leader Card Cost, otherwise it returns false.
     * @param player is the Player who wants to check if he can activate a Leader Card.
     * @return true if the Player satisfies the Leader Card Cost, otherwise it returns false.
     */
    @Override
    public boolean isLeaderCardCostSatisfied(Player player){
        List<DevCardColour> devCardColourCostList = getLeaderCardCost();
        List<DevCardColour> devCardColourPlayerList = new ArrayList<>();
        List<DevCard> devCardList = player.getDevCardDashboard().getActiveDevCards();

        for(DevCard devCard : devCardList){
            devCardColourPlayerList.add(devCard.getCardColour());
        }

        for(DevCardColour devCardColour : devCardColourCostList){
            if(!devCardColourPlayerList.contains(devCardColour)){
                return false;
            }
            else{
                devCardColourPlayerList.remove(devCardColour);
            }
        }

        return true;
    }

}
