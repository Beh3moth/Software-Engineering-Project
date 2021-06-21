package it.polimi.ngsw.model;

import it.polimi.ngsw.view.cli.AsciiArt.Color;
import it.polimi.ngsw.view.cli.AsciiArt.ResourcesArt;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of the ability of having the possibility to chose a resources given a white marble
 */
public class WhiteMarbleAbility extends LeaderCardBaseDecorator{

    private final int PV;
    private final String abilityName = "white marble";
    private final List<DevCardColour> leaderCardCost;
    private final Resource resourceToObtain;
    private ResourcesArt art = new ResourcesArt();
    private boolean isActive = false;
    private String id;

    public WhiteMarbleAbility(LeaderCard leaderCard, int PV, List<DevCardColour> leaderCardCost, Resource resourceToObtain, String id) {
        super(leaderCard);
        this.PV = PV;
        this.leaderCardCost = leaderCardCost;
        this.resourceToObtain = resourceToObtain;
        this.id = id;
    }

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
            isActive = true;
            player.setWhiteMarblePower(resourceToObtain);
        }
    }

    /**
     * Method that check if the leadercard is active
     * @return if is active
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
     * Method that takes the ability of the leader card as a string
     * @return the cost as a string
     */
    @Override
    public String[] getLeaderCardAbilityAsString() {
        List<String> stringList = new ArrayList<>();
        stringList.add("@");
        stringList.add(" ");
        stringList.add("=");
        stringList.add(" ");
        stringList.add(art.getColour(this.resourceToObtain) + "@" + art.getReset());
        return stringList.toArray(new String[0]);
    }

    /**
     * Method that takes cost of the leader card as a map
     * @return the cost
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
