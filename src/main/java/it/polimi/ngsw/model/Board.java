package it.polimi.ngsw.model;
//import Player library
//import devcardcolour enum

import java.io.IOException; //uhmmmm
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class of the Board with the single marble, the market e the matrix of develop cards, this class has methods for taking column or rows
 * of resource, and than resetting the market
 */
public class Board{
    public static final int MAX_ROWS_MARKET = 3;
    public static final int MAX_COLUMNS_MARKET = 4;
    private Marble[][] marketDashboard;
    private Marble singleMarble;
    private DevCardSpace[][] devDashboard;

    public Board(){
        Marble[] marbles = new Marble[13];
        marbles[0] = new RedMarble();
        marbles[1] = new BlueMarble();
        marbles[2] = new BlueMarble();
        marbles[3] = new YellowMarble();
        marbles[4] = new YellowMarble();
        marbles[5] = new PurpleMarble();
        marbles[6] = new PurpleMarble();
        marbles[7] = new GrayMarble();
        marbles[8] = new GrayMarble();
        marbles[9] = new WhiteMarble();
        marbles[10] = new WhiteMarble();
        marbles[11] = new WhiteMarble();
        marbles[12] = new WhiteMarble();
        List<Marble> marblesList = Arrays.asList(marbles);
        Collections.shuffle(marblesList);
        int z = 0;
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; i < 4 ; j++){
                marketDashboard[i][j] = marblesList.get(z);
                z++;
            }
        }
        this.singleMarble = marblesList.get(12);
        this.devDashboard = new DevCardSpace[MAX_ROWS_MARKET][MAX_COLUMNS_MARKET];
        initDevDashboard();

    }
    /**
     * method that initialize the matrix of the Developing Cards
     */
    public void initDevDashboard(){    //AGGIUNGERE SU UML
        int z = 0;
        DevCardColour spaceColour = DevCardColour.EMPTY;
        for (int i = 0; i < MAX_ROWS_MARKET; i++) {
            for (int j = 0; j < MAX_COLUMNS_MARKET; j++) {
                if(i == 0){z=3;}
                else if(i == 1){z=2;}
                else if(i== 2){z=1;}
                if(j == 0){spaceColour = DevCardColour.GREEN;}
                else if(j == 1){spaceColour = DevCardColour.BLUE;}
                else if(j == 2){spaceColour = DevCardColour.YELLOW;}
                else if(j == 3){spaceColour = DevCardColour.PURPLE;}
                devDashboard[i][j] = new DevCardSpace(z, spaceColour);  //devo inizializzare i vari livelli e colori

            }
        }
    }

    /**
     * Method that return the single Marble
     * @return singleMarble
     */
    public Marble getSingleMarble(){   //probabilmente intutile, non ci accedo mai da esterno
        return singleMarble;
    }


    /**
     * Method that change the single Marble
     * @param newSingleMarble the new single Marble
     */
    public void setSingleMarble(Marble newSingleMarble){  //probabilmente intutile, non ci accedo mai da esterno
        this.singleMarble = newSingleMarble;
    }

    /**
     * Method that permit to take a space of the dashboard
     * @param row wich row
     * @param column wich column
     * @return the space
     */
    public DevCardSpace getDevCardSpace(int row, int column){  //CAMBIARE NOME SU UML
        return devDashboard[row][column];
    }

    /**
     * Method that permit the player to take all resources of a row
     * @param row  the row that player want the resources of
     * @param activePlayer  the player
     */
    public void getMarbleRow(int row, Player activePlayer){
        for(int j = 0; j < 3; j++ ){
            this.marketDashboard[row][j].actionMarble(activePlayer);
        }
        Marble supportMarble = this.singleMarble;
        this.singleMarble = this.marketDashboard[row][0];
        this.marketDashboard[row][0] = this.marketDashboard[row][1];
        this.marketDashboard[row][1] = this.marketDashboard[row][2];
        this.marketDashboard[row][2] = this.marketDashboard[row][3];
        this.marketDashboard[row][3] = supportMarble;
    }

    /**
     * Method that permit the player to take all resources of a column
     * @param column  the column that player want the resources of
     * @param activePlayer  the player
     */
    public void getMarbleColumn(int column, Player activePlayer){
        for(int j = 0; j < 2; j++ ){
            this.marketDashboard[j][column].actionMarble(activePlayer);
        }
        Marble supportMarble = this.singleMarble;
        this.singleMarble = this.marketDashboard[0][column];
        this.marketDashboard[0][column] = this.marketDashboard[1][column];
        this.marketDashboard[1][column] = this.marketDashboard[2][column];
        this.marketDashboard[2][column] = supportMarble;
    }

    /**
     * Method that permit to receive a specific Marble of the market
     * @param row row of the market
     * @param column column of the market
     * @return the Marble
     */
    public Marble getMarble(int row, int column){
        return marketDashboard[row][column];
    }

}