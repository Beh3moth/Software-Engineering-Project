package it.polimi.ngsw.model;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
public class PurpleMarbleTest {
    Player player = new Player("john");
    PurpleMarble blue = new PurpleMarble();

    @Test
    public void actionMarbleTest(){
        blue.actionMarble(player);
        assertEquals(player.getWarehouse().getWarehouseStock().get(0), blue.getResource());
    }

    @Test
    public void getColourTest(){
        assertEquals(blue.getColour(), MarbleColour.PURPLE);
    }
    @Test
    public void getResourceTest(){
        assertEquals(blue.getResource(), Resource.SLAVE);
    }
}

