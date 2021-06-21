package it.polimi.ngsw.model;

public class MoveAndScrum implements ActionToken {

    private final String ActionTokenName = "Lawrence The Magnificent proceeded in the Faith Path and he shuffled the Action Tokens' deck";

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

    /**
     * Takes the name of the action token
     * @return the name
     */
    @Override
    public String getActionTokenName() {
        return ActionTokenName;
    }

}
