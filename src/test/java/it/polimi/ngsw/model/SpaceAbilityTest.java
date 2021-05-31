package it.polimi.ngsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class SpaceAbilityTest {

    Player player = new Player("jhon");
    List<Resource> list = new ArrayList<>();

    @Test
    public void activateAbilityTest(){
        list.add(0, Resource.MONEY);
        LeaderCardBaseDecorator leader1 = new SpaceAbility(new ConcreteLeaderCard(), 2, list, Resource.MONEY, "1");
        leader1.activateAbility(player);
        assert(player.getWarehouse().getLeaderLevelType(1).equals(Resource.MONEY));
    }

    @Test
    public void getAbilityTest(){
        list.add(0, Resource.MONEY);
        LeaderCardBaseDecorator leader1 = new SpaceAbility(new ConcreteLeaderCard(), 2, list, Resource.SHIELD, "1");
        assertEquals("space", leader1.getAbilityName());
    }

    @Test
    public void getPVTest(){
        list.add(0, Resource.MONEY);
        LeaderCardBaseDecorator leader1 = new SpaceAbility(new ConcreteLeaderCard(), 2, list, Resource.SHIELD, "1");
        assertEquals(2, leader1.getPV());
    }

}
