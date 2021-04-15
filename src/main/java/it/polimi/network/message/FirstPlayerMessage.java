package it.polimi.network.message;
import java.util.List;
public class FirstPlayerMessage extends Message{

    private final List<String> activePlayers;
    private final String activePlayerNickname;
    public FirstPlayerMessage(String senderNickname, MessageType messageType, List<String> activePlayers, String activePlayerNickname) {
        super(senderNickname, messageType);
        this.activePlayers = activePlayers;
        this.activePlayerNickname = activePlayerNickname;
    }

    public List<String> getActivePlayers() {
        return activePlayers;
    }

    public String getActivePlayerNickname() {
        return activePlayerNickname;
    }

    @Override
    public String toString() {
        return "FirstPlayerMessage{" +
                "nickname=" + getNickname() +
                ", activePlayers=" + activePlayers +
                '}';
    }
}
