package it.polimi.ngsw.model;

public class YellowMarble extends Marble{
    private Resource typeResource;
    private MarbleColour marbleColour;

    public YellowMarble(){
        this.typeResource = Resource.MONEY;
        this.marbleColour = MarbleColour.YELLOW;
    }

    /**
     * Method that gives the player a money and put it into the stock
     * @param player the player
     */
    @Override
    public void actionMarble(Player player){
        player.getWarehouse().addResourceToStock(typeResource);
    };

    /**
     * Method that return the colour of the marble
     * @return the colour
     */
    public MarbleColour getColour(){
        return marbleColour;
    }

    /**
     * Method that return the resource type of the marble
     * @return the  resource type
     */
    public Resource getResource(){
        return typeResource;
    }
}