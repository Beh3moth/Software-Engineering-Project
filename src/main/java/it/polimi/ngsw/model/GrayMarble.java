package it.polimi.ngsw.model;
//package Marble
//import enum delle marble color
//import le enum delle risorse
//import libreria player

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GrayMarble extends Marble{
    private Resource typeResource;
    private MarbleColour marbleColour;

    public GrayMarble(){
        this.typeResource = Resource.STONE;
        this.marbleColour = MarbleColour.GRAY;
    }

    //IDEA, butto tutto sullo stock magazzino,
    // che avrà al massimo quattro elementi alla volta, finito di prendere col ciclo for gli elementi, sempre dentro al metodo chiamo 
    //un metodo gestiscimagazzino() che prima mi fa vedere situazione magazzino e stock, e per ogni elemento dello stock decido se scartare o fare cosa

    /**
     * Method that gives the player a stone and put it into the stock
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