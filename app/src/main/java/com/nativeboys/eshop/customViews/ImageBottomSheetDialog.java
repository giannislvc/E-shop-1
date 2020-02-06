package com.nativeboys.eshop.customViews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nativeboys.eshop.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ImageBottomSheetDialog extends BottomSheetDialogFragment {

    public enum Option {
        CAMERA,
        FILE
    }

    public interface OnUserInteractionListener {
        void onOptionClicked(@NonNull Option option);
    }

    private OnUserInteractionListener listener;
    private TextView picturesBtn, takePhotoBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        picturesBtn = view.findViewById(R.id.pictures_btn);
        takePhotoBtn = view.findViewById(R.id.take_photo_btn);
        setUpListeners();
    }

    private void setUpListeners() {
        picturesBtn.setOnClickListener(view -> onButtonClicked(Option.FILE));
        takePhotoBtn.setOnClickListener(view -> onButtonClicked(Option.CAMERA));
    }

    private void onButtonClicked(@NonNull Option option) {
        if (listener != null) listener.onOptionClicked(option);
        dismiss();
    }

    public void setListener(OnUserInteractionListener listener) {
        this.listener = listener;
    }
}
