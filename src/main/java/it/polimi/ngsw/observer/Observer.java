package it.polimi.ngsw.observer;

import it.polimi.ngsw.network.message.Message;

/**
 * Observer interface. It supports a generic method of update.
 */
public interface Observer {
    void update(Message message);
}
