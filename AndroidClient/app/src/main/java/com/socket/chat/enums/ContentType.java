package com.socket.chat.enums;

public enum ContentType {
    MESSAGE("MESSAGE"),
    VOICE("VOICE"),
    FILE("FILE"),
    VIDEO("VIDEO");

    private String type;
    ContentType(String type) {
        this.type = type;
    }
}
