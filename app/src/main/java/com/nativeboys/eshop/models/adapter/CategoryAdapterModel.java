package com.nativeboys.eshop.models.adapter;

import com.nativeboys.eshop.models.node.Category;

public class CategoryAdapterModel extends Category {

    private boolean selected;

    public CategoryAdapterModel(String category_id, String name, String description, String image_url) {
        super(category_id, name, description, image_url);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
