package it.polimi.ngsw.network.message;

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
}
