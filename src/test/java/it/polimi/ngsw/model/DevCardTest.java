package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class DevCardTest {

    Map<Resource, Integer> map;
    ProductionPower productionPower = new ProductionPower();
    DevCard devCard = new DevCard(2, DevCardColour.BLUE, Resource.SHIELD, 10, productionPower, 2);

    @Test
    public void getDevLevelTest(){
        assertEquals(2, devCard.getDevLevel());
    }

    @Test
    public void getCardColourTest(){
        assertEquals(DevCardColour.BLUE, devCard.getCardColour());
    }

    @Test
    public void getPVTest(){
        assertEquals(2, devCard.getPV());
    }

    @Test
    public void getDevCostAsMapTest(){
        map = devCard.getDevCostAsMap();
    }

}
