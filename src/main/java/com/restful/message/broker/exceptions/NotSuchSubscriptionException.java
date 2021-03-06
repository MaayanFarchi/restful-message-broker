package com.restful.message.broker.exceptions;

public class NotSuchSubscriptionException extends RuntimeException {

    public NotSuchSubscriptionException(String msg) { super(msg); }
}
