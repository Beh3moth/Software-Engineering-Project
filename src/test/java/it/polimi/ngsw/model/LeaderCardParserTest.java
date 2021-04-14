package it.polimi.ngsw.model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardParserTest {

    LeaderCardParser leaderCardParser = new LeaderCardParser();

    @Test
    public void test1() throws FileNotFoundException {

        leaderCardParser.initSpaceLeaderCards();

        for(LeaderCard leaderCard : leaderCardParser.returnSpaceAbilityList()){
            assertEquals("space", leaderCard.getAbilityName());
        }

    }

    @Test
    public void test2() throws FileNotFoundException {

        leaderCardParser.initDiscountLeaderCards();

        for(LeaderCard leaderCard : leaderCardParser.returnDiscountAbilityList()){
            assertEquals("discount", leaderCard.getAbilityName());
        }

    }

    @Test
    public void test3() throws FileNotFoundException {

        leaderCardParser.initWhiteMarbleLeaderCards();

        for(LeaderCard leaderCard : leaderCardParser.returnWhiteMarbleAbilityList()){
            assertEquals("white marble", leaderCard.getAbilityName());
        }

    }

    @Test
    public void test4() throws FileNotFoundException {

        leaderCardParser.initProductionPowerLeaderCards();

        for(LeaderCard leaderCard : leaderCardParser.returnProductionPowerList()){
            assertEquals("production power", leaderCard.getAbilityName());
        }

    }

}
