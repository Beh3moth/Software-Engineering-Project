package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ngsw.model.Chest;
import it.polimi.ngsw.model.Resource;
import org.junit.jupiter.api.Test;


public class ChestTest {

    Chest chest = new Chest();

    @Test
    public void addResourceToChestTest() {
        int x = 0;
        for(int i = -5; i<5; i++){
            for(int j = -5; j<5; j++){

                chest.addResourceToChest(Resource.SHIELD, i);
                if(i>0)
                    x+=i;
                chest.addResourceToChest(Resource.SHIELD, j);
                if(j>0)
                    x+=j;
                assertEquals( x, chest.getShieldFromChest());

            }
        }

    }

    @Test
    public void removeResourceToChestTest() {
        int x = 0;
        for(int i = -5; i<5; i++) {
            for (int j = -5; j < 5; j++) {
                chest.addResourceToChest(Resource.SHIELD, i);
                if(i > 0){
                    x += i;
                }
                chest.removeResourceFromChest(Resource.SHIELD, j);
                if(x - j > 0){
                    x -= j;
                } else x = 0;
                assertEquals(x, chest.getShieldFromChest());
            }
        }
    }

    @Test
    public void getShieldFromChestTest(){
        chest.addResourceToChest(Resource.SHIELD, 1);
        chest.addResourceToChest(Resource.SHIELD, 2);
        assertEquals(3, chest.getShieldFromChest());
    }

    @Test
    public void getStoneFromChestTest(){
        chest.addResourceToChest(Resource.STONE, 1);
        chest.addResourceToChest(Resource.STONE, 2);
        assertEquals(3, chest.getStoneFromChest());
    }

    @Test
    public void getSlaveFromChestTest(){
        chest.addResourceToChest(Resource.SLAVE, 1);
        chest.addResourceToChest(Resource.SLAVE, 2);
        assertEquals(3, chest.getSlaveFromChest0());
    }

    @Test
    public void getMoneyFromChestTest(){
        chest.addResourceToChest(Resource.MONEY, 1);
        chest.addResourceToChest(Resource.MONEY, 2);
        assertEquals(3, chest.getMoneyFromChest());
    }

}