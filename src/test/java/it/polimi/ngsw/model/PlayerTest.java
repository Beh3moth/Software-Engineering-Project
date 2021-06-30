package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

public class PlayerTest {

    Game game = new Game();
    Player player = new Player("Jhon");

    public void randomChest(Chest chest){
        chest.addResource(Resource.SHIELD, new Random().nextInt(100));
        chest.addResource(Resource.STONE, new Random().nextInt(100));
        chest.addResource(Resource.SLAVE, new Random().nextInt(100));
        chest.addResource(Resource.MONEY, new Random().nextInt(100));
    }

    @Test
    public void getMapFromChestAndWarehouseTest(){
        randomChest(player.getChest());
        assertEquals(player.getMapFromChestAndWarehouse(), player.getChest().getResourcesAsMap());
    }

    @Test
    public void canAffordTest(){
        randomChest(player.getChest());
        assertTrue(player.canAfford(player.getChest().getResourcesAsMap()));
    }


    @Test
    public void payProductionPowerTest() throws FileNotFoundException {
        randomChest(player.getChest());
        player.getWarehouse().addResourceToWarehouse(1, Resource.MONEY);
        DevCardDashboard devCardDashboard = new DevCardDashboard();
        DevCardParser devCardParser = new DevCardParser();
        ProductionPower productionPower = devCardParser.parseDevDeck("src/main/java/it/polimi/ngsw/resources/parsingInfo/blue_level_one.json").get(0).getProductionPower();
        Boolean[] isWarehouse = {true, false, false};
        Integer[] shelfLevel = {1, 0, 0};
        Resource[] resources = {Resource.MONEY, Resource.STONE, Resource.SLAVE};
        assertTrue(player.payProductionPower(productionPower, isWarehouse, shelfLevel, resources));
        assertTrue(player.getAbilityList().isEmpty());
        assertFalse(player.getPaidList().isEmpty());
        player.rejectProductionPower(productionPower);
        assertTrue(player.getPaidList().isEmpty());
    }

    @Test
    public void checkForLeaderProductionPowerAbilityTest(){

        int i=0;
        for(LeaderCard leaderCard : game.getLeaderCards()){
            if(leaderCard.getAbilityName().equals("production power") && i<2){
                leaderCard.activateAbility(player);
                i++;
            }
        }

        player.addProductionPowerToPaidList(player.getDevCardDashboard().getProductionPower(4));
        player.addProductionPowerToPaidList(player.getDevCardDashboard().getProductionPower(5));

        for(ProductionPower productionPower : player.getAbilityList()){
            assertTrue(productionPower.equals(player.getDevCardDashboard().getProductionPower(4)) || productionPower.equals(player.getDevCardDashboard().getProductionPower(5)));
        }

        assertTrue(player.getAbilityList().isEmpty());
        assertTrue(player.activateProductionPowers());

    }


    @Test
    public void getPVFromLeaderCardsTest(){
        player.receiveLeaderCards(game.removeAndReturnTheLastFourLeaderCards());
        player.discardLeaderCard(0);
        player.discardLeaderCard(0);
        int PV = 0;
        for(LeaderCard leaderCard : player.getLeaderCards()){
            if(leaderCard.isActive()){
                PV += leaderCard.getPV();
            }
        }
        assertEquals(PV, player.getPVFromLeaderCards());
    }

    @Test
    public void getPVFromResourcesTest(){
        player.getChest().addResource(Resource.SHIELD, 20);
        player.getWarehouse().addResourceToWarehouse(1, Resource.MONEY);
        assertEquals(4, player.getPVFormResources());
    }



}
