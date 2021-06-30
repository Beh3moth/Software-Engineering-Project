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
    public void buyFromMarketTest(){
        boolean bool = false;
        Player p = new Player("Jackson");
        Player h = new Player("J");
        Game g = new Game();
        g.addPlayer(p);
        g.addPlayer(h);
        g.setChosenMaxPlayers(2);
        g.setNumberOfPlayers(2);
        g.buyFromMarket(1, 1, p);
        if(p.getWarehouse().getWarehouseStock().size() <= 4 && p.getWarehouse().getWarehouseStock().size() >= 0){
            bool = true;
        }
        assertTrue(bool);
        bool = false;
        int n = 0;
        for(int i= 0; i < p.getWarehouse().getWarehouseStock().size(); i++){
            if(p.getWarehouse().getWarehouseStock().get(i) == Resource.EMPTY){
                bool = true;
                n++;
            }
        }
        int j = p.getWarehouse().getWarehouseStock().size();
        int k = p.getWarehouse().getWhiteStock().size();
        g.manageWhiteResources(p);
        assertEquals(n, p.getWarehouse().getWhiteStock().size());
        assertEquals(j, p.getWarehouse().getWarehouseStock().size());
        p.getWarehouse().removeAllStock();
        p.setWhiteMarblePower(Resource.MONEY);
        g.buyFromMarket(1,1, p);
        bool = false;
        n = 0;
        int i = p.getWarehouse().getWarehouseStock().size();
        int w = p.getWarehouse().getWhiteStock().size();
        g.manageWhiteResources(p);
        assertEquals(0, p.getWarehouse().getWhiteStock().size());
        assertEquals(i + w, p.getWarehouse().getWarehouseStock().size());
        p.getFaithPath().increaseCrossPosition(26);
        assertTrue(g.isGameEndedMultiPlayers());
    }

    @Test
    public void initLawrenceFaithTest(){
        Player p = new Player("Jackson");
        Game g = new Game();
        g.addPlayer(p);
        g.setChosenMaxPlayers(1);
        assertFalse(g.SinglePlayerIsTheWinner());

        p.getFaithPath().increaseCrossPosition(27);
        assertTrue(g.SinglePlayerIsTheWinner());
        g.initLawrenceFaithPath();
        g.initLawrenceFaithPath();
        g.getLawrenceFaithPath().increaseCrossPosition(27);
        assertTrue(g.lawrenceIsTheWinner());
        g.increaseOtherFaithPoints(p, 3);
        assertEquals(30, g.getLawrenceFaithPath().getCrossPosition());
    }

    @Test
    public void leaderCardTest(){
        Player p = new Player("Jackson");
        Game g = new Game();
        g.addPlayer(p);
        g.setChosenMaxPlayers(1);
        assertEquals(16, g.getLeaderCards().size());
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
