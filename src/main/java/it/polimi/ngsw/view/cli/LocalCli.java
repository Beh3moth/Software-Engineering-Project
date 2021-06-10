package it.polimi.ngsw.view.cli;

import it.polimi.ngsw.controller.ClientController;
import it.polimi.ngsw.model.*;
import it.polimi.ngsw.network.server.LocalServer;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.LightModel;
import it.polimi.ngsw.view.View;
import it.polimi.ngsw.view.cli.AsciiArt.Color;
import it.polimi.ngsw.view.cli.AsciiArt.RectangleArt;
import it.polimi.ngsw.view.cli.AsciiArt.ResourcesArt;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;


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
                out.println("Please, enter one ID confirm with ENTER."); //non scegli lo stesso cojone
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

    public String[][] getPrintableLeaderCard(LeaderCard leaderCard) {
        fillEmptyLeaderCard();
        loadLeaderCardCost(leaderCard);
        loadLeaderPV(leaderCard);
        loadLeaderCardPower(leaderCard);
        return leaderCardTiles;
    }

    private void loadLeaderCardPower(LeaderCard leaderCard) {
        for (int i = 0; i < leaderCard.getLeaderCardAbilityAsString().length; i++) {
            leaderCardTiles[6][i + 2] = leaderCard.getLeaderCardAbilityAsString()[i];
        }
    }

    private void loadLeaderPV(LeaderCard leaderCard) {
        leaderCardTiles[4][8] = String.valueOf(leaderCard.getPV());
    }

    private void loadLeaderCardCost(LeaderCard leaderCard) {
        for (int i = 0; i < leaderCard.getLeaderCardCostAsString().length; i++) {
            leaderCardTiles[1][i + 2] = leaderCard.getLeaderCardCostAsString()[i];
        }
    }

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
                   /* if (lightModel.isGameFinished() == true) afterLastMainMove(1, Leaders);
                    else {
                        askToManageLeaderCards(Leaders, turnZone);
                    }*/
                }//leadercard choice. middle turn
            } else if (actionTypology == 2) {
                this.leaderCardStatus[wichCard] = 0;
                if (lightModel.isGameFinished() == true) endGame();
                else {
                    endTurn();
                }
            }
            //fine turno
        }
    }

    public void mainMove() {
        try {
            printDevCardMarket();
            printMarket();
            printPlayerDashBoard();
            out.println("\nChose what you want to do: 1) Reorder warehouse 2) Take resources from the market 3) Buy a Development Card 4) Activate Production Powers 5) Visualize other Player Dashboard");
            int chose = numberInput(1, 5, "Which move? ");
            if (chose == 1) {
                server.ReorderWarehouse(true);
            } else if (chose == 2) takeResourcesFromMarket();
            else if (chose == 3) chooseDevCard();
           // else if (chose == 4) productionPowerMove();
           // else if (chose == 5) watchOtherPlayerInfo();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }

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

        //aggiunge subito risorse allo stock e white marble allo stock white, poi ritorno un messaggio con situazione stock (se ha whitre ability, butta le white dentro,
        //se no le cancella.... con lo stock decido risorsa per risorsa che fare, se buttare dentro al mercato o scartare, salvo lo stock dentro alla cli, posso passare al riordina
        //magazzino e poi ritornare allom stock, alla fine di tutto devo avere uno stock VUOTO.
    }

    private void endGame() {
        out.println("\n\nThe game is ended, now it's time to calculate the PV of every player.");
        //notifyObserver(obs -> obs.onUpdateCalculatePVEndGame());
    }

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

    private void loadPV(DevCard devCard) {
        if (devCard.getPV() > 9) {
            tiles[7][7] = String.valueOf(devCard.getPV()).substring(0, 1);
            tiles[7][8] = String.valueOf(devCard.getPV()).substring(1, 2);
        } else {
            tiles[7][7] = String.valueOf(devCard.getPV());
        }
    }

    private void loadDevCardLevel(DevCard devCard) {
        for (int i = 0; i < devCard.getDevLevel(); i++) {
            tiles[1 + i][14] = ".";
        }

    }

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

    public void printFaithPath(int crossPosition, int victoryPoints, boolean papalCardOne, boolean papalCardTwo, boolean papalCardThree) {
        out.println();
        out.println("Cross position: " + crossPosition);
        out.println("Victory points: " + victoryPoints);
        out.println("Papal Card One: " + papalCardOne);
        out.println("Papal Card Two: " + papalCardTwo);
        out.println("Papal Card Three: " + papalCardThree);
    }

    private void printPlayerDashBoard() {
        printFaithPath(lightModel.getCrossPosition(), lightModel.getVictoryPoints(), lightModel.isPapalCardOne(), lightModel.isPapalCardTwo(), lightModel.isPapalCardThree());
        printStartTurnWarehouse();
        printChest();
        printBaseProductionPower();
        printPlayerDevCards();
        printLeaderProductionPowers();
    }

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

    public void endTurn() {
        out.println("Your turn is ended! ");
        //notifyObserver(obs -> obs.onEndTurn());
    }

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

    private void resetCliWarehouse() {
        this.newFirstShelf = Resource.EMPTY;
        if (this.newSecondShelf != null) this.newSecondShelf.clear();
        if (this.newThirdShelf != null) this.newThirdShelf.clear();
        if (this.newFirstSpecialShelf != null) this.newFirstSpecialShelf.clear();
        if (this.newSecondSpecialShelf != null) this.newSecondSpecialShelf.clear();
    }

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
}