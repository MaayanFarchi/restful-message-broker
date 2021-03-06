package com.restful.message.broker.engine;
import com.restful.message.broker.exceptions.TopicDoseNotExistsException;
import com.restful.message.broker.model.Subscription;
import com.restful.message.broker.model.Topic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MessageBroker {

    List<Topic> topics;

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
        return optionalTopic.orElse(new Topic(topicName));
    }
}