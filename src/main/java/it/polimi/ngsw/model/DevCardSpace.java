package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *Class that contains a matrix of mini deck of 4 cards, it has a specific level and colour
 */
public class DevCardSpace {
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
     * Method that return the number of cards remaining
     * @return
     */
    public int getNumberOfCards(){
        return numberOfCards;
    }

    /**
     * Method that remove the first card of the list
     */
    public void removeFirstCard(){
        this.developDeck.remove(0);
        this.numberOfCards--;
    }

    /**
     * Method that return the first card, if empty, return an empty card
     * @return
     */
    public DevCard firstDevCard(){
        if(this.developDeck.size()>0){
            return this.developDeck.get(0);
        }
        else {
            List<Resource> list = new ArrayList<>();
            list.add(Resource.EMPTY);
            return new DevCard(0, DevCardColour.EMPTY, list, null, 0);
        }
    }

    /**
     * Remove the devcard from a deck
     * @return
     */
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
