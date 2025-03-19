package com.example.cricketApp.Service;

import java.util.concurrent.TimeUnit;

public interface RedisService {

    void savePlayerStats(String key, Object value, long ttl, TimeUnit unit);

    Object getPlayerStats(String key);

    void deletePlayerStats(String key);
}