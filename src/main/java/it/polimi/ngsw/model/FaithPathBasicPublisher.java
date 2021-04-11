package it.polimi.ngsw.model;

import java.util.ArrayList;
import java.util.List;

public class FaithPathBasicPublisher {

    private List<FaithPathListener> listeners;

    public FaithPathBasicPublisher(){
        this.listeners = new ArrayList<>();
    }

    public void subscribe(FaithPathListener faithPathListener){
        listeners.add(faithPathListener);
    }

    public void unsubscribe(FaithPathListener faithPathListener){
        listeners.remove(faithPathListener);
    }

    public void notify(int crossPosition){
        for(FaithPathListener faithPathListener : listeners){
            faithPathListener.update(crossPosition);
        }
    }

}
