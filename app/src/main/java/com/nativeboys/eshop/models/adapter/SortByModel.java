package com.nativeboys.eshop.models.adapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SortByModel {

    private final int id;
    private String description;

    private SortByModel(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static List<SortByModel> SearchTypes() {
        List<SortByModel> list = new ArrayList<>();
        list.add(new SortByModel(0, "Rate"));
        list.add(new SortByModel(1, "Upload time"));
        list.add(new SortByModel(2, "Price"));
        list.add(new SortByModel(3, "Popularity"));
        return list;
    }

    @NonNull
    @Override
    public String toString() {
        return "SortByModel{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
