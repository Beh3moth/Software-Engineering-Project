package it.polimi.ngsw.view.cli;


import it.polimi.ngsw.controller.ClientController;

import it.polimi.ngsw.model.LeaderCard;
import it.polimi.ngsw.model.Resource;
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
            playerNumber = numberInput(1, 4,  question);
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
     * @param LeaderCards    the list of the available LeaderCards.
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
                do{
                IdChoosenTwo = numberInput(1, LeaderCards.size(), (2) + "° LeaderCard ID: ") - 1;
                if(IdChoosen != IdChoosenTwo){
                    goneRight = true;
                } }
                while(!goneRight);

                chosenCard.add(LeaderCards.get(IdChoosen));

                notifyObserver(obs -> obs.onUpdateLeaderCard(chosenCard));}
             catch (ExecutionException e) {
                out.println("Input canceled");
            }

        } else {
            //showErrorAndExit("no gods found in the request.");
        }
    }


    public void distribuiteInitialResources(int number){
        System.out.println("You start with " + number + " resources");
        int Chosen;
        int ChosenTwo;
        int FirstPos;
        int SecondPos;
        if(number == 1){
            Resource resourceOne;
            try {
                System.out.println(" Chose between 1) Money 2) Slave 3) Shield 4) Stone ");
                Chosen = numberInput(1, 4, "Pick resource");
                if(Chosen == 1){resourceOne = Resource.MONEY; }
                else if(Chosen == 2){resourceOne = Resource.SLAVE;}
                else if(Chosen == 3){resourceOne = Resource.SHIELD;}
                else{resourceOne = Resource.STONE;}
                System.out.println("Chose where to put it 1)FirstFloor 2)SecondFloor 3)ThirdFloor ");
                FirstPos = numberInput(1, 3, "Floor");
                notifyObserver(obs -> obs.onUpdatePickedResources(number, resourceOne, null, FirstPos, 0));
            }
            catch (ExecutionException e) {
                out.println("Input canceled");
            }
        }else if(number == 2){
            Resource resourceOne;
            Resource resourceTwo;
            try {
                System.out.println(" Chose between 1) Money 2) Slave 3) Shield 4) Stone ");
                Chosen = numberInput(1, 4, "Pick resource");
                System.out.println(" Chose between 1) Money 2) Slave 3) Shield 4) Stone ");
                ChosenTwo = numberInput(1, 4, "Pick resource two");
                if(Chosen == 1){resourceOne = Resource.MONEY; }
                else if(Chosen == 2){resourceOne = Resource.SLAVE;}
                else if(Chosen == 3){resourceOne = Resource.SHIELD;}
                else {resourceOne = Resource.STONE;}
                System.out.println("Chose where to put it 1)FirstFloor 2)SecondFloor 3)ThirdFloor ");
                FirstPos = numberInput(1, 3, "Floor");
                if(ChosenTwo == 1){resourceTwo = Resource.MONEY; }
                else if(ChosenTwo == 2){resourceTwo = Resource.SLAVE;}
                else if(ChosenTwo == 3){resourceTwo = Resource.SHIELD;}
                else {resourceTwo = Resource.STONE;}
                boolean goneRight = false;
                int Pos;
                do {
                    System.out.println("Chose where to put it (must be different) 1)FirstFloor 2)SecondFloor 3)ThirdFloor ");
                    Pos = numberInput(1, 3, "Floor");
                    if(Pos != FirstPos){goneRight = true;}
                }while(!goneRight);
                SecondPos = Pos;
                notifyObserver(obs -> obs.onUpdatePickedResources(number, resourceOne, resourceTwo, FirstPos, SecondPos));
            }
            catch (ExecutionException e) {
                out.println("Input canceled");
            }
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