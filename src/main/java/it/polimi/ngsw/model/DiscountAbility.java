package it.polimi.ngsw.model;

import it.polimi.ngsw.view.cli.AsciiArt.ResourcesArt;

import java.util.ArrayList;
import java.util.List;

/**
 * The ability discount of the leadercard
 */
public class DiscountAbility extends LeaderCardBaseDecorator {

    private final int PV;
    private final String abilityName = "discount";
    private final List<DevCardColour> leaderCardCost;
    private final Resource discountResource;
    private ResourcesArt art = new ResourcesArt();
    private boolean isActive = false;
    private String id;

    public DiscountAbility(LeaderCard leaderCard, int PV, List<DevCardColour> leaderCardCost, Resource discountResource, String id) {
        super(leaderCard);
        this.PV = PV;
        this.leaderCardCost = leaderCardCost;
        this.discountResource = discountResource;
        this.id = id;
    }

    /**
     * Takes the id
     * @return the id
     */
    @Override
    public String getLeaderCardId() {
        return id;
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
        isActive = true;
        if(!discountResource.equals(Resource.EMPTY)){
            player.setDiscountPower(discountResource);
        }
    }

    /**
     * Check if is active
     * @return a boolean of if it is active
     */
    @Override
    public boolean isActive(){
        return isActive;
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
            devCardColourPlayerList.remove(devCardColour);
        }

        return true;

    }

    /**
     * Method that takes the ability of the leadercard
     * @return the ability as string
     */
    @Override
    public String[] getLeaderCardAbilityAsString() {
        List<String> stringList = new ArrayList<>();
        stringList.add("-");
        stringList.add(art.getColour(this.discountResource) + "@" + art.getReset());
        return stringList.toArray(new String[0]);
    }

    /**
     * Method that take the cost of the leadercard
     * @return the string of the cost
     */
    @Override
    public String[] getLeaderCardCostAsString() {
        List<String> stringList = new ArrayList<>();
        for(DevCardColour devCardColour : leaderCardCost){
            stringList.add(art.getColour(devCardColour) + "<");
            stringList.add(art.getColour(devCardColour) + "?");
            stringList.add(art.getColour(devCardColour) + ">");
        }
        return stringList.toArray(new String[0]);
    }


}
