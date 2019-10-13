package com.nativeboys.eshop.conversation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

    private FragmentActivity activity;

    private ConstraintLayout parent_view;
    private ImageView back_arrow;
    private TextView headline;
    private RecyclerView recycler_view;
    private EditText message_field;
    private Button send_button;
    private ConversationAdapter adapter;

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
        parent_view = view.findViewById(R.id.parent_view);
        back_arrow = view.findViewById(R.id.back_arrow);
        headline = view.findViewById(R.id.headline);
        recycler_view = view.findViewById(R.id.recycler_view);
        message_field = view.findViewById(R.id.message_field);
        send_button = view.findViewById(R.id.send_button);
        adapter = new ConversationAdapter(activity.getApplicationContext(), "200");
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(activity));
        setUpListeners();
        //adapter.submitList(new ArrayList<>(mockData()));
    }

    private void setUpListeners() {
        parent_view.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (oldBottom > bottom) scrollToBottom();
        });
        back_arrow.setOnClickListener(v -> dismiss());
        send_button.setOnClickListener( v-> {
            String message = message_field.getText() != null ? message_field.getText().toString() : "";
            if (message.isEmpty()) { return; }
            // TODO: implement Send Message
            message_field.setText(null);
        });
    }

    private void scrollToBottom() {
        if(adapter.getItemCount() < 1) { return; }
        recycler_view.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

}
