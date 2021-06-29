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
    @Test
    public void testPlayer(){
        Game g = new Game();
        assertFalse(g.isGameEndedMultiPlayers());
        assertEquals(0, g.getChosenPlayersNumber());
        Player p = new Player("Jhon");
        g.addPlayer(p);
        g.setChosenMaxPlayers(4);
        assertFalse(g.setChosenMaxPlayers(0));
        assertEquals(1, g.getNumCurrentPlayers());
        assertTrue(g.isNicknameTaken("Jhon"));
        g.setNumberOfPlayers(1);
        assertFalse(g.isGameEndedMultiPlayers());
        g.waitChosenNmber();
        g.initLawrenceFaithPath();
        assertTrue(g.removePlayerByNickname("Jhon", true));
        assertFalse(g.lawrenceIsTheWinner());
    }

/*
    @Test
    public void drawActionTokenTest(){
        game.setNumberOfPlayers(1);
        game.createPlayers();
        for(int i=0; i<6; i++){
            ActionToken tempActionToken = game.getTokensDeque().getFirst();
            if( !(tempActionToken instanceof MoveAndScrum) ){
                assertTrue(game.drawActionToken());
                assertEquals(tempActionToken, game.getTokensDeque().getLast());
            }
        }
    }*/

}
