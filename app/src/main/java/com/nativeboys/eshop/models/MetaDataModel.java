package com.nativeboys.eshop.models;

import androidx.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MetaDataModel {

    private String id;
    private String conversationId;
    private MessageModel lastMessage;

    public MetaDataModel() {
        // Required Empty
    }

    public MetaDataModel(String id, String conversationId, MessageModel lastMessage) {
        this.id = id;
        this.conversationId = conversationId;
        this.lastMessage = lastMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public MessageModel getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageModel lastMessage) {
        this.lastMessage = lastMessage;
    }

    @NonNull
    @Override
    public String toString() {
        return "MetaDataModel{" +
                "id='" + id + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", lastMessage=" + lastMessage +
                '}';
    }
}
