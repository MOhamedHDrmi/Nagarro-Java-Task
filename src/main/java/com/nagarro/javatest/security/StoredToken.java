package com.nagarro.javatest.security;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class StoredToken {

    private static ConcurrentHashMap<String, String> usersTokens = new ConcurrentHashMap<>();

    public void putToken(String key, String value) {
        usersTokens.put(key, value);
    }

    public void removeTokenByKey(String key) {
        usersTokens.remove(key);
    }

    public String getTokenByUserName(String key) {
        return usersTokens.get(key);
    }

    public boolean checkExistsUser(String key) {
		return usersTokens.containsKey(key);
    }
}