package it.polimi.ngsw.model;

import java.io.Serializable;


/**
 * The abstract class of the marbles
 */
public abstract class Marble implements Serializable {

    private static final long serialVersionUID = -9110488227991188648L;
    private Resource typeResource;
    private MarbleColour marblecolour;

    public void actionMarble(Player player){
    }

    public  Resource getResource(){return typeResource;};

    public MarbleColour getColour(){return marblecolour;}
}