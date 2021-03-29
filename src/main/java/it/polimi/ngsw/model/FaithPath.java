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
     * This method get the black cross position.
     * @return the int value blackCrossPosition.
     */
    public int getBlackCrossPosition() {
        return blackCrossPosition;
    }


    /**
     * The method returns the faith path's victory points of a player. Papal Cards' victory points not included.
     * @return an int of victory points based on the player's position. Papal Cards' victory points are excluded.
     */
    public int getPV(){
        int PV = 0;
        if(crossPosition >= 3){
            PV += 1;
        }
        if(crossPosition >= 6){
            PV += 2;
        }
        if(crossPosition >= 9){
            PV += 4;
        }
        if(crossPosition >= 12){
            PV += 6;
        }
        if(crossPosition >= 15){
            PV += 9;
        }
        if(crossPosition >= 18){
            PV += 12;
        }
        if(crossPosition >= 21){
            PV += 16;
        }
        if(crossPosition >= 24){
            PV += 20;
        }
        return PV;
    }

}