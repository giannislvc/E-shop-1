package com.nativeboys.eshop.conversation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
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

import java.util.ArrayList;

public class ConversationFragment extends DialogFragment {

    private final static String CONVERSATION_ID = "conversation_id";
    private final static String USER_ID = "user_id";
    private final static String FRIEND_ID = "friend_id";

    private FragmentActivity activity;
    private ConversationViewModel viewModel;
    private String conversationId, userId, friendId;

    private ConstraintLayout bottom;
    private ImageView back_arrow;
    private TextView headline;
    private RecyclerView recycler_view;
    private EditText message_field;
    private Button send_button;
    private ConversationAdapter adapter;

    public ConversationFragment() {
        // Required empty public constructor
    }

    public static ConversationFragment newInstance(@NonNull String conversationId, @NonNull String userId, @NonNull String friendId) {
        ConversationFragment fragment = new ConversationFragment();
        Bundle args = new Bundle();
        args.putString(CONVERSATION_ID, conversationId);
        args.putString(USER_ID, userId);
        args.putString(FRIEND_ID, friendId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FadeFragmentTheme);
        if (getArguments() != null) {
            conversationId = getArguments().getString(CONVERSATION_ID);
            userId = getArguments().getString(USER_ID);
            friendId = getArguments().getString(FRIEND_ID);
        }
        ConversationViewModelFactory factory = new ConversationViewModelFactory(activity.getApplication(), conversationId, userId, friendId);
        viewModel = ViewModelProviders.of(this, factory).get(ConversationViewModel.class);
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
        bottom = view.findViewById(R.id.bottom);
        back_arrow = view.findViewById(R.id.back_arrow);
        headline = view.findViewById(R.id.headline);
        recycler_view = view.findViewById(R.id.recycler_view);
        message_field = view.findViewById(R.id.message_field);
        send_button = view.findViewById(R.id.send_button);
        adapter = new ConversationAdapter(activity.getApplicationContext(), userId);
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(activity));
        setUpListeners();
    }

    private void setUpListeners() {
        bottom.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (oldBottom > bottom || top < oldTop) scrollToBottom();
        });
        back_arrow.setOnClickListener(v -> dismiss());
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                scrollToBottom();
            }
        });
        send_button.setOnClickListener( v-> {
            String message = message_field.getText() != null ? message_field.getText().toString() : "";
            if (message.isEmpty()) return;
            viewModel.sendMessage(message.trim());
            message_field.setText(null);
        });
        viewModel.getMessages().observe(this, messages -> {
            if (messages == null) return;
            adapter.submitList(new ArrayList<>(messages));
        });
    }

    private void scrollToBottom() {
        if(adapter.getItemCount() < 1) return;
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
