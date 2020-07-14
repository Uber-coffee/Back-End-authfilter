package com.ubercoffee.authstarter.token;

import java.util.List;

public interface TokenHandler {
    Long getId(String token) throws TokenException;
    List<String> getRoles(String token) throws TokenException;
}
