package it.polimi.ngsw.network.message;
import it.polimi.ngsw.model.DevCardColour;

/**
 * Message of the devard chosen to buy
 */
public class ChosenDevCardMessage extends Message {
    private int level;
    private int column;
    private int slotToPut;
    DevCardColour devCardColour;

    public ChosenDevCardMessage(String nickname, int level, int column, int slotToPut){
        super(nickname, MessageType.CHOSENDEVCARD);
        this.level = level;
        this.column = column;
        this.slotToPut = slotToPut;
        switch(column){
            case 1: {this.devCardColour = DevCardColour.GREEN;break;}
            case 2: {this.devCardColour = DevCardColour.BLUE;break;}
            case 3: {this.devCardColour = DevCardColour.YELLOW;break;}
            case 4: {this.devCardColour = DevCardColour.PURPLE;break;}
        }
    }

    public int getSlotToPut(){return this.slotToPut;}
    public int getLevel(){return this.level;}
    public int getColumn(){return this.column;}
    public DevCardColour getDevCardColour(){return this.devCardColour;}

    @Override
    public String toString() {
        return "Player " + this.getNickname() + " chose to buy devCard " + getLevel() + getDevCardColour();
    }

}
