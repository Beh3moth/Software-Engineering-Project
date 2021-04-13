package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

public class DevCardDashboardTest {

    @Test
    public void getActiveDevCardsTest() throws FileNotFoundException {
        DevCardDashboard devCardDashboard = new DevCardDashboard();
        DevCardParser devCardParser = new DevCardParser();
        List<DevCard> devCardList = devCardParser.parseDevDeck("src/main/java/it/polimi/resources/blue_level_one.json");
        devCardDashboard.putDevCardIn(0, devCardList.get(0));
        devCardDashboard.putDevCardIn(1, devCardList.get(1));
        devCardDashboard.putDevCardIn(2, devCardList.get(2));
        List<DevCard> activeDevCardList = devCardDashboard.getActiveDevCards();
        for(int i=0; i<3; i++){
            assertTrue(activeDevCardList.contains(devCardList.get(i)));
        }
    }

    @Test
    public void activateProductionPowerAbilityTest(){

        for(int i=0; i<10; i++){
            DevCardDashboard  devCardDashboard = new DevCardDashboard();
            Game game = new Game();
            /*game.setNumberOfPlayers(1);
            game.createPlayers();*/
            game.getPlayerFromList(0).receiveLeaderCards(game.removeAndReturnTheLastFourLeaderCards());
            List<LeaderCard> leaderCardList = game.getPlayerFromList(0).getLeaderCards();
            leaderCardList.removeIf(leaderCard -> !leaderCard.getAbilityName().equals("production power"));
            if(leaderCardList.size() > 0){
                for (LeaderCard leaderCard : leaderCardList) {
                    devCardDashboard.activateProductionPowerAbility(leaderCard);
                }
                System.out.println(devCardDashboard.getProductionPower(4).getResourceToPay());
                assertNotEquals(devCardDashboard.getProductionPower(4), null);
            }
            else assertTrue(true);
        }

    }

    @Test
    public void getProductionPowerTestWithoutInitialization(){
        DevCardDashboard devCardDashboard = new DevCardDashboard();
        assertTrue(devCardDashboard.getProductionPower(0).isBaseProductionPower());
        assertNull(devCardDashboard.getProductionPower(1));
        assertNull(devCardDashboard.getProductionPower(2));
        assertNull(devCardDashboard.getProductionPower(3));
        assertNull(devCardDashboard.getProductionPower(4));
        assertNull(devCardDashboard.getProductionPower(5));
    }

    @Test
    public void getProductionPowerTestWithInitialization() throws FileNotFoundException {
        DevCardDashboard devCardDashboard = new DevCardDashboard();
        DevCardParser devCardParser = new DevCardParser();
        List<DevCard> devCardList = devCardParser.parseDevDeck("src/main/java/it/polimi/resources/blue_level_one.json");
        devCardDashboard.putDevCardIn(0, devCardList.get(0));
        devCardDashboard.putDevCardIn(1, devCardList.get(1));
        devCardDashboard.putDevCardIn(2, devCardList.get(2));
        assertTrue(devCardDashboard.getProductionPower(0).isBaseProductionPower());
        assertNotNull(devCardDashboard.getProductionPower(1));
        assertEquals(devCardDashboard.getProductionPower(1), devCardList.get(0).getProductionPower());
        assertNotNull(devCardDashboard.getProductionPower(2));
        assertEquals(devCardDashboard.getProductionPower(2), devCardList.get(1).getProductionPower());
        assertNotNull(devCardDashboard.getProductionPower(3));
        assertEquals(devCardDashboard.getProductionPower(3), devCardList.get(2).getProductionPower());
        assertNull(devCardDashboard.getProductionPower(4));
        assertNull(devCardDashboard.getProductionPower(5));
    }

}
