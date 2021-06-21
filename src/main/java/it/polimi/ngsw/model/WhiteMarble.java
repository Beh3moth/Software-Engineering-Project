package it.polimi.ngsw.model;

public class WhiteMarble extends Marble{
    private Resource typeResource;
    private MarbleColour marbleColour;

    public WhiteMarble(){
        this.typeResource = Resource.EMPTY;
        this.marbleColour = MarbleColour.WHITE;
    }

    /**
     * Method that gives the player a the choice between two leader white marble, or just one, and than put the resources inside the warehouse's stock
     * @param player the player
     */
    @Override
    public void actionMarble(Player player){
        player.getWarehouse().addResourceToWhiteStock(typeResource);

    }

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