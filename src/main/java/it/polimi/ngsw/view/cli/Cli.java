package it.polimi.ngsw.view.cli;


import it.polimi.ngsw.controller.ClientController;

import it.polimi.ngsw.model.Game;
import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.Marble;
import it.polimi.ngsw.model.Resource;
import it.polimi.ngsw.network.message.StartTurnMessage;
import it.polimi.ngsw.observer.ViewObservable;
import it.polimi.ngsw.view.View;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


/**
 * This class offers a User Interface to the user via terminal. It is an implementation of the {@link View}.
 */
public class Cli extends ViewObservable implements View {

    private final PrintStream out;
    private Thread inputThread;
    private int[] leaderCardStatus; //1 means not activated but usable, 0 means discarded, 2 means activated
    private Marble singleMarble;
    private Marble[] firstRow;
    private Marble[] secondRow;
    private Marble[] thirdRow;
    private List<Resource> newResources;
    private Resource newFirstShelf;
    private List<Resource> newSecondShelf;
    private List<Resource> newThirdShelf;
    private List<Resource> newFirstSpecialShelf;
    private List<Resource> newSecondSpecialShelf;
    private List<Resource> discardList;

    /**
     * Default constructor.
     */
    public Cli() {
        out = System.out;
    }

    /**
     * Starts the command-line interface.
     */
    public void init() {
        out.println("Welcome to Maestri del rinascimento");

        try {
            askServerInfo();
        } catch (ExecutionException e) {
            out.println("User input canceled.");
        }
    }


