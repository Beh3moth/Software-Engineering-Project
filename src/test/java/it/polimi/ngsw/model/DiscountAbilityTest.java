package it.polimi.ngsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;


public class DiscountAbilityTest {

    Player player = new Player();
    ProductionPower productionPower;
    DevCard devCard = new DevCard(2, DevCardColour.BLUE, Resource.SHIELD, 3, productionPower, 4);
    List<DevCard> list = new ArrayList<>();

    @Test
    public void activateAbilityTest(){
        list.add(0, devCard);
        LeaderCardBaseDecorator leader1 = new DiscountAbility(new ConcreteLeaderCard(), 2, list, Resource.SHIELD);
        leader1.activateAbility(player);
        assert(player.getDiscountPowerOne().equals(Resource.SHIELD));
    }

    @Test
    public void getAbilityTest(){
        list.add(0, devCard);
        LeaderCardBaseDecorator leader1 = new DiscountAbility(new ConcreteLeaderCard(), 2, list, Resource.SHIELD);
        assertEquals("discount", leader1.getAbilityName());
    }

    @Test
    public void getPVTest(){
        list.add(0, devCard);
        LeaderCardBaseDecorator leader1 = new DiscountAbility(new ConcreteLeaderCard(), 2, list, Resource.SHIELD);
        assertEquals(2, leader1.getPV());
    }

}
