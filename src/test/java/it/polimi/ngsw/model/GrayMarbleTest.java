package it.polimi.ngsw.model;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
public class GrayMarbleTest {
    Player player = new Player("john");
    GrayMarble blue = new GrayMarble();

    @Test
    public void actionMarbleTest(){
        blue.actionMarble(player);
        assertEquals(player.getWarehouse().getWarehouseStock().get(0), blue.getResource());
    }

    @Test
    public void getColourTest(){
        assertEquals(blue.getColour(), MarbleColour.GRAY);
    }
    @Test
    public void getResourceTest(){
        assertEquals(blue.getResource(), Resource.STONE);
    }
}
