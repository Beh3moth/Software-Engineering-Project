package it.polimi.ngsw.model;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
public class YellowMarbleTest {
    Player player = new Player("john");
    YellowMarble blue = new YellowMarble();

    @Test
    public void actionMarbleTest(){
        blue.actionMarble(player);
        assertEquals(player.getWarehouse().getWarehouseStock().get(0), blue.getResource());
    }

    @Test
    public void getColourTest(){
        assertEquals(blue.getColour(), MarbleColour.YELLOW);
    }
    @Test
    public void getResourceTest(){
        assertEquals(blue.getResource(), Resource.MONEY);
    }
}
