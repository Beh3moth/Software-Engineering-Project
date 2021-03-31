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
        List<Marble> marbles = new ArrayList<Marble>();
        marbles.add(new RedMarble());
        marbles.add(new BlueMarble());
        marbles.add(new BlueMarble());
        marbles.add(new YellowMarble());
        marbles.add(new YellowMarble());
        marbles.add(new PurpleMarble());
        marbles.add(new PurpleMarble());
        marbles.add(new GrayMarble());
        marbles.add(new GrayMarble());
        marbles.add(new WhiteMarble());
        marbles.add(new WhiteMarble());
        marbles.add(new WhiteMarble());
        marbles.add(new WhiteMarble());
        Collections.shuffle(marbles);
        Marble[] support = marbles.toArray(new Marble[marbles.size()]);
        int z = 0;
        for(int i = 0; i < 3 ; i++){
            for(int j = 0; i < 4 ; j++){
                marketDashboard[i][j] = support[z];
                z++;
            }
        }
        this.singleMarble = support[12];
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
   // public DevCardSpace getDevCardSpace(int row, int column){  //CAMBIARE NOME SU UML
     //   return devDashboard[row][column];
   // }

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
        for(int j = 0; j < 4; j++ ){
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

    /**
     * Method that remove two cards of a color, it search until it find to remove two cards, or end of the game
     * @param cardColour wich colour
     */
    public void removeDevCard(DevCardColour cardColour){
        int remove = 2;
        if(cardColour == DevCardColour.GREEN){
            if(devDashboard[2][0].getNumberOfCards() >= 2){
                devDashboard[2][0].setNumberOfCards(remove);
                remove = 0;
               return;
            }
            else if(devDashboard[2][0].getNumberOfCards() == 1){
                devDashboard[2][0].setNumberOfCards(1);
                remove = 1;
            }
            if(devDashboard[2][0].getNumberOfCards() == 0){
                if(devDashboard[1][0].getNumberOfCards() >= 2 && remove == 2){
                    devDashboard[1][0].setNumberOfCards(remove);
                    return;
                }
                else if(devDashboard[1][0].getNumberOfCards() >= 1 && remove == 1){
                    devDashboard[1][0].setNumberOfCards(remove);
                    remove = 0;
                    return;

                }
                else if(devDashboard[1][0].getNumberOfCards() == 1 && remove == 2){
                    devDashboard[1][0].setNumberOfCards(1);
                    remove = 1;

                }
                if(devDashboard[1][0].getNumberOfCards() == 0){
                    if(devDashboard[0][0].getNumberOfCards() >=3 && remove == 2){
                        devDashboard[0][0].setNumberOfCards(remove);
                        remove = 0;
                        return;
                    }
                    else if(devDashboard[0][0].getNumberOfCards() == 2 && remove == 2){
                        devDashboard[0][0].setNumberOfCards(remove);
                        remove = 0;
                        //end of the game
                        return;
                    }
                    else if(devDashboard[0][0].getNumberOfCards() >= 1 && remove == 1){
                        devDashboard[0][0].setNumberOfCards(remove);
                        remove = 0;
                        return;
                    }
                    else if(devDashboard[0][0].getNumberOfCards() < 2 && remove == 2){
                        //call end of game
                        return;
                    } else if (devDashboard[0][0].getNumberOfCards() < 1 && remove == 1){
                        //call end of game
                        return;
                    }
                }
            }
        }
        else if(cardColour == DevCardColour.BLUE){
            if(devDashboard[2][1].getNumberOfCards() >= 2){
                devDashboard[2][1].setNumberOfCards(remove);
                remove = 0;
                return;
            }
            else if(devDashboard[2][1].getNumberOfCards() == 1){
                devDashboard[2][1].setNumberOfCards(1);
                remove = 1;
            }
            if(devDashboard[2][1].getNumberOfCards() == 0){
                if(devDashboard[1][1].getNumberOfCards() >= 2 && remove == 2){
                    devDashboard[1][1].setNumberOfCards(remove);
                    return;
                }
                else if(devDashboard[1][1].getNumberOfCards() >= 1 && remove == 1){
                    devDashboard[1][1].setNumberOfCards(remove);
                    remove = 0;
                    return;

                }
                else if(devDashboard[1][1].getNumberOfCards() == 1 && remove == 2){
                    devDashboard[1][1].setNumberOfCards(1);
                    remove = 1;

                }
                if(devDashboard[1][1].getNumberOfCards() == 0){
                    if(devDashboard[0][1].getNumberOfCards() >=3 && remove == 2){
                        devDashboard[0][1].setNumberOfCards(remove);
                        remove = 0;
                        return;
                    }
                    else if(devDashboard[0][1].getNumberOfCards() == 2 && remove == 2){
                        devDashboard[0][1].setNumberOfCards(remove);
                        remove = 0;
                        //end of the game
                        return;
                    }
                    else if(devDashboard[0][1].getNumberOfCards() >= 1 && remove == 1){
                        devDashboard[0][1].setNumberOfCards(remove);
                        remove = 0;
                        return;
                    }
                    else if(devDashboard[0][1].getNumberOfCards() < 2 && remove == 2){
                        //call end of game
                        return;
                    } else if (devDashboard[0][1].getNumberOfCards() < 1 && remove == 1){
                        //call end of game
                        return;
                    }
                }
            }
        }
        else if(cardColour == DevCardColour.YELLOW){
            if(devDashboard[2][2].getNumberOfCards() >= 2){
                devDashboard[2][2].setNumberOfCards(remove);
                remove = 0;
                return;
            }
            else if(devDashboard[2][2].getNumberOfCards() == 1){
                devDashboard[2][2].setNumberOfCards(1);
                remove = 1;
            }
            if(devDashboard[2][2].getNumberOfCards() == 0){
                if(devDashboard[1][2].getNumberOfCards() >= 2 && remove == 2){
                    devDashboard[1][2].setNumberOfCards(remove);
                    return;
                }
                else if(devDashboard[1][2].getNumberOfCards() >= 1 && remove == 1){
                    devDashboard[1][2].setNumberOfCards(remove);
                    remove = 0;
                    return;

                }
                else if(devDashboard[1][2].getNumberOfCards() == 1 && remove == 2){
                    devDashboard[1][2].setNumberOfCards(1);
                    remove = 1;

                }
                if(devDashboard[1][2].getNumberOfCards() == 0){
                    if(devDashboard[0][2].getNumberOfCards() >=3 && remove == 2){
                        devDashboard[0][2].setNumberOfCards(remove);
                        remove = 0;
                        return;
                    }
                    else if(devDashboard[0][2].getNumberOfCards() == 2 && remove == 2){
                        devDashboard[0][2].setNumberOfCards(remove);
                        remove = 0;
                        //end of the game
                        return;
                    }
                    else if(devDashboard[0][2].getNumberOfCards() >= 1 && remove == 1){
                        devDashboard[0][2].setNumberOfCards(remove);
                        remove = 0;
                        return;
                    }
                    else if(devDashboard[0][2].getNumberOfCards() < 2 && remove == 2){
                        //call end of game
                        return;
                    } else if (devDashboard[0][2].getNumberOfCards() < 1 && remove == 1){
                        //call end of game
                        return;
                    }
                }
            }
        }
        else if(cardColour == DevCardColour.PURPLE){
            if(devDashboard[2][3].getNumberOfCards() >= 2){
                devDashboard[2][3].setNumberOfCards(remove);
                remove = 0;
                return;
            }

            else if(devDashboard[2][3].getNumberOfCards() == 1){
                devDashboard[2][3].setNumberOfCards(1);
                remove = 1;
            }
            if(devDashboard[2][3].getNumberOfCards() == 0){
                if(devDashboard[1][3].getNumberOfCards() >= 2 && remove == 2){
                    devDashboard[1][3].setNumberOfCards(remove);
                    return;
                }
                else if(devDashboard[1][3].getNumberOfCards() >= 1 && remove == 1){
                    devDashboard[1][3].setNumberOfCards(remove);
                    remove = 0;
                    return;

                }
                else if(devDashboard[1][3].getNumberOfCards() == 1 && remove == 2){
                    devDashboard[1][3].setNumberOfCards(1);
                    remove = 1;

                }
                if(devDashboard[1][3].getNumberOfCards() == 0){
                    if(devDashboard[0][3].getNumberOfCards() >=3 && remove == 2){
                        devDashboard[0][3].setNumberOfCards(remove);
                        remove = 0;
                        return;
                    }
                    else if(devDashboard[0][3].getNumberOfCards() == 2 && remove == 2){
                        devDashboard[0][3].setNumberOfCards(remove);
                        remove = 0;
                        //end of the game
                        return;
                    }
                    else if(devDashboard[0][3].getNumberOfCards() >= 1 && remove == 1){
                        devDashboard[0][3].setNumberOfCards(remove);
                        remove = 0;
                        return;
                    }
                    else if(devDashboard[0][3].getNumberOfCards() < 2 && remove == 2){
                        //call end of game
                        return;
                    } else if (devDashboard[0][3].getNumberOfCards() < 1 && remove == 1){
                        //call end of game
                        return;
                    }
                }
            }
        }
}}