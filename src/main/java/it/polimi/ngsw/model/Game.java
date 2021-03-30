package it.polimi.ngsw.model;
//import player

import java.io.IOException; //uhmmmm
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private Board board;
    private Player activePlayer;
    private List<Player> players;

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
                }while(!allRightTwo);
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
                }while(!allRightTwo);
                this.board.getMarbleRow(row, activePlayer);
            }
        }while(!allRight);

        Scanner input = new Scanner(System.in);
        int up = activePlayer.getWarehouse().getStockResourceNumber();
        for(int j = 0; j < up;j++){  //ragiona sulla j e up, quando tolgo elementi, lo stock si riduce, quindi viene influenzato
            System.out.println("Choose what to do with this resource " + activePlayer.getWarehouse().getStockResource(j));
            boolean validator = false;
            int choosen;
            do{
                System.out.println("1) Discard 2)Add Warehouse 3)Add special warehouse") ;
                choosen = input.nextInt();
                if(0 < choosen && choosen < 4){
                    validator = true;
                if(choosen == 3 && (activePlayer.getWarehouse().getLeaderLevelType(1) == Resource.EMPTY)) {
                    validator = false;
                    System.out.println("You don't have leader card shelf");
                }
                }}
            while(!validator);
            int level;
            if(choosen == 1){
                activePlayer.getWarehouse().removeFirstResourceFromStock();
                //add faith point a tutti gli altri giocatori!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                j--;
                up--; //guardare se posso farloooooo!!!!!!!!!!!!!!!!!!!!1
            }
            else if(choosen == 2){
                System.out.println("Choose wich shelf of the warehouse");
                boolean goneRight = false;
                do{
                    level = input.nextInt();
                    if (0 < level && level < 4){
                        goneRight = true;
                    }
                }while(!goneRight);
                activePlayer.getWarehouse().addResourceToWarehouse(level, activePlayer.getWarehouse().getStockResource(0));
                activePlayer.getWarehouse().removeFirstResourceFromStock();
            }
            else if(choosen == 3){
                int levelUp = 1;
                if(activePlayer.getWarehouse().getLeaderLevelType(2) != Resource.EMPTY){
                    levelUp = 2;
                }
                System.out.println("Choose wich specialshelf of the warehouse");
                boolean goneRight = false;
                do{
                    level = input.nextInt();
                    if (0 < level && level <= levelUp){
                        goneRight = true;
                    }
                }while(!goneRight);
                activePlayer.getWarehouse().addResourceToSpecialLevel(level, activePlayer.getWarehouse().getStockResource(0));
                activePlayer.getWarehouse().removeFirstResourceFromStock();

            }

        }
    }
}
