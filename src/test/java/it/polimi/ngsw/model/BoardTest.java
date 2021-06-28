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

    @Test
    public void getSingleMarbleTest(){
        BlueMarble blue = new BlueMarble();
        game.getBoard().setSingleMarble(blue);
        assertEquals(game.getBoard().getSingleMarble(), blue );
    }

    @Test
    public void setSingleMarbleTest(){
        BlueMarble blue = new BlueMarble();
        game.getBoard().setSingleMarble(blue);
        assertEquals(game.getBoard().getSingleMarble(), blue );
    }

    @Test
    public void getDevCardSpaceTest(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(game.getBoard().getDevCardSpace(i, j).getNumberOfCards(), 4);
            }
        }
    }

    @Test
    public void getDevCardMarketTest(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(game.getBoard().getDevCardMarket()[i][j], game.getBoard().getDevCardSpace(i, j).firstDevCard());
            }
        }
    }

    @Test
    public void getMarbleColumnTest(){
        Player pla = new Player("x");
        Marble exSingle = game.getBoard().getSingleMarble();
        Marble first = game.getBoard().getMarble(0,0);
        Marble second = game.getBoard().getMarble(1,0);
        Marble third = game.getBoard().getMarble(2,0);
        game.getBoard().getMarbleColumn(1, pla);
        assertEquals(exSingle, game.getBoard().getMarble(2,0));
        assertEquals(first, game.getBoard().getSingleMarble());
        assertEquals(second, game.getBoard().getMarble(0,0));
        assertEquals(third, game.getBoard().getMarble(1,0));
    }
    @Test
    public void getMarbleRowTest(){
        Player pla = new Player("x");
        Marble exSingle = game.getBoard().getSingleMarble();
        Marble first = game.getBoard().getMarble(0,0);
        Marble second = game.getBoard().getMarble(0,1);
        Marble third = game.getBoard().getMarble(0,2);
        Marble fourth = game.getBoard().getMarble(0,3);
        game.getBoard().getMarbleRow(1, pla);
        assertEquals(exSingle, game.getBoard().getMarble(0,3));
        assertEquals(first, game.getBoard().getSingleMarble());
        assertEquals(second, game.getBoard().getMarble(0,0));
        assertEquals(third, game.getBoard().getMarble(0,1));
        assertEquals(fourth, game.getBoard().getMarble(0,2));
    }

}
