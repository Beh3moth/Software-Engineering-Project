package it.polimi.ngsw.model;
//package Marble
//import enum delle marble color
//import le enum delle risorse
//import libreria player

import java.io.IOException; //uhmmmm
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WhiteMarble extends Marble{
    private Resource typeResource;
    private MarbleColour marbleColour;

    public WhiteMarble(){
        this.typeResource = Resource.EMPTY;
        this.marbleColour = MarbleColour.WHITE;
    }


    //IDEA, butto tutto sullo stock magazzino,
    // che avrà al massimo quattro elementi alla volta, finito di prendere col ciclo for gli elementi, sempre dentro al metodo chiamo 
    //un metodo gestiscimagazzino() che prima mi fa vedere situazione magazzino e stock, e per ogni elemento dello stock decido se scartare o fare cosa

    /**
     * Method that gives the player a the choice between two leader white marble, or just one, and than put the resources inside the warehouse's stock
     * @param player the player
     */
    @Override
    public void actionMarble(Player player){        //quando la chiamo devo controllare se ho nella player la whitemarble leader abilitata, se no chiamo la classica che non fa nulla
        Resource firstResource = player.getWhiteMarblePowerOne();    //aggiungere questi due metodi, controllare che su player sono iniziailizzati con NULL
        Resource secondResource = player.getWhiteMarblePowerTwo();
        if(firstResource != Resource.EMPTY && secondResource == Resource.EMPTY){
            typeResource = firstResource;
            player.getWarehouse().addResourceToStock(typeResource);
        }else if (firstResource != Resource.EMPTY && secondResource != Resource.EMPTY){
            //potrei fare metodo askPlayer() dalla view che mi ritorna quale delle due, intanto lo implemento qua, al massimo lo sposto
            System.out.println("Choose between this two resources: " + firstResource + ' ' + secondResource);
            Scanner scan = new Scanner(System.in);
            int i = scan.nextInt();
            if(i == 1){
                typeResource = firstResource;
                player.getWarehouse().addResourceToStock(typeResource);
            }else if (i == 2){
                typeResource = secondResource;
                player.getWarehouse().addResourceToStock(typeResource);
            }
        }
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