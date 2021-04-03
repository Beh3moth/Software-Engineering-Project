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
                assertTrue(game.drawActionToken());
                assertEquals(tempActionToken, game.getTokensDeque().getLast());
            }
        }
    }

    @Test
    public void canBuyProductionPowerTest(){
        //Initialize player resources
        Player player = new Player();
        player.getWarehouse().addResourceToWarehouse(3, Resource.SHIELD);
        player.getChest().addResourceToChest(Resource.SHIELD,10);
        //Initialize a Production Power
        List<Resource> resourceList1 = new ArrayList<>();
        resourceList1.add(Resource.SHIELD);
        resourceList1.add(Resource.STONE);
        resourceList1.add(Resource.MONEY);
        List<Resource> resourceList2 = new ArrayList<>();
        resourceList2.add(Resource.SHIELD);
        resourceList2.add(Resource.SLAVE);
        resourceList2.add(Resource.STONE);
        ProductionPower productionPower1 = new ProductionPower(resourceList1, resourceList2);

        assertFalse(game.canBuyProductionPower(player, productionPower1));
        List<Resource> resourceList3 = new ArrayList<>();
        resourceList3.add(Resource.SHIELD);
        List<Resource> resourceList4 = new ArrayList<>();
        resourceList4.add(Resource.SHIELD);
        ProductionPower productionPower2 = new ProductionPower(resourceList3, resourceList4);
        assertTrue(game.canBuyProductionPower(player, productionPower2));
    }

    @Test
    public void chooseProductionPowerTest(){

        Player player = new Player();
        player.getWarehouse().addResourceToWarehouse(3, Resource.SHIELD);
        player.getChest().addResourceToChest(Resource.SHIELD,10);

        List<Resource> resourceList1 = new ArrayList<>();
        resourceList1.add(Resource.SHIELD);
        resourceList1.add(Resource.STONE);
        resourceList1.add(Resource.MONEY);
        List<Resource> resourceList2 = new ArrayList<>();
        resourceList2.add(Resource.SHIELD);
        resourceList2.add(Resource.SLAVE);
        resourceList2.add(Resource.STONE);
        ProductionPower productionPower1 = new ProductionPower(resourceList1, resourceList2);

        List<Resource> resourceList3 = new ArrayList<>();
        resourceList3.add(Resource.SHIELD);
        List<Resource> resourceList4 = new ArrayList<>();
        resourceList4.add(Resource.SHIELD);
        ProductionPower productionPower2 = new ProductionPower(resourceList3, resourceList4);

        DevCard devCard1 = new DevCard(1, DevCardColour.PURPLE, Resource.SHIELD, 2, productionPower1, 2);
        DevCard devCard2 = new DevCard(1, DevCardColour.PURPLE, Resource.SHIELD, 2, productionPower2, 2);

        player.getDevCardDashboard().putDevCardIn(0, devCard1);
        player.getDevCardDashboard().putDevCardIn(1, devCard2);

        assertFalse(game.chooseProductionPower(player, 0));
        assertTrue(game.chooseProductionPower(player, 1));
        assertFalse(game.chooseProductionPower(player, 2));
        assertFalse(game.chooseProductionPower(player, 3));
        assertFalse(game.chooseProductionPower(player, 4));
        assertFalse(game.chooseProductionPower(player, 5));

    }

    @Test
    public void removeResourcesFormProductionPowerTest(){

        Player player = new Player();

        List<List<Object>> listOfList = new ArrayList<>();
        List<Object> tempList = new ArrayList<>();
        tempList.add(Resource.SHIELD);
        tempList.add(true);
        tempList.add(3);
        listOfList.add(tempList);
        List<Object> tempList1 = new ArrayList<>();
        tempList1.add(Resource.SHIELD);
        tempList1.add(false);
        tempList1.add(0);
        listOfList.add(tempList1);

        player.getWarehouse().addResourceToWarehouse(3, Resource.SHIELD);
        player.getChest().addResourceToChest(Resource.SHIELD,10);

        List<Resource> resourceList3 = new ArrayList<>();
        resourceList3.add(Resource.SHIELD);
        List<Resource> resourceList4 = new ArrayList<>();
        resourceList4.add(Resource.SHIELD);
        ProductionPower productionPower2 = new ProductionPower(resourceList3, resourceList4);

        assertTrue(game.payProductionPower(player, listOfList, productionPower2));
        assertTrue(game.removeResourcesFormProductionPower(player, productionPower2));

    }

    @Test
    public void activateProductionPowersTest(){

        canBuyProductionPowerTest();
        assertTrue(game.activateProductionPowers());

    }

}
