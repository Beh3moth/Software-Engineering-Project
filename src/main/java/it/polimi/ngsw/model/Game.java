package it.polimi.ngsw.model;
//import player

import java.io.IOException; //uhmmmm
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private Board board;

    public Game(){
        this.board = new Board();
    }


    /**
     * Method that permit to take resources from the market, it asks the user if column or row, and wich one
     * @param activePlayer the player that do the action
     */
    public void getBoardResources(Player activePlayer){
        //chiedo al client se riga o colonna
        boolean allRight = false;
        do{

            System.out.println("Choose between column and row: 1 = column  2 = row"); //ovviamente qusti metodi lli buttero nel CLI
            Scanner input = new Scanner(System.in);
            int sector = input.nextInt();
            if(sector == 1){
                allRight = true;
                int column;
                boolean allRightTwo = false;
                do{
                System.out.println("Choose between 1 and 4");
                column= input.nextInt();
                if(column > 0 && column < 5){
                    allRightTwo = true;
                }
                }while(allRightTwo);
                this.board.getMarbleColumn(column, activePlayer);
            }
            else if(sector == 2){
                allRight = true;
                int row;
                boolean allRightTwo = false;
                do{
                    System.out.println("Choose between 1 and 3");
                    row= input.nextInt();
                    if(row > 0 && row < 4){
                        allRightTwo = true;
                    }
                }while(allRightTwo);
                this.board.getMarbleRow(row, activePlayer);
            }
        }while(allRight);
    }
}
