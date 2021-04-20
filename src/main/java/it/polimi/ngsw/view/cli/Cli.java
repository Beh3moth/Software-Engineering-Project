package it.polimi.ngsw.view.cli;


import it.polimi.ngsw.controller.ClientController;

import it.polimi.ngsw.model.Game;
import it.polimi.ngsw.model.LeaderCard;
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
                validInput = false; }
        } while (!validInput);
        do { out.print("Enter the server port [" + defaultPort + "]: ");
            String port = readLine();
            if (port.equals("")) {
                serverInfo.put("port", defaultPort);
                validInput = true;
            } else {
                if (ClientController.isValidPort(port)) {
                    serverInfo.put("port", port);
                    validInput = true; }
                else {
                    out.println("Invalid port!");
                    validInput = false; } } } while (!validInput);
        notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo)); }


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
                this.leaderCardStatus = new int[]{1,1};
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
    public void startTurnMessage(List<LeaderCard> Leaders) {
        out.println("It's your turn!");
        if(this.leaderCardStatus[0] == 1 || this.leaderCardStatus[1] == 1){
            askToManageLeaderCards(Leaders, 1);
        }
        else if((this.leaderCardStatus[0] == 0 && this.leaderCardStatus[1] == 0) || (this.leaderCardStatus[0] == 2 && this.leaderCardStatus[1] == 2)){
            out.println("You don't have usable leader cards");
            //mainMove();
        }
    }

    @Override
    public void continueTurn(int turnZone, int actionTypology, int goneRight, int wichCard, List<LeaderCard> Leaders) {
        if(turnZone == 1) { //inizio turno
            if (actionTypology == 1) { //1 vuol dire che era stata chiamata una leadercard request, 2 una discard card
                if (goneRight == 0) {  //0 vuol dire non attivata, quindi richiedi, 1 attivata
                    askToManageLeaderCards(Leaders, turnZone);
                } else if (goneRight == 1) {
                    this.leaderCardStatus[wichCard] = 2;
                    //mainMove();
                }
            }
            else if(actionTypology == 2){
                this.leaderCardStatus[wichCard] = 0;
                //mainMove();
            }
        }else if(turnZone == 2){
            if(actionTypology == 1){
                if(goneRight == 1){
                    this.leaderCardStatus[wichCard] = 2;
                    //endTurn();
                }else if(goneRight == 0){}//leadercard choice. middle turn
            }
            else if(actionTypology == 2){
                this.leaderCardStatus[wichCard] = 0;
                //endTurn();
            }
            //fine turno
        }
    }


    public void askToManageLeaderCards(List<LeaderCard> Leaders, int turnZone) {
        try {
            if(this.leaderCardStatus[0] == 1)printLeaderCard(Leaders.get(0));
            if(this.leaderCardStatus[1] == 1)printLeaderCard(Leaders.get(1));
            out.println("Do you want to activate one of these leaderCard? 1) YES 0) NO");
            int chose = numberInput(0, 1, "What? ");
            if(chose == 1)activateLeaderCard(Leaders, turnZone);
            else if(chose == 0)askToDiscardLeaderCard(turnZone);
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }

    public void askToDiscardLeaderCard(int turnZone){
        try{
        out.println("Do you want to discard a leadercard? 1) YES 0) NO");
        int chose = numberInput(0, 1, "What? ");
            if(chose == 1)discardCard(turnZone);
            else if(chose == 0){}//mainMove();
        }
        catch (ExecutionException e) {
            out.println("Input canceled");
        }
        //mainMove();
        //CHIEDO SE VOGLI SCARTARE, SE NO, VADO AL MAIN
    }

    public void discardCard(int turnZone){
        try {
            if(this.leaderCardStatus[0] == 1 && this.leaderCardStatus[1] == 1){
                out.println("Wich one of the above cards? Type 1 to pick the first one, 2 to pick the second one");
                int chose = numberInput(1, 2, "Which? ");
                notifyObserver(obs -> obs.onUpdateDiscardCard(chose - 1, turnZone));
            }
            else if(this.leaderCardStatus[1] == 1 && (this.leaderCardStatus[0] == 2 || this.leaderCardStatus[0] == 0))
                notifyObserver(obs -> obs.onUpdateDiscardCard(1, turnZone));
            else if(this.leaderCardStatus[0] == 1 && (this.leaderCardStatus[1] == 2 || this.leaderCardStatus[1] == 0))
                notifyObserver(obs -> obs.onUpdateDiscardCard(0, turnZone));
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
    }

    public void activateLeaderCard(List<LeaderCard> Leaders, int turnZone){
        try {
            if(this.leaderCardStatus[0] == 1 && this.leaderCardStatus[1] == 1){
                out.println("Wich one of the above cards? Type 1 to pick the first one, 2 to pick the second one");
                int chose = numberInput(1, 2, "Which? ");
                notifyObserver(obs -> obs.onUpdateLeaderCardActivation(chose - 1, turnZone));
            }
            else if(this.leaderCardStatus[1] == 1 && (this.leaderCardStatus[0] == 2 || this.leaderCardStatus[0] == 0))
                notifyObserver(obs -> obs.onUpdateLeaderCardActivation(1, turnZone));
            else if(this.leaderCardStatus[0] == 1 && (this.leaderCardStatus[1] == 2 || this.leaderCardStatus[1] == 0))
                notifyObserver(obs -> obs.onUpdateLeaderCardActivation(0, turnZone));
        } catch (ExecutionException e) {
            out.println("Input canceled");
        }
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

    private void printLeaderCard(LeaderCard card){
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