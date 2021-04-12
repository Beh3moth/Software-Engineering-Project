package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameTest {

    Game game = new Game();

    public void randomChest(Chest chest){
        chest.addResourceToChest(Resource.SHIELD, new Random().nextInt(100));
        chest.addResourceToChest(Resource.STONE, new Random().nextInt(100));
        chest.addResourceToChest(Resource.SLAVE, new Random().nextInt(100));
        chest.addResourceToChest(Resource.MONEY, new Random().nextInt(100));
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
            for(int j=0; j<4; j++){
                System.out.println(list.get(j).getAbilityName());
            }
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

}
