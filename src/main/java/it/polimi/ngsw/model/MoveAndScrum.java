package it.polimi.ngsw.model;

public class MoveAndScrum implements ActionToken {

    private final String ActionTokenName = "MoveAndScrum";

    /**
     * The method increases the Lawrence's cross position of a single unit.
     * @param lawrenceFaithPath Lawrence's faith path.
     * @param board unuseful.
     * @param game is the instance of Game where the ActionTokens are.
     */
    @Override
    public void applyToken(FaithPath lawrenceFaithPath, Board board, Game game) {
        lawrenceFaithPath.increaseCrossPosition();
        game.shuffleActionTokensDeque();
    }

    @Override
    public String getActionTokenName() {
        return ActionTokenName;
    }

}
