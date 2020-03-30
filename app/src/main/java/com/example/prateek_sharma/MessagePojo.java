package com.example.prateek_sharma;

public class MessagePojo {
    private String sender;
    private String message;
    private long time;

    public MessagePojo(){}

    public MessagePojo(String sender, String message,long time) {
        this.sender = sender;
        this.message = message;
        this.time=time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
