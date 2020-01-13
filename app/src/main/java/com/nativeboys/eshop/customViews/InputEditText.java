package com.nativeboys.eshop.customViews;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class InputEditText extends androidx.appcompat.widget.AppCompatEditText {

    public interface OnTextChangedListener {
        void onTextChanged(@NonNull EditText editText, String text);
    }

    private OnTextChangedListener onTextChangedListener;

    public InputEditText(Context context) {
        super(context);
        addTextListener();
    }

    public InputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextListener();
    }

    public InputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTextListener();
    }

    private void addTextListener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (onTextChangedListener != null) {
                    onTextChangedListener.onTextChanged(InputEditText.this, s.toString());
                }
            }
        });
    }

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }

}
