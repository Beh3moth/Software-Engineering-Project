package it.polimi.ngsw.model;

/**
 * The FaithPath class is a representation of the faith trace.
 */
public class FaithPath {

    private int crossPosition;
    private int blackCrossPosition;

    /**
     *  Initialize crossPosition and blackCrossPosition at zero.
     */
    public FaithPath() {
        crossPosition = 0;
        blackCrossPosition = 0;
    }

    /**
     *  Increase crossPosition of one.
     */
    public void increaseCrossPosition() {
        crossPosition++;
    }

    /**
     *  Increase blackCrossPosition of one.
     */
    public void increaseBlackCrossPosition() {
        blackCrossPosition++;
    }

    /**
     * This method get the cross position.
     * @return the int value crossPosition.
     */
    public int getCrossPosition() {
        return crossPosition;
    }

    /**
     * his method get the black cross position.
     * @return the int value blackCrossPosition.
     */
    public int getBlackCrossPosition() {
        return blackCrossPosition;
    }

}