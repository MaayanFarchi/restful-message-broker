package com.restful.message.broker.broker;
import com.restful.message.broker.model.Subscription;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockingQueueRepository implements SubscriptionMessageRepository {

    Map<Subscription, BlockingQueue<String>> subscribersMap;

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
        return subscribersMap.get(subscription).poll();
    }

    @Override
    public void addMessage(Subscription subscription, String message) {
        subscribersMap.get(subscription).add(message);
    }
}
