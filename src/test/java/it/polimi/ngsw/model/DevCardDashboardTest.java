package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DevCardDashboardTest {

    @Test
    public void getActiveProductionPowerListTest(){
        Game game = new Game();
        game.setNumberOfPlayers(1);
        Player player = new Player("jhon");
        assertNotNull(player.getDevCardDashboard().getActiveProductionPowerList());
    }

    @Test
    public void putDevCardInTest(){
        Game game = new Game();
        game.setNumberOfPlayers(1);
        Player player = new Player("jhon");

        for(int j=0; j<3; j++){
            for(int i=2; i>=0; i--){
                assertTrue(player.getDevCardDashboard().putDevCardIn(j, game.getBoard().getDevCardSpace(i, 0).getDevelopDeck().get(0)));
            }
        }
    }

    @Test
    public void getActiveDevCardsTest(){
        Game game = new Game();
        game.setNumberOfPlayers(1);
        Player player = new Player("jhon");
        for(int j=0; j<3; j++){
            for(int i=2; i>=0; i--){
                assertTrue(player.getDevCardDashboard().putDevCardIn(j, game.getBoard().getDevCardSpace(i, 0).getDevelopDeck().get(0)));
            }
        }
        for(DevCard devCard : player.getDevCardDashboard().getActiveDevCards()){
            assertEquals(DevCardColour.GREEN, devCard.getCardColour());
            assertEquals(3, devCard.getDevLevel());
        }
    }



    @Test
    public void activateProductionPowerAbilityTest(){

        for(int i=0; i<10; i++){
            DevCardDashboard  devCardDashboard = new DevCardDashboard();
            Game game = new Game();
            game.setNumberOfPlayers(1);
            Player player = new Player("jhon");
            player.receiveLeaderCards(game.removeAndReturnTheLastFourLeaderCards());
            List<LeaderCard> leaderCardList = player.getLeaderCards();
            leaderCardList.removeIf(leaderCard -> !leaderCard.getAbilityName().equals("production power"));
            if(leaderCardList.size() > 0){
                for (LeaderCard leaderCard : leaderCardList) {
                    ProductionPowerAbility prod = new ProductionPowerAbility(leaderCard, 1, DevCardColour.GREEN, Resource.SHIELD, "test");
                    devCardDashboard.activateProductionPowerAbility(prod);
                }
                assertNotEquals(devCardDashboard.getProductionPower(4), null);
            }
            else assertTrue(true);
        }

    }



    @Test
    public void getProductionPowerTestWithoutInitialization(){
        DevCardDashboard devCardDashboard = new DevCardDashboard();
        assertTrue(devCardDashboard.getProductionPower(0).isBaseProductionPower());
        assertNull(devCardDashboard.getProductionPower(2));
        assertNull(devCardDashboard.getProductionPower(3));
        assertNull(devCardDashboard.getProductionPower(4));
        assertNull(devCardDashboard.getProductionPower(5));
    }


    @Test
    public void getProductionPowerTestWithInitialization() throws FileNotFoundException {
        DevCardDashboard devCardDashboard = new DevCardDashboard();
        DevCardParser devCardParser = new DevCardParser();
        List<DevCard> devCardList = devCardParser.parseDevDeck("/parsingInfo/blue_level_one.json");
        devCardDashboard.putDevCardIn(0, devCardList.get(0));
        devCardDashboard.putDevCardIn(1, devCardList.get(1));
        devCardDashboard.putDevCardIn(2, devCardList.get(2));
        assertTrue(devCardDashboard.getProductionPower(0).isBaseProductionPower());
        assertNotNull(devCardDashboard.getProductionPower(2));
        assertEquals(devCardDashboard.getProductionPower(2), devCardList.get(1).getProductionPower());
        assertNotNull(devCardDashboard.getProductionPower(3));
        assertEquals(devCardDashboard.getProductionPower(3), devCardList.get(2).getProductionPower());
        assertNull(devCardDashboard.getProductionPower(4));
        assertNull(devCardDashboard.getProductionPower(5));
    }

    @Test
    public void chooseProductionPowerTest() throws FileNotFoundException {
        DevCardDashboard devCardDashboard = new DevCardDashboard();
        DevCardParser devCardParser = new DevCardParser();

        for(int i=0; i<3;i++){
            devCardDashboard.putDevCardIn(i, devCardParser.parseDevDeck("/parsingInfo/blue_level_one.json").get(i));
        }


    }


    @Test
    public void getPVTest() throws FileNotFoundException {
        DevCardDashboard devCardDashboard = new DevCardDashboard();
        DevCardParser devCardParser = new DevCardParser();
        for(int i=0; i<3;i++){
            devCardDashboard.putDevCardIn(i, devCardParser.parseDevDeck("/parsingInfo/blue_level_one.json").get(i));
        }
        assertEquals(8, devCardDashboard.getPV());
    }
    @Test
    public void getBaseTest(){
        Player p = new Player("x");
        assertEquals(p.getDevCardDashboard().getBaseProductionPower().getResourceToReceive(), null);
    }

    @Test
    public void getDevCardsTest(){
        Player p = new Player("x");
        assertEquals(true, p.getDevCardDashboard().getDevCards().isEmpty());
        assertEquals(true, p.getDevCardDashboard().getActiveDevCardsAsMap().isEmpty());
        assertEquals(null, p.getDevCardDashboard().getActiveProductionPowerList().get(0).getResourceToReceive());
        assertEquals(0, p.getDevCardDashboard().getDevCardNumber());
        assertEquals(0, p.getDevCardDashboard().getLevel(1));
        assertEquals(true, p.getDevCardDashboard().getLeaderProductionPowerList().isEmpty());
    }





}
