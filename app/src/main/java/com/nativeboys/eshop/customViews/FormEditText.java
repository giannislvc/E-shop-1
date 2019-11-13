package com.nativeboys.eshop.customViews;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.nativeboys.eshop.R;

import java.util.regex.Pattern;

public class FormEditText extends androidx.appcompat.widget.AppCompatEditText {

    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z0-9@#$%^&+=])" +      // any letter
                    "(?=\\S+$)" +           // no white spaces
                    ".{6,}" +               // at least 6 characters
                    "$");

    public static final Pattern USERNAME_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])" +      // any character
                    "(?=\\S+$)" +           // no white spaces
                    ".+" +               // at least 1 character
                    "$");

    private State currentState;
    private Pattern pattern;

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
                if (currentState != null && currentState == State.INVALID) setState(State.NORMAL);
            }
        });
    }

    public void setState(@NonNull State state) {
        if (currentState == state) return;
        currentState = state;
        int drawable;
        switch (currentState) {
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
        boolean valid = false;
        if (getText() != null) {
            valid = pattern.matcher(getText().toString()).matches();
        }
        setState(valid ? State.NORMAL : State.INVALID);
        return valid;
    }

    public void setPattern(@NonNull Pattern pattern) {
        this.pattern = pattern;
    }

}
