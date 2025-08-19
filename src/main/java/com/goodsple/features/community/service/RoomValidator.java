package com.goodsple.features.community.service;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RoomValidator {

    private static final Set<String> ALLOWED = Set.of("K-POP","MOVIE","GAME","ANIMATION");

    public void ensureValid(String roomId) {
        if(!ALLOWED.contains(roomId)) throw new IllegalArgumentException("Invalid roomId");
    }
}

