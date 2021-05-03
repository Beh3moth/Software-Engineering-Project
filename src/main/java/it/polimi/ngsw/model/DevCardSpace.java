package it.polimi.ngsw.model;

import java.util.Collections;
import java.util.List;

/**
 *Class that contains a matrix of mini deck of 4 cards, it has a specific level and colour
 */
public class DevCardSpace {
    private int deckLevel;
    private DevCardColour deckColour;
    private int numberOfCards;
    private List<DevCard> developDeck;

    public DevCardSpace(List<DevCard> developDeck){
        this.developDeck = developDeck;
        Collections.shuffle(this.developDeck);
        this.numberOfCards = this.developDeck.size();
    }

    public List<DevCard> getDevelopDeck(){
        return developDeck;
    }

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
    public void removeDevCardFromDevelopDeck(Player activePlayer, int slot){
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

    public int removeDevCardFromDevelopDeck(){
        int removedCards = 0;
        int numberOfCards = getNumberOfCards();
        for(int i = 0; i < numberOfCards-1 && removedCards<2; i++){
            removeFirstCard();
            removedCards++;
        }
        return removedCards;
    }

}
