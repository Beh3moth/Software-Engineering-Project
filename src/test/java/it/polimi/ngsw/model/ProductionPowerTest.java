package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ProductionPowerTest {

    @Test
    public void addCoordinateTest(){
        Player player = new Player();
        player.getWarehouse().addResourceToWarehouse(3, Resource.SHIELD);
        player.getChest().addResourceToChest(Resource.SHIELD,10);
        List<Resource> resourceList1 = new ArrayList<>();
        resourceList1.add(Resource.SHIELD);
        resourceList1.add(Resource.STONE);
        resourceList1.add(Resource.MONEY);
        List<Resource> resourceList2 = new ArrayList<>();
        resourceList2.add(Resource.MONEY);
        resourceList2.add(Resource.SLAVE);
        resourceList2.add(Resource.STONE);
        ProductionPower productionPower = new ProductionPower(resourceList1, resourceList2);
        assertTrue(productionPower.addCoordinate(Resource.SHIELD, true, 3, player));
        assertTrue(productionPower.addCoordinate(Resource.SHIELD, false, 0, player));
        assertFalse(productionPower.addCoordinate(Resource.MONEY, false, 0, player));
    }

}
