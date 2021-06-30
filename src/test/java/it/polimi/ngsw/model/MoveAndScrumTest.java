package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MoveAndScrumTest {

    MoveAndScrum moveAndScrum = new MoveAndScrum();

    @Test
    public void applyToken(){
        FaithPath faithPath = new FaithPath();
        moveAndScrum.applyToken(faithPath, null, new Game());
        assertEquals(1, faithPath.getCrossPosition());
    }

    @Test
    public void getActionTokenNameTest(){
        MoveAndScrum moveAndScrum = new MoveAndScrum();
        assertEquals("Lawrence The Magnificent proceeded in the Faith Path and he shuffled the Action Tokens' deck", moveAndScrum.getActionTokenName());
    }

}
