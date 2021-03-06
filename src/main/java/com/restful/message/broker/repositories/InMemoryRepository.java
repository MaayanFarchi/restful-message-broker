package com.restful.message.broker.repositories;
import com.restful.message.broker.exceptions.InterruptedRuntimeException;
import com.restful.message.broker.exceptions.NotSubscriptionException;
import com.restful.message.broker.model.Subscription;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Component
public class InMemoryRepository implements SubscriptionMessageRepository {

    Map<Subscription, BlockingQueue<String>> subscribersMap = new HashMap<>();

    @Value("${max.poll.wait.time:5}")
    Long maxPollWaitTime;

    @Override
    @Synchronized
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
        if(!subscribersMap.containsKey(subscription)) throw new NotSubscriptionException("following subscription dose not exists: " + subscription);
        try {
            return subscribersMap.get(subscription).poll(maxPollWaitTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new InterruptedRuntimeException(e.getMessage());
        }
    }

    @Override
    @Synchronized
    public void addMessage(String message, String topicName) {
        subscribersMap.forEach((key, value) -> {
            if(key.getTopic().equals(topicName)) value.add(message);
        });
    }
}
