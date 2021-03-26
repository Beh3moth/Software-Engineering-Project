package it.polimi.ngsw.model;

public class LeaderCardBaseDecorator implements LeaderCard{

    private LeaderCard wrapper;

    public LeaderCardBaseDecorator(LeaderCard leaderCard){
        this.wrapper = leaderCard;
    }

    @Override
    public void activateAbility(Player player, Resource resource, int resourceNumber) {
        wrapper.activateAbility(player, resource, resourceNumber);
    }

    @Override
    public int getPV() {
        return 0;
    }

    @Override
    public String getAbilityName() {
        return null;
    }

}
