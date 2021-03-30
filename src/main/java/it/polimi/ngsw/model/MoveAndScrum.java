package it.polimi.ngsw.model;

public class MoveAndScrum implements ActionToken {

    /**
     * The method increases the Lawrence's cross position of a single unit.
     * @param lawrenceFaithPath Lawrence's faith path.
     * @param board unuseful.
     */
    @Override
    public void applyToken(FaithPath lawrenceFaithPath, Board board) {
        lawrenceFaithPath.increaseCrossPosition();
        //Chiama un metodo (che non so ancora bene dove sarà) che mischia i token.
    }
}
