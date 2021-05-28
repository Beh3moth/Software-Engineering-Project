package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;

public class GameTest {

    Game game = new Game();

    public void randomChest(Chest chest){
        chest.addResource(Resource.SHIELD, new Random().nextInt(100));
        chest.addResource(Resource.STONE, new Random().nextInt(100));
        chest.addResource(Resource.SLAVE, new Random().nextInt(100));
        chest.addResource(Resource.MONEY, new Random().nextInt(100));
    }

    @Test
    public void increaseOtherFaithPointsTest(){
        Player player1 = new Player("jhon");
        Player player2 = new Player("william");
        Player player3 = new Player("peter");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        for(int i=0; i<9; i++){
            player2.getFaithPath().increaseCrossPosition();
            player3.getFaithPath().increaseCrossPosition();
        }
        for(int i=0; i<4; i++){
            player2.getFaithPath().increaseCrossPosition();
        }
        assertEquals(13, player2.getFaithPath().getCrossPosition());
        assertEquals(9, player3.getFaithPath().getCrossPosition());

        game.increaseOtherFaithPoints(player1, 3);

        assertTrue(player3.getFaithPath().getPapalCardOne());
        assertTrue(player2.getFaithPath().getPapalCardOne());
        assertTrue(player3.getFaithPath().getPapalCardTwo());
        assertTrue(player2.getFaithPath().getPapalCardTwo());

    }

    @Test
    public void removeAndReturnTheLastFourLeaderCardsTest(){
        for(int i=0; i<4; i++){
            List<LeaderCard> list = new ArrayList<>();
            try {
                  list = game.removeAndReturnTheLastFourLeaderCards();
            }
            catch (IndexOutOfBoundsException e){
                fail();
            }
            assertEquals(4, list.size());
        }
    }

    @Test
    public void shuffleActionTokensDequeTest(){
        List<ActionToken> testList1 = new ArrayList<>(game.getTokensDeque());
        assertTrue(game.shuffleActionTokensDeque());
        List<ActionToken> testList2 = new ArrayList<>(game.getTokensDeque());
        for(int i=0; i<6; i++){
            assertNotEquals(testList1, testList2);
        }
    }

    /*

    @Test
    public void drawActionTokenTest(){
        game.setNumberOfPlayers(1);
        game.createPlayers();
        for(int i=0; i<6; i++){
            ActionToken tempActionToken = game.getTokensDeque().getFirst();
            if( !(tempActionToken instanceof MoveAndScrum) ){
                //assertTrue(game.drawActionToken());
                assertEquals(tempActionToken, game.getTokensDeque().getLast());
            }
        }
    }

     */

    @Test
    public void updateTest1(){

        game.setNumberOfPlayers(3);
        game.createPlayers();

        for(int i=0; i<25; i++){
            game.getPlayerFromList(0).getFaithPath().increaseCrossPosition();
        }
        for(int i=0; i<25; i++){
            game.getPlayerFromList(1).getFaithPath().increaseCrossPosition();
        }
        for(int i=0; i<25; i++){
            game.getPlayerFromList(2).getFaithPath().increaseCrossPosition();
        }
        assertTrue(game.getPlayerFromList(0).getFaithPath().getPapalCardOne());
        assertTrue(game.getPlayerFromList(0).getFaithPath().getPapalCardTwo());
        assertTrue(game.getPlayerFromList(0).getFaithPath().getPapalCardThree());
        assertFalse(game.getPlayerFromList(1).getFaithPath().getPapalCardOne());
        assertFalse(game.getPlayerFromList(1).getFaithPath().getPapalCardTwo());
        assertFalse(game.getPlayerFromList(1).getFaithPath().getPapalCardThree());
        assertFalse(game.getPlayerFromList(2).getFaithPath().getPapalCardOne());
        assertFalse(game.getPlayerFromList(2).getFaithPath().getPapalCardTwo());
        assertFalse(game.getPlayerFromList(2).getFaithPath().getPapalCardThree());
    }

    @Test
    public void updateTest2(){

        game.setNumberOfPlayers(3);
        game.createPlayers();

        for(int i=0; i<7; i++){
            game.getPlayerFromList(0).getFaithPath().increaseCrossPosition();
        }
        for(int i=0; i<8; i++){
            game.getPlayerFromList(1).getFaithPath().increaseCrossPosition();
        }
        assertTrue(game.getPlayerFromList(0).getFaithPath().getPapalCardOne());
        assertTrue(game.getPlayerFromList(1).getFaithPath().getPapalCardOne());

    }

    @Test
    public void updateTest3(){

        game.setNumberOfPlayers(3);
        game.createPlayers();

        for(int i=0; i<9; i++){
            game.getPlayerFromList(0).getFaithPath().increaseCrossPosition();
            game.getPlayerFromList(1).getFaithPath().increaseCrossPosition();
            game.getPlayerFromList(2).getFaithPath().increaseCrossPosition();
        }
        assertTrue(game.getPlayerFromList(0).getFaithPath().getPapalCardOne());
        assertTrue(game.getPlayerFromList(1).getFaithPath().getPapalCardOne());
        assertTrue(game.getPlayerFromList(2).getFaithPath().getPapalCardOne());

    }

}
