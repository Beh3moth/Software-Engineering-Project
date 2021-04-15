package it.polimi.observer;

import it.polimi.network.message.Message;

/**
 * Observer interface. It supports a generic method of update.
 */
public interface Observer {
    void update(Message message);
}
