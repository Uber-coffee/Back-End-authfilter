package com.ubercoffee.authstarter.token;

public class TokenException extends Exception {
    public TokenException() {
        super("Token cannot be parsed");
    }

    public TokenException(String message) {
        super(message);
    }
}
