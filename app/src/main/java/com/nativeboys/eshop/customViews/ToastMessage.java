package com.nativeboys.eshop.customViews;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nativeboys.eshop.R;

public class ToastMessage extends Toast {

    private TextView textView;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */

    public ToastMessage(Context context) {
        super(context);
        Activity activity = (Activity) context;
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, activity.findViewById(R.id.container));
        textView = layout.findViewById(R.id.text_field);
        setDuration(Toast.LENGTH_SHORT);
        setView(layout);
    }

    public void setText(String text) {
        textView.setText(text);
    }

}
