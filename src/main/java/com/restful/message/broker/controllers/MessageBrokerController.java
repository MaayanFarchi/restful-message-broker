package com.restful.message.broker.controllers;

import com.restful.message.broker.broker.MessageBroker;
import com.restful.message.broker.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageBrokerController {

    @Autowired
    MessageBroker messageBroker;

    @PostMapping("/publish/{topicId}")
    public void publish(@RequestBody Message message, @PathVariable String topicId) {
        System.out.println("Sending a message.");
    }

    @GetMapping("/subscribe/{topicId}")
    public void subscribe(@PathVariable String topicId) {
        //TODO
    }

    @DeleteMapping("/subscribe/{topicId}")
    public void listen(@PathVariable String topicId) {
        //TODO
    }

    @DeleteMapping("/subscribe/{topicId}")
    public void unsubscribe(@PathVariable String topicId) {
        //TODO
    }
}
