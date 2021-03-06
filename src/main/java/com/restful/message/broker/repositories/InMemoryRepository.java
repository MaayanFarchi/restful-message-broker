package com.restful.message.broker.repositories;
import com.restful.message.broker.exceptions.NotSubscriptionException;
import com.restful.message.broker.model.Subscription;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class InMemoryRepository implements SubscriptionMessageRepository {

    Map<Subscription, BlockingQueue<String>> subscribersMap = new HashMap<>();

    @Override
    public void addSubscription(Subscription subscription) {
        BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();
        subscribersMap.putIfAbsent(subscription, blockingQueue);
    }

    @Override
    public void removeSubscription(Subscription subscription) {
        subscribersMap.remove(subscription);
    }

    @Override
    public String getMessage(Subscription subscription) {
        if(!subscribersMap.containsKey(subscription)) throw new NotSubscriptionException();
        return subscribersMap.get(subscription).poll();
    }

    @Override
    public void addMessage(String message) {
        subscribersMap.forEach((key, value) -> value.add(message));
    }
}
