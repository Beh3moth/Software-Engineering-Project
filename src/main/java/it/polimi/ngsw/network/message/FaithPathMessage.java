package it.polimi.ngsw.network.message;

public class FaithPathMessage extends Message {

    private int crossPosition;
    private int victoryPoints;
    private boolean papalCardOne;
    private boolean papalCardTwo;
    private boolean papalCardThree;

    public FaithPathMessage( String nickname, int crossPosition, int victoryPoints, boolean papalCardOne, boolean papalCardTwo, boolean papalCardThree ) {
        super(nickname, MessageType.FAITH_PATH_MESSAGE);
        this.crossPosition = crossPosition;
        this.victoryPoints = victoryPoints;
        this.papalCardOne = papalCardOne;
        this.papalCardTwo = papalCardTwo;
        this.papalCardThree = papalCardThree;
    }

    public int getCrossPosition() {
        return crossPosition;
    }

    public int getVictoryPoints(){
        return victoryPoints;
    }

    public boolean isPapalCardOneActive(){
        return papalCardOne;
    }

    public boolean isPapalCardTwoActive(){
        return papalCardTwo;
    }

    public boolean isPapalCardThreeActive(){
        return papalCardThree;
    }

    @Override
    public String toString() {
        return "return the gaithpath of the player" + getNickname();
    }

}
