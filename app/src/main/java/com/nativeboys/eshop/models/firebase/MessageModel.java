package com.nativeboys.eshop.models.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Locale;

@IgnoreExtraProperties
public class MessageModel {

    private String id;
    private String senderId;
    private String text;
    private long timestamp;
    private int type; // text or image

    public MessageModel() {
        // Required empty
    }

    public MessageModel(String senderId, String text, long timestamp, int type) {
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm", Locale.US);
        return sdf.format(getTimestamp());
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof MessageModel) {
            MessageModel msg = (MessageModel) obj;
            return msg.getId().equals(id);
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return "MessageModel{" +
                "id='" + id + '\'' +
                ", senderId='" + senderId + '\'' +
                ", text='" + text + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", type=" + type +
                '}';
    }
}
