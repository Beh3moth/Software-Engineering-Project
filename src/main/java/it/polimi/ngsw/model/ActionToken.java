package it.polimi.ngsw.model;


/**
 * The interface of the token of the solo game
 */
public interface ActionToken {
    void applyToken(FaithPath lawrenceFaithPath, Board board, Game game);
    String getActionTokenName();
}
