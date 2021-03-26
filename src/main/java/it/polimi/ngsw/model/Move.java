package it.polimi.ngsw.model;

public class Move implements ActionToken {

    @Override
    public void applyToken(Player player, Board board) {
        for(int i = 0; i < 2; i++){
            player.getFaithPath().increaseBlackCrossPosition();
        }
    }
}
