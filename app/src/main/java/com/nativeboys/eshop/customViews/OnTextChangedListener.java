package com.nativeboys.eshop.customViews;

import android.widget.EditText;

import androidx.annotation.NonNull;

public interface OnTextChangedListener {

    void onTextChanged(@NonNull EditText editText, String text);

}
