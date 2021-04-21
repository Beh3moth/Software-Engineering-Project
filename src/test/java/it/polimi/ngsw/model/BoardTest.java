package it.polimi.ngsw.model;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BoardTest {

    Game game = new Game();

    @Test
    public void checkShuffle (){
        Board board = new Board();
        System.out.println(board.getSingleMarble());
    }

    @Test
    public void removeDevCardTest(){
        for(DevCardColour devCardColour : DevCardColour.values()){
            if(!devCardColour.equals(DevCardColour.EMPTY)){
                assertTrue(game.getBoard().removeTwoDevCard(devCardColour));
                assertEquals(2, game.getBoard().getDevCardSpace(2, game.getBoard().getDevCardColumn(devCardColour)).getNumberOfCards());
            }
        }
    }

}
