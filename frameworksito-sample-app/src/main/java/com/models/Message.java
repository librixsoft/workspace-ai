package com.models;

public class Message {

    private String message;
    private String status;

    public Message() {}

    public Message(String message) {
        this.message = message;
        this.status = "ok";
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
