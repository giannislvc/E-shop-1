package com.nativeboys.eshop.customViews;

import android.widget.EditText;

import androidx.annotation.NonNull;

public interface AutoCompleteListener {

    void onTextChanged(@NonNull EditText editText, String text);

    void onSearchClicked(@NonNull EditText editText, String text);

}
