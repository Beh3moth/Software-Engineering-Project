package it.polimi.ngsw.network.message;

/**
 * Message of the end of the game in single player
 */
public class EndGameSinglePlayerMessage extends Message {

    private int playerVictoryPoints;
    private int lawrenceCrossPosition;
    private boolean winner;

    public EndGameSinglePlayerMessage(String nickname, int playerVictoryPoints, int lawrenceCrossPosition, boolean winner) {
        super(nickname, MessageType.END_GAME_SINGLE_PLAYER);
        this.playerVictoryPoints = playerVictoryPoints;
        this.lawrenceCrossPosition = lawrenceCrossPosition;
        this.winner = winner;
    }


    public int getPlayerVictoryPoints() {
        return playerVictoryPoints;
    }

    public int getLawrenceCrossPosition() {
        return lawrenceCrossPosition;
    }

    public boolean isWinner() {
        return winner;
    }
    @Override
    public String toString() {
        return "The game is ended";
    }
}
