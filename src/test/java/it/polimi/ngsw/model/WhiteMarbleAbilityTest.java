package it.polimi.ngsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class WhiteMarbleAbilityTest {

    Player player = new Player("john");
    ProductionPower productionPower;
    List<DevCardColour> list = new ArrayList<>();

    @Test
    public void activateAbilityTest(){
        list.add(0, DevCardColour.BLUE);
        LeaderCardBaseDecorator leader1 = new WhiteMarbleAbility(new ConcreteLeaderCard(), 2, list, Resource.MONEY, "1");
        leader1.activateAbility(player);
        assertEquals(Resource.MONEY, player.getWhiteMarblePowerOne());
    }

    @Test
    public void getAbilityTest(){
        list.add(0, DevCardColour.BLUE);
        LeaderCardBaseDecorator leader1 = new WhiteMarbleAbility(new ConcreteLeaderCard(), 2, list, Resource.SHIELD, "1");
        assertEquals("white marble", leader1.getAbilityName());
    }

    @Test
    public void getPVTest(){
        list.add(0, DevCardColour.BLUE);
        LeaderCardBaseDecorator leader1 = new WhiteMarbleAbility(new ConcreteLeaderCard(), 2, list, Resource.SHIELD, "1");
        assertEquals(2, leader1.getPV());
    }

}
