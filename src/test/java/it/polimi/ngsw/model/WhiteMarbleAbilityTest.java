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
    public void getIdTest(){
        List<DevCardColour> devCardColourList = new ArrayList<>();

        WhiteMarbleAbility w = new WhiteMarbleAbility(new ConcreteLeaderCard(), 2,devCardColourList,Resource.SHIELD, "id" );
        assertEquals("id", w.getLeaderCardId());
        assertEquals(devCardColourList, w.getLeaderCardCost());
        assertEquals(false, w.isActive());
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
