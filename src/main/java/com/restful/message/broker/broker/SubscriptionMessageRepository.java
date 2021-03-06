package com.restful.message.broker.broker;
import com.restful.message.broker.model.Subscription;

public interface SubscriptionMessageRepository {
    public void addSubscription(Subscription subscription);
    public void removeSubscription(Subscription subscription);
    public String getMessage(Subscription subscription);
    public void addMessage(Subscription subscription, String message);
}
