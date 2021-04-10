package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DevCardParserTest {

    DevCardParser devCardParser = new DevCardParser();

    @Test
    public void parseDevDeckTest() throws FileNotFoundException {
        String path = "src/main/java/it/polimi/resources/blue_level_one.json";
        List<DevCard> devDeck = devCardParser.parseDevDeck(path);
        for(DevCard devCard : devDeck){
            System.out.println(devCard.getDevLevel());
            System.out.println(devCard.getCardColour());
            System.out.println(devCard.getPV());
            System.out.println(devCard.getProductionPower().getResourceToPay());
        }
    }
}

