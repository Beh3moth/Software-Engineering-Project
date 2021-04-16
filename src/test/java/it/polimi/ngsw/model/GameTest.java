package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.*;

public class GameTest {

    Game game = new Game();

    public void randomChest(Chest chest){
        chest.addResource(Resource.SHIELD, new Random().nextInt(100));
        chest.addResource(Resource.STONE, new Random().nextInt(100));
        chest.addResource(Resource.SLAVE, new Random().nextInt(100));
        chest.addResource(Resource.MONEY, new Random().nextInt(100));
    }

    @Test
    public void removeAndReturnTheLastFourLeaderCardsTest(){
        for(int i=0; i<4; i++){
            List<LeaderCard> list = new ArrayList<>();
            try {
                  list = game.removeAndReturnTheLastFourLeaderCards();
            }
            catch (IndexOutOfBoundsException e){
                fail();
            }
            assertEquals(4, list.size());
        }
    }

    @Test
    public void shuffleActionTokensDequeTest(){
        List<ActionToken> testList1 = new ArrayList<>(game.getTokensDeque());
        assertTrue(game.shuffleActionTokensDeque());
        List<ActionToken> testList2 = new ArrayList<>(game.getTokensDeque());
        for(int i=0; i<6; i++){
            assertNotEquals(testList1, testList2);
        }
    }

    @Test
    public void drawActionTokenTest(){
        game.setNumberOfPlayers(1);
        game.createPlayers();
        for(int i=0; i<6; i++){
            ActionToken tempActionToken = game.getTokensDeque().getFirst();
            if( !(tempActionToken instanceof MoveAndScrum) ){
                assertTrue(game.drawActionToken());
                assertEquals(tempActionToken, game.getTokensDeque().getLast());
            }
        }
    }

    @Test
    public void chooseProductionPowerTest() throws FileNotFoundException {
        game.setNumberOfPlayers(1);
        game.createPlayers();
        randomChest(game.getPlayerFromList(0).getChest());
        DevCardDashboard devCardDashboard = game.getPlayerFromList(0).getDevCardDashboard();
        DevCardParser devCardParser = new DevCardParser();
        List<DevCard> devCardList = devCardParser.parseDevDeck("src/main/java/it/polimi/resources/blue_level_one.json");
        devCardDashboard.putDevCardIn(0, devCardList.get(0));
        devCardDashboard.putDevCardIn(1, devCardList.get(1));
        devCardDashboard.putDevCardIn(2, devCardList.get(2));
        assertNull(game.chooseProductionPower(game.getPlayerFromList(0), 0).getResourceToPay());
        assertNull(game.chooseProductionPower(game.getPlayerFromList(0), 0).getResourceToReceive());
        assertEquals(devCardList.get(0).getProductionPower(), game.chooseProductionPower(game.getPlayerFromList(0), 1));
        assertEquals(devCardList.get(1).getProductionPower(), game.chooseProductionPower(game.getPlayerFromList(0), 2));
        assertEquals(devCardList.get(2).getProductionPower(), game.chooseProductionPower(game.getPlayerFromList(0), 3));
        assertNull(game.chooseProductionPower(game.getPlayerFromList(0), 4));
    }

    @Test
    public void setBaseProductionPowerResourceListsTest() throws FileNotFoundException {
        game.setNumberOfPlayers(1);
        game.createPlayers();
        randomChest(game.getPlayerFromList(0).getChest());
        DevCardDashboard devCardDashboard = game.getPlayerFromList(0).getDevCardDashboard();
        DevCardParser devCardParser = new DevCardParser();
        List<DevCard> devCardList = devCardParser.parseDevDeck("src/main/java/it/polimi/resources/blue_level_one.json");
        devCardDashboard.putDevCardIn(0, devCardList.get(0));
        devCardDashboard.putDevCardIn(1, devCardList.get(1));
        devCardDashboard.putDevCardIn(2, devCardList.get(2));

        List<Resource> resourcesToPay = new ArrayList<>();
        resourcesToPay.add(Resource.STONE);
        resourcesToPay.add(Resource.MONEY);
        List<Resource> resourcesToReceive = new ArrayList<>();
        resourcesToReceive.add(Resource.SHIELD);

        assertTrue(game.setBaseProductionPowerResourceLists(resourcesToPay, resourcesToReceive, game.chooseProductionPower(game.getPlayerFromList(0), 0)));
        resourcesToPay.add(Resource.SLAVE);
        assertFalse(game.setBaseProductionPowerResourceLists(resourcesToPay, resourcesToReceive, game.chooseProductionPower(game.getPlayerFromList(0), 0)));
    }

