package com.restful.message.broker.broker;

import com.restful.message.broker.model.Subscription;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Getter
@Setter
public class Topic {

    private String name;

    @Autowired
    SubscriptionMessageRepository subscriptionMessageRepository;

    public Topic(String name) {
        this.name=name;
    }
}
