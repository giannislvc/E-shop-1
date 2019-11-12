package com.nativeboys.eshop.customViews;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.nativeboys.eshop.R;

public class FormEditText extends androidx.appcompat.widget.AppCompatEditText {

    private State cState;

    public enum State {
        NORMAL,
        VALID,
        INVALID
    }

    public FormEditText(Context context) {
        super(context);
        setUpTextListener();
    }

    public FormEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpTextListener();
    }

    public FormEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpTextListener();
    }

    private void setUpTextListener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (cState != null && cState == State.INVALID) setState(State.NORMAL);
            }
        });
    }

    public void setState(@NonNull State state) {
        if (cState == state) return;
        cState = state;
        int drawable;
        switch (cState) {
            case VALID: {
                drawable = R.drawable.edit_text_valid_shape;
                break;
            }
            case INVALID: {
                drawable = R.drawable.edit_text_invalid_shape;
                break;
            }
            default: {
                drawable = R.drawable.edit_text_normal_shape;
                break;
            }
        }
        setBackground(ContextCompat.getDrawable(getContext(), drawable));
    }

    public boolean isValid() {
        boolean isValid = false;
        if (getText() != null) {
            isValid = !getText().toString().isEmpty();
        }
        setState(isValid ? State.NORMAL : State.INVALID);
        return isValid;
    }

}
