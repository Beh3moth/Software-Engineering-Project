package it.polimi.ngsw.model;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ngsw.model.Chest;
import it.polimi.ngsw.model.Resource;
import org.junit.jupiter.api.Test;

public class BoardTest {
    @Test
    public void checkShuffle (){
        Board board = new Board();
        Player player = new Player("dbhvjs");
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource());
            }
        }
    }

    @Test
    public void checkFirstRowRefactor (){
        Board board = new Board();
        Player player = new Player("dbhvjs");
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
        board.getMarbleRow(1, player);
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
    }

    @Test
    public void checkSecondRowRefactor (){
        Board board = new Board();
        Player player = new Player("dbhvjs");
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
        board.getMarbleRow(2, player);
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
    }

    @Test
    public void checkThirdRowRefactor (){
        Board board = new Board();
        Player player = new Player("dbhvjs");
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
        board.getMarbleRow(3, player);
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
    }

    @Test
    public void checkFirstColumnRefactor (){
        Board board = new Board();
        Player player = new Player("dbhvjs");
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
        board.getMarbleColumn(1, player);
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
    }

    @Test
    public void checkSecondColumnRefactor (){
        Board board = new Board();
        Player player = new Player("dbhvjs");
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
        board.getMarbleColumn(2, player);
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
    }

    @Test
    public void checkThirdColumnRefactor (){
        Board board = new Board();
        Player player = new Player("dbhvjs");
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
        board.getMarbleColumn(3, player);
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
    }

    @Test
    public void checkForthColumnRefactor (){
        Board board = new Board();
        Player player = new Player("dbhvjs");
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
        board.getMarbleColumn(4, player);
        System.out.println(board.getSingleMarble().getResource());
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; j < 4 ; j++){
                System.out.println(board.getMarble(i,j).getResource() + " "+ i + " "+ j);
            }
        }
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
