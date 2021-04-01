package it.polimi.ngsw.model;

public class Move implements ActionToken {

    private final String ActionTokenName = "Move";

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

    @Override
    public String getActionTokenName() {
        return ActionTokenName;
    }
}
