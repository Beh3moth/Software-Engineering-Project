package it.polimi.ngsw.model;

import it.polimi.ngsw.view.cli.AsciiArt.ResourcesArt;

import java.util.ArrayList;
import java.util.List;

/**
 * The production power ability of the leadercard
 */
public class ProductionPowerAbility extends LeaderCardBaseDecorator{

    private final int PV;
    private final String abilityName = "production power";
    private final DevCardColour leaderCardCost;
    private final int devCardLevel = 2;
    private final Resource inputResource;
    private ResourcesArt art = new ResourcesArt();
    private boolean isActive = false;
    private String id;

    public ProductionPowerAbility(LeaderCard leaderCard, int PV, DevCardColour leaderCardCost, Resource inputResource, String id) {
        super(leaderCard);
        this.PV = PV;
        this.leaderCardCost = leaderCardCost;
        this.inputResource = inputResource;
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
     * The method activates the Leader Card's ability by calling the method @activateLeaderAbility
     * @param player is the player who decided to activate the Leader Card's ability.
     */
    @Override
    public void activateAbility(Player player) {
        activateLeaderAbility(player);
    }

    /**
     * The method returns the Input Resource required by the Leader Card Production Power.
     * @return the Input Resource required by the Leader Card Production Power.
     */
    @Override
    public Resource getInputResource(){
        return inputResource;
    }

    /**
     * Method that return the leader card's cost.
     * @return a list of Development Cards List<DevCard>
     */
    public DevCardColour getLeaderCardCostDevCardColour() {
        return leaderCardCost;
    }

    /**
     * The method returns the DevCardLevel required by the Leader Card Cost.
     * @return the DevCardLevel required.
     */
    public int getLeaderCardCostDevCardLevel() {
        return devCardLevel;
    }

    /**
     * The method activates the Leader's production power calling the method Board's method activateLeaderProduction()
     * @param player the player who wants to activate the power.
     */
    public void activateLeaderAbility(Player player){
        isActive = true;
        player.getDevCardDashboard().activateProductionPowerAbility(this);
    }

    /**
     * @return check if is active
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
        List<DevCard> devCardPlayerList = player.getDevCardDashboard().getDevCards();
        for(DevCard devCard : devCardPlayerList){
            if(devCard.getCardColour().equals(getLeaderCardCostDevCardColour()) && devCard.getDevLevel()>=getLeaderCardCostDevCardLevel()){
                return true;
            }
        }
        return false;
    }

    /**
     * @return the ability of the leadercard as a string
     */
    @Override
    public String[] getLeaderCardAbilityAsString() {
        List<String> stringList = new ArrayList<>();
        stringList.add(art.getColour(this.inputResource) + "@" + art.getReset());
        stringList.add(" ");
        stringList.add("-");
        stringList.add(">");
        stringList.add(" ");
        stringList.add("?");
        stringList.add(" ");
        stringList.add("+");
        stringList.add(" ");
        stringList.add(art.getColour(Resource.FAITHPOINT) + "@" + art.getReset());
        return stringList.toArray(new String[0]);
    }

    /**
     * @return the cost of the leadercard as a string
     */
    @Override
    public String[] getLeaderCardCostAsString() {
        List<String> stringList = new ArrayList<>();
        stringList.add(art.getColour(leaderCardCost) + "<");
        stringList.add(art.getColour(leaderCardCost) + "2");
        stringList.add(art.getColour(leaderCardCost) + ">");
        return stringList.toArray(new String[0]);
    }

    private String getResourceArt (Resource resource) {
        switch (resource){
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
