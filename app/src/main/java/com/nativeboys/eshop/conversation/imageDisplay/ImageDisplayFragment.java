package com.nativeboys.eshop.conversation.imageDisplay;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nativeboys.eshop.R;

public class ImageDisplayFragment extends DialogFragment {

    private static final String ARG_PARAM_PICK_PATH = "pick_path";

    private String pickPath;

    public ImageDisplayFragment() {
        // Required empty public constructor
    }

    public static ImageDisplayFragment newInstance(@NonNull String pickPath) {
        ImageDisplayFragment fragment = new ImageDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_PICK_PATH, pickPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FadeFragmentTheme);
        if (getArguments() != null) {
            pickPath = getArguments().getString(ARG_PARAM_PICK_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView imageView = view.findViewById(R.id.image_view);
        imageView.setOnClickListener(view1 -> dismiss());
        if (pickPath != null) {
            Glide.with(imageView.getContext())
                    .load(pickPath)
                    .into(imageView);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