    @Test
    public void canBuyProductionPowerTestWithResourcesInChest() throws FileNotFoundException {
        game.setNumberOfPlayers(1);
        game.createPlayers();
        randomChest(game.getPlayerFromList(0).getChest());
        DevCardDashboard devCardDashboard = game.getPlayerFromList(0).getDevCardDashboard();
        DevCardParser devCardParser = new DevCardParser();
        List<DevCard> devCardList = devCardParser.parseDevDeck("src/main/java/it/polimi/resources/green_level_one.json");
        devCardDashboard.putDevCardIn(0, devCardList.get(0));
        devCardDashboard.putDevCardIn(1, devCardList.get(1));
        devCardDashboard.putDevCardIn(2, devCardList.get(2));
        assertTrue(game.canBuyProductionPower(game.getPlayerFromList(0), game.chooseProductionPower(game.getPlayerFromList(0), 2)));
    }

    @Test
    public void canBuyProductionPowerTestWithoutResourcesInChest() throws FileNotFoundException {
        game.setNumberOfPlayers(1);
        game.createPlayers();
        DevCardDashboard devCardDashboard = game.getPlayerFromList(0).getDevCardDashboard();
        DevCardParser devCardParser = new DevCardParser();
        List<DevCard> devCardList = devCardParser.parseDevDeck("src/main/java/it/polimi/resources/green_level_one.json");
        devCardDashboard.putDevCardIn(0, devCardList.get(0));
        devCardDashboard.putDevCardIn(1, devCardList.get(1));
        devCardDashboard.putDevCardIn(2, devCardList.get(2));
        assertFalse(game.canBuyProductionPower(game.getPlayerFromList(0), game.chooseProductionPower(game.getPlayerFromList(0), 2)));
    }

    @Test
    public void canBuyProductionPowerTestWithBaseProductionPowerWithResources(){

        game.setNumberOfPlayers(1);
        game.createPlayers();
        Player player = game.getPlayerFromList(0);
        if( game.chooseProductionPower(player, 0).isBaseProductionPower){
            List<Resource> resourcesToPay = new ArrayList<>();
            resourcesToPay.add(Resource.STONE);
            resourcesToPay.add(Resource.MONEY);
            List<Resource> resourcesToReceive = new ArrayList<>();
            resourcesToReceive.add(Resource.SHIELD);
            game.setBaseProductionPowerResourceLists(resourcesToPay, resourcesToReceive, game.chooseProductionPower(player, 0));
            assertFalse(game.canBuyProductionPower(player, game.chooseProductionPower(player, 0)));
            randomChest(player.getChest());
            assertTrue(game.canBuyProductionPower(player, game.chooseProductionPower(player, 0)));
        }

    }

    public List<List<Object>> createChestCoordinates(Resource resourceToPayOne, Resource resourceToPayTwo){
        List<List<Object>> coordinates = new ArrayList<>();
        List<Object> objectList = new ArrayList<>();
        objectList.add(resourceToPayOne);
        objectList.add(true);
        objectList.add(1);
        coordinates.add(objectList);
        List<Object> objectList2 = new ArrayList<>();
        objectList2.add(resourceToPayTwo);
        objectList2.add(true);
        objectList2.add(0);
        coordinates.add(objectList2);
        return coordinates;
    }

    @Test
    public void payProductionPowerTest() throws FileNotFoundException {
        game.setNumberOfPlayers(1);
        game.createPlayers();
        Player player = game.getPlayerFromList(0);
        DevCardParser devCardParser = new DevCardParser();
        List<DevCard> devCardList = devCardParser.parseDevDeck("src/main/java/it/polimi/resources/green_level_one.json");
        ProductionPower productionPower = devCardList.get(0).getProductionPower();
        player.getWarehouse().addResourceToWarehouse(1, Resource.MONEY);
        List<List<Object>> coordinates = createChestCoordinates(Resource.MONEY, Resource.SHIELD);
        assertFalse(game.payProductionPower(player, coordinates, productionPower));
    }

    @Test
    public void activateProductionPowersTest(){

        game.setNumberOfPlayers(1);
        game.createPlayers();
        Player player = game.getPlayerFromList(0);
        randomChest(player.getChest());

        int i=0;
        for(LeaderCard leaderCard : game.getLeaderCards()){
            if(leaderCard.getAbilityName().equals("production power") && i<2){
                leaderCard.activateAbility(player);
                i++;
            }
        }

        List<List<Object>> coordinates = new ArrayList<>();
        List<Object> objectList = new ArrayList<>();
        objectList.add(game.chooseProductionPower(player, 4).getResourceToPay().get(0));
        objectList.add(false);
        objectList.add(0);
        coordinates.add(objectList);

        assertTrue(game.canBuyProductionPower(player, game.chooseProductionPower(player, 4)));
        assertTrue(game.payProductionPower(player, coordinates, game.chooseProductionPower(player, 4)));
        assertTrue(game.setResourceToReceiveFromLeaderProductionPowerAbility(player, Resource.MONEY, game.chooseProductionPower(player, 4)));
        assertTrue(game.activateProductionPowers(player));

    }

