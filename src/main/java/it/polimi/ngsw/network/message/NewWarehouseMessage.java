package it.polimi.ngsw.network.message;

import it.polimi.ngsw.model.Resource;

import java.util.List;

public class NewWarehouseMessage extends Message{
    private Resource newFirstShelf;
    private List<Resource> newSecondShelf;
    private List<Resource> newThirdShelf;
    private List<Resource> newFirstSpecialShelf;
    private List<Resource> newSecondSpecialShelf;
    private List<Resource> discardList;
    public NewWarehouseMessage(String nickname, Resource newFirstShelf, List<Resource> newSecondShelf, List<Resource> newThirdShelf, List<Resource> newFirstSpecialShelf, List<Resource> newSecondSpecialShelf, List<Resource> discardList){
        super(nickname, MessageType.NEW_WAREHOUSE);
        this.newFirstShelf = newFirstShelf;
        this.newSecondShelf = newSecondShelf;
        this.newThirdShelf = newThirdShelf;
        this.newFirstSpecialShelf = newFirstSpecialShelf;
        this.newSecondSpecialShelf = newSecondSpecialShelf;
        this.discardList = discardList;
    }

    public Resource getNewFirstShelf() {
        return newFirstShelf;
    }

    public List<Resource> getDiscardList() {
        return discardList;
    }

    public List<Resource> getNewFirstSpecialShelf() {
        return newFirstSpecialShelf;
    }

    public List<Resource> getNewSecondSpecialShelf() {
        return newSecondSpecialShelf;
    }

    public List<Resource> getNewSecondShelf() {
        return newSecondShelf;
    }

    public List<Resource> getNewThirdShelf() {
        return newThirdShelf;
    }

    @Override
    public String toString() {
        return "New Warehouse{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                '}';
    }
}
