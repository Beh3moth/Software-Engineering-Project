package it.polimi.ngsw.model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardParserTest {

    LeaderCardParser leaderCardParser = new LeaderCardParser();

    @Test
    public void SpaceAbilityParsingTest() throws FileNotFoundException {
        leaderCardParser.initSpaceLeaderCards();

        for(LeaderCard leaderCard : leaderCardParser.returnSpaceAbilityList()){
            System.out.println(leaderCard.getAbilityName());
            System.out.println(leaderCard.getPV());
        }

    }

    @Test
    public void DiscountAbilityParsingTest() throws FileNotFoundException {
        leaderCardParser.initDiscountLeaderCards();

        for(LeaderCard leaderCard : leaderCardParser.returnDiscountAbilityList()){
            System.out.println(leaderCard.getAbilityName());
            System.out.println(leaderCard.getPV());
        }

    }

    @Test
    public void WhiteMarbleAbilityParsingTest() throws FileNotFoundException {
        leaderCardParser.initWhiteMarbleLeaderCards();

        for(LeaderCard leaderCard : leaderCardParser.returnWhiteMarbleAbilityList()){
            System.out.println(leaderCard.getAbilityName());
            System.out.println(leaderCard.getPV());
        }

    }

    @Test
    public void ProductionPowerAbilityParsingTest() throws FileNotFoundException {
        leaderCardParser.initProductionPowerLeaderCards();

        for(LeaderCard leaderCard : leaderCardParser.returnProductionPowerList()){
            System.out.println(leaderCard.getAbilityName());
            System.out.println(leaderCard.getPV());
        }

    }

}
