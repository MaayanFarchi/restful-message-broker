package com.restful.message.broker.controllers;

import com.restful.message.broker.engine.MessageBroker;
import com.restful.message.broker.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
public class MessageBrokerController {

    @Autowired
    MessageBroker messageBroker;

    @Value("${max.poll.wait.time:5}")
    Long maxPollWaitTime;

    @PostMapping("publish/{topicId}")
    public void publish(@PathVariable String topicId, @RequestBody Message message) {
        messageBroker.publish(topicId, message.getPayload());
    }

    @GetMapping("subscribe/{topicId}")
    public void subscribe(@RequestHeader("client-id") String clientId, @PathVariable String topicId) {
        messageBroker.subscribe(clientId, topicId);
    }

    @GetMapping("consumer/{topicId}")
    public DeferredResult<String> listen(@RequestHeader("client-id") String clientId, @PathVariable String topicId) {
        Long timeOutInMilliSec = 1000000L;
        String timeOutResp = "Time Out!";
        DeferredResult<String> deferredResult = new DeferredResult<>(timeOutInMilliSec,timeOutResp);
        CompletableFuture.runAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(maxPollWaitTime);
                String msg = messageBroker.listen(clientId,topicId);
                deferredResult.setResult(msg);
            }catch (Exception ex){
            }

        });
        return deferredResult;
    }

    @DeleteMapping("subscribe/{topicId}")
    public void unsubscribe(@RequestHeader("client-id") String clientId, @PathVariable String topicId) {
        messageBroker.unsubscribe(clientId, topicId);
    }
}
