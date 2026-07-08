package com.yourorg.stackspike.greeting.internal;

import com.yourorg.stackspike.greeting.GreetingApi;
import org.springframework.stereotype.Service;

@Service
class GreetingService implements GreetingApi {
    public String greet(String name) {
        return "Hello, " + name + ", from a Spring Modulith module.";
    }
}