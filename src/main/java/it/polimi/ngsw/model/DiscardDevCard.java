package it.polimi.ngsw.model;

public class DiscardDevCard implements ActionToken {

    DevCardColour devCardColour;
    int devCardLevel;

    public DiscardDevCard(DevCardColour devCardColour, int devCardLevel){
        this.devCardColour = devCardColour;
        this.devCardLevel = devCardLevel;
    }

    @Override
    public void applyToken(Player player, Board board) {
        //Chiedere ad Aaron se è meglio fare il metodo qui o fare solo l'invocazione
    }
}
