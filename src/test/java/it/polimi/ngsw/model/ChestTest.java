package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Random;


public class ChestTest {

    Chest chest = new Chest();

    public void randomChest(){
        chest.addResource(Resource.SHIELD, new Random().nextInt(1));
        chest.addResource(Resource.STONE, new Random().nextInt(1));
        chest.addResource(Resource.SLAVE, new Random().nextInt(1));
        chest.addResource(Resource.MONEY, new Random().nextInt(1));
    }

    /*@Test
    public void randomTest(){
        randomChest();
        for(int i=0; i<100; i++){
            if( (new Random().nextInt(100))%2 == 0 ){
                assertTrue(chest.addResource(Resource.SHIELD, new Random().nextInt(100)));
                assertTrue(chest.addResource(Resource.STONE, new Random().nextInt(100)));
                assertTrue(chest.addResource(Resource.SLAVE, new Random().nextInt(100)));
                assertTrue(chest.addResource(Resource.MONEY, new Random().nextInt(100)));
            }
            else{
                assertTrue(chest.removeResource(Resource.SHIELD, new Random().nextInt(chest.getShield())));
                assertTrue(chest.removeResource(Resource.STONE, new Random().nextInt(chest.getStone())));
                assertTrue(chest.removeResource(Resource.SLAVE, new Random().nextInt(chest.getSlave())));
                assertTrue(chest.removeResource(Resource.MONEY, new Random().nextInt(chest.getMoney())));
            }
        }
    }
    */

    @Test
    public void getTotalNumberOfResourcesTest(){
        chest.addResource(Resource.SHIELD, 1);
        chest.addResource(Resource.MONEY, 1);
        chest.addResource(Resource.SHIELD, 1);
        chest.addResource(Resource.STONE, 1);
        assertEquals(4, chest.getTotalNumberOfResources());
    }

    @Test
    public void getResourceNumberTest(){
        randomChest();
        assertEquals(chest.getResourceNumber(Resource.MONEY), chest.getMoney());
        assertEquals(chest.getResourceNumber(Resource.SHIELD), chest.getShield());
        assertEquals(chest.getResourceNumber(Resource.SLAVE), chest.getSlave());
        assertEquals(chest.getResourceNumber(Resource.STONE), chest.getStone());
    }

    @Test
    public void addResourceToChestTest() {

        int x = 0;
        for(int i = -5; i<5; i++){
            for(int j = -5; j<5; j++){

                chest.addResource(Resource.SHIELD, i);
                if(i>0)
                    x+=i;
                chest.addResource(Resource.SHIELD, j);
                if(j>0)
                    x+=j;
                assertEquals( x, chest.getShield());

            }
        }

    }

    @Test
    public void removeResourceToChestTest() {
        assertFalse(chest.removeResource(Resource.SHIELD, 4));
        int x = 0;
        for(int i = -5; i<5; i++) {
            for (int j = -5; j < 5; j++) {
                chest.addResource(Resource.SHIELD, i);
                if(i > 0){
                    x += i;
                }
                chest.removeResource(Resource.SHIELD, j);
                if(x - j > 0){
                    x -= j;
                } else x = 0;
                assertEquals(x, chest.getShield());
            }
        }
    }

    @Test
    public void getShieldFromChestTest(){
        chest.addResource(Resource.SHIELD, 1);
        chest.addResource(Resource.SHIELD, 2);
        assertEquals(3, chest.getShield());
    }

    @Test
    public void getStoneFromChestTest(){
        chest.addResource(Resource.STONE, 1);
        chest.addResource(Resource.STONE, 2);
        assertEquals(3, chest.getStone());
    }

    @Test
    public void getSlaveFromChestTest(){
        chest.addResource(Resource.SLAVE, 1);
        chest.addResource(Resource.SLAVE, 2);
        assertEquals(3, chest.getSlave());
    }

    @Test
    public void getMoneyFromChestTest(){
        chest.addResource(Resource.MONEY, 1);
        chest.addResource(Resource.MONEY, 2);
        assertEquals(3, chest.getMoney());
    }

    @Test
    public void containsTest(){
        chest.addResource(Resource.MONEY, 1);
        assertTrue(chest.contains(Resource.MONEY, 1));
        assertTrue(chest.contains(Resource.MONEY, 0));
    }
}