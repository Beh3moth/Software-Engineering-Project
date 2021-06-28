package it.polimi.ngsw.model;

import it.polimi.ngsw.view.cli.AsciiArt.ResourcesArt;

import java.util.ArrayList;
import java.util.List;

/**
 * The space ability of the leadercard
 */
public class SpaceAbility extends LeaderCardBaseDecorator{

    private final int PV;
    private final String abilityName = "space";
    private final List<Resource> leaderCardCost;
    private final Resource resourceToIncrease;
    private ResourcesArt art = new ResourcesArt();
    private boolean isActive = false;
    private String id;

    public SpaceAbility(LeaderCard leaderCard, int PV, List<Resource> leaderCardCost, Resource resourceToIncrease, String id) {
        super(leaderCard);
        this.PV = PV;
        this.leaderCardCost = leaderCardCost;
        this.resourceToIncrease = resourceToIncrease;
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
     * @return a list of resources List<Resource>
     */
    public List<Resource> getLeaderCardCost() {
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
     * Method that calls the Warehouse's method to activate the additional shelf of a certain resource.
     * @param player who wants to activate the ability.
     */
    public void activateLeaderAbility(Player player){
        if(!resourceToIncrease.equals(Resource.EMPTY)){
            isActive = true;
            player.getWarehouse().unlockLeaderLevel(resourceToIncrease);
        }
    }

    /**
     * Check if the ability is active
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
        List<Resource> resourceCostList = getLeaderCardCost();
        Resource resourceType = resourceCostList.get(0);
        int resourceNumber = 5;
        int resourcesOwned = player.getChest().getResourcesAsMap().get(resourceType) + player.getWarehouse().getResourcesAsMap().get(resourceType);
        return resourcesOwned >= resourceNumber;
    }

    /**
     * Method that takes the leader card ability as a string
     * @return the ability
     */
    @Override
    public String[] getLeaderCardAbilityAsString() {
        List<String> stringList = new ArrayList<>();
        stringList.add(art.getColour(this.resourceToIncrease) + "#");
        stringList.add(" ");
        stringList.add(art.getColour(this.resourceToIncrease) + "#");
        return stringList.toArray(new String[0]);
    }

    /**
     * Method that takes the leader card cost as a string
     * @return the cost
     */
    @Override
    public String[] getLeaderCardCostAsString() {
        List<String> stringList = new ArrayList<>();
        stringList.add(getResourceArt(leaderCardCost.get(0)));
        stringList.add("5");
        return stringList.toArray(new String[0]);
    }

    public Resource getResourceToIncrease(){
        return resourceToIncrease;
    }

    /**
     *
     * @param resource the resource
     * @return a string of the art of the resource
     */
    private String getResourceArt (Resource resource) {
        switch (resource) {
            case SLAVE:
                return art.slave();
            case STONE:
                return art.stone();
            case MONEY:
                return art.money();
            case SHIELD:
                return art.shield();
            default:
                return "";
        }
    }

}
