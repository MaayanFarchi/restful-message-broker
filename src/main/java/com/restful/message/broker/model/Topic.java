package com.restful.message.broker.model;

import com.restful.message.broker.repositories.SubscriptionMessageRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Topic {

    private String name;

    SubscriptionMessageRepository subscriptionMessageRepository;

    public void addSubscription(Subscription subscription) { subscriptionMessageRepository.addSubscription(subscription);}

    public void removeSubscription(Subscription subscription) { subscriptionMessageRepository.removeSubscription(subscription);}

    public String getMessage(Subscription subscription) {return subscriptionMessageRepository.getMessage(subscription);}

    public void addMessage(String message, String topicName) {subscriptionMessageRepository.addMessage(message, topicName);}
}
