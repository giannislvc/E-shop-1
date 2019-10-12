package com.nativeboys.eshop.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserModel {

    private String id;
    private String name;
    private String lastName;
    private String pickPath;

    public UserModel() {
        // Required empty
    }

    public UserModel(String id, String name, String lastName, String pickPath) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.pickPath = pickPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPickPath() {
        return pickPath;
    }

    public void setPickPath(String pickPath) {
        this.pickPath = pickPath;
    }
}
