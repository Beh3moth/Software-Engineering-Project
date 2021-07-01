package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.sql.Array;


public class WarehouseTest {

    Warehouse warehouse = new Warehouse();

    @Test
    public void getTotalNumberOfResourcesTest(){
        warehouse.addResourceToWarehouse(1, Resource.SLAVE);
        assertEquals(warehouse.getTotalNumberOfResources(), 1);
    }

    @Test
    public void addResourceToWhiteStockTest(){
        warehouse.addResourceToWhiteStock(Resource.STONE);
        assertEquals(warehouse.getWhiteStock().get(0), Resource.STONE);
    }

    @Test
    public void controlSpecialShelfTest(){
        warehouse.unlockLeaderLevel(Resource.STONE);
        assertEquals(warehouse.controlSpecialShelf(4, Resource.SLAVE), false);
        assertEquals(warehouse.controlSpecialShelf(4, Resource.STONE), true);
        warehouse.addResourceToWarehouse(4,Resource.STONE);
        warehouse.addResourceToWarehouse(4,Resource.STONE);
        assertEquals(warehouse.controlSpecialShelf(4, Resource.STONE), false);
    }

    @Test
    public void controlShelfTest(){
        assertEquals(warehouse.controlShelf(1, Resource.STONE), true);
        warehouse.addResourceToWarehouse(1, Resource.STONE);
        assertEquals(warehouse.controlShelf(1, Resource.MONEY), false);
    }

    @Test
    public void addResourceToWarehouseTest(){
        assertEquals(warehouse.addResourceToWarehouse(4, Resource.STONE), false);
        warehouse.unlockLeaderLevel(Resource.STONE);
        assertEquals(warehouse.addResourceToWarehouse(4, Resource.STONE), true);
        assertEquals(warehouse.addResourceToWarehouse(1, Resource.MONEY), true);
        assertEquals(warehouse.addResourceToWarehouse(1, Resource.STONE), false);
        assertEquals(warehouse.addResourceToWarehouse(1, Resource.MONEY), true);
        assertEquals(warehouse.addResourceToWarehouse(1,Resource.STONE), false);
    }

    @Test
    public void discardResourceFromWarehouseTest(){

        //test legalità primo piano
        assertEquals(warehouse.discardResourceFromWarehouse(1), false);
        warehouse.addResourceToWarehouse(1,Resource.MONEY);
        warehouse.discardResourceFromWarehouse(1);
        assertEquals(warehouse.getShelf(1).getResourceType(), Resource.EMPTY);
        assertEquals(warehouse.getShelf(1).getResourceNumber(), 0);

        //test legalità secondo piano
        assertEquals(warehouse.discardResourceFromWarehouse(2), false);
        warehouse.addResourceToWarehouse(2,Resource.MONEY);
        warehouse.discardResourceFromWarehouse(2);
        assertEquals(warehouse.getShelf(2).getResourceType(), Resource.EMPTY);
        assertEquals(warehouse.getShelf(2).getResourceNumber(), 0);
        for(int i = 0; i < 2; i++){
            warehouse.addResourceToWarehouse(2,Resource.SHIELD);
        }

        warehouse.discardResourceFromWarehouse(2);
        assertEquals(warehouse.getShelf(2).getResourceNumber(), 1);
        assertEquals(warehouse.getShelf(2).getResourceType(), Resource.SHIELD);

        //test legalità terzo piano
        assertEquals(warehouse.discardResourceFromWarehouse(3), false);
        warehouse.addResourceToWarehouse(3,Resource.STONE);
        warehouse.discardResourceFromWarehouse(3);
        assertEquals(warehouse.getShelf(3).getResourceType(), Resource.EMPTY);
        assertEquals(warehouse.getShelf(3).getResourceNumber(), 0);
        for(int i = 0; i < 3; i++){
            warehouse.addResourceToWarehouse(3,Resource.STONE);
        }
        warehouse.discardResourceFromWarehouse(3);
        assertEquals(warehouse.getShelf(3).getResourceType(), Resource.STONE);
        assertEquals(warehouse.getShelf(3).getResourceNumber(), 2);
        warehouse.discardResourceFromWarehouse(3);
        assertEquals(warehouse.getShelf(3).getResourceType(), Resource.STONE);
        assertEquals(warehouse.getShelf(3).getResourceNumber(), 1);
    }


    @Test
    public void unlockLeaderLevelTest(){
        assertEquals(warehouse.getLeaderLevelType(1), Resource.EMPTY);
        assertEquals(warehouse.getLeaderLevelType(2), Resource.EMPTY);
        warehouse.unlockLeaderLevel(Resource.MONEY);
        assertEquals(warehouse.getLeaderLevelType(1), Resource.MONEY);
        assertEquals(warehouse.getLeaderLevelType(2), Resource.EMPTY);
        warehouse.unlockLeaderLevel(Resource.MONEY);
        assertEquals(warehouse.getLeaderLevelType(1), Resource.MONEY);
        assertEquals(warehouse.getLeaderLevelType(2), Resource.MONEY);
        assertEquals(warehouse.unlockLeaderLevel(Resource.MONEY), false);
    }

    @Test
    public void containsTest(){
        assertEquals(warehouse.contains(1, Resource.STONE), false);
        warehouse.addResourceToWarehouse(1, Resource.STONE);
        assertEquals(warehouse.contains(1, Resource.STONE), true);
        warehouse.discardResourceFromWarehouse(1);
        warehouse.addResourceToWarehouse(2, Resource.MONEY);
        warehouse.addResourceToWarehouse(3, Resource.SLAVE);
        warehouse.unlockLeaderLevel(Resource.SHIELD);
        warehouse.addResourceToWarehouse(4, Resource.SHIELD);
        warehouse.unlockLeaderLevel(Resource.STONE);
        warehouse.addResourceToWarehouse(5, Resource.STONE);
        assertEquals(warehouse.contains(1, Resource.STONE), true);
        assertEquals(warehouse.contains(1, Resource.SHIELD), true);
        assertEquals(warehouse.contains(1, Resource.MONEY), true);
        assertEquals(warehouse.contains(1, Resource.SLAVE), true);
    }

    @Test
    public void canBuyTest(){

        Resource[] resources = new Resource[2];
        Boolean[] warehouses = new Boolean[2];
        Integer[] level = new Integer[2];

        resources[0] = Resource.SHIELD;
        resources[1] = Resource.SLAVE;
        warehouses[0] = false;
        warehouses[1] = true;
        level[0] = 0;
        level[1] = 1;

        assertEquals(warehouse.canBuy(resources, warehouses, level), false);
        warehouse.addResourceToWarehouse(1, Resource.SLAVE);
        assertEquals(warehouse.canBuy(resources, warehouses, level), true);
    }

    @Test
    public void getNumberOfTest(){
        assertEquals(warehouse.getNumberOf(Resource.SLAVE), 0);
        warehouse.addResourceToWarehouse(1, Resource.SLAVE);
        warehouse.unlockLeaderLevel(Resource.SLAVE);
        warehouse.addResourceToWarehouse(4, Resource.SLAVE);
        assertEquals(warehouse.getNumberOf(Resource.SLAVE), 2);
    }

}
