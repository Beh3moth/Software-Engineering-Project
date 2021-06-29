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
    public void getIdTest(){
        List<Resource> devCardColourList = new ArrayList<>();

        SpaceAbility w = new SpaceAbility(new ConcreteLeaderCard(), 2,devCardColourList,Resource.SHIELD, "id" );
        assertEquals("id", w.getLeaderCardId());
        assertEquals(devCardColourList, w.getLeaderCardCost());
        assertEquals(false, w.isActive());
        assertEquals(Resource.SHIELD, w.getResourceToIncrease());
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
