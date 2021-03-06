package com.restful.message.broker.broker;
import com.restful.message.broker.model.Subscription;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MessageBroker {

    List<Topic> topics;

    public void subscribe(String clientId, String topicName) {
        Optional<Topic> optionalTopic = topics
                .stream()
                .filter(t -> t.getName().equals(topicName)).findFirst();
        Topic topic = optionalTopic.orElse(new Topic(topicName));
        topic.getSubscriptionMessageRepository().addSubscription(new Subscription(clientId, topicName));
    }
}