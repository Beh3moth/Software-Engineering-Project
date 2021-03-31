package it.polimi.ngsw.model;

public class LeaderCardBaseDecorator implements LeaderCard{

    private LeaderCard wrapper;

    public LeaderCardBaseDecorator(LeaderCard leaderCard){
        this.wrapper = leaderCard;
    }

    @Override
    public void activateAbility(Player player) {
        wrapper.activateAbility(player);
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