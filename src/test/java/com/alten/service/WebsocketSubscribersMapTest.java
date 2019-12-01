package com.alten.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

public class WebsocketSubscribersMapTest {

    WebsocketSubscribersMap websocketSubscribersMap;

    @BeforeEach
    public void init() {
        websocketSubscribersMap = new WebsocketSubscribersMap();
    }

    @Test
    public void testSubscriptionList() {
        Map<String,?> subscriptions = websocketSubscribersMap.getSubscriptions();
        websocketSubscribersMap.subscribe("123", "");
        assertTrue(subscriptions.containsKey("123"));
        websocketSubscribersMap.unsubscribe("123");
        assertFalse(subscriptions.containsKey("123"));
    }
}

