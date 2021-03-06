package com.restful.message.broker.engine;
import com.restful.message.broker.exceptions.TopicDoseNotExistsException;
import com.restful.message.broker.model.Subscription;
import com.restful.message.broker.model.Topic;
import com.restful.message.broker.repositories.InMemoryRepository;
import com.restful.message.broker.repositories.SubscriptionMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class MessageBroker {

    Map<String, Topic> topics = new HashMap<>();

    SubscriptionMessageRepository subscriptionMessageRepository;

    public void publish(String topicName, String message) {
        Topic topic = topics.getOrDefault(topicName, new Topic(topicName, subscriptionMessageRepository));
        topic.getSubscriptionMessageRepository().addMessage(message, topicName);
    }

    public String listen(String clientId, String topicName) {
        Topic topic = topics.get(topicName);
        if (topic == null) throw new TopicDoseNotExistsException();
        Subscription subscription = new Subscription(clientId, topicName);
        return topic.getSubscriptionMessageRepository().getMessage(subscription);
    }

    public void subscribe(String clientId, String topicName) {
        Topic topic = topics.getOrDefault(topicName, new Topic(topicName, subscriptionMessageRepository));
        Subscription subscription = new Subscription(clientId, topicName);
        topic.getSubscriptionMessageRepository().addSubscription(subscription);
    }

    public void unsubscribe(String clientId, String topicName) {
        Topic topic = topics.get(topicName);
        if(topic != null) {
            Subscription subscription = new Subscription(clientId, topicName);
            topic.getSubscriptionMessageRepository().removeSubscription(subscription);
        }
    }
}