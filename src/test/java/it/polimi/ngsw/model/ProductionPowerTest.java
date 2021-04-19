package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductionPowerTest {

    ProductionPower productionPower = new ProductionPower(null, null);

    @Test
    public void setBaseProductionPowerListsTest(){
        Game game = new Game();
        game.setNumberOfPlayers(1);
        game.createPlayers();
        List<Resource> resourcesToPay = new ArrayList<>(2);
        resourcesToPay.add(Resource.SLAVE);
        resourcesToPay.add(Resource.MONEY);
        List<Resource> resourcesToReceive = new ArrayList<>(1);
        resourcesToReceive.add(Resource.STONE);
        assertTrue(game.getPlayerFromList(0).getDevCardDashboard().getProductionPower(0).setBaseProductionPowerLists(resourcesToPay, resourcesToReceive));
    }

    @Test
    public void getResourceToPayAsMapTest(){
        List<Resource> list1 = new ArrayList<>();
        List<Resource> list2 = new ArrayList<>();
        list1.add(Resource.STONE);
        list1.add(Resource.STONE);
        list1.add(Resource.STONE);
        list2.add(Resource.STONE);
        list2.add(Resource.STONE);
        ProductionPower productionPower = new ProductionPower(list1, list2);
        Map<Resource, Integer> map = new HashMap<>();
        map = productionPower.getResourceToPayAsMap();
        assertEquals(3, map.get(Resource.STONE));
    }

    @Test
    public void removeBaseProductionPowerListsTest(){
        List<Resource> list1 = new ArrayList<>();
        List<Resource> list2 = new ArrayList<>();
        list1.add(Resource.STONE);
        list1.add(Resource.STONE);
        list2.add(Resource.STONE);
        ProductionPower productionPower = new ProductionPower(null, null);
        productionPower.setBaseProductionPowerToTrue();
        assertFalse(productionPower.removeBaseProductionPowerLists());
        assertTrue(productionPower.setBaseProductionPowerLists(list1, list2));
        assertTrue(productionPower.removeBaseProductionPowerLists());
    }

    @Test
    public void setLeaderProductionPowerResourceToReceiveTest(){
        Resource resource = Resource.STONE;
        ProductionPower productionPower = new ProductionPower(null, null);
        assertTrue(productionPower.setLeaderProductionPowerResourceToReceive(resource));
    }

    @Test
    public void addCoordinatesTest(){

        Resource[] resourceType = {Resource.STONE,Resource.STONE, Resource.STONE};
        Boolean[] isWarehouse = {true, false, true};
        Integer[] shelfLevel = {0, 1, 2};
        assertTrue( productionPower.addCoordinates(resourceType, isWarehouse, shelfLevel));

        for(int i=0; i<3; i++){
            assertEquals(productionPower.getShelfLevel().get(i), shelfLevel[i]);
            assertEquals(productionPower.getResourceType().get(i), resourceType[i]);
            assertEquals(productionPower.getIsWarehouse().get(i), isWarehouse[i]);
        }

        Resource[] resourceType2 = {Resource.EMPTY,Resource.STONE, Resource.STONE};
        Boolean[] isWarehouse2 = {true, false, true};
        Integer[] shelfLevel2 = {0, 1, 2};
        assertFalse( productionPower.addCoordinates(resourceType2, isWarehouse2, shelfLevel2));

    }

    @Test
    public void moveResourcesToOriginTest(){
        Player player = new Player("Jhon");
        Resource[] resourceType = {Resource.STONE,Resource.STONE, Resource.MONEY};
        Boolean[] isWarehouse = {true, false, true};
        Integer[] shelfLevel = {1, 0, 2};
        assertTrue( productionPower.addCoordinates(resourceType, isWarehouse, shelfLevel));
        assertTrue(productionPower.moveResourcesToOrigin(player));
        assertTrue(productionPower.cleanCoordinates());
    }

}
