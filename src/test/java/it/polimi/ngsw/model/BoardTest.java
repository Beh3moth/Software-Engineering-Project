package it.polimi.ngsw.model;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ngsw.model.Chest;
import it.polimi.ngsw.model.Resource;
import org.junit.jupiter.api.Test;

public class BoardTest {

    @Test
    public void checkShuffle (){
        Board board = new Board();
        System.out.println(board.getSingleMarble());
    }

    @Test
    public void BoardConstructorTest(){
        Board board = new Board();
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                for(int k=0; k<4; k++){
                    System.out.print("PV: ");
                    System.out.println(board.getDevCardSpace(i, j).getDevelopDeck().get(k).getPV());
                    System.out.print("Level: ");
                    System.out.println(board.getDevCardSpace(i, j).getDevelopDeck().get(k).getDevLevel());
                    System.out.print("DevColour: ");
                    System.out.println(board.getDevCardSpace(i, j).getDevelopDeck().get(k).getCardColour());
                }
            }
        }

    }

}
