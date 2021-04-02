package it.polimi.ngsw.model;
//import player

import java.io.IOException; //uhmmmm
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    public static final int MAX_PLAYERS = 3;

    private Board board;
    private Player activePlayer;
    private List<Player> players;
    private int playerNumbers;
    private FaithPath lawrenceFaithPath = new FaithPath();

    public Game(){
        this.board = new Board();
        this.players = new ArrayList<>();
    }

    /**
     * Returns a player given his {@code nickname}.
     * Only the first occurrence is returned because
     * the player nickname is considered to be unique.
     * If no player is found {@code null} is returned.
     *
     * @param nickname the nickname of the player to be found.
     * @return Returns the player given his {@code nickname}, {@code null} otherwise.
     */
    public Player getPlayerByNickname(String nickname) {
        return players.stream()
                .filter(player -> nickname.equals(player.getNickName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a player to the game.
     * Notifies all the views if the playersNumber is already set.
     *
     * @param player the player to add to the game.
     */
    public void addPlayer(Player player) {
        players.add(player);
       /* if (chosenPlayersNumber != 0) {
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.chosenPlayersNumber));
        }*/
    }

    /**
     * Removes a player from the game.
     * Notifies all the views if the notifyEnabled argument is set to {@code true}.
     *
     * @param nickname      the nickname of the player to remove from the game.
     * @param notifyEnabled set to {@code true} to enable a lobby disconnection message, {@code false} otherwise.
     * @return {@code true} if the player is removed, {@code false} otherwise.
     */
    public boolean removePlayerByNickname(String nickname, boolean notifyEnabled) {
        boolean result = players.remove(getPlayerByNickname(nickname));

        /*if (notifyEnabled) {
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.chosenPlayersNumber));
        }*/

        return result;
    }

    /**
     * Number of current players added in the game.
     *
     * @return the number of players.
     */
    public int getNumCurrentPlayers() {
        return players.size();
    }

    /**
     * Returns the number of players chosen by the first player.
     *
     * @return the number of players chosen by the first player.
     */
    public int getChosenPlayersNumber() {
        return playerNumbers;
    }

    /**
     * Search a nickname in the current Game.
     *
     * @param nickname the nickname of the player.
     * @return {@code true} if the nickname is found, {@code false} otherwise.
     */
    public boolean isNicknameTaken(String nickname) {
        return players.stream()
                .anyMatch(p -> nickname.equals(p.getNickName()));
    }

    /**
     * Returns a list of player nicknames that are already in-game.
     *
     * @return a list with all nicknames in the Game
     */
    public List<String> getPlayersNicknames() {
        List<String> nicknames = new ArrayList<>();
        for (Player p : players) {
            nicknames.add(p.getNickName());
        }
        return nicknames;
    }

    /**
     * Returns the current board.
     *
     * @return the board of the game.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns a list of players.
     *
     * @return the players.
     */
    public List<Player> getPlayers() {
        return players;
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
        } while (!allRight);

        Scanner input = new Scanner(System.in);
        int up = activePlayer.getWarehouse().getStockResourceNumber();
        int j = 0;
        while (j < up) {
            boolean generalValidator = false;
            do {
                System.out.println("Choose what to do with this resource " + activePlayer.getWarehouse().getStockResource(j));
                boolean validator = false;
                int choosen;
                do {
                    System.out.println("1) Discard 2)Add Warehouse 3)Add special warehouse 4) reorder warehouse"); //potrei aggiungere
                    choosen = input.nextInt();
                    if (0 < choosen && choosen < 5) {
                        validator = true;
                    }
                    if (choosen == 3 && (activePlayer.getWarehouse().getLeaderLevelType(1) == Resource.EMPTY)) {
                        validator = false;
                        System.out.println("You don't have leader card shelf");
                    }
                }
                while (!validator);
                int level;
                if (choosen == 1) {
                    generalValidator = activePlayer.getWarehouse().removeFirstResourceFromStock();
                    addOtherFaithPoint(activePlayer);
                    j--;
                    up--;
                } else if (choosen == 2) {
                    System.out.println("Choose wich shelf of the warehouse");
                    boolean goneRight = false;
                    do {
                        level = input.nextInt();
                        if (0 < level && level < 4) {
                            goneRight = true;
                        }

                    } while (!goneRight);
                    generalValidator = activePlayer.getWarehouse().addResourceToWarehouse(level, activePlayer.getWarehouse().getStockResource(0)); //devo controllare poi se una volta dentro la inserisco veramente
                    if(generalValidator == true){activePlayer.getWarehouse().removeFirstResourceFromStock();
                        j--;
                        up--;}
                } else if (choosen == 3) {
                    int levelUp = 1;
                    if (activePlayer.getWarehouse().getLeaderLevelType(2) != Resource.EMPTY) {
                        levelUp = 2;
                    }
                    System.out.println("Choose wich specialshelf of the warehouse");
                    boolean goneRight = false;
                    do {
                        level = input.nextInt();
                        if (0 < level && level <= levelUp) {
                            goneRight = true;
                        }
                    } while (!goneRight);
                    generalValidator = activePlayer.getWarehouse().addResourceToSpecialLevel(level, activePlayer.getWarehouse().getStockResource(0));
                    if(generalValidator == true){
                        activePlayer.getWarehouse().removeFirstResourceFromStock();
                        j--;
                        up--;}
                } else if (choosen == 4) {
                    reorderWarehouse(activePlayer);
                }
            } while (!generalValidator);
            j++;

        }
    }

    public void reorderWarehouse(Player activePlayer) {
        Scanner input = new Scanner(System.in);
        int choosen;
        boolean verifier = false;
        do{
            System.out.println("Chose what to do: 1)Finish 2)Throw away resource 2)Move resources");
            choosen = input.nextInt();
            boolean goneWell = false;
            if(choosen == 1){
                verifier = true;
            }
            else if(choosen == 2){
                System.out.println("Chose a shelf to remove from (between 1 and 3");
                choosen = input.nextInt();

                if(choosen < 4 && 0 < choosen){
                    goneWell = activePlayer.getWarehouse().removeResourceWarehouse(choosen);
                }
            }
                else if(choosen == 4){
                    int levelUp = 1;
                    int level;
                    if (activePlayer.getWarehouse().getLeaderLevelType(2) != Resource.EMPTY) {
                        levelUp = 2;
                    }
                    System.out.println("Choose wich specialshelf of the warehouse");
                    boolean goneRight = false;
                    do {
                        level = input.nextInt();
                        if (0 < level && level <= levelUp) {
                            goneRight = true;
                        }

                    } while (!goneRight);
                    goneWell = activePlayer.getWarehouse().removeSpecialResourceWarehouse(level);
                }
                if(goneWell == true){
                    addOtherFaithPoint(activePlayer);
                }

            else if(choosen == 2){

            }
        }while(!verifier);
    }

    public void addOtherFaithPoint(Player activePlayer){
        for(int i = 0; i < this.playerNumbers; i++){
            if(players.get(i) != activePlayer){
                players.get(i).getFaithPath().increaseCrossPosition();
            }
        }
    }
}


