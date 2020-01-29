package com.nativeboys.eshop.customViews;

import android.os.Bundle;

import com.nativeboys.eshop.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FullDialogFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FadeFragmentTheme);
    }
}
