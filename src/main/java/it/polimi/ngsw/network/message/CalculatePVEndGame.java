package it.polimi.ngsw.network.message;

/**
 * Method that calculate the PV after ending the game
 */
public class CalculatePVEndGame extends Message {
    public CalculatePVEndGame(String nickname) {
        super(nickname, MessageType.CALCULATE_PV_WIN);
    }

    @Override
    public String toString() {
        return "Calculate PV of players";
    }
}
