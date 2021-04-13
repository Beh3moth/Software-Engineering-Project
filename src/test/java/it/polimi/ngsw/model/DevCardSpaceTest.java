package it.polimi.ngsw.model;

import org.junit.jupiter.api.Test;

public class DevCardSpaceTest {
    Board board = new Board();
     @Test
    public void removeCardTest(){
         for(int i=0; i <4 ; i++){
             System.out.println(this.board.getDevCardSpace(0,0).getDevCard(i).getPV());
         }
         System.out.println(this.board.getDevCardSpace(0,0).getNumberOfCards());
         this.board.getDevCardSpace(0,0).removeFirstCard();
         for(int i=0; i < 3 ; i++){
             System.out.println(this.board.getDevCardSpace(0,0).getDevCard(i).getPV());
         }
     }

}
