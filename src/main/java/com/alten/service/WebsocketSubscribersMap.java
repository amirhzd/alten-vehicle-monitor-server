package com.alten.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic subscription map for web-socket channel
 * => This class must always remain singleton.
 * @param <T>
 *
 * @author amir
 */
@Component
public class WebsocketSubscribersMap<T> {

    private Map<String, T> subscriptions = new HashMap<>();

    public void subscribe(String sessionId, T filter) {
        subscriptions.put(sessionId, filter);
    }

    public boolean unsubscribe(String sessionId) {
        return subscriptions.remove(sessionId) != null;
    }

    protected Map<String, T> getSubscriptions() {
        return subscriptions;
    }
}
