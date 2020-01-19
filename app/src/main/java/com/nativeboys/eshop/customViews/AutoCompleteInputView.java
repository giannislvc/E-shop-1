package com.nativeboys.eshop.customViews;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class AutoCompleteInputView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    private OnTextChangedListener onTextChangedListener;

    public AutoCompleteInputView(Context context) {
        super(context);
        addTextListener();
    }

    public AutoCompleteInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextListener();
    }

    public AutoCompleteInputView(Context context, AttributeSet attrs, int defStyleAttr) {
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
                if (onTextChangedListener == null) return;
                onTextChangedListener.onTextChanged(AutoCompleteInputView.this, s.toString());
            }
        });
    }

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }

}
