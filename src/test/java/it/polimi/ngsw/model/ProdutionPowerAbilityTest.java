package it.polimi.ngsw.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
public class ProdutionPowerAbilityTest {

    @Test
    public void getIdTest(){

        ProductionPowerAbility w = new ProductionPowerAbility(new ConcreteLeaderCard(), 2,DevCardColour.GREEN,Resource.SHIELD, "id" );
        assertEquals("id", w.getLeaderCardId());
        assertEquals(2, w.getPV());

        assertEquals(DevCardColour.GREEN, w.getLeaderCardCostDevCardColour());
        assertEquals(false, w.isActive());
        assertEquals(Resource.SHIELD, w.getInputResource());
        assertEquals(2,w.getLeaderCardCostDevCardLevel());

    }
}
