package com.restful.message.broker.engine;
import com.restful.message.broker.exceptions.TopicDoseNotExistsException;
import com.restful.message.broker.model.Subscription;
import com.restful.message.broker.model.Topic;
import com.restful.message.broker.repositories.InMemoryRepository;
import com.restful.message.broker.repositories.SubscriptionMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MessageBroker {

    Map<String, Topic> topics = new HashMap<>();

    @Autowired
    SubscriptionMessageRepository subscriptionMessageRepository;

    public void publish(String topicName, String message) {
        Topic topic = topics.getOrDefault(topicName, new Topic(topicName, subscriptionMessageRepository));
        topic.addMessage(message, topicName);
    }

    public String listen(String clientId, String topicName) {
        Topic topic = topics.get(topicName);
        if (topic == null) throw new TopicDoseNotExistsException("topic dose not exists: " + topicName);
        Subscription subscription = new Subscription(clientId, topicName);
        return topic.getMessage(subscription);
    }

    public void subscribe(String clientId, String topicName) {
        Topic topic = topics.getOrDefault(topicName, new Topic(topicName, subscriptionMessageRepository));
        topics.putIfAbsent(topicName, topic);
        Subscription subscription = new Subscription(clientId, topicName);
        topic.addSubscription(subscription);
    }

    public void unsubscribe(String clientId, String topicName) {
        Topic topic = topics.get(topicName);
        if(topic != null) {
            Subscription subscription = new Subscription(clientId, topicName);
            topic.removeSubscription(subscription);
        }
    }
}