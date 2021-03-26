package it.polimi.ngsw.model;

public interface LeaderCard {

    public void activateAbility(Player player, Resource resource, int resourceNumber);
    public int getPV();
    public String getAbilityName();

}
