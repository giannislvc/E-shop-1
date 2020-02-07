package com.nativeboys.eshop.callbacks;

import android.view.View;

import com.nativeboys.eshop.models.node.Product;

public interface OnProductClickListener {
    void onClick(View itemView, Product product);
}