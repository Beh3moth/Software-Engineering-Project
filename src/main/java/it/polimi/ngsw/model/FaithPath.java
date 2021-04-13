package it.polimi.ngsw.model;

/**
 * The FaithPath class is a representation of the faith trace.
 */
public class FaithPath {

    private int crossPosition;
    public FaithPathBasicPublisher events;
    private boolean papalCardOne;
    private boolean papalCardTwo;
    private boolean papalCardThree;

    /**
     *  Initialize crossPosition and blackCrossPosition at zero.
     */
    public FaithPath() {
        crossPosition = 0;
        this.events = new FaithPathBasicPublisher();
        papalCardOne = false;
        papalCardTwo = false;
        papalCardThree = false;
    }

    /**
     *  Increase crossPosition of one.
     */
    public void increaseCrossPosition() {
        crossPosition++;
        events.notify(crossPosition);
    }

    /**
     * This method get the cross position.
     * @return the int value crossPosition.
     */
    public int getCrossPosition() {
        return crossPosition;
    }

    /**
     * The method returns the faith path's victory points of a player. Papal Cards' victory points are included.
     * @return an int of victory points based on the player's position.
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
        if(papalCardOne){
            PV += 2;
        }
        if (papalCardTwo) {
            PV += 3;
        }
        if (papalCardThree) {
            PV += 4;
        }
        return PV;
    }

    /**
     * The method activates the PapalCardOne.
     */
    public void activatePapalCardOne(){
        papalCardOne = true;
    }

    /**
     * The method activates the PapalCardTwo.
     */
    public void activatePapalCardTwo(){
        papalCardTwo = true;
    }

    /**
     * The method activates the PapalCardThree.
     */
    public void activatePapalCardThree(){
        papalCardThree = true;
    }

    /**
     * The method returns the value of Papal Cards One.
     * @return true if the card is activated, false otherwise.
     */
    public boolean getPapalCardOne(){
        return papalCardOne;
    }

    /**
     * The method returns the value of Papal Cards Two.
     * @return true if the card is activated, false otherwise.
     */
    public boolean getPapalCardTwo(){
        return papalCardTwo;
    }

    /**
     * The method returns the value of Papal Cards Three.
     * @return true if the card is activated, false otherwise.
     */
    public boolean getPapalCardThree(){
        return papalCardThree;
    }

}