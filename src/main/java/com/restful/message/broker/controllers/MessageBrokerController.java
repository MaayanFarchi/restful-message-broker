package com.restful.message.broker.controllers;

import com.restful.message.broker.engine.MessageBroker;
import com.restful.message.broker.exceptions.NotSuchSubscriptionException;
import com.restful.message.broker.exceptions.TopicDoseNotExistsException;
import com.restful.message.broker.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

@RestController
public class MessageBrokerController {

    @Autowired
    MessageBroker messageBroker;

    @Value("${consumer.poll.wait.max.ms:10000}")
    Long maxPollWaitTimeMilliSec;

    @PostMapping("publish/{topicId}")
    public void publish(@PathVariable String topicId, @RequestBody Message message) {
        messageBroker.publish(topicId, message.getPayload());
    }

    @GetMapping("subscribe/{topicId}")
    public void subscribe(@RequestHeader("client-id") String clientId, @PathVariable String topicId) {
        messageBroker.subscribe(clientId, topicId);
    }

    @GetMapping("consumer/{topicId}")
    public DeferredResult<ResponseEntity<String>> listen(@RequestHeader("client-id") String clientId, @PathVariable String topicId) {
        String timeOutResp = "Time Out!";
        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>(maxPollWaitTimeMilliSec, timeOutResp);
        deferredResult.onTimeout(() ->
                deferredResult.setErrorResult(
                        ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                                .body("Request timeout occurred.")));

        CompletableFuture.runAsync(() -> {
            try {
                String msg = messageBroker.listen(clientId, topicId);
                if (msg == null) throw new RuntimeException();
                deferredResult.setResult(ResponseEntity.status(HttpStatus.OK).body("message from topic: " + msg));
            } catch (Exception ex) {
                deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.NO_CONTENT).body("no messages."));
            }

        });
        return deferredResult;
    }

    @DeleteMapping("subscribe/{topicId}")
    public void unsubscribe(@RequestHeader("client-id") String clientId, @PathVariable String topicId) {
        messageBroker.unsubscribe(clientId, topicId);
    }

    @ExceptionHandler({NotSuchSubscriptionException.class})
    public ResponseEntity<String> handleNotSubscriptionException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No subscription, subscribe to topic.");
    }

    @ExceptionHandler({TopicDoseNotExistsException.class})
    public ResponseEntity<String> handleTopicDoseNotExistsException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Topic dose not exists, subscribe to create topic.");
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<String> handleGeneralException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("General exception occurred.");
    }
}

