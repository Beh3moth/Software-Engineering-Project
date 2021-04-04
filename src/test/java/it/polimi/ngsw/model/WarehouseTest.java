package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class WarehouseTest {

    Warehouse warehouse = new Warehouse();

    /**
    @Test
    public void activateAbilityTest(){
        list.add(0, devCard);
        LeaderCardBaseDecorator leader1 = new DiscountAbility(new ConcreteLeaderCard(), 2, list, Resource.SHIELD);
        leader1.activateAbility(player, Resource.EMPTY, 0);
        assert(player.getDiscountPowerOne().equals(Resource.SHIELD));
    }
    */

    @Test
    public void addResourceToWarehouseTest(){
        //test legalità primo piano
        warehouse.addResourceToWarehouse(1,Resource.MONEY);
        assertEquals(warehouse.getShelf(1).getResourceNumber(), 1);
        assertEquals(warehouse.getShelf(1).getResourceType(), Resource.MONEY);
        assertEquals(warehouse.addResourceToWarehouse(1,Resource.MONEY), false);
        assertEquals(warehouse.getShelf(1).getResourceNumber(), 1);
        assertEquals(warehouse.getShelf(1).getResourceType(), Resource.MONEY);

        //test legalità secondo piano
        warehouse.addResourceToWarehouse(2, Resource.MONEY);
        assertEquals(warehouse.getShelf(2).getResourceNumber(), 1);
        assertEquals(warehouse.getShelf(2).getResourceType(), Resource.MONEY);
        warehouse.addResourceToWarehouse(2, Resource.MONEY);
        assertEquals(warehouse.getShelf(2).getResourceNumber(), 2);
        assertEquals(warehouse.getShelf(2).getResourceType(), Resource.MONEY);
        assertEquals(warehouse.addResourceToWarehouse(2,Resource.MONEY), false);
        assertEquals(warehouse.getShelf(2).getResourceNumber(), 2);
        assertEquals(warehouse.getShelf(2).getResourceType(), Resource.MONEY);

        //test legalità terzo piano
        warehouse.addResourceToWarehouse(3, Resource.MONEY);
        assertEquals(warehouse.getShelf(3).getResourceNumber(), 1);
        assertEquals(warehouse.getShelf(3).getResourceType(), Resource.MONEY);
        warehouse.addResourceToWarehouse(3, Resource.MONEY);
        assertEquals(warehouse.getShelf(3).getResourceNumber(), 2);
        assertEquals(warehouse.getShelf(3).getResourceType(), Resource.MONEY);
        warehouse.addResourceToWarehouse(3, Resource.MONEY);
        assertEquals(warehouse.getShelf(3).getResourceNumber(), 3);
        assertEquals(warehouse.getShelf(3).getResourceType(), Resource.MONEY);
        assertEquals(warehouse.addResourceToWarehouse(3,Resource.MONEY), false);
        assertEquals(warehouse.getShelf(3).getResourceNumber(), 3);
        assertEquals(warehouse.getShelf(3).getResourceType(), Resource.MONEY);
    }

    @Test
    public void addResourceToSpecialLevelTest(){

    }

    @Test
    public void discardResourceFromWarehouseTest(){

        //test legalità primo piano
        assertEquals(warehouse.discardResourceFromWarehouse(1), false);
        warehouse.addResourceToWarehouse(1,Resource.MONEY);
        warehouse.discardResourceFromWarehouse(1);
        assertEquals(warehouse.getLeaderShelf(1).getResourceType(), Resource.EMPTY);
        assertEquals(warehouse.getLeaderShelf(1).getResourceNumber(), 0);

        //test legalità secondo piano
        assertEquals(warehouse.discardResourceFromWarehouse(2), false);
        warehouse.addResourceToWarehouse(2,Resource.MONEY);
        warehouse.discardResourceFromWarehouse(2);
        assertEquals(warehouse.getLeaderShelf(2).getResourceType(), Resource.EMPTY);
        assertEquals(warehouse.getLeaderShelf(2).getResourceNumber(), 0);
        for(int i = 0; i < 2; i++){
            warehouse.addResourceToWarehouse(2,Resource.MONEY);
        }
        warehouse.discardResourceFromWarehouse(2);
        assertEquals(warehouse.getLeaderShelf(2).getResourceType(), Resource.MONEY);
        assertEquals(warehouse.getLeaderShelf(2).getResourceNumber(), 1);

        //test legalità terzo piano
        assertEquals(warehouse.discardResourceFromWarehouse(3), false);
        warehouse.addResourceToWarehouse(3,Resource.MONEY);
        warehouse.discardResourceFromWarehouse(3);
        assertEquals(warehouse.getLeaderShelf(3).getResourceType(), Resource.EMPTY);
        assertEquals(warehouse.getLeaderShelf(3).getResourceNumber(), 0);
        for(int i = 0; i < 3; i++){
            warehouse.addResourceToWarehouse(3,Resource.MONEY);
        }
        warehouse.discardResourceFromWarehouse(3);
        assertEquals(warehouse.getLeaderShelf(3).getResourceType(), Resource.MONEY);
        assertEquals(warehouse.getLeaderShelf(3).getResourceNumber(), 2);
        warehouse.discardResourceFromWarehouse(3);
        assertEquals(warehouse.getLeaderShelf(3).getResourceType(), Resource.MONEY);
        assertEquals(warehouse.getLeaderShelf(3).getResourceNumber(), 2);
    }

    public void discardResourceFromSpecialLevel(){

    }

    @Test
    public void addResourceToStock(){
        assertEquals(warehouse.addResourceToStock(Resource.EMPTY), false);
        assertEquals(warehouse.addResourceToStock(Resource.FAITHPOINT), false);
        warehouse.addResourceToStock(Resource.MONEY);
        assertEquals(Resource.MONEY,warehouse.getStockResource(0));
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
    public void moveResourceToStockTest(){

    }

    @Test
    public void discardResourceFromStockTest(){

    }

    @Test
    public void removeResourceFromStockTest(){

    }

}
