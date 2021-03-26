package it.polimi.ngsw.model;
//package Marble
//import enum delle marble color
//import le enum delle risorse
//import libreria player

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RedMarble extends Marble{
    private Resource typeResource;
    private MarbleColour marbleColour;

    public RedMarble(){
        this.typeResource = Resource.FAITHPOINT;
        this.marbleColour = MarbleColour.RED;
    }
    /**
     * Method that increase of a position the player cursor on the faith trace
     * @param player the player
     */
    @Override
    public void actionMarble(Player player){
        player.getFaithPath().increaseCrossPosition();  //bisogna aggiungere il metodo getTraceFait() ragaaaaaa
    }  //cambiare addposition in increase cross position etc

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