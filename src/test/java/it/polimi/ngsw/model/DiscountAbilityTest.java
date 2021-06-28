package it.polimi.ngsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;


public class DiscountAbilityTest {

    Player player = new Player("john");
    List<DevCardColour> list = new ArrayList<>();

    @Test
    public void isLeaderCardCostSatisfiedTest(){
        Game game = new Game();
        game.setNumberOfPlayers(1);
        game.addPlayer(player);
    }

    @Test
    public void activateAbilityTest(){
        list.add(0, DevCardColour.BLUE);
        LeaderCardBaseDecorator leader1 = new DiscountAbility(new ConcreteLeaderCard(), 2, list, Resource.SHIELD, "1");
        leader1.activateAbility(player);
        assert(player.getDiscountPowerOne().equals(Resource.SHIELD));
    }

    @Test
    public void getAbilityTest(){
        list.add(0, DevCardColour.BLUE);
        LeaderCardBaseDecorator leader1 = new DiscountAbility(new ConcreteLeaderCard(), 2, list, Resource.SHIELD, "1");
        assertEquals("discount", leader1.getAbilityName());
    }

    @Test
    public void getPVTest(){
        list.add(0, DevCardColour.BLUE);
        LeaderCardBaseDecorator leader1 = new DiscountAbility(new ConcreteLeaderCard(), 2, list, Resource.SHIELD, "1");
        assertEquals(2, leader1.getPV());
    }

}
