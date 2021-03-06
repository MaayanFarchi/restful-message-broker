package com.restful.message.broker.repositories;
import com.restful.message.broker.model.Subscription;

public interface SubscriptionMessageRepository {
    public void addSubscription(Subscription subscription);
    public void removeSubscription(Subscription subscription);
    public String getMessage(Subscription subscription);
    public void addMessage(String message);
}
