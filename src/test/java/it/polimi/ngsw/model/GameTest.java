package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GameTest {

    Game game = new Game();

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
        for(int i=0; i<6; i++){
            ActionToken tempActionToken = game.getTokensDeque().getFirst();
            if( !(tempActionToken instanceof MoveAndScrum) ){
                game.drawActionToken();
                assertEquals(tempActionToken, game.getTokensDeque().getLast());
            }
        }
    }
    /*
    @Test
    public void playerPaysProductionPowerTest(){

        Player player = new Player();
        player.getChest().addResourceToChest(Resource.SHIELD, 10);
        player.getChest().addResourceToChest(Resource.MONEY, 10);
        player.getChest().addResourceToChest(Resource.STONE, 10);
        player.getChest().addResourceToChest(Resource.SLAVE, 10);
        player.getWarehouse().addResourceToWarehouse(1, Resource.SHIELD);
        player.getWarehouse().addResourceToWarehouse(2, Resource.STONE);
        player.getWarehouse().addResourceToWarehouse(2, Resource.STONE);
        player.getWarehouse().addResourceToWarehouse(3, Resource.MONEY);
        player.getWarehouse().addResourceToWarehouse(3, Resource.MONEY);
        player.getWarehouse().addResourceToWarehouse(3, Resource.MONEY);

        List<Resource> resourceList1 = new ArrayList<Resource>();
        resourceList1.add(Resource.SHIELD);
        resourceList1.add(Resource.STONE);
        resourceList1.add(Resource.MONEY);
        List<Resource> resourceList2 = new ArrayList<Resource>();
        resourceList2.add(Resource.MONEY);
        resourceList2.add(Resource.SLAVE);
        resourceList2.add(Resource.STONE);
        ProductionPower productionPower = new ProductionPower(resourceList1, resourceList2);
        assertTrue(game.playerPaysProductionPower(player, productionPower));

    }
    */
}
