package com.nativeboys.eshop.models;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface SettingsModel {

    String getId();
    String getDescription();
    boolean isSelected();
    void setSelected(boolean selected);

    static <T extends SettingsModel> List<T> setSelectedItem(@NonNull List<T> list, @NonNull String id) {
        for (T t : list) {
            if (t.getId().equals(id)) {
                t.setSelected(!t.isSelected());
            } else {
                t.setSelected(false);
            }
        }
        return list;
    }

    @Nullable
    static <T extends SettingsModel> T getSelectedItem(@NonNull List<T> list) {
        for (T t : list) {
            if (t.isSelected()) return t;
        }
        return null;
    }

}
