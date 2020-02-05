package com.nativeboys.eshop.models.adapter;

import com.nativeboys.eshop.models.SettingsModel;
import com.nativeboys.eshop.models.query.Sort;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class SortModel implements SettingsModel {

    private final static List<SortModel> types = new ArrayList<SortModel>() {{
        add(new SortModel(0, "Rate"));
        add(new SortModel(1, "Upload Time"));
        add(new SortModel(2, "Price"));
        add(new SortModel(3, "Popularity"));
    }};

    public static List<SortModel> getTypes() {
        return types;
    }

    private final int id;
    private final String description;
    private boolean selected;

    public SortModel(int id, String description) {
        this.id = id;
        this.description = description;
        this.selected = false;
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getDescription() {
        return description;
    }

    public int getNumericId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return "SortModel{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", selected=" + selected +
                '}';
    }
}
