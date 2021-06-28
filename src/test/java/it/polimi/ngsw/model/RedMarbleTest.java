package it.polimi.ngsw.model;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
public class RedMarbleTest {
    Player player = new Player("john");
    RedMarble blue = new RedMarble();

    @Test
    public void actionMarbleTest(){
        blue.actionMarble(player);
        assertEquals(player.getFaithPath().getCrossPosition(), 1);
    }

    @Test
    public void getColourTest(){
        assertEquals(blue.getColour(), MarbleColour.RED);
    }
    @Test
    public void getResourceTest(){
        assertEquals(blue.getResource(), Resource.FAITHPOINT);
    }
}
