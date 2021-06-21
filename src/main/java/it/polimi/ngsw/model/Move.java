package it.polimi.ngsw.model;

/**
 * The move of the action token
 */
public class Move implements ActionToken {

    private final String ActionTokenName = "Lawrence The Magnificent proceeded in the Faith Path.";

    /**
     * The method activate an action token that increase Lawrence's cross position of two.
     * @param lawrenceFaithPath is the FaithPath of Lawrence The Magnific.
     * @param board unuseful.
     * @param game unuseful.
     */
    @Override
    public void applyToken(FaithPath lawrenceFaithPath, Board board, Game game) {
        for(int i = 0; i < 2; i++){
            lawrenceFaithPath.increaseCrossPosition();
        }
    }

    /**
     * Takes the name of the action token
     * @return
     */
    @Override
    public String getActionTokenName() {
        return ActionTokenName;
    }
}
