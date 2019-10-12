package com.nativeboys.eshop.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MessageModel {

    private String id;
    private String senderId;
    private String text;
    private String timestamp;
    private String type; // text or image

    public MessageModel() {
        // Required empty
    }

    public MessageModel(String id, String senderId, String text, String timestamp, String type) {
        this.id = id;
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