    /**
     * Asks the server address and port to the user.
     *
     * @throws ExecutionException if the input stream thread is interrupted.
     */
    public void askServerInfo() throws ExecutionException {
        Map<String, String> serverInfo = new HashMap<>();
        String defaultAddress = "localhost";
        String defaultPort = "16847";
        boolean validInput;
        out.println("Please specify the following settings. The default value is shown between brackets.");
        do {
            out.print("Enter the server address [" + defaultAddress + "]: ");
            String address = readLine();
            if (address.equals("")) {
                serverInfo.put("address", defaultAddress);
                validInput = true;
            } else if (ClientController.isValidIpAddress(address)) {
                serverInfo.put("address", address);
                validInput = true;
            } else {
                out.println("Invalid address!");
                clearCli();
                validInput = false;
            }
        } while (!validInput);
        do {
            out.print("Enter the server port [" + defaultPort + "]: ");
            String port = readLine();
            if (port.equals("")) {
                serverInfo.put("port", defaultPort);
                validInput = true;
            } else {
                if (ClientController.isValidPort(port)) {
                    serverInfo.put("port", port);
                    validInput = true;
                } else {
                    out.println("Invalid port!");
                    validInput = false;
                }
            }
        } while (!validInput);
        notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo));
    }


    @Override
    public void askNickname() {
        out.print("Enter your nickname: ");
        try {
            String nickname = readLine();
            notifyObserver(obs -> obs.onUpdateNickname(nickname));
        } catch (ExecutionException e) {
            out.println("User input canceled.");
        }
    }

    @Override
    public void askPlayersNumber() {
        int playerNumber;
        String question = "How many players are going to play? (You can choose between 1 or 4 players): ";

        try {
            playerNumber = numberInput(1, 4, question);
            notifyObserver(obs -> obs.onUpdatePlayersNumber(playerNumber));
        } catch (ExecutionException e) {
            out.println("User input canceled.");
        }
    }

    @Override
    public void askFirstPlayer(List<String> nicknameQueue) {
        out.println("Choose the first player: ");
        out.print("Online players: " + String.join(", ", nicknameQueue));
        try {

            String nickname;
            do {
                out.print("\nType the exact name of the player: ");

                nickname = readLine();
                if (!nicknameQueue.contains(nickname)) {
                    out.println("You have selected an invalid player! Please try again.");
                }
            } while (!nicknameQueue.contains(nickname));

            String finalNickname = nickname;
            notifyObserver(obs -> obs.onUpdateFirstPlayer(finalNickname));
        } catch (ExecutionException e) {
            out.println("Inpnut canceled");
        }
    }


    /**
     * The user pick two leaderCards out of 4 available.
     *
     * @param LeaderCards the list of the available LeaderCards.
     */
    @Override
    public void askLeaderCard(List<LeaderCard> LeaderCards) {
        clearCli();
        int IdChoosen;
        int IdChoosenTwo;
        if (LeaderCards.size() == 4) {
            List<LeaderCard> chosenCard = new ArrayList<>();
            out.println("Select two Leader Cards from the list.");
            printLeaderCardList(LeaderCards);
            try {
                out.println("Please, enter one ID confirm with ENTER.");
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
                notifyObserver(obs -> obs.onUpdateLeaderCard(chosenCard));
            } catch (ExecutionException e) {
                out.println("Input canceled");
            }

        } else {
            showErrorAndExit("no leadersCards found in the request.");
        }
    }


    @Override
    public void distribuiteInitialResources(int number) {
        out.println("You start with " + number + " resources");
        int Chosen;
        int ChosenTwo;
        int FirstPos;
        int SecondPos;
        if (number == 1) {
            Resource resourceOne;
            try {
                out.println(" Chose between 1) Money 2) Slave 3) Shield 4) Stone ");
                Chosen = numberInput(1, 4, "Pick resource");
                if (Chosen == 1) {
                    resourceOne = Resource.MONEY;
                } else if (Chosen == 2) {
                    resourceOne = Resource.SLAVE;
                } else if (Chosen == 3) {
                    resourceOne = Resource.SHIELD;
                } else {
                    resourceOne = Resource.STONE;
                }
                out.println("Chose where to put it 1)FirstFloor 2)SecondFloor 3)ThirdFloor ");
                FirstPos = numberInput(1, 3, "Floor");
                notifyObserver(obs -> obs.onUpdatePickedResources(number, resourceOne, null, FirstPos, 0));
            } catch (ExecutionException e) {
                out.println("Input canceled");
            }
        } else if (number == 2) {
            Resource resourceOne;
            Resource resourceTwo;
            try {
                out.println(" Chose between 1) Money 2) Slave 3) Shield 4) Stone ");
                Chosen = numberInput(1, 4, "Pick resource");
                if (Chosen == 1) {
                    resourceOne = Resource.MONEY;
                } else if (Chosen == 2) {
                    resourceOne = Resource.SLAVE;
                } else if (Chosen == 3) {
                    resourceOne = Resource.SHIELD;
                } else {
                    resourceOne = Resource.STONE;
                }
                out.println("Chose where to put it 1)FirstFloor 2)SecondFloor 3)ThirdFloor ");
                FirstPos = numberInput(1, 3, "Floor");
                boolean goneRight = false;
                int Pos;
                Resource res;
                do {
                    out.println(" Chose between 1) Money 2) Slave 3) Shield 4) Stone ");
                    ChosenTwo = numberInput(1, 4, "Pick resource two");
                    out.println("Chose where to put it (must be different if the resource is different) 1)FirstFloor 2)SecondFloor 3)ThirdFloor ");
                    if (ChosenTwo == 1) {
                        res = Resource.MONEY;
                    } else if (ChosenTwo == 2) {
                        res = Resource.SLAVE;
                    } else if (ChosenTwo == 3) {
                        res = Resource.SHIELD;
                    } else {
                        res = Resource.STONE;
                    }
                    Pos = numberInput(1, 3, "Floor");
                    if ((Pos != FirstPos) && (resourceOne != res) || (Pos == FirstPos) && (Pos != 1) && (resourceOne == res)) {
                        goneRight = true;
                    }
                } while (!goneRight);
                resourceTwo = res;
                SecondPos = Pos;
                notifyObserver(obs -> obs.onUpdatePickedResources(number, resourceOne, resourceTwo, FirstPos, SecondPos));
            } catch (ExecutionException e) {
                out.println("Input canceled");
            }
        }
    }

    @Override
    public void startTurnMessage(List<LeaderCard> Leaders, Marble singleMarble, Marble[] firstRow, Marble[] secondRow, Marble[] thirdRow) {
        out.println("It's your turn!");
        this.singleMarble = singleMarble;
        this.firstRow = firstRow;
        this.secondRow = secondRow;
        this.thirdRow = thirdRow;
        if (this.leaderCardStatus[0] == 1 || this.leaderCardStatus[1] == 1) {
            askToManageLeaderCards(Leaders, 1);
        } else {
            out.println("You don't have usable leader cards");
            mainMove();
        }
    }

    @Override
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
                    //endTurn();
                } else if (goneRight == 0) {
                }//leadercard choice. middle turn
            } else if (actionTypology == 2) {
                this.leaderCardStatus[wichCard] = 0;
                //endTurn();
            }
            //fine turno
        }
    }

    @Override
    public void buyMarketResource(List<Resource> resources, Resource firstWhite, Resource secondWhite) {
        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i) == Resource.EMPTY) this.newResources.set(i, choseResource(firstWhite, secondWhite));
        }
        out.println("You have this new resources to manage : ");
        for (int i = 0; i < resources.size(); i++) {
            out.print(resources.get(i).toString() + " ");
        }
        out.println(" ");
        notifyObserver(obs -> obs.onUpdateReorderWarehouse());
    }

    private void resetCliWarehouse() {
        this.newFirstShelf = Resource.EMPTY;
        this.newSecondShelf.clear();
        this.newThirdShelf.clear();
        this.newFirstSpecialShelf.clear();
        this.newSecondSpecialShelf.clear();
    }

    @Override
    public void reorderWarehouse(Map<Resource, Integer> mapResources, Resource firstLevel, Resource secondLevel) {
        resetCliWarehouse();
        out.println("You have this resources in your warehouse at the moment: ");
        mapResources.forEach((key, value) -> out.println(key + ":" + value));
        if (firstLevel != Resource.EMPTY)
            out.println("You have a special shelf with this resource: " + firstLevel.toString());
        if (secondLevel != Resource.EMPTY)
            out.println("You have a special shelf with this resource: " + secondLevel.toString());
        buildWarehouse(mapResources, firstLevel, secondLevel);
        printWarehouse();
        askToSendNewWarehouse(mapResources, firstLevel, secondLevel);
    }

    private void askToSendNewWarehouse(Map<Resource, Integer> mapResources, Resource firstLevel, Resource secondLevel) {
        try{
            out.println("Do you want this new warehouse and discard this resources? ");
            int chose = numberInput(0, 1, "Chose 0)No, reorder warehouse 1) Yes");
            if(chose == 0){
                reorderWarehouse(mapResources,firstLevel,secondLevel);
            }else{
                newResources.clear();
                notifyObserver(obs -> obs.onUpdateNewWarehouse(newFirstShelf, newSecondShelf, newThirdShelf, newFirstSpecialShelf, newSecondSpecialShelf, discardList));
            }
        }catch (ExecutionException e) {
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
                        out.println("Chose where to put this resources " + entry.getKey().toString());
                        if (firstLevel == Resource.EMPTY && secondLevel == Resource.EMPTY) {
                            chose = numberInput(0, 3, "Chose floor 0) Add to discard list 1) First 2) Second 3) Third");
                        } else if (firstLevel != Resource.EMPTY && secondLevel == Resource.EMPTY) {
                            chose = numberInput(0, 4, "Chose floor 0) Add to discard list 1) First 2) Second 3) Third 4) FirstSpecial");
                        } else if (firstLevel != Resource.EMPTY && secondLevel != Resource.EMPTY) {
                            chose = numberInput(0, 5, "Chose floor 0) Add to discard list 1) First 2) Second 3) Third 4) FirstSpecial 5) SecondSpecial");
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
                for (int i = 0; i < newResources.size(); i++) {
                    int chose = 0;
                    boolean goneRight = false;
                    do { out.println("Chose where to put this resources " + newResources.get(i).toString());
                        if (firstLevel == Resource.EMPTY && secondLevel == Resource.EMPTY) {
                            chose = numberInput(0, 3, "Chose floor 0) Add to discard list 1) First 2) Second 3) Third");
                        } else if (firstLevel != Resource.EMPTY && secondLevel == Resource.EMPTY) {
                            chose = numberInput(0, 4, "Chose floor 0) Add to discard list 1) First 2) Second 3) Third 4) FirstSpecial");
                        } else if (firstLevel != Resource.EMPTY && secondLevel != Resource.EMPTY) {
                            chose = numberInput(0, 5, "Chose floor 0) Add to discard list 1) First 2) Second 3) Third 4) FirstSpecial 5) SecondSpecial"); }
                        if (chose == 0) { this.discardList.add(entry.getKey());goneRight = true;
                        } else if (controllFloor(chose, entry.getKey(), firstLevel, secondLevel) && chose != 0) { goneRight = true; } }
                    while (!goneRight);
                }
            }
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }

    private void printWarehouse() {
        out.println("Warehouse");
        out.println("First shelf ");//DA PRINTARE TUTTOOOO
    }

    private boolean controllFloor(int chose, Resource resource, Resource firstFloor, Resource secondFloor) {
        if (chose == 1) {
            if (this.newFirstShelf == Resource.EMPTY) {
                this.newFirstShelf = resource;
                out.println("Resource " + resource.toString() + " added to the first shelf");
                return true;
            } else {
                out.println("First shelf already full ");
                return false;
            }
        } else if (chose == 2) {
            if (this.newSecondShelf.size() == 0) {
                this.newSecondShelf.set(0, resource);
                out.println("Added a " + resource.toString() + " to the second floor");
                return true;
            } else {
                if (this.newSecondShelf.get(0) == resource && this.newSecondShelf.size() < 2) {
                    this.newSecondShelf.set(this.newSecondShelf.size(), resource);
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
        } else if (chose == 3)
            if (this.newThirdShelf.size() == 0) {
                this.newThirdShelf.set(0, resource);
                out.println("Added a " + resource.toString() + " to the third floor");
                return true;
            } else {
                if (this.newThirdShelf.get(0) == resource && this.newThirdShelf.size() < 3) {
                    this.newThirdShelf.set(this.newThirdShelf.size(), resource);
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
        else if (chose == 4) {
            if (newFirstSpecialShelf.size() < 2 && firstFloor == resource) {
                this.newFirstSpecialShelf.set(this.newFirstSpecialShelf.size(), resource);
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
                this.newSecondSpecialShelf.set(this.newSecondSpecialShelf.size(), resource);
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

    public void askToManageLeaderCards(List<LeaderCard> Leaders, int turnZone) {
        try {
            if (this.leaderCardStatus[0] == 1) printLeaderCard(Leaders.get(0));
            if (this.leaderCardStatus[1] == 1) printLeaderCard(Leaders.get(1));
            out.println("Do you want to activate one of these leaderCard? 1) YES 0) NO");
            int chose = numberInput(0, 1, "What? ");
            if (chose == 1) activateLeaderCard(Leaders, turnZone);
            else if (chose == 0) askToDiscardLeaderCard(turnZone);
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }

    public void askToDiscardLeaderCard(int turnZone) {
        try {
            out.println("Do you want to discard a leadercard? 1) YES 0) NO");
            int chose = numberInput(0, 1, "What? ");
            if (chose == 1) discardCard(turnZone);
            else if (chose == 0) {
                mainMove();
            }
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }

    }

    public void discardCard(int turnZone) {
        try {
            if (this.leaderCardStatus[0] == 1 && this.leaderCardStatus[1] == 1) {
                out.println("Wich one of the above cards? Type 1 to pick the first one, 2 to pick the second one");
                int chose = numberInput(1, 2, "Which? ");
                notifyObserver(obs -> obs.onUpdateDiscardCard(chose - 1, turnZone));
            } else if (this.leaderCardStatus[1] == 1 && (this.leaderCardStatus[0] == 2 || this.leaderCardStatus[0] == 0))
                notifyObserver(obs -> obs.onUpdateDiscardCard(1, turnZone));
            else if (this.leaderCardStatus[0] == 1 && (this.leaderCardStatus[1] == 2 || this.leaderCardStatus[1] == 0))
                notifyObserver(obs -> obs.onUpdateDiscardCard(0, turnZone));
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }

    public void activateLeaderCard(List<LeaderCard> Leaders, int turnZone) {
        try {
            if (this.leaderCardStatus[0] == 1 && this.leaderCardStatus[1] == 1) {
                out.println("Wich one of the above cards? Type 1 to pick the first one, 2 to pick the second one");
                int chose = numberInput(1, 2, "Which? ");
                notifyObserver(obs -> obs.onUpdateLeaderCardActivation(chose - 1, turnZone));
            } else if (this.leaderCardStatus[1] == 1 && (this.leaderCardStatus[0] == 2 || this.leaderCardStatus[0] == 0))
                notifyObserver(obs -> obs.onUpdateLeaderCardActivation(1, turnZone));
            else if (this.leaderCardStatus[0] == 1 && (this.leaderCardStatus[1] == 2 || this.leaderCardStatus[1] == 0))
                notifyObserver(obs -> obs.onUpdateLeaderCardActivation(0, turnZone));
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }


    public void mainMove() {
        try {
            printMarket();
            out.println("Chose what you want to do: 1) Reorder warehouse 2) Take resources from the market 3) Buy a develop Card 4) Start production");
            int chose = numberInput(1, 4, "Which move? ");
            if (chose == 1) {/*reoderWarehouse();*/} else if (chose == 2) {
                takeResourcesFromMarket();
            } else if (chose == 3) {/*buyDevCard();*/} else if (chose == 4) {/*startProduction();*/}
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }

    public void takeResourcesFromMarket() {
        try {
            out.println("Do you want to take a column or a row? 1) Column 2) Row");
            int chose = numberInput(1, 2, "Which sector? ");
            if (chose == 1) {
                out.println("Chose a column ( 1 - 2 - 3 - 4 ) ");
                int choseColumn = numberInput(1, 4, "Which column? ");
                Marble support = this.singleMarble;
                this.singleMarble = this.firstRow[choseColumn - 1];
                this.firstRow[choseColumn - 1] = this.secondRow[choseColumn - 1];
                this.secondRow[choseColumn - 1] = this.thirdRow[choseColumn - 1];
                this.thirdRow[choseColumn - 1] = support;
                printMarket();
                notifyObserver(obs -> obs.onUpdateBuyFromMarket(0, choseColumn));
            } else if (chose == 2) {
                out.println("Chose a row ( 1 - 2 - 3 ) ");
                int choseRow = numberInput(1, 3, "Which row? ");
                Marble support = this.singleMarble;
                if (choseRow == 1) {
                    this.singleMarble = this.firstRow[0];
                    this.firstRow[0] = this.firstRow[1];
                    this.firstRow[1] = this.firstRow[2];
                    this.firstRow[2] = this.firstRow[3];
                    this.firstRow[3] = support;
                } else if (choseRow == 2) {
                    this.singleMarble = this.secondRow[0];
                    this.secondRow[0] = this.secondRow[1];
                    this.secondRow[1] = this.secondRow[2];
                    this.secondRow[2] = this.secondRow[3];
                    this.secondRow[3] = support;
                } else if (choseRow == 3) {
                    this.singleMarble = this.thirdRow[0];
                    this.thirdRow[0] = this.thirdRow[1];
                    this.thirdRow[1] = this.thirdRow[2];
                    this.thirdRow[2] = this.thirdRow[3];
                    this.thirdRow[3] = support;
                }
                printMarket();
                notifyObserver(obs -> obs.onUpdateBuyFromMarket(1, choseRow));
            }
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }

        //aggiunge subito risorse allo stock e white marble allo stock white, poi ritorno un messaggio con situazione stock (se ha whitre ability, butta le white dentro,
        //se no le cancella.... con lo stock decido risorsa per risorsa che fare, se buttare dentro al mercato o scartare, salvo lo stock dentro alla cli, posso passare al riordina
        //magazzino e poi ritornare allom stock, alla fine di tutto devo avere uno stock VUOTO.
    }

    public void printMarket() {
        out.println("                   MARKET  ");
        out.print("SINGLE MARBLE:                                     ");
        out.println(this.singleMarble.getResource().toString());
        for (int j = 0; j < 4; j++) {
            out.print("  " + this.firstRow[j].getResource().toString() + "    ");
        }
        out.println("");
        for (int j = 0; j < 4; j++) {
            out.print("  " + this.secondRow[j].getResource().toString() + "    ");
        }
        out.println("");
        for (int j = 0; j < 4; j++) {
            out.print("  " + this.secondRow[j].getResource().toString() + "    ");
        }
        out.println("");
    }

    /**
     * Shows the lobby screen on the terminal.
     *
     * @param nicknameList list of players.
     * @param numPlayers   number of players.
     */
    @Override
    public void showLobby(List<String> nicknameList, int numPlayers) {
        out.println("LOBBY:");
        for (String nick : nicknameList) {
            out.println(nick + "\n");
        }
        out.println("Current players in lobby: " + nicknameList.size() + " / " + numPlayers);
    }

    @Override
    public void showWinMessage(String winner) {

    }

    /**
     * Shows a Generic Message from Server
     *
     * @param genericMessage Generic Message from Server.
     */
    @Override
    public void showGenericMessage(String genericMessage) {
        out.println(genericMessage);
    }

    /**
     * Print a list of gods
     *
     * @param cards the list of gods You want to print
     */
    private void printLeaderCardList(List<LeaderCard> cards) {
        for (int i = 0; i < cards.size(); i++) {
            LeaderCard card = cards.get(i);
            out.println("ID: " + (i + 1));
            out.println("PV: " + card.getPV());
            out.println("AbilityName: " + card.getAbilityName());
        }
    }

    private void printLeaderCard(LeaderCard card) {
        out.println("PV: " + card.getPV());
        out.println("AbilityName: " + card.getAbilityName());
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
     * Shows the login result on the terminal.
     * On login fail, the program is terminated immediatly.
     *
     * @param nicknameAccepted     indicates if the chosen nickname has been accepted.
     * @param connectionSuccessful indicates if the connection has been successful.
     * @param nickname             the nickname of the player to be greeted.
     */
    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        clearCli();

        if (nicknameAccepted && connectionSuccessful) {
            out.println("Hi, " + nickname + "! You connected to the server.");
        } else if (connectionSuccessful) {
            askNickname();
        } else if (nicknameAccepted) {
            out.println("Max players reached. Connection refused.");
            out.println("EXIT.");

            System.exit(1);
        } else {
            // showErrorAndExit("Could not contact server.");
        }
    }

    /**
     * Shows a player disconnection message and exit.
     *
     * @param nicknameDisconnected the nickname of the disconnected player.
     * @param text                 the text to be shown.
     */
    @Override
    public void showDisconnectionMessage(String nicknameDisconnected, String text) {
        inputThread.interrupt();
        out.println("\n" + nicknameDisconnected + text);

        System.exit(1);
    }

    /**
     * Shows an error message and exit.
     *
     * @param error the error to be shown.
     */
    @Override
    public void showErrorAndExit(String error) {
        inputThread.interrupt();

        out.println("\nERROR: " + error);
        out.println("EXIT.");

        System.exit(1);
    }


    /**
     * Clears the CLI terminal.
     */
    public void clearCli() {
        //out.print(ColorCli.CLEAR);
        out.flush();
    }

}