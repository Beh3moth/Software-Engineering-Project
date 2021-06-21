package it.polimi.ngsw.network.message;

/**
 * Message that send a request of nuying resources from the market
 */
public class BuyFromMarketMessage extends Message {
    private static final long serialVersionUID = -3468770065853971221L;
    private int rowOrColumn;
    private int wichOne;

    public BuyFromMarketMessage(String nickname, int rowOrColumn, int wichOne) {
        super(nickname, MessageType.BUY_MARKET);
        this.rowOrColumn = rowOrColumn;
        this.wichOne = wichOne;
    }

    public int getRowOrColumn(){
        return this.rowOrColumn;
    }
    public int getWichOne(){
        return this.wichOne;
    }
    @Override
    public String toString() {
        return "Player " + this.getNickname() + " chose to buy from the market " + getWichOne();
    }
}
