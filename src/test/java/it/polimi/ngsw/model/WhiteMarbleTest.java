package it.polimi.ngsw.model;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
public class WhiteMarbleTest {
    Player player = new Player("john");
    WhiteMarble blue = new WhiteMarble();

    @Test
    public void actionMarbleTest(){
        blue.actionMarble(player);
        assertEquals(player.getWarehouse().getWhiteStock().get(0), blue.getResource());
    }

    @Test
    public void getColourTest(){
        assertEquals(blue.getColour(), MarbleColour.WHITE);
    }
    @Test
    public void getResourceTest(){
        assertEquals(blue.getResource(), Resource.EMPTY);
    }
}

