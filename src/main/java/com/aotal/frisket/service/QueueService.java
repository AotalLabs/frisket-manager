package com.aotal.frisket.service;

/**
 * Created by allan on 13/01/17.
 */
public interface QueueService {
    /**
     * Puts the given string into the queue
     *
     * @param message
     */
    void putMessage(String message);
}
