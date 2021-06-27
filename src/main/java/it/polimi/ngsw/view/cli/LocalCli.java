package it.polimi.ngsw.view.cli;

import it.polimi.ngsw.model.*;
import it.polimi.ngsw.network.server.LocalServer;
import it.polimi.ngsw.view.LightModel;
import it.polimi.ngsw.view.cli.AsciiArt.Color;
import it.polimi.ngsw.view.cli.AsciiArt.RectangleArt;
import it.polimi.ngsw.view.cli.AsciiArt.ResourcesArt;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * This class is used if the player wants to play in local.
 * It represents the CLI.
 */
public class LocalCli {

    ResourcesArt resourcesArt = new ResourcesArt();
    RectangleArt rectangleArt = new RectangleArt();
    private static final int MAX_VERT_TILES = 9; //rows.
    private static final int MAX_HORIZON_TILES = 18; //cols.
    private String[][] tiles = new String[MAX_VERT_TILES][MAX_HORIZON_TILES];

    private final PrintStream out;
    private Thread inputThread;
    LightModel lightModel = new LightModel();


    private int[] leaderCardStatus; //1 means not activated but usable, 0 means discarded, 2 means activated
    private List<Resource> newResources;
    private Resource newFirstShelf;
    private List<Resource> newSecondShelf;
    private List<Resource> newThirdShelf;
    private List<Resource> newFirstSpecialShelf;
    private List<Resource> newSecondSpecialShelf;
    private List<Resource> discardList;

    private LocalServer server;

    public LocalCli() {
        out = System.out;
        this.newResources = new ArrayList<>();
        this.newSecondShelf = new ArrayList<>();
        this.newThirdShelf = new ArrayList<>();
        this.newFirstSpecialShelf = new ArrayList<>();
        this.newSecondSpecialShelf = new ArrayList<>();
        this.discardList = new ArrayList<>();
        this.server = new LocalServer(this);
    }

