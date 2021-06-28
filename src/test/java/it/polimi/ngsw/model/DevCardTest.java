package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DevCardTest {
    List<Resource> res = new ArrayList<>();

    @Test
    public void gettterTest() {
        res.add(Resource.SLAVE);
        ProductionPower prod = new ProductionPower(res, res);
        DevCard dev = new DevCard(1, DevCardColour.EMPTY, res, prod, 5);
        assertEquals(1, dev.getDevLevel());
        assertEquals(Resource.SLAVE, dev.getResourceToPay().get(0));
        assertEquals(5, dev.getPV());
        assertEquals(DevCardColour.EMPTY, dev.getCardColour());
        assertEquals(dev.getProductionPower().getResourceToPay().get(0), res.get(0));
        assertEquals(dev.getProductionPower().getResourceToReceive().get(0), res.get(0));
    }


}
