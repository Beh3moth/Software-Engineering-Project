package it.polimi.ngsw.model;

import java.io.FileNotFoundException;
import java.util.*;
import it.polimi.ngsw.network.message.LobbyMessage;
import it.polimi.ngsw.observer.Observable;

public class Game extends Observable implements FaithPathListener{

    private static Game instance;
    public static final int MAX_PLAYERS = 4;
    public static final String SERVER_NICKNAME = "server";
    private Board board = new Board();
    private Player activePlayer;
    private List<Player> players;
    private int playerNumbers;
    private FaithPath lawrenceFaithPath = null;
    private Deque<ActionToken> actionTokensDeque = new ArrayDeque<>(6);
    private List<LeaderCard> leaderCards;
    private LeaderCardParser leaderCardParser = new LeaderCardParser();
    
    public Game(){
        players = new ArrayList<>(0);
        initActionTokensDeque();
        this.leaderCards = initLeaderCards();
    }

    public Board getBoard(){
        return this.board;
    }
    /**
     * @return the singleton instance.
     */
    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
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
                .filter(player -> nickname.equals(player.getNickname()))
                .findFirst()
                .orElse(null);
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

        if (notifyEnabled) {
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.playerNumbers));
        }

        return result;
    }

    /**
     * Returns a list of player nicknames that are already in-game.
     *
     * @return a list with all nicknames in the Game
     */
    public List<String> getPlayersNicknames() {
        List<String> nicknames = new ArrayList<>();
        for (Player p : players) {
            nicknames.add(p.getNickname());
        }
        return nicknames;
    }

    /**
     * Search a nickname in the current Game.
     *
     * @param nickname the nickname of the player.
     * @return {@code true} if the nickname is found, {@code false} otherwise.
     */
    public boolean isNicknameTaken(String nickname) {
        return players.stream()
                .anyMatch(p -> nickname.equals(p.getNickname()));
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
     * this method allows you to set the number of players
     * @param numberOfPlayers
     * @return true if the number of players is allowed
     */
    public boolean setNumberOfPlayers(int numberOfPlayers){
        if(numberOfPlayers < 1 || numberOfPlayers > 4) return false;
        else{
            this.playerNumbers = numberOfPlayers;
            return true;
        }
    }

    /**
     * this method create the player
     */
    public void createPlayers(){
        for(int i = 0; i < this.playerNumbers; i++){
            Player newPlayer = new Player("jhon");
            players.add(newPlayer);
            makeGameListenerOfPlayerFaithPath(players.get(i));
        }
        if(players.size()==1){
            Player newPlayer = new Player("john");
            players.add(newPlayer);
            makeGameListenerOfPlayerFaithPath(players.get(0));
            initLawrenceFaithPath();
            makeGameListenerOfLawrenceFaithPath();
        }
    }

    /**
     * Adds a player to the game.
     * Notifies all the views if the playersNumber is already set.
     *
     * @param player the player to add to the game.
     */
    public void addPlayer(Player player) {
        players.add(player);
        if (playerNumbers != 0) {
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.playerNumbers));
        }
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
     * Sets the max number of players chosen by the first player joining the game.
     *
     * @param chosenMaxPlayers the max players number. Value can be {@code 0 < x < MAX_PLAYERS}.
     * @return {@code true} if the argument value is {@code 0 < x < MAX_PLAYERS}, {@code false} otherwise.
     */
    public boolean setChosenMaxPlayers(int chosenMaxPlayers) {
        if (chosenMaxPlayers > 0 && chosenMaxPlayers <= MAX_PLAYERS) {
            this.playerNumbers = chosenMaxPlayers;
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.playerNumbers));
            return true;
        }
        return false;
    }

    //Init game

    /**
     * this method initialize the Lawrence's FaithPath
     */
    public void initLawrenceFaithPath(){
        this.lawrenceFaithPath = new FaithPath();
    }

    /**
     * this method checks that the multiplayers' game is over
     * @return true if the multiplayers' game is ended, false otherwise
     */
    public boolean isGameEndedMultiPlayers(){
        for(int i = 0; i < playerNumbers; i++){
            if((players.get(i).getFaithPath().getCrossPosition() == 20) ||
                    (players.get(i).getDevCardDashboard().getDevCardNumber() == 7))return true;
        }
        return false;
    }

    /**
     * this method checks that the singleplayer's game is over
     * @return true if the singleplayer's game is ended, false otherwise
     */
    public boolean isGameEndedSinglePlayer(){
        for(int i = 0; i < 4; i++){
            if(this.board.getDevCardSpace(0,i).getNumberOfCards() == 0 &&
                    this.board.getDevCardSpace(1,i).getNumberOfCards() == 0 &&
                    this.board.getDevCardSpace(2,i).getNumberOfCards() == 0
            )return true;
        }

        if(this.lawrenceFaithPath.getCrossPosition() == 20)return true;

        if(this.players.get(0).getFaithPath().getCrossPosition() == 20)return true;

        if(this.players.get(0).getDevCardDashboard().getDevCardNumber() == 7)return true;

        return false;
    }

    public Player getPlayerFromList(int indexNumber){
        return this.players.get(indexNumber);
    }


    public void manageWhiteResources(Player activePlayer, int whitePower){ //avrò un while che in base al numero di risorce nel white stock, scelgo per ognuna cosa voglio e chiamo questo metodo
        if(whitePower == 1){
            activePlayer.getWarehouse().addResourceToStock(activePlayer.getWhiteMarblePowerOne());
            activePlayer.getWarehouse().removeFromWhiteStock();
        }
        else if(whitePower == 2){
            activePlayer.getWarehouse().addResourceToStock(activePlayer.getWhiteMarblePowerTwo());
            activePlayer.getWarehouse().removeFromWhiteStock();
        }
    }

    /**
     * The method increases the FaithPoints of every player excluded the active player given as a parameter.
     * @param activePlayer is the Player to not add FaithPoints.
     * @param increase is the amount of FaithPoints to give to the other players.
     */
    public void increaseOtherFaithPoints(Player activePlayer, int increase){

        for(int i=0; i<increase; i++){
            int maxPos = 0;
            for(Player player : players){
                if(!player.equals(activePlayer)){
                    player.getFaithPath().increaseCrossPosition(1);
                    if(player.getFaithPath().getCrossPosition()>maxPos){
                        maxPos = player.getFaithPath().getCrossPosition();
                    }
                }
            }
            if(lawrenceFaithPath!=null){
                lawrenceFaithPath.increaseCrossPosition(1);
                if(lawrenceFaithPath.getCrossPosition()>maxPos){
                    maxPos = lawrenceFaithPath.getCrossPosition();
                }
            }
            checkForVaticanReport(maxPos);
        }

    }

    //LeaderCard methods

    /**
     * The method return a list of every LeaderCard of the game.
     * @return a list of LeaderCards.
     */
    private List<LeaderCard> initLeaderCards() {
        try{
            return leaderCardParser.initLeaderCards();
        }
        catch (FileNotFoundException e){
            return null;
        }
    }

    /**
     * The method returns a list of Leader Cards.
     */
    public List<LeaderCard> getLeaderCards(){
        return leaderCards;
    }

    /**
     * The method removes the last four LeaderCards from the leaderCards list of the player and returning them.
     * @return a list of LeaderCards.
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public List<LeaderCard> removeAndReturnTheLastFourLeaderCards() throws IndexOutOfBoundsException {
        List<LeaderCard> leaderCardList = new ArrayList<>();
        for(int i=0; i<4; i++){
            leaderCardList.add(leaderCards.remove(leaderCards.size()-1));
        }
        return leaderCardList;
    }


    //ActionToken's methods

    /**
     * The method returns a Deque of ActionToken.
     * @return a Deque of ActionToken.
     */
    public Deque<ActionToken> getTokensDeque(){
        return actionTokensDeque;
    }

    /**
     * The method initialized the tokens and shuffles the Deque of ActionToken using a List as support.
     * @return true if execution is successful and does not modify the Deque.
     */
    private void initActionTokensDeque(){
        this.actionTokensDeque.add(new DiscardDevCard(DevCardColour.BLUE));
        this.actionTokensDeque.add(new DiscardDevCard(DevCardColour.YELLOW));
        this.actionTokensDeque.add(new DiscardDevCard(DevCardColour.GREEN));
        this.actionTokensDeque.add(new DiscardDevCard(DevCardColour.PURPLE));
        this.actionTokensDeque.add(new Move());
        this.actionTokensDeque.add(new MoveAndScrum());
        shuffleActionTokensDeque();
    }

    /**
     * The method shuffles the ActionToken's Deque using an ArrayList as support.
     * @return true if it is successful.
     */
    public boolean shuffleActionTokensDeque(){
        List<ActionToken> actionTokensList = new ArrayList<>(actionTokensDeque);
        Collections.shuffle(actionTokensList);
        for(int i=0; i<6; i++){
            actionTokensDeque.removeLast();
            actionTokensDeque.addFirst(actionTokensList.get(i));
        }
        return actionTokensDeque.size()==6;
    }

    /**
     * The method puts the first element of the deque at the end of it. Then it activates the last token's effect.
     * @return true if successful.
     */
    public boolean drawActionToken(){
        ActionToken tempActionToken = actionTokensDeque.pollFirst();
        actionTokensDeque.addLast(tempActionToken);
        actionTokensDeque.getLast().applyToken(lawrenceFaithPath, board, this);
        return true;
    }


    //Vatican Report management

    /**
     * The method makes the class Game listener of a player FaithPath.
     */
    public void makeGameListenerOfPlayerFaithPath(Player player){
        player.getFaithPath().events.subscribe(this);
    }

    /**
     * The method makes the class Game listener of Lawrence's FaithPath.
     */
    public void makeGameListenerOfLawrenceFaithPath(){
        lawrenceFaithPath.events.subscribe(this);
    }

    /**
     * The method creates a list of every FaithPath in the game: both of players and Lawrence The Magnificent.
     * @return a list of every FaithPath in the game: both of players and Lawrence The Magnificent.
     */
    private List<FaithPath> createFaithPathList(){
        List<FaithPath> faithPathList = new ArrayList<>();
        for(Player player : players){
            faithPathList.add(player.getFaithPath());
        }
        if(lawrenceFaithPath != null){
            faithPathList.add(lawrenceFaithPath);
        }
        return faithPathList;
    }

    /**
     * The method receives the cross position upgraded, verify if the position requires a Vatican Report and in case activates the Papal Cards of the players in the right range.
     */
    @Override
    public void checkForVaticanReport(int crossPosition){

        List<FaithPath> faithPathList = createFaithPathList();

        if(crossPosition>=8){
            if(isVaticanReportOne(faithPathList)){
                for(FaithPath faithPath : faithPathList){
                    if(faithPath.getCrossPosition()>=5 && faithPath.getCrossPosition()<=8){
                        faithPath.activatePapalCardOne();
                    }
                }
            }
        }

        if(crossPosition>=16){
            if(isVaticanReportTwo(faithPathList)){
                for(FaithPath faithPath : faithPathList){
                    if(faithPath.getCrossPosition()>=12 && faithPath.getCrossPosition()<=16){
                        faithPath.activatePapalCardTwo();
                    }
                }
            }
        }

        if(crossPosition>=24){
            if(isVaticanReportThree(faithPathList)){
                for(FaithPath faithPath : faithPathList){
                    if(faithPath.getCrossPosition()>=19){
                        faithPath.activatePapalCardThree();
                    }
                }
            }
        }

    }

    /**
     * The method verifies if the cross position requires a Vatican Report.
     * @param faithPathList is a list of every active FaithPath in the game.
     * @return In case the Vatican Report is required the method returns true, false otherwise.
     */
    private boolean isVaticanReportOne(List<FaithPath> faithPathList){
        for(FaithPath faithpath : faithPathList){
            if(faithpath.getPapalCardOne()){
                return false;
            }
        }
        return true;
    }

    /**
     * The method verifies if the cross position requires a Vatican Report.
     * @param faithPathList is a list of every active FaithPath in the game.
     * @return In case the Vatican Report is required the method returns true, false otherwise.
     */
    private boolean isVaticanReportTwo(List<FaithPath> faithPathList){
        for(FaithPath faithpath : faithPathList){
            if(faithpath.getPapalCardTwo()){
                return false;
            }
        }
        return true;
    }

    /**
     * The method verifies if the cross position requires a Vatican Report.
     * @param faithPathList is a list of every active FaithPath in the game.
     * @return In case the Vatican Report is required the method returns true, false otherwise.
     */
    private boolean isVaticanReportThree(List<FaithPath> faithPathList){
        for(FaithPath faithpath : faithPathList){
            if(faithpath.getPapalCardThree()){
                return false;
            }
        }
        return true;
    }


    /**
     * Resets the game instance. After this operations, all the game data is lost.
     */
    public static void resetInstance() {
        Game.instance = null;
    }
}