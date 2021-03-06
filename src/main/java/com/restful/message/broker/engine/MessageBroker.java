package com.restful.message.broker.engine;
import com.restful.message.broker.exceptions.TopicDoseNotExistsException;
import com.restful.message.broker.model.Subscription;
import com.restful.message.broker.model.Topic;
import com.restful.message.broker.repositories.SubscriptionMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component

public class MessageBroker {

    List<Topic> topics = new ArrayList<>();

    @Autowired
    SubscriptionMessageRepository subscriptionMessageRepository;

    public void publish(String topicName, String message) {
        Topic topic = obtainTopic(topicName);
        topic.getSubscriptionMessageRepository().addMessage(message);
    }

    public String listen(String clientId, String topicName) {
        Optional<Topic> optionalTopic = topics
                .stream()
                .filter(t -> t.getName().equals(topicName)).findFirst();
        if (!optionalTopic.isPresent()) throw new TopicDoseNotExistsException();
        Subscription subscription = new Subscription(clientId, topicName);
        return optionalTopic.get().getSubscriptionMessageRepository().getMessage(subscription);
    }

    public void subscribe(String clientId, String topicName) {
        Topic topic = obtainTopic(topicName);
        Subscription subscription = new Subscription(clientId, topicName);
        topic.getSubscriptionMessageRepository().addSubscription(subscription);
    }

    public void unsubscribe(String clientId, String topicName) {
        Optional<Topic> optionalTopic = topics
                .stream()
                .filter(t -> t.getName().equals(topicName)).findFirst();
        Subscription subscription = new Subscription(clientId, topicName);
        optionalTopic.ifPresent(t -> t.getSubscriptionMessageRepository().removeSubscription(subscription));
    }

    private Topic obtainTopic(String topicName) {
        Optional<Topic> optionalTopic = topics
                .stream()
                .filter(t -> t.getName().equals(topicName)).findFirst();
        Topic topic = optionalTopic.orElse(new Topic(topicName, subscriptionMessageRepository));
        topics.add(topic);
        return topic;
    }
}