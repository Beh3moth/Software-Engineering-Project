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
    }

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

    @Test
    public void canBuyDevCardTest(){
        Game game = new Game();
        Map<Resource, Integer> costTest = new HashMap<>();
        game.setNumberOfPlayers(2);
        game.createPlayers();
        game.getPlayerFromList(0).getWarehouse().addResourceToWarehouse(1, Resource.SHIELD);
        costTest.put(Resource.SHIELD, 1);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), true);
        costTest.put(Resource.SHIELD, 2);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), false);
        game.getPlayerFromList(0).getChest().addResource(Resource.SHIELD, 5);
        costTest.put(Resource.SHIELD, 6);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), true);
        costTest.put(Resource.MONEY, 2);
        game.getPlayerFromList(0).getChest().addResource(Resource.MONEY, 1);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), false);
        game.getPlayerFromList(0).getWarehouse().addResourceToWarehouse(2, Resource.MONEY);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), true);
        game.getPlayerFromList(0).getChest().addResource(Resource.SLAVE,20);
        costTest.put(Resource.SLAVE, 7);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), true);
        costTest.put(Resource.SLAVE, 21);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), false);
        game.getPlayerFromList(0).getWarehouse().unlockLeaderLevel(Resource.STONE);
        game.getPlayerFromList(0).getWarehouse().addResourceToSpecialLevel(1,Resource.STONE);
        game.getPlayerFromList(0).getWarehouse().addResourceToSpecialLevel(1,Resource.STONE);
        costTest.put(Resource.SLAVE, 20);
        costTest.put(Resource.STONE, 2);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), true);
    }

    @Test
    public void chooseDevCardTest(){

    }

    @Test
    public void buyDevCardTest(){

    }

    @Test
    public void canBuyDevCardWithList(){

    }
}
