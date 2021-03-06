package com.restful.message.broker.model;

import com.restful.message.broker.repositories.SubscriptionMessageRepository;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Topic {

    private String name;

    @Autowired
    SubscriptionMessageRepository subscriptionMessageRepository;

    public Topic(String name) {
        this.name=name;
    }
}
