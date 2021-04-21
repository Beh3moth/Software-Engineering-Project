package it.polimi.ngsw.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class of the Board with the single marble, the market e the matrix of develop cards, this class has methods for taking column or rows
 * of resource, and than resetting the market
 */
public class Board {
    public static final int MAX_ROWS_MARKET = 3;
    public static final int MAX_COLUMNS_MARKET = 4;
    private Marble[][] marketDashboard;
    private Marble singleMarble;
    private DevCardSpace[][] devDashboard;
    private List<String> pathList = new ArrayList<>();
    DevCardParser devCardParser = new DevCardParser();

    /**
     * Constructor method, it creates the market with random marble inside, also the single Marble.
     * It also creates the devCard Space
     */
    public Board(){
        marketDashboard = new Marble[3][4];
        List<Marble> marbles = new ArrayList<>();
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
            for(int j = 0; j < 4 ; j++){
                marketDashboard[i][j] = support[z];
                z++;
            }
        }
        this.singleMarble = support[12];
         //testare se funziona questo codice sotto
        this.devDashboard = new DevCardSpace[MAX_ROWS_MARKET][MAX_COLUMNS_MARKET];
        try {
            initDevCardSpace();
        }
        catch (FileNotFoundException e) {
            System.err.println("File not found");
        }

    }

    /**
     * method that initialize the matrix of the Developing Cards
     */
    private void initDevCardSpace() throws FileNotFoundException {

        pathList.add("src/main/java/it/polimi/ngsw/resources/green_level_three.json");
        pathList.add("src/main/java/it/polimi/ngsw/resources/blue_level_three.json");
        pathList.add("src/main/java/it/polimi/ngsw/resources/yellow_level_three.json");
        pathList.add("src/main/java/it/polimi/ngsw/resources/purple_level_three.json");
        pathList.add("src/main/java/it/polimi/ngsw/resources/green_level_two.json");
        pathList.add("src/main/java/it/polimi/ngsw/resources/blue_level_two.json");
        pathList.add("src/main/java/it/polimi/ngsw/resources/yellow_level_two.json");
        pathList.add("src/main/java/it/polimi/ngsw/resources/purple_level_two.json");
        pathList.add("src/main/java/it/polimi/ngsw/resources/green_level_one.json");
        pathList.add("src/main/java/it/polimi/ngsw/resources/blue_level_one.json");
        pathList.add("src/main/java/it/polimi/ngsw/resources/yellow_level_one.json");
        pathList.add("src/main/java/it/polimi/ngsw/resources/purple_level_one.json");

        int z = 0;
        int k = 0;
        DevCardColour spaceColour = DevCardColour.EMPTY;
        for (int i = 0; i < MAX_ROWS_MARKET; i++) {
            for (int j = 0; j < MAX_COLUMNS_MARKET; j++) {
                if(i == 0){z=3;}
                else if(i == 1){z=2;}
                else if(i == 2){z=1;}
                if(j == 0){spaceColour = DevCardColour.GREEN;}
                else if(j == 1){spaceColour = DevCardColour.BLUE;}
                else if(j == 2){spaceColour = DevCardColour.YELLOW;}
                else if(j == 3){spaceColour = DevCardColour.PURPLE;}
                devDashboard[i][j] = new DevCardSpace(devCardParser.parseDevDeck(pathList.get(k)));
                k++;
            }
        }

    }

    /**
     * Method that return the single Marble
     * @return singleMarble
     */
    public Marble getSingleMarble(){
        return singleMarble;
    }


    /**
     * Method that change the single Marble
     * @param newSingleMarble the new single Marble
     */
    public void setSingleMarble(Marble newSingleMarble){
        this.singleMarble = newSingleMarble;
    }

    /**
     * Method that permit to take a space of the dashboard
     * @param row which row
     * @param column which column
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
        for(int j = 0; j < 4; j++ ){
            this.marketDashboard[row-1][j].actionMarble(activePlayer);
        }
        Marble supportMarble = this.singleMarble;
        this.singleMarble = this.marketDashboard[row-1][0];
        this.marketDashboard[row-1][0] = this.marketDashboard[row-1][1];
        this.marketDashboard[row-1][1] = this.marketDashboard[row-1][2];
        this.marketDashboard[row-1][2] = this.marketDashboard[row-1][3];
        this.marketDashboard[row-1][3] = supportMarble;

    }

    /**
     * Method that permit the player to take all resources of a column
     * @param column  the column that player want the resources of
     * @param activePlayer  the player
     */
    public void getMarbleColumn(int column, Player activePlayer){
        for(int j = 0; j < 3; j++ ){
            this.marketDashboard[j][column-1].actionMarble(activePlayer);
        }
        Marble supportMarble = this.singleMarble;
        this.singleMarble = this.marketDashboard[0][column-1];
        this.marketDashboard[0][column-1] = this.marketDashboard[1][column-1];
        this.marketDashboard[1][column-1] = this.marketDashboard[2][column-1];
        this.marketDashboard[2][column-1] = supportMarble;
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

    public int getDevCardColumn(DevCardColour colour){
        switch(colour){
            case GREEN: return 0;
            case BLUE: return 1;
            case YELLOW: return 2;
            case PURPLE: return 3;
            default: return  -1;
        }
    }

    /**
     * The method receives a Development Card Colour to delete and try to delete two Development Cards of that colour starting from the cards of a lower level.
     * @param cardColour is the colour to delete.
     * @return the number of cards deleted.
     */
    public boolean removeTwoDevCard(DevCardColour cardColour){

        int col = getDevCardColumn(cardColour);

        if(col==-1){
            return false;
        }

        int deletedDevCards = 0;

        for(int i=2; (i>=0 && deletedDevCards<2); i--){

            deletedDevCards += devDashboard[i][col].removeDevCardFromDevelopDeck();
            if(i==0 && deletedDevCards<2){
                return true;
            }

        }

        return true;

    }

}
