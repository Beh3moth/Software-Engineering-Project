package it.polimi.ngsw.model;

public class DiscardDevCard implements ActionToken {

    DevCardColour devCardColour;
    private final String ActionTokenName = "Lawrence The Magnificent discarded a Development Card";

    public DiscardDevCard(DevCardColour devCardColour){
        this.devCardColour = devCardColour;
    }

    /**
     * The method activates the effect of an action token by calling the Board's method removeDevCard().
     * @param lawrenceFaithPath unuseful.
     * @param board the common board.
     * @param game unuseful.
     */
    @Override
    public void applyToken(FaithPath lawrenceFaithPath, Board board, Game game) {
        board.removeTwoDevCard(devCardColour);
    }

    /**
     * Get the name of the token
     * @return the name
     */
    @Override
    public String getActionTokenName() {
        return ActionTokenName;
    }
}
