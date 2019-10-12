package com.nativeboys.eshop.conversationFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nativeboys.eshop.R;

public class ConversationFragment extends DialogFragment {

    private ImageView back_arrow;
    private TextView headline;
    private RecyclerView recycler_view;
    private EditText message_field;
    private Button send_button;

    public ConversationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FadeFragmentTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back_arrow = view.findViewById(R.id.back_arrow);
        headline = view.findViewById(R.id.headline);
        recycler_view = view.findViewById(R.id.recycler_view);
        message_field = view.findViewById(R.id.message_field);
        send_button = view.findViewById(R.id.send_button);
        setUpListeners();
    }

    private void setUpListeners() {
        back_arrow.setOnClickListener(v -> dismiss());
    }

}
