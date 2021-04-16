package it.polimi.network.message;

import it.polimi.ngsw.model.Game;
import it.polimi.ngsw.model.LeaderCard;

import java.util.List;

/**
 * Message which contains the 4 leaderCard available.
 */
public class LeaderCardListMessage extends Message {

    private static final long serialVersionUID = -1116045089001448271L;
    private final List<LeaderCard> leaderCardList;

    /**
     * Default constructor.
     *
     * @param nickname the nickname of the player.
     * @param leaderCardList  the list of leaderCard to be sent.
     */
    public LeaderCardListMessage(String nickname, List<LeaderCard> leaderCardList) {
        super(nickname, MessageType.LEADERCARDREQUEST);
        this.leaderCardList = leaderCardList;
    }

    public List<LeaderCard> getLeaderCardList() {
        return leaderCardList;
    }


    @Override
    public String toString() {
        return "LeaderCardList{" +
                "nickname=" + getNickname() +
                ", godList=" + leaderCardList +
                '}';
    }
}
