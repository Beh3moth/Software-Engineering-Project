package it.polimi.ngsw.model;
//import enum devcardcolour
//import player
import java.io.IOException; //uhmmmm
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
/**
 *Class that contains a matrix of mini deck of 4 cards, it has a specific level and colour
 */
public class DevCardSpace {
    //level of the deck, it can be 3 or 2 or 1 and it doesn't change in the game
    private int deckLevel;
    //colour of the deck, it doesn't change during the game
    private DevCardColour deckColour;
    //number of cards in that moment, it starts with 4
    private int numberOfCards;
    private List<DevCard> developDeck;

    public DevCardSpace(int level, DevCardColour colour){
        this.deckLevel = level;
        this.deckColour = colour;
        this.numberOfCards = 4;
        //  BISOGNO DI METODO CHE CREA LE CARTE, GLI PASSA VALORE COLORE ETC, SERVE PARSING !!!!!!!!!!
    }



    // (init card) !!!!!!!!!!!



    /**
     * Method that return the level of the Deck.
     * @return the level, value between 1 and 3.
     */
    public int getDeckLevel(){
        return deckLevel;
    }

    /**
     * Method that return the colour of the Deck.
     * @return the colour.
     */
    public DevCardColour getDeckColour(){
        return deckColour;
    }

    /**
     * Method that return the number of cards remaining
     * @return
     */
    public int getNumberOfCards(){
        return numberOfCards;
    }

    /**
     * Set the number of cards in a deck, it is used to remove one or two card at time
     * @param quantity of cards to remove in a deck
     */
    public void setNumberOfCards(int quantity){this.numberOfCards = this.numberOfCards - quantity;}

    /**
     * Method that permit to take a card and put it into the player dash board on a given slot
     * @param activePlayer the player
     * @param slot wich slot
     */
    public void removeDevCard(Player activePlayer, int slot){
        activePlayer.getDevCardDashboard().putDevCardIn(slot, developDeck.get(numberOfCards - 1) );
        this.numberOfCards--;
    }

    public void removeFirstCard(){
        this.developDeck.remove(0);
        this.numberOfCards--;
    }

    public DevCard firstDevCard(){
        return this.developDeck.get(0);
    }
}
