package it.polimi.ngsw.model;

public interface ActionToken {
    void applyToken(FaithPath lawrenceFaithPath, Board board, Game game);
    String getActionTokenName();
}
