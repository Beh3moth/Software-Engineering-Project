package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class WarehouseTest {

    Warehouse warehouse = new Warehouse();
/*
    @Test
    public void addResourceToWarehouseTest(){
        //test legalità primo piano
        warehouse.addResourceToWarehouse(1,Resource.MONEY);
        assertEquals(warehouse.getShelf(1).getResourceNumber(), 1);
        assertEquals(warehouse.getShelf(1).getResourceType(), Resource.MONEY);
        assertEquals(warehouse.addResourceToWarehouse(1,Resource.MONEY), true);
        assertEquals(warehouse.getShelf(1).getResourceNumber(), 2);
        assertEquals(warehouse.getShelf(1).getResourceType(), Resource.MONEY);

        //test legalità secondo piano
        warehouse.addResourceToWarehouse(2, Resource.SHIELD);
        assertEquals(warehouse.getShelf(2).getResourceNumber(), 1);
        assertEquals(warehouse.getShelf(2).getResourceType(), Resource.SHIELD);
        warehouse.addResourceToWarehouse(2, Resource.SHIELD);
        assertEquals(warehouse.getShelf(2).getResourceNumber(), 2);
        assertEquals(warehouse.getShelf(2).getResourceType(), Resource.SHIELD);
        assertEquals(warehouse.addResourceToWarehouse(2,Resource.SHIELD), false);
        assertEquals(warehouse.getShelf(2).getResourceNumber(), 2);
        assertEquals(warehouse.getShelf(2).getResourceType(), Resource.SHIELD);

        //test legalità terzo piano
        warehouse.addResourceToWarehouse(3, Resource.STONE);
        assertEquals(warehouse.getShelf(3).getResourceNumber(), 1);
        assertEquals(warehouse.getShelf(3).getResourceType(), Resource.STONE);
        warehouse.addResourceToWarehouse(3, Resource.STONE);
        assertEquals(warehouse.getShelf(3).getResourceNumber(), 2);
        assertEquals(warehouse.getShelf(3).getResourceType(), Resource.STONE);
        warehouse.addResourceToWarehouse(3, Resource.STONE);
        assertEquals(warehouse.getShelf(3).getResourceNumber(), 3);
        assertEquals(warehouse.getShelf(3).getResourceType(), Resource.STONE);
        assertEquals(warehouse.addResourceToWarehouse(3,Resource.STONE), false);
        assertEquals(warehouse.getShelf(3).getResourceNumber(), 3);
        assertEquals(warehouse.getShelf(3).getResourceType(), Resource.STONE);
    }
*/

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


}
