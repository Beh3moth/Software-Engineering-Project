package it.polimi.ngsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveTest {

    Move move = new Move();

    @Test
    public void applyToken(){
        FaithPath faithPath = new FaithPath();
        move.applyToken(faithPath, null, new Game());
        assertEquals(2, faithPath.getCrossPosition());
    }

    @Test
    public void getActionTokenNameTest(){
        Move move = new Move();
        assertEquals("Lawrence The Magnificent proceeded in the Faith Path.", move.getActionTokenName());
    }

}
