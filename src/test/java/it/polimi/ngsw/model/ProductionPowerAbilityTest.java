package it.polimi.ngsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ProductionPowerAbilityTest {

    LeaderCardBaseDecorator leaderCard = new ProductionPowerAbility(
            new ConcreteLeaderCard(),
            3,
            new ArrayList<>(),
            Resource.SHIELD
    );

    Player player = new Player();

    @Test
    public void activateAbilityTest(){
        leaderCard.activateAbility(player);
    }
}
