package com.nativeboys.eshop.customViews;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class NonScrollLayoutManager extends LinearLayoutManager {

    public NonScrollLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