    @Test
    public void checkForLeaderProductionPowerAbilityTest(){

        game.setNumberOfPlayers(1);
        game.createPlayers();
        Player player = game.getPlayerFromList(0);

        assertNull(game.checkForLeaderProductionPowerAbility());

        int i=0;
        for(LeaderCard leaderCard : game.getLeaderCards()){
            if(leaderCard.getAbilityName().equals("production power") && i<2){
                leaderCard.activateAbility(player);
                i++;
            }
        }


        game.addProductionPowerToPaidList(player.getDevCardDashboard().getProductionPower(4));
        game.addProductionPowerToPaidList(player.getDevCardDashboard().getProductionPower(5));

        for(ProductionPower productionPower : game.checkForLeaderProductionPowerAbility()){
            assertTrue(productionPower.equals(player.getDevCardDashboard().getProductionPower(4)) || productionPower.equals(player.getDevCardDashboard().getProductionPower(5)));
        }

    }

    @Test
    public void updateTest1(){

        game.setNumberOfPlayers(3);
        game.createPlayers();

        for(int i=0; i<25; i++){
            game.getPlayerFromList(0).getFaithPath().increaseCrossPosition();
        }
        for(int i=0; i<25; i++){
            game.getPlayerFromList(1).getFaithPath().increaseCrossPosition();
        }
        for(int i=0; i<25; i++){
            game.getPlayerFromList(2).getFaithPath().increaseCrossPosition();
        }
        assertTrue(game.getPlayerFromList(0).getFaithPath().getPapalCardOne());
        assertTrue(game.getPlayerFromList(0).getFaithPath().getPapalCardTwo());
        assertTrue(game.getPlayerFromList(0).getFaithPath().getPapalCardThree());
        assertFalse(game.getPlayerFromList(1).getFaithPath().getPapalCardOne());
        assertFalse(game.getPlayerFromList(1).getFaithPath().getPapalCardTwo());
        assertFalse(game.getPlayerFromList(1).getFaithPath().getPapalCardThree());
        assertFalse(game.getPlayerFromList(2).getFaithPath().getPapalCardOne());
        assertFalse(game.getPlayerFromList(2).getFaithPath().getPapalCardTwo());
        assertFalse(game.getPlayerFromList(2).getFaithPath().getPapalCardThree());
    }

    @Test
    public void updateTest2(){

        game.setNumberOfPlayers(3);
        game.createPlayers();

        for(int i=0; i<7; i++){
            game.getPlayerFromList(0).getFaithPath().increaseCrossPosition();
        }
        for(int i=0; i<8; i++){
            game.getPlayerFromList(1).getFaithPath().increaseCrossPosition();
        }
        assertTrue(game.getPlayerFromList(0).getFaithPath().getPapalCardOne());
        assertTrue(game.getPlayerFromList(1).getFaithPath().getPapalCardOne());

    }

    @Test
    public void updateTest3(){

        game.setNumberOfPlayers(3);
        game.createPlayers();

        for(int i=0; i<9; i++){
            game.getPlayerFromList(0).getFaithPath().increaseCrossPosition();
            game.getPlayerFromList(1).getFaithPath().increaseCrossPosition();
            game.getPlayerFromList(2).getFaithPath().increaseCrossPosition();
        }
        assertTrue(game.getPlayerFromList(0).getFaithPath().getPapalCardOne());
        assertTrue(game.getPlayerFromList(1).getFaithPath().getPapalCardOne());
        assertTrue(game.getPlayerFromList(2).getFaithPath().getPapalCardOne());

    }

    @Test
    public void canBuyDevCardTest(){
        Game game = new Game();
        Map<Resource, Integer> costTest = new HashMap<>();
        game.setNumberOfPlayers(2);
        game.createPlayers();
        game.getPlayerFromList(0).getWarehouse().addResourceToWarehouse(1, Resource.SHIELD);
        costTest.put(Resource.SHIELD, 1);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), true);
        costTest.put(Resource.SHIELD, 2);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), false);
        game.getPlayerFromList(0).getChest().addResource(Resource.SHIELD, 5);
        costTest.put(Resource.SHIELD, 6);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), true);
        costTest.put(Resource.MONEY, 2);
        game.getPlayerFromList(0).getChest().addResource(Resource.MONEY, 1);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), false);
        game.getPlayerFromList(0).getWarehouse().addResourceToWarehouse(2, Resource.MONEY);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), true);
        game.getPlayerFromList(0).getChest().addResource(Resource.SLAVE,20);
        costTest.put(Resource.SLAVE, 7);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), true);
        costTest.put(Resource.SLAVE, 21);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), false);
        game.getPlayerFromList(0).getWarehouse().unlockLeaderLevel(Resource.STONE);
        game.getPlayerFromList(0).getWarehouse().addResourceToSpecialLevel(1,Resource.STONE);
        game.getPlayerFromList(0).getWarehouse().addResourceToSpecialLevel(1,Resource.STONE);
        costTest.put(Resource.SLAVE, 20);
        costTest.put(Resource.STONE, 2);
        assertEquals(game.canBuyDevCard(game.getPlayerFromList(0), costTest), true);
    }

    @Test
    public void chooseDevCardTest(){

    }

    @Test
    public void buyDevCardTest(){

    }

    @Test
    public void canBuyDevCardWithList(){

    }
}
