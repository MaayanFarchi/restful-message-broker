package com.restful.message.broker.model;

import com.restful.message.broker.repositories.SubscriptionMessageRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Topic {

    private String name = "";

    @Autowired
    SubscriptionMessageRepository subscriptionMessageRepository;
}
