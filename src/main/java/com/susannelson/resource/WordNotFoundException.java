package com.susannelson.resource;

public class WordNotFoundException extends RuntimeException {

    public WordNotFoundException(String message) {
        super(message);
    }
}
