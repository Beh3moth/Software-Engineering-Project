package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
public class DevCardSpaceTest {
    Board b = new Board();
    @Test
    public void getterTest(){
        b.getDevCardSpace(1,1).removeFirstCard();
        assertEquals(3, b.getDevCardSpace(1,1).getNumberOfCards());
        b.getDevCardSpace(1,1).removeFirstCard();
        b.getDevCardSpace(1,1).removeFirstCard();
        b.getDevCardSpace(1,1).removeFirstCard();
        assertEquals(0, b.getDevCardSpace(1,1).firstDevCard().getPV());
        b.getDevCardSpace(2,3).removeDevCardFromDevelopDeck();
        assertEquals(2, b.getDevCardSpace(2,3).getNumberOfCards());
    }
}
