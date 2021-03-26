package it.polimi.ngsw.model;

public class MoveAndScrum implements ActionToken {
    @Override
    public void applyToken(Player player, Board board) {
        player.getFaithPath().increaseBlackCrossPosition();
        //Chiama un metodo (che non so ancora bene dove sarà) che mischia i token.
    }
}
