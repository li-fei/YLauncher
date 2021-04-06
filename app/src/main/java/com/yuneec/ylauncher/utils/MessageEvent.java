package com.yuneec.ylauncher.utils;

public class MessageEvent {
    public static int AppDetail = 1;
    private String message;
    private int FLAG;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(int flag) {
        this.FLAG = flag;
    }

    public MessageEvent(String message, int flag) {
        this.message = message;
        this.FLAG = flag;
    }

    public int getFLAG() {
        return FLAG;
    }

    public void setFLAG(int FLAG) {
        this.FLAG = FLAG;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