    /**
     * The start Game Method
     */
    public void start() {
        out.println("Welcome to Maestri del rinascimento");
        out.print("Enter your nickname: ");
        try {
            String nickname = readLine();
            out.print("Welcome " + nickname + ", you are playing against Lorenzo Il Magnifico");

            server.loginHandler(nickname);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    /**
     * Asks the player the leadercards
     * @param LeaderCards the leadercards
     */
    public void askLeaderCard(List<LeaderCard> LeaderCards) {
        int IdChoosen;
        int IdChoosenTwo;
        if (LeaderCards.size() == 4) {
            List<LeaderCard> chosenCard = new ArrayList<>();
            out.println("\n\nSelect two Leader Cards from the list.\n");
            printLeaderCard(LeaderCards);
            try {
                out.println("\n\nPlease, enter one ID confirm with ENTER.");
                IdChoosen = numberInput(1, LeaderCards.size(), (1) + "° LeaderCard ID: ") - 1;
                chosenCard.add(LeaderCards.get(IdChoosen));
                out.println("Please, enter one ID confirm with ENTER.");
                boolean goneRight = false;
                do {
                    IdChoosenTwo = numberInput(1, LeaderCards.size(), (2) + "° LeaderCard ID: ") - 1;
                    if (IdChoosen != IdChoosenTwo) {
                        goneRight = true;
                    }
                }
                while (!goneRight);
                chosenCard.add(LeaderCards.get(IdChoosenTwo));
                this.leaderCardStatus = new int[]{1, 1};
                server.leaderCardHandler(chosenCard);
            } catch (ExecutionException e) {
                out.println("Input canceled");
            }

        } else {
            out.println("no leadersCards found in the request. End of game.");
            return;
        }
    }

    /**
     * Print the leadercard
     * @param leaderCards the leadercard
     */
    public void printLeaderCard(List<LeaderCard> leaderCards) {

        int col = 10;
        int rig = 80;
        int jIterator = 0;

        String leaderCardsTile[][] = new String[col][rig];

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < rig; j++) {
                leaderCardsTile[i][j] = " ";
            }
        }

        int counter = 1;
        for (LeaderCard leaderCard : leaderCards) {
            leaderCardsTile[0][8 + (jIterator * 19)] = String.valueOf(counter);
            String[][] leaderCardTile = getPrintableLeaderCard(leaderCard);
            for (int i = 0; i < MAX_VERT_TILES; i++) {
                for (int j = 0; j < MAX_HORIZON_TILES; j++) {
                    leaderCardsTile[i + 1][j + (jIterator * 19)] = leaderCardTile[i][j];
                }
            }
            jIterator++;
            counter++;
        }

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < rig; j++) {
                out.print(leaderCardsTile[i][j]);
            }
            out.println();
        }

    }

    String[][] leaderCardTiles = new String[MAX_VERT_TILES][MAX_HORIZON_TILES];

    /**
     * Get the leadercard that are printable
     * @param leaderCard the leadercard
     * @return a string
     */
    public String[][] getPrintableLeaderCard(LeaderCard leaderCard) {
        fillEmptyLeaderCard();
        loadLeaderCardCost(leaderCard);
        loadLeaderPV(leaderCard);
        loadLeaderCardPower(leaderCard);
        return leaderCardTiles;
    }

    /**
     * This method load the leadercard production power
     * @param leaderCard the leadercard
     */
    private void loadLeaderCardPower(LeaderCard leaderCard) {
        for (int i = 0; i < leaderCard.getLeaderCardAbilityAsString().length; i++) {
            leaderCardTiles[6][i + 2] = leaderCard.getLeaderCardAbilityAsString()[i];
        }
    }

    /**
     * This method load the leadercard PV
     * @param leaderCard the leadercard
     */
    private void loadLeaderPV(LeaderCard leaderCard) {
        leaderCardTiles[4][8] = String.valueOf(leaderCard.getPV());
    }

    /**
     * This method load the leader card Cost
     * @param leaderCard the leadercard
     */
    private void loadLeaderCardCost(LeaderCard leaderCard) {
        for (int i = 0; i < leaderCard.getLeaderCardCostAsString().length; i++) {
            leaderCardTiles[1][i + 2] = leaderCard.getLeaderCardCostAsString()[i];
        }
    }

    /**
     * This method fill the empty part of the leadercard
     */
    private void fillEmptyLeaderCard() {

        leaderCardTiles[0][0] = rectangleArt.getLeftTopAngle(Color.ANSI_RED);
        for (int c = 1; c < MAX_HORIZON_TILES - 1; c++) {
            leaderCardTiles[0][c] = rectangleArt.getTopDownBorder(Color.ANSI_RED);
        }

        leaderCardTiles[0][MAX_HORIZON_TILES - 1] = rectangleArt.getRightTopAngle(Color.ANSI_RED);

        for (int r = 1; r < MAX_VERT_TILES - 1; r++) {
            leaderCardTiles[r][0] = rectangleArt.getLeftRightBorder(Color.ANSI_RED);
            for (int c = 1; c < MAX_HORIZON_TILES - 1; c++) {
                leaderCardTiles[r][c] = " ";
            }
            leaderCardTiles[r][MAX_HORIZON_TILES - 1] = rectangleArt.getLeftRightBorder(Color.ANSI_RED);
        }

        leaderCardTiles[MAX_VERT_TILES - 1][0] = rectangleArt.getLeftDownAngle(Color.ANSI_RED);
        for (int c = 1; c < MAX_HORIZON_TILES - 1; c++) {
            leaderCardTiles[MAX_VERT_TILES - 1][c] = rectangleArt.getTopDownBorder(Color.ANSI_RED);
        }

        leaderCardTiles[MAX_VERT_TILES - 1][MAX_HORIZON_TILES - 1] = rectangleArt.getRightDownAngle(Color.ANSI_RED);

    }


    /**
     * Reads a line from the standard input.
     *
     * @return the string read from the input.
     * @throws ExecutionException if the input stream thread is interrupted.
     */
    public String readLine() throws ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(new InputReadTask());
        inputThread = new Thread(futureTask);
        inputThread.start();

        String input = null;

        try {
            input = futureTask.get();
        } catch (InterruptedException e) {
            futureTask.cancel(true);
            Thread.currentThread().interrupt();
        }
        return input;
    }

    /**
     * Asks the user for a input number. The number must be between minValue and maxValue.
     * A wrong number (outside the range) or a non-number will result in a new request of the input.
     * A forbidden list of numbers inside the range can be set through jumpList parameter.
     * An output question can be set via question parameter.
     *
     * @param minValue the minimum value which can be inserted (included).
     * @param maxValue the maximum value which can be inserted (included).
     * @param question a question which will be shown to the user.
     * @return the number inserted by the user.
     * @throws ExecutionException if the input stream thread is interrupted.
     */
    private int numberInput(int minValue, int maxValue, String question) throws ExecutionException {
        int number = minValue - 1;

        do {
            try {
                out.print(question);
                number = Integer.parseInt(readLine());

                if (number < minValue || number > maxValue) {
                    out.println("Invalid number! Please try again.\n");
                }
            } catch (NumberFormatException e) {
                out.println("Invalid input! Please try again.\n");
            }
        } while (number < minValue || number > maxValue);

        return number;
    }

    /**
     * This method starts the turn, it set the model of the client
     */
    public void startTurnMessage(List<LeaderCard> Leaders, Marble singleMarble, Marble[] firstRow, Marble[] secondRow, Marble[] thirdRow, List<ProductionPower> leaderProductionPowerList, Map<Integer, DevCard> activeDevCardMap, ProductionPower baseProductionPower, DevCard[][] devCardMarket, Resource firstShelf, Resource secondShelf, int secondShelfNumber, Resource thirdShelf, int thirdShelfNumber, Map<Resource, Integer> chest, int crossPosition, int victoryPoints, boolean papalCardOne, boolean papalCardTwo, boolean papalCardThree) {
        out.println("\n\n It's your turn! \n\n");
        lightModel.setSingleMarble(singleMarble);
        lightModel.setFirstRow(firstRow);
        lightModel.setSecondRow(secondRow);
        lightModel.setThirdRow(thirdRow);
        lightModel.setDevCardMarket(devCardMarket);
        lightModel.setLeaderProductionPowerList(leaderProductionPowerList);
        lightModel.setActiveDevCardMap(activeDevCardMap);
        this.lightModel.setFirstShelf(firstShelf);
        this.lightModel.setSecondShelf(secondShelf);
        this.lightModel.setThirdShelf(thirdShelf);
        this.lightModel.setSecondShelfNumber(secondShelfNumber);
        this.lightModel.setThirdShelfNumber(thirdShelfNumber);
        lightModel.setBaseProductionPower(baseProductionPower);
        lightModel.setChest(chest);
        lightModel.setCrossPosition(crossPosition);
        lightModel.setVictoryPoints(victoryPoints);
        lightModel.setPapalCardOne(papalCardOne);
        lightModel.setPapalCardTwo(papalCardTwo);
        lightModel.setPapalCardThree(papalCardThree);
        if (this.leaderCardStatus[0] == 1 || this.leaderCardStatus[1] == 1) {
            askToManageLeaderCards(Leaders, 1);
        } else {
            out.println("You don't have usable leader cards");
            mainMove();
        }
    }

    /**
     * Ask the player to manage his leader card
     * @param Leaders the leader cards
     * @param turnZone wich turn zone
     */
    public void askToManageLeaderCards(List<LeaderCard> Leaders, int turnZone) {
        try {
            List<LeaderCard> leaderCardList = new ArrayList<>();
            if (this.leaderCardStatus[0] == 1) leaderCardList.add(Leaders.get(0));
            if (this.leaderCardStatus[1] == 1) leaderCardList.add(Leaders.get(1));
            printLeaderCard(leaderCardList);
            out.println("\nDo you want to activate one of these leaderCard? 1) YES 0) NO ");
            int chose = numberInput(0, 1, "What? ");
            if (chose == 1) activateLeaderCard(Leaders, turnZone);
            else if (chose == 0) askToDiscardLeaderCard(turnZone);
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }
    /**
     * This method is for the action "Activate leadercard"
     * @param Leaders the leader cards
     * @param turnZone wich turn zone
     */
    public void activateLeaderCard(List<LeaderCard> Leaders, int turnZone) {
        try {
            if (this.leaderCardStatus[0] == 1 && this.leaderCardStatus[1] == 1) {
                out.println("Wich one of the above cards? Type 1 to pick the first one, 2 to pick the second one");
                int chose = numberInput(1, 2, "Which? ");
                server.activateLeaderCard(chose - 1, turnZone);
            } else if (this.leaderCardStatus[1] == 1 && (this.leaderCardStatus[0] == 2 || this.leaderCardStatus[0] == 0))
                server.activateLeaderCard(1, turnZone);
            else if (this.leaderCardStatus[0] == 1 && (this.leaderCardStatus[1] == 2 || this.leaderCardStatus[1] == 0))
                server.activateLeaderCard(0, turnZone);
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }

    /**
     * This method permit the player to discard a leadercard
     * @param turnZone wich turn zone
     */
    public void askToDiscardLeaderCard(int turnZone) {
        try {
            out.println("\nDo you want to discard a leadercard? 1) YES 0) NO ");
            int chose = numberInput(0, 1, "What? ");
            if (chose == 1) discardCard(turnZone);
            else if (chose == 0) {
                if (turnZone == 1) mainMove();
                else if (lightModel.isGameFinished() == false && turnZone == 2) endTurn();
                else {
                    endGame();
                }
            }
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }

    }

    /**
     * This method permit to continue the turn after a leader card action
     * @param turnZone wich turn zone
     * @param actionTypology the typology of the action
     * @param goneRight if went well
     * @param wichCard wich card was managed
     * @param Leaders the leader cards
     */
    public void continueTurn(int turnZone, int actionTypology, int goneRight, int wichCard, List<LeaderCard> Leaders) {
        if (turnZone == 1) { //inizio turno
            if (actionTypology == 1) { //1 vuol dire che era stata chiamata una leadercard request, 2 una discard card
                if (goneRight == 0) {  //0 vuol dire non attivata, quindi richiedi, 1 attivata
                    askToManageLeaderCards(Leaders, turnZone);
                } else if (goneRight == 1) {
                    this.leaderCardStatus[wichCard] = 2;
                    mainMove();
                }
            } else if (actionTypology == 2) {
                this.leaderCardStatus[wichCard] = 0;
                mainMove();
            }
        } else if (turnZone == 2) {
            if (actionTypology == 1) {
                if (goneRight == 1) {
                    this.leaderCardStatus[wichCard] = 2;
                    if (lightModel.isGameFinished() == true) endGame();
                    else {
                        endTurn();
                    }
                } else if (goneRight == 0) {

                        askToManageLeaderCards(Leaders, turnZone);

                }//leadercard choice. middle turn
            } else if (actionTypology == 2) {
                this.leaderCardStatus[wichCard] = 0;
                if (lightModel.isGameFinished() == true) endGame();
                else {
                    endTurn();
                }
            }
        }
    }

    /**
     * This is the main move, the player chose wich action to do during his turn
     */
    public void mainMove() {
        try {
            printDevCardMarket();
            printMarket();
            printPlayerDashBoard();
            out.println("\nChose what you want to do: 1) Reorder warehouse 2) Take resources from the market 3) Buy a Development Card 4) Activate Production Powers");
            int chose = numberInput(1, 4, "Which move? ");
            if (chose == 1) {
                server.ReorderWarehouse(true);
            } else if (chose == 2) takeResourcesFromMarket();
            else if (chose == 3) chooseDevCard();
            else if (chose == 4) productionPowerMove();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }

    /**
     * This method permit to chose a devcard to play
     */
    public void chooseDevCard() {
        int level = 0;
        int column = 0;
        int slotToPut = 0;
        boolean goneRight = false;

        do {
            try {
                level = numberInput(1, 3, "Which level? ");
            } catch (ExecutionException e) {
                out.println("Wrong input");
            }

            try {
                column = numberInput(1, 4, "Which Column? (1: green, 2: blue, 3: yellow, 4: purple): ");
            } catch (ExecutionException e) {
                out.println("Wrong input");
            }
            try {
                slotToPut = numberInput(1, 3, "which slot to put? ");
            } catch (ExecutionException e) {
                out.println("Wrong Input");
            }
            if (lightModel.getDevCardMarket()[3 - level][column - 1].getPV() != 0) {
                goneRight = true;
            } else {
                out.println("The slot is empty.");
            }
        } while (!goneRight);


        int finalSlotToPut = slotToPut;
        int finalLevel = level;
        int finalColumn = column;

        server.ChooseDevCard(finalLevel, finalColumn, finalSlotToPut);
    }

    /**
     * This method permit the player to take resources from the market
     */
    public void takeResourcesFromMarket() {
        try {
            out.println("Do you want to take a column or a row? 1) Column 2) Row");
            int chose = numberInput(1, 2, "Which sector? ");
            if (chose == 1) {
                out.println("Chose a column ( 1 - 2 - 3 - 4 ) ");
                int choseColumn = numberInput(1, 4, "Which column? ");
                Marble support = lightModel.getSingleMarble();
                lightModel.setSingleMarble(lightModel.getFirstRow()[choseColumn - 1]);
                lightModel.setMarbleInFirstRow(choseColumn - 1, lightModel.getSecondRow()[choseColumn - 1]);
                lightModel.setMarbleInSecondRow(choseColumn - 1, lightModel.getThirdRow()[choseColumn - 1]);
                lightModel.setMarbleInThirdRow(choseColumn - 1, support);
                printMarket();
                server.buyFromMarket(0, choseColumn);
            } else if (chose == 2) {
                out.println("Chose a row ( 1 - 2 - 3 ) ");
                int choseRow = numberInput(1, 3, "Which row? ");
                Marble support = lightModel.getSingleMarble();
                if (choseRow == 1) {
                    lightModel.setSingleMarble(lightModel.getFirstRow()[0]);
                    lightModel.setMarbleInFirstRow(0, lightModel.getFirstRow()[1]);
                    lightModel.setMarbleInFirstRow(1, lightModel.getFirstRow()[2]);
                    lightModel.setMarbleInFirstRow(2, lightModel.getFirstRow()[3]);
                    lightModel.setMarbleInFirstRow(3, support);
                } else if (choseRow == 2) {
                    lightModel.setSingleMarble(lightModel.getSecondRow()[0]);
                    lightModel.setMarbleInSecondRow(0, lightModel.getSecondRow()[1]);
                    lightModel.setMarbleInSecondRow(1, lightModel.getSecondRow()[2]);
                    lightModel.setMarbleInSecondRow(2, lightModel.getSecondRow()[3]);
                    lightModel.setMarbleInSecondRow(3, support);
                } else if (choseRow == 3) {
                    lightModel.setSingleMarble(lightModel.getThirdRow()[0]);
                    lightModel.setMarbleInThirdRow(0, lightModel.getThirdRow()[1]);
                    lightModel.setMarbleInThirdRow(1, lightModel.getThirdRow()[2]);
                    lightModel.setMarbleInThirdRow(2, lightModel.getThirdRow()[3]);
                    lightModel.setMarbleInThirdRow(3, support);
                }
                printMarket();
                server.buyFromMarket(1, choseRow);
            }
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }

    /**
     * This method ends the game
     */
    private void endGame() {
    }

    /**
     * This method permit the player to discard a card
     * @param turnZone wich turn zone
     */
    public void discardCard(int turnZone) {
        try {
            if (this.leaderCardStatus[0] == 1 && this.leaderCardStatus[1] == 1) {
                out.println("Wich one of the above cards? Type 1 to pick the first one, 2 to pick the second one");
                int chose = numberInput(1, 2, "Which? ");
                out.println("You received 1 faith point \n");
                server.discardCard(chose - 1, turnZone);
            } else if (this.leaderCardStatus[1] == 1 && (this.leaderCardStatus[0] == 2 || this.leaderCardStatus[0] == 0))
                server.discardCard(1, turnZone);
            else if (this.leaderCardStatus[0] == 1 && (this.leaderCardStatus[1] == 2 || this.leaderCardStatus[1] == 0))
                server.discardCard(0, turnZone);
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }

    /**
     * Print the devcard market
     */
    private void printDevCardMarket() {

        String[][] tilesArray = new String[31][80];


        int pIterator = 0;
        int kIterator = 0;

        for (int i = 0; i < 31; i++) {
            for (int j = 0; j < 80; j++) {
                tilesArray[i][j] = " ";
            }
        }

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 4; j++) {

                for (int k = 0; k < MAX_VERT_TILES; k++) {

                    for (int p = 0; p < MAX_HORIZON_TILES; p++) {
                        tilesArray[k + (kIterator * 10)][p + (pIterator * 19)] = getPrintableDevCard(lightModel.getDevCardMarket()[i][j])[k][p];
                    }

                }

                pIterator++;

            }

            kIterator++;
            pIterator = 0;

        }

        for (int i = 0; i < 31; i++) {
            for (int j = 0; j < 80; j++) {
                System.out.print(tilesArray[i][j]);
            }
            out.println();
        }

    }

    /**
     * This method return al the printable leadercard
     * @param devCard the devcard
     * @return
     */
    public String[][] getPrintableDevCard(DevCard devCard) {
        fillEmpty(devCard);
        if (devCard.getPV() != 0) {
            loadDevCardCost(devCard);
            loadDevCardLevel(devCard);
            loadDevCardProductionPower(devCard);
            loadPV(devCard);
        }
        return tiles;
    }

    /**
     * This method load the PV of a devcard
     * @param devCard the devcard
     */
    private void loadPV(DevCard devCard) {
        if (devCard.getPV() > 9) {
            tiles[7][7] = String.valueOf(devCard.getPV()).substring(0, 1);
            tiles[7][8] = String.valueOf(devCard.getPV()).substring(1, 2);
        } else {
            tiles[7][7] = String.valueOf(devCard.getPV());
        }
    }

    /**
     * This method load the dev card level
     * @param devCard the devcard
     */
    private void loadDevCardLevel(DevCard devCard) {
        for (int i = 0; i < devCard.getDevLevel(); i++) {
            tiles[1 + i][14] = ".";
        }

    }

    /**
     * This method load the devcard cost
     * @param devCard the devcard
     */
    private void loadDevCardCost(DevCard devCard) {

        Map<Resource, Integer> devCardCost = devCard.getDevCostAsMap();

        int i = 1;
        for (Resource resource : Resource.values()) {
            if (resource != Resource.EMPTY && resource != Resource.FAITHPOINT) {
                tiles[i][3] = getResourceArt(resource);
                if (devCardCost.get((resource)) != null) {
                    tiles[i][5] = devCardCost.get((resource)).toString();
                }
            }
            i++;
        }

    }

    /**
     * This method load the devcard's production power
     * @param devCard the devcard
     */
    private void loadDevCardProductionPower(DevCard devCard) {
        int i = 0;

        for (Resource resource : devCard.getProductionPower().getResourceToPay()) {
            if (resource != Resource.EMPTY && resource != Resource.FAITHPOINT) {
                tiles[5][3 + i] = getResourceArt(resource);
                i++;
            }
        }
        i++;
        tiles[5][3 + i] = "=";
        i += 2;
        for (Resource resource : devCard.getProductionPower().getResourceToReceive()) {
            if (resource != Resource.EMPTY) {
                tiles[5][3 + i] = getResourceArt(resource);
                i++;
            }
        }
    }

    /**
     * This method print the empty part of the devcard
     * @param devCard the devcard
     */
    private void fillEmpty(DevCard devCard) {

        tiles[0][0] = rectangleArt.getLeftTopAngle(devCard);
        for (int c = 1; c < MAX_HORIZON_TILES - 1; c++) {
            tiles[0][c] = rectangleArt.getTopDownBorder(devCard);
        }

        tiles[0][MAX_HORIZON_TILES - 1] = rectangleArt.getRightTopAngle(devCard);

        for (int r = 1; r < MAX_VERT_TILES - 1; r++) {
            tiles[r][0] = rectangleArt.getLeftRightBorder(devCard);
            for (int c = 1; c < MAX_HORIZON_TILES - 1; c++) {
                tiles[r][c] = " ";
            }
            tiles[r][MAX_HORIZON_TILES - 1] = rectangleArt.getLeftRightBorder(devCard);
        }

        tiles[MAX_VERT_TILES - 1][0] = rectangleArt.getLeftDownAngle(devCard);
        for (int c = 1; c < MAX_HORIZON_TILES - 1; c++) {
            tiles[MAX_VERT_TILES - 1][c] = rectangleArt.getTopDownBorder(devCard);
        }

        tiles[MAX_VERT_TILES - 1][MAX_HORIZON_TILES - 1] = rectangleArt.getRightDownAngle(devCard);

    }

    private String[][] fillEmpty() {

        String[][] tiles = new String[MAX_VERT_TILES][MAX_HORIZON_TILES];

        tiles[0][0] = rectangleArt.getLeftTopAngle(Color.ANSI_BRIGHT_BLACK);
        for (int c = 1; c < MAX_HORIZON_TILES - 1; c++) {
            tiles[0][c] = rectangleArt.getTopDownBorder(Color.ANSI_BRIGHT_BLACK);
        }

        tiles[0][MAX_HORIZON_TILES - 1] = rectangleArt.getRightTopAngle(Color.ANSI_BRIGHT_BLACK);

        for (int r = 1; r < MAX_VERT_TILES - 1; r++) {
            tiles[r][0] = rectangleArt.getLeftRightBorder(Color.ANSI_BRIGHT_BLACK);
            for (int c = 1; c < MAX_HORIZON_TILES - 1; c++) {
                tiles[r][c] = " ";
            }
            tiles[r][MAX_HORIZON_TILES -1] = rectangleArt.getLeftRightBorder(Color.ANSI_BRIGHT_BLACK);
        }

        tiles[MAX_VERT_TILES - 1][0] = rectangleArt.getLeftDownAngle(Color.ANSI_BRIGHT_BLACK);
        for (int c = 1; c < MAX_HORIZON_TILES - 1; c++) {
            tiles[MAX_VERT_TILES - 1][c] = rectangleArt.getTopDownBorder(Color.ANSI_BRIGHT_BLACK);
        }

        tiles[MAX_VERT_TILES - 1][MAX_HORIZON_TILES - 1] = rectangleArt.getRightDownAngle(Color.ANSI_BRIGHT_BLACK);

        return tiles;

    }

    /**
     * Print the resource as a cli art
     * @param resource the resource
     * @return the art
     */
    private String getResourceArt(Resource resource) {
        switch (resource) {
            case SLAVE:
                return resourcesArt.slave();
            case STONE:
                return resourcesArt.stone();
            case MONEY:
                return resourcesArt.money();
            case SHIELD:
                return resourcesArt.shield();
            case FAITHPOINT:
                return resourcesArt.faithPoint();
            case EMPTY:
                return resourcesArt.whiteMarble();
            default:
                return " ";
        }
    }

    /**
     * This method permit the player to print the market
     */
    public void printMarket() {
        out.println("MARKET");
        out.print("SINGLE MARBLE: ");
        out.println(getResourceArt(lightModel.getSingleMarble().getResource()));
        for (int j = 0; j < 4; j++) {
            out.print("  " + getResourceArt(lightModel.getFirstRow()[j].getResource()) + "    ");
        }
        out.println("");
        for (int j = 0; j < 4; j++) {
            out.print("  " + getResourceArt(lightModel.getSecondRow()[j].getResource()) + "    ");
        }
        out.println("");
        for (int j = 0; j < 4; j++) {
            out.print("  " + getResourceArt(lightModel.getThirdRow()[j].getResource()) + "    ");
        }
        out.println("");
        out.println("");
        out.println("");
    }

    /**
     * This method print the faith path
     * @param crossPosition the position on the faith path
     * @param victoryPoints the PV
     * @param papalCardOne the status of the first papal card
     * @param papalCardTwo the status of the second papal card
     * @param papalCardThree the status of the fourth papal card
     */
    public void printFaithPath(int crossPosition, int victoryPoints, boolean papalCardOne, boolean papalCardTwo, boolean papalCardThree) {
        out.println();
        out.println("Cross position: " + crossPosition);
        out.println("Victory points: " + victoryPoints);
        out.println("Papal Card One: " + papalCardOne);
        out.println("Papal Card Two: " + papalCardTwo);
        out.println("Papal Card Three: " + papalCardThree);
    }

    /**
     * This method print the dashboard of the player
     */
    private void printPlayerDashBoard() {
        printFaithPath(lightModel.getCrossPosition(), lightModel.getVictoryPoints(), lightModel.isPapalCardOne(), lightModel.isPapalCardTwo(), lightModel.isPapalCardThree());
        printStartTurnWarehouse();
        printChest();
        printBaseProductionPower();
        printPlayerDevCards();
        printLeaderProductionPowers();
    }

    /**
     * This method print the warehouse at the begining of the turn
     */
    private void printStartTurnWarehouse() {
        out.println("\nWarehouse");
        out.print("First shelf: ");
        if (lightModel.getFirstShelf() != Resource.EMPTY && lightModel.getFirstShelf() != Resource.FAITHPOINT) {
            out.print("[ ");
            printResource(lightModel.getFirstShelf());
            out.print("]");
        }
        out.println();
        out.print("Second shelf: ");
        for (int i = 0; i < lightModel.getSecondShelfNumber(); i++) {
            out.print("[ ");
            printResource(lightModel.getSecondShelf());
            out.print("]");
        }
        out.print("\nThird shelf: ");
        for (int i = 0; i < lightModel.getThirdShelfNumber(); i++) {
            out.print("[ ");
            printResource(lightModel.getThirdShelf());
            out.print("]");
        }
        out.println();
    }

    /**
     * This method print the chest of the player
     */
    public void printChest() {
        out.println();
        out.println("CHEST");
        for (Resource resource : Resource.values()) {
            if (resource != Resource.EMPTY && resource != Resource.FAITHPOINT) {
                printResource(resource);
                out.println("- " + lightModel.getChest().get(resource));
            }
        }
    }

    /**
     * This method print the player's dev cards
     */
    private void printPlayerDevCards() {
        if(lightModel.getActiveDevCardMap().isEmpty()){
            out.println();
            out.println();
            out.println("You don't own Development Cards.");
        }
        else {
            String tiles[][] = new String[11][80];
            for(int i=0; i<11; i++){
                for(int j=0; j<80; j++){
                    tiles[i][j] = " ";
                }
            }
            out.println("\nDevCards:");
            int counter = 1;
            int jIterator=0;

            for(int i=0; i<3; i++){
                tiles[0][8+(jIterator*18)] = String.valueOf(counter);
                String[][] devCardTiles;
                if(lightModel.getActiveDevCardMap().containsKey(i)){
                    devCardTiles = getPrintableDevCard(lightModel.getActiveDevCardMap().get(i));
                }
                else {
                    devCardTiles = fillEmpty();
                }
                for(int k = 0; k < MAX_VERT_TILES; k++) {
                    for (int j = 0; j < MAX_HORIZON_TILES; j++) {
                        tiles[k+1][j+(jIterator*19)] = devCardTiles[k][j];
                    }
                }
                jIterator++;
                counter++;
            }

            for(int i=0; i<11; i++){
                for(int j=0; j<80; j++){
                    out.print(tiles[i][j]);
                }
                out.println();
            }

        }
    }

    /**
     * This method print the production powers of the leadercards
     */
    private void printLeaderProductionPowers() {

        if (lightModel.getLeaderProductionPowerList().isEmpty()) {
            out.println();
            out.println("You don't own Leader Production Powers");
        } else {
            out.println("Leader Production Powers");
            int counter = 4;
            for (ProductionPower productionPower : lightModel.getLeaderProductionPowerList()) {
                out.print(counter + " - ");
                for (Resource resource : productionPower.getResourceToPay()) {
                    printResource(resource);
                }
                out.print(" -> ");
                for (Resource resource : productionPower.getResourceToReceive()) {
                    printResource(resource);
                }
                counter++;
            }
            out.println();
        }

    }

    /**
     * This method print the base production power
     */
    private void printBaseProductionPower() {
        out.println();
        out.println("Base Production Power");
        out.print("0 - ");

        if (lightModel.getBaseProductionPower().getResourceToPay() != null) {
            for (Resource resource : lightModel.getBaseProductionPower().getResourceToPay()) {
                printResource(resource);
            }
        } else {
            out.print("? + ?");
        }

        out.print(" -> ");

        if (lightModel.getBaseProductionPower().getResourceToReceive() != null) {
            for (Resource resource : lightModel.getBaseProductionPower().getResourceToReceive()) {
                printResource(resource);
            }
        } else {
            out.print("?");
        }

    }

    /**
     * This method permit to print the resource
     * @param resource the resource
     */
    public void printResource(Resource resource) {
        switch (resource) {
            case SLAVE:
                out.print(resourcesArt.slave() + " ");
                break;
            case STONE:
                out.print(resourcesArt.stone() + " ");
                break;
            case MONEY:
                out.print(resourcesArt.money() + " ");
                break;
            case SHIELD:
                out.print(resourcesArt.shield() + " ");
                break;
            case FAITHPOINT:
                out.print(resourcesArt.faithPoint() + " ");
                break;
            default:
                out.print("? ");
                break;
        }
    }

    /**
     * This method ends the turn of the player
     */
    public void endTurn() {
        out.println("Your turn is ended! ");
        server.continueGame();
    }

    /**
     * This method permit to buy the resources from the market
     * @param resources a list of the resources
     * @param firstWhite the first ability of the white marble
     * @param secondWhite the second ability of the white marble
     */
    public void buyMarketResource(List<Resource> resources, Resource firstWhite, Resource secondWhite) {
        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i) == Resource.EMPTY) this.newResources.add(choseResource(firstWhite, secondWhite));
            else {
                this.newResources.add(resources.get(i));
            }
        }
        out.println("You have these new resources to manage : ");
        for (int i = 0; i < resources.size(); i++) {
            out.print(resources.get(i).toString() + " ");
        }
        out.println(" ");
        out.println("");
        server.ReorderWarehouse(false);
    }

    /**
     * This method permit the player, if he is able, to selecet a resources when he receive a white marble
     * @param firstWhite the first white ability
     * @param secondWhite the second one
     * @return the resources e picked
     */
    public Resource choseResource(Resource firstWhite, Resource secondWhite) {
        Resource chosen = Resource.EMPTY;
        try {
            if (firstWhite != Resource.EMPTY && secondWhite != Resource.EMPTY) {
                out.println("Chose between this two possibility: 1) " + firstWhite.toString() + " and 2)" + secondWhite.toString() + " ");
                int chose = numberInput(1, 2, "What? ");
                if (chose == 1) chosen = firstWhite;
                else chosen = secondWhite;
            } else if (firstWhite != Resource.EMPTY && secondWhite == Resource.EMPTY) chosen = firstWhite;
            return chosen;
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
        return chosen;
    }

    /**
     * This method permit the player to reorder the warehouse
     * @param mapResources the resources he already has
     * @param firstLevel fist special level
     * @param secondLevel second special level
     * @param isIndipendent if is independent
     */
    public void reorderWarehouse(Map<Resource, Integer> mapResources, Resource firstLevel, Resource secondLevel, Boolean isIndipendent) {
        resetCliWarehouse();
        out.println("You have this resources in your warehouse at the moment: \n");
        mapResources.forEach((key, value) -> out.println(key + " : " + value));
        out.println("");
        out.println("");
        if (firstLevel != Resource.EMPTY)
            out.println("You have a special shelf with this resource ability: " + firstLevel.toString());
        if (secondLevel != Resource.EMPTY)
            out.println("You have a special shelf with this resource ability: " + secondLevel.toString());
        buildWarehouse(mapResources, firstLevel, secondLevel);
        printWarehouse();
        askToSendNewWarehouse(mapResources, firstLevel, secondLevel, isIndipendent);
    }

    /**
     * This method reset the warehouse in the cli
     */
    private void resetCliWarehouse() {
        this.newFirstShelf = Resource.EMPTY;
        if (this.newSecondShelf != null) this.newSecondShelf.clear();
        if (this.newThirdShelf != null) this.newThirdShelf.clear();
        if (this.newFirstSpecialShelf != null) this.newFirstSpecialShelf.clear();
        if (this.newSecondSpecialShelf != null) this.newSecondSpecialShelf.clear();
    }

    /**
     * This method send to the model the new warehouse
     * @param mapResources the resources he had to managed
     * @param firstLevel first special level
     * @param secondLevel second special level
     * @param isIndipendent if is independet
     */
    private void askToSendNewWarehouse(Map<Resource, Integer> mapResources, Resource firstLevel, Resource secondLevel, Boolean isIndipendent) {
        try {
            out.println("Do you want this new warehouse and discard the other resources? " + discardList.toString());
            int chose = numberInput(0, 1, "Chose 0) No, reorder warehouse 1) Yes ");
            if (chose == 0) {
                reorderWarehouse(mapResources, firstLevel, secondLevel, isIndipendent);
            } else {
                List<Resource> supportDiscard = new ArrayList<>();
                supportDiscard.addAll(discardList);
                if (newResources != null) newResources.clear();
                if (discardList != null) discardList.clear();
                server.NewWarehouse(newFirstShelf, newSecondShelf, newThirdShelf, newFirstSpecialShelf, newSecondSpecialShelf, supportDiscard, isIndipendent);
            }
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }

    /**
     * This method permit the player to build the new warehouse
     * @param mapResources the resources he has to manage
     * @param firstLevel the first special shelf
     * @param secondLevel the second special shelf
     */
    private void buildWarehouse(Map<Resource, Integer> mapResources, Resource firstLevel, Resource secondLevel) {
        try {
            for (Map.Entry<Resource, Integer> entry : mapResources.entrySet()) {
                for (int i = 0; i < entry.getValue().intValue(); i++) {
                    int chose = 0;
                    boolean goneRight = false;
                    do {
                        out.println("Chose where to put this resource: " + entry.getKey().toString());
                        if (firstLevel == Resource.EMPTY && secondLevel == Resource.EMPTY) {
                            chose = numberInput(0, 3, "Chose floor 0) Add to discard list 1) First 2) Second 3) Third    ");
                        } else if (firstLevel != Resource.EMPTY && secondLevel == Resource.EMPTY) {
                            chose = numberInput(0, 4, "Chose floor 0) Add to discard list 1) First 2) Second 3) Third 4) FirstSpecial    ");
                        } else if (firstLevel != Resource.EMPTY && secondLevel != Resource.EMPTY) {
                            chose = numberInput(0, 5, "Chose floor 0) Add to discard list 1) First 2) Second 3) Third 4) FirstSpecial 5) SecondSpecial    ");
                        }
                        if (chose == 0) {
                            this.discardList.add(entry.getKey());
                            goneRight = true;
                        } else if (controllFloor(chose, entry.getKey(), firstLevel, secondLevel) && chose != 0) {
                            goneRight = true;
                        }
                    }
                    while (!goneRight);
                }
            }
            if (this.newResources != null) {
                for (int i = 0; i < newResources.size(); i++) {
                    int chose = 0;
                    boolean goneRight = false;
                    do {
                        out.println("Chose where to put this resources " + newResources.get(i).toString());
                        if (firstLevel == Resource.EMPTY && secondLevel == Resource.EMPTY) {
                            chose = numberInput(0, 3, "Chose floor 0) Add to discard list 1) First 2) Second 3) Third   ");
                        } else if (firstLevel != Resource.EMPTY && secondLevel == Resource.EMPTY) {
                            chose = numberInput(0, 4, "Chose floor 0) Add to discard list 1) First 2) Second 3) Third 4) FirstSpecial   ");
                        } else if (firstLevel != Resource.EMPTY && secondLevel != Resource.EMPTY) {
                            chose = numberInput(0, 5, "Chose floor 0) Add to discard list 1) First 2) Second 3) Third 4) FirstSpecial 5) SecondSpecial   ");
                        }
                        if (chose == 0) {
                            this.discardList.add(newResources.get(i));
                            goneRight = true;
                        } else if (controllFloor(chose, newResources.get(i), firstLevel, secondLevel) && chose != 0) {
                            goneRight = true;
                        }
                    }
                    while (!goneRight);
                }
            }
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }

    /**
     * This method print the warehouse
     */
    private void printWarehouse() {
        out.println();
        out.println("Warehouse");
        out.println("First shelf " + this.newFirstShelf.toString());
        out.println("Second shelf " + this.newSecondShelf.toString());
        out.println("Third shelf " + this.newThirdShelf.toString());
        out.println();
        if (this.newFirstShelf != null) {
            out.println("First Special shelf " + this.newFirstSpecialShelf.toString());
        }
        if (this.newSecondShelf != null) {
            out.println("Second Special shelf " + this.newSecondSpecialShelf.toString());
        }
    }

    /**
     * This method control if the shelf respects the regolamentation
     * @param chose wich shelf
     * @param resource wich type of resources
     * @param firstFloor the special shelf level
     * @param secondFloor the second special shelf level
     * @return a boolean if is ok to put the resources
     */
    private boolean controllFloor(int chose, Resource resource, Resource firstFloor, Resource secondFloor) {
        if (chose == 1) {
            Resource evitateOne = Resource.EMPTY;
            Resource evitateTwo = Resource.EMPTY;
            if (newSecondShelf != null && !newSecondShelf.isEmpty()) {
                evitateOne = newSecondShelf.get(0);
            }
            if (newThirdShelf != null && !newThirdShelf.isEmpty()) {
                evitateTwo = newThirdShelf.get(0);
            }
            if (this.newFirstShelf == Resource.EMPTY && resource != evitateOne && resource != evitateTwo) {
                this.newFirstShelf = resource;
                out.println("Resource " + resource.toString() + " added to the first shelf");
                return true;
            } else if (this.newFirstShelf == Resource.EMPTY && (resource == evitateOne || resource == evitateTwo)) {
                out.println("Resources already putted in another floor");
                return false;
            } else {
                out.println("First shelf already full ");
                return false;
            }
        } else if (chose == 2) {
            Resource evitateOne = Resource.EMPTY;
            Resource evitateTwo = Resource.EMPTY;
            if (newFirstShelf != null) {
                evitateOne = newFirstShelf;
            }
            if (newThirdShelf != null && !newThirdShelf.isEmpty()) {
                evitateTwo = newThirdShelf.get(0);
            }
            if (this.newSecondShelf.size() == 0 && resource != evitateOne && resource != evitateTwo) {
                this.newSecondShelf.add(resource);
                out.println("Added a " + resource.toString() + " to the second floor");
                return true;
            } else if (this.newSecondShelf.size() == 0 && (resource == evitateOne || resource == evitateTwo)) {
                out.println("Resources already putted in another floor");
                return false;
            } else {
                if (this.newSecondShelf.get(0) == resource && this.newSecondShelf.size() < 2) {
                    this.newSecondShelf.add(resource);
                    out.println("Added a " + resource.toString() + " to the second floor");
                    return true;
                } else if (this.newSecondShelf.get(0) == resource && this.newSecondShelf.size() == 2) {
                    out.println("Second floor is full");
                    return false;
                } else if (this.newSecondShelf.get(0) != resource) {
                    out.println("Wrong resource ");
                    return false;
                }
            }
        } else if (chose == 3) {
            Resource evitateOne = Resource.EMPTY;
            Resource evitateTwo = Resource.EMPTY;
            if (newFirstShelf != null) {
                evitateOne = newFirstShelf;
            }
            if (newSecondShelf != null && !newSecondShelf.isEmpty()) {
                evitateTwo = newSecondShelf.get(0);
            }
            if (this.newThirdShelf.size() == 0 && resource != evitateOne && resource != evitateTwo) {
                this.newThirdShelf.add(resource);
                out.println("Added a " + resource.toString() + " to the third floor");
                return true;
            } else if (this.newThirdShelf.size() == 0 && (resource == evitateOne || resource == evitateTwo)) {
                out.println("Resources already putted in another floor");
                return false;
            } else {
                if (this.newThirdShelf.get(0) == resource && this.newThirdShelf.size() < 3) {
                    this.newThirdShelf.add(resource);
                    out.println("Added a " + resource.toString() + " to the third floor");
                    return true;
                } else if (this.newThirdShelf.get(0) == resource && this.newThirdShelf.size() == 3) {
                    out.println("Third floor is full");
                    return false;
                } else if (this.newThirdShelf.get(0) != resource) {
                    out.println("Wrong resource ");
                    return false;
                }
            }
        } else if (chose == 4) {
            if (newFirstSpecialShelf.size() < 2 && firstFloor == resource) {
                this.newFirstSpecialShelf.add(resource);
                out.println("Added a " + resource.toString() + " to the first special shelf");
                return true;
            } else if (newFirstSpecialShelf.size() == 0 && firstFloor != resource) {
                out.println("Wrong resource ");
                return false;
            } else if (newFirstSpecialShelf.size() == 2) {
                out.println("Already filled! ");
                return false;
            }
        } else if (chose == 5) {
            if (newSecondSpecialShelf.size() < 2 && secondFloor == resource) {
                this.newSecondSpecialShelf.add(resource);
                out.println("Added a " + resource.toString() + " to the second special shelf");
                return true;
            } else if (newSecondSpecialShelf.size() == 0 && secondFloor != resource) {
                out.println("Wrong resource ");
                return false;
            } else if (newSecondSpecialShelf.size() == 2) {
                out.println("Already filled! ");
                return false;
            }
        }
        return false;
    }

    /**
     * This method permit to the player to continue the turn after reordering the warehouse
     * @param i if is independent
     * @param leaders the leadercards
     */
    public void afterReorder(int i, List<LeaderCard> leaders) {
        if (i == 1) {
            if (this.leaderCardStatus[0] == 1 || this.leaderCardStatus[1] == 1) {
                askToManageLeaderCards(leaders, 2);
            } else {
                out.println("You don't have usable leader cards");
                if (lightModel.isGameFinished() == true) endGame();
                else {
                    endTurn();
                }
            }
        } else {
            mainMove();
        }
    }

    public void devCard(DevCard devCardChosen, int finalSlotToPut, Resource discountPowerOne, Resource discountPowerTwo) {
        if(devCardChosen == null){
            out.println("Wrong input");
            mainMove();
        }
        else{
            payDevCard(devCardChosen, finalSlotToPut, discountPowerOne, discountPowerTwo);
        }
    }

    public void payDevCard(DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo){

        int nResource;
        out.println("Pay the DevCard chosen.");
        out.print("[  ");
        for(Resource resource : Resource.values()) {
            nResource = 0;
            if(resource != Resource.EMPTY && resource != Resource.FAITHPOINT) {
                printResource(resource);
                nResource = devCard.getDevCostAsMap().get(resource);
                out.print(": " + nResource);
            }
        }
        out.println(" ]");

        List<Boolean> isWarehouse = new ArrayList<>();
        List<Integer> shelfLevel = new ArrayList<>();
        List<Resource> resourceType = new ArrayList<>();

        List<Resource> l = devCard.getResourceToPay();
        if(discountPowerOne != Resource.EMPTY)l.remove(discountPowerOne);
        if(discountPowerTwo != Resource.EMPTY)l.remove(discountPowerTwo);

        for(Resource resource : l){

            out.print("What deposit do you want to pay for the resource ");
            printResource(resource);
            out.println(" 1)Warehouse - 2)Chest");

            int fromWhere = 0;
            try {
                fromWhere = numberInput(1, 2, "Warehouse or Chest? ");
            }
            catch (ExecutionException e) {
                out.println("Wrong input");
            }

            if (fromWhere == 1) {
                isWarehouse.add(true);
            }
            else {
                isWarehouse.add(false);
            }

            if (fromWhere == 1) {
                out.println("Which shelf? 1) 2) 3) 4) 5): ");
                int shelf = 0;
                try {
                    shelf = numberInput(1, 5, "Choose: ");
                }
                catch (ExecutionException e) {
                    out.println("Wrong input");
                }
                shelfLevel.add(shelf);
            }
            else {
                shelfLevel.add(0);
            }

            resourceType.add(resource);

        }

        server.PayDevCard(isWarehouse.toArray(new Boolean[0]), shelfLevel.toArray(new Integer[0]), resourceType.toArray(new Resource[0]), devCard, slotToPut, discountPowerOne, discountPowerTwo);

    }

    public void devCardResponse(boolean success, String payDevCard, DevCard devCard, int slotToPut, Resource discountPowerOne, Resource discountPowerTwo) {
        if(success){
            out.println("Successfully buy the development card.");
        }
        else{
            out.println("you can't pay like you said, try again");
            payDevCard(devCard, slotToPut, discountPowerOne, discountPowerTwo);
        }
    }
    List<ProductionPower> paidProductionPowerList = new ArrayList<>();
    List <Integer> chosenIntegerList = new ArrayList<>();
    public void productionPowerMove() {

        out.println();
        printPlayerDashBoard();
        printPaidProductionPowerList(paidProductionPowerList);
        int choseAction = 0;
        out.println();
        out.println("Chose your action");
        out.println("Type '0' if you want to chose a Production Power, '1' to activate the Production Powers or to end your action.");

        try{
            choseAction = numberInput(0, 1, "Which action? ");
        }
        catch (ExecutionException e){
            out.println("Wrong input");
        }

        if (choseAction == 0) {
            choseProductionPower();
        }
        else if (choseAction == 1) {
            activateProductionPowers();
        }

    }

    public void activateProductionPowers(){
        out.println("Activation...");
        for(ProductionPower productionPower : paidProductionPowerList){
            if(productionPower.isLeaderProductionPower()){
                lightModel.setCrossPosition(lightModel.getCrossPosition()+1);
            }
            else {
                for(Resource resource : productionPower.getResourceToReceive()){
                    if(resource.equals(Resource.FAITHPOINT)){
                        lightModel.setCrossPosition(lightModel.getCrossPosition()+1);
                    }
                }
            }

        }
        server.ProductionPowerActivation();
    }

    /**
     * Permit the player to chose a production power
     */
    public void choseProductionPower(){
        int productionPowerChosen = 0;

        try {
            productionPowerChosen = numberInput(0, 5, "Which Production Power? ");
        }
        catch (ExecutionException e) {
            out.println("Wrong input");
        }

        if(productionPowerChosen==0){
            chosenBaseProductionPower();
        }
        else if(productionPowerChosen>=1 && productionPowerChosen<=3){
            chosenDevCardProductionPower(productionPowerChosen);
        }
        else if(productionPowerChosen>=4 && productionPowerChosen <= 5){
            leaderProductionPowerChosen(productionPowerChosen);
        }
    }

    public void leaderProductionPowerChosen(int productionPowerChosen){
        if(!chosenIntegerList.contains(productionPowerChosen) && productionPowerChosen-3<=lightModel.getLeaderProductionPowerList().size()){
            chosenIntegerList.add(productionPowerChosen);
            setLeaderProductionPower(lightModel.getLeaderProductionPowerList().get(productionPowerChosen-4));
        }
        else {
            out.println("You can't active this Production Power.");
            productionPowerMove();
        }
    }

    public void chosenDevCardProductionPower(int productionPowerChosen){
        if(!chosenIntegerList.contains(productionPowerChosen) && lightModel.getActiveDevCardMap().containsKey(productionPowerChosen-1)){
            chosenIntegerList.add(productionPowerChosen);
            List<ProductionPower> productionPower = new ArrayList<>();
            productionPower.add(lightModel.getActiveDevCardMap().get(productionPowerChosen-1).getProductionPower());
            server.ProductionPowerList(productionPower, "productionPowerChosen");
        }
        else {
            out.println("You can't active this Production Power.");
            productionPowerMove();
        }
    }



    public void chosenBaseProductionPower(){
        if(!chosenIntegerList.contains(0)){
            chosenIntegerList.add(0);
            setBaseProductionPower();
        }
        else {
            out.println("You can't active this Production Power.");
            productionPowerMove();
        }
    }

    public void payProductionPower(ProductionPower productionPower){

        out.println("Pay the Production Power chosen.");
        out.print("[   ");
        for(Resource resource : productionPower.getResourceToPay()){
            printResource(resource);
            out.print("  ");
        }
        out.println(" ]");

        List<Boolean> isWarehouse = new ArrayList<>();
        List<Integer> shelfLevel = new ArrayList<>();
        List<Resource> resourceType = new ArrayList<>();

        for(Resource resource : productionPower.getResourceToPay()){

            out.print("From where do you want to pay for the resource ");
            printResource(resource);
            out.println(" 1)Warehouse - 2)Chest");

            int fromWhere = 0;
            try {
                fromWhere = numberInput(1, 2, "Warehouse or Chest? ");
            }
            catch (ExecutionException e) {
                out.println("Wrong input");
            }

            if (fromWhere == 1) {
                isWarehouse.add(true);
            }
            else {
                isWarehouse.add(false);
            }

            if (fromWhere == 1) {
                out.println("Which shelf? 1) 2) 3) 4) 5): ");
                int shelf = 0;
                try {
                    shelf = numberInput(1, 5, "Choose: ");
                }
                catch (ExecutionException e) {
                    out.println("Wrong input");
                }
                shelfLevel.add(shelf);
            }
            else {
                shelfLevel.add(0);
            }

            resourceType.add(resource);

        }

        server.PayProductionPower(isWarehouse.toArray(new Boolean[0]), shelfLevel.toArray(new Integer[0]), resourceType.toArray(new Resource[0]), productionPower);

    }

    public void setLeaderProductionPower(ProductionPower productionPower){
        out.println("You have chosen a Leader Production Power. Choose a resource to receive.");
        Resource resourceChosen = choseResource();
        productionPower.setLeaderProductionPowerResourceToReceive(resourceChosen);
        server.ProductionPowerResource(resourceChosen, productionPower);
    }

    public void setBaseProductionPower(){
        out.println("Set the Base Production Power Resources");
        List<Resource> resourcesToPayList = new ArrayList<>();
        List<Resource> resourceToReceiveList = new ArrayList<>();
        for( int i=0; i<2; i++){
            out.println("Set the resource " + (i+1) + " to pay");
            resourcesToPayList.add( choseResource() );
        }
        out.println("Set the resource to receive");
        resourceToReceiveList.add( choseResource() );
        server.TwoResourceList(resourcesToPayList, resourceToReceiveList, "setBaseProductionPower");
    }

    /**
     * Permit the player to chose a resource
     * @return the resources picked
     */
    public Resource choseResource(){
        int resource = 0;
        out.println("Type: 0)MONEY - 1)STONE - 2)SLAVE - 3)SHIELD");
        try {
            resource = numberInput(0, 3, "Which resource? ");
        }
        catch (ExecutionException e) {
            out.println("Wrong input");
        }
        switch (resource) {
            case 0:
                return Resource.MONEY;
            case 1:
                return Resource.STONE;
            case 2:
                return Resource.SLAVE;
            case 3:
                return Resource.SHIELD;
            default :
                return null;
        }
    }

    public void printPaidProductionPowerList(List<ProductionPower> list){

        int productionPowerCounter = 0;
        out.println();
        out.println("Paid Production Power List:");

        for(ProductionPower productionPower : list){

            if(productionPower.isBaseProductionPower()){
                out.println("Base Production Power:");
            }
            if(productionPower.isLeaderProductionPower()){
                out.println("Leader Production Power:");
            }
            out.print(productionPowerCounter + " - ");
            for(Resource resource : productionPower.getResourceToPay()){
                printResource(resource);
                out.print(" ");
            }
            out.print(" --> ");
            for(Resource resource : productionPower.getResourceToReceive()){
                printResource(resource);
                out.print(" ");
            }
            out.println();
            productionPowerCounter++;

        }

    }

    public void productionPowerResponse(boolean response, String action, ProductionPower productionPower) {
        switch (action) {
            case "setBaseProductionPower":
                if (response) {
                    out.println("the resources have been set up.");
                    lightModel.getBaseProductionPower().setBaseProductionPowerLists(productionPower.getResourceToPay(), productionPower.getResourceToReceive());
                    payProductionPower(productionPower);
                } else {
                    out.println("the resources haven't been set up.");
                    mainMove();
                }
                break;
            case "productionPowerCheck":
                if (response) {
                    out.println("Production Power have been chosen.");
                    payProductionPower(productionPower);
                } else {
                    out.println("Production Power have been chosen, but you can't afford it.");
                    if(productionPower.isLeaderProductionPower()){
                        for(ProductionPower power : lightModel.getLeaderProductionPowerList()){
                            if(power.equals(productionPower)){
                                power.resetLeaderProductionPower();
                            }
                        }
                    }
                    mainMove();
                }
                break;
            case "payProductionPower":
                if (response) {
                    out.println("You have successfully paid the Production Power.");
                    paidProductionPowerList.add(productionPower);
                    productionPowerMove();
                } else {
                    out.println("You haven't successfully paid the Production Power chosen.");
                    payProductionPower(productionPower); //to check (loop?)
                }
                break;
            case "activation":
                if (response) {
                    out.println("Successfully activated the Production Powers.");
                    paidProductionPowerList.clear();
                    lightModel.getBaseProductionPower().resetBaseProductionPower();
                    for(ProductionPower leaderProductionPower : lightModel.getLeaderProductionPowerList()){
                        leaderProductionPower.resetLeaderProductionPower();
                    }
                    chosenIntegerList.clear();
                }
                else {
                    out.println("Activation FAIL.");
                }
                break;
            case "setLeaderProductionPower":
                if (response) {
                    out.println("Leader Production Power have been set successfully.");
                    payProductionPower(productionPower);
                }
                else {
                    out.println("FAIL.");
                    for(ProductionPower productionPowers : lightModel.getLeaderProductionPowerList()){
                        if(productionPowers.equals(productionPower)){
                            productionPowers.resetLeaderProductionPower();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * A generic message is shown
     * @param drawActionToken the message
     */
    public void broadcastGenericMessage(String drawActionToken) {
        out.println("\n \n" +drawActionToken + "\n \n");

    }

    /**s
     * This method ends the game of the single player
     * @param pv the PV of the player
     * @param crossPosition the position on the faith path
     * @param b if the player has won
     */
    public void endGameSinglePlayer(int pv, int crossPosition, boolean b) {
        if(b){
            out.println();
            out.println("You are the winner!");
            out.println("You have " + pv + " Victory Points");
            out.println("Lawrence The Magnificent cross position is " + crossPosition);
        }
        else {
            out.println();
            out.println("Lawrence The Magnificent is the winner!");
            out.println("You have " + pv + " Victory Points");
            out.println("Lawrence The Magnificent cross position is " + crossPosition);
        }
        System.exit(0);
    }
}
