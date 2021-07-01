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
        String path = "/parsingInfo/blue_level_one.json";
        List<DevCard> devDeck = devCardParser.parseDevDeck(path);
        assertEquals(4, devDeck.size());
    }
}

