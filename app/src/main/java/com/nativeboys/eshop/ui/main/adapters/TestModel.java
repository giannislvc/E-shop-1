package com.nativeboys.eshop.ui.main.adapters;

import androidx.annotation.NonNull;

import com.nativeboys.eshop.R;

import java.util.ArrayList;
import java.util.List;

public class TestModel {

    private final String id, name, resource;
    private boolean selected;

    private TestModel(String id, String name, String resource) {
        this.id = id;
        this.name = name;
        this.resource = resource;
    }

    public static List<TestModel> getDummyData() {
        List<TestModel> list = new ArrayList<>();
        String baseUrl = "http://192.168.1.4:5000/uploads/";
        list.add(new TestModel("1", "Entertainment", (baseUrl + "video.png")));
        list.add(new TestModel("2", "Games", (baseUrl + "joystick.png")));
        list.add(new TestModel("3", "Electronics", (baseUrl + "smartphone.png")));
        list.add(new TestModel("4", "Tools", (baseUrl + "support.png")));
        list.add(new TestModel("5", "Vehicles", (baseUrl + "car.png")));
        list.add(new TestModel("6", "Sport", (baseUrl + "dumbbell.png")));
        list.add(new TestModel("7", "Music", (baseUrl + "microphone.png")));
        list.add(new TestModel("8", "Home", (baseUrl + "home.png")));
        list.add(new TestModel("9", "Accessories and clothing", (baseUrl + "cloth.png")));
        return  list;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getResource() {
        return resource;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean areItemsTheSame(@NonNull TestModel other) {
        return id.equals(other.id);
    }

    public boolean areContentsTheSame(@NonNull TestModel other) {
        return resource.equals(other.resource) &&
                selected == other.selected &&
                name.equals(other.name);
    }

    @NonNull
    @Override
    public String toString() {
        return "TestModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", resource=" + resource +
                ", selected=" + selected +
                '}';
    }
}
