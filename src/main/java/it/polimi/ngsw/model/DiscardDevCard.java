package it.polimi.ngsw.model;

public class DiscardDevCard implements ActionToken {

    DevCardColour devCardColour;
    int devCardLevel;

    public DiscardDevCard(DevCardColour devCardColour, int devCardLevel){
        this.devCardColour = devCardColour;
        this.devCardLevel = devCardLevel;
    }

    /**
     * The method activates the effect of an action token by calling the Board's method removeDevCard().
     * @param lawrenceFaithPath unuseful.
     * @param board the common board.
     */
    @Override
    public void applyToken(FaithPath lawrenceFaithPath, Board board) {
        board.removeDevCard(devCardColour);
    }
}
