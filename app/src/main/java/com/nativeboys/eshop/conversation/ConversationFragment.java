package com.nativeboys.eshop.conversation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.conversation.imageDisplay.ImageDisplayFragment;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ConversationFragment extends DialogFragment {

    private final String TAG = getClass().getSimpleName();

    private static final int PICK_IMAGE_REQUEST = 1;

    private static final String CONVERSATION_ID = "conversation_id";
    private static final String USER_ID = "user_id";
    private static final String FRIEND_ID = "friend_id";

    private FragmentActivity activity;
    private ConversationViewModel viewModel;
    private String conversationId, userId, friendId;

    private ConstraintLayout bottom;
    private LinearLayout moreOptionsContainer;

    private TextView headline, picturesBtn, takePhotoBtn;
    private ImageView backBtn, moreOptionsBtn;
    private EditText messageField;
    private Button sendBtn;

    private RecyclerView recyclerView;
    private ConversationAdapter adapter;
    private LinearLayoutManager manager;

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
        backBtn = view.findViewById(R.id.back_arrow);
        headline = view.findViewById(R.id.headline);
        recyclerView = view.findViewById(R.id.recycler_view);
        messageField = view.findViewById(R.id.message_field);
        sendBtn = view.findViewById(R.id.send_button);

        moreOptionsContainer = view.findViewById(R.id.more_options_holder);
        moreOptionsBtn = view.findViewById(R.id.more_options_btn);
        takePhotoBtn = view.findViewById(R.id.take_photo_btn);
        picturesBtn = view.findViewById(R.id.pictures_btn);

        adapter = new ConversationAdapter(userId);
        manager = new LinearLayoutManager(recyclerView.getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        setUpListeners();
    }

    private void setUpListeners() {

        backBtn.setOnClickListener(v -> dismiss());

        sendBtn.setOnClickListener(v-> {
            String message = messageField.getText() != null ? messageField.getText().toString() : "";
            if (message.isEmpty()) return;
            viewModel.sendMessage(message.trim());
            messageField.setText(null);
        });

        moreOptionsBtn.setOnClickListener(view ->
                moreOptionsContainer.setVisibility(moreOptionsContainer.getVisibility() == View.VISIBLE ? View.GONE: View.VISIBLE));

        viewModel.getMessages().observe(this, messageModels -> adapter.submitList(new ArrayList<>(messageModels)));

        picturesBtn.setOnClickListener(view -> openGallery());

        bottom.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (oldBottom > bottom || top < oldTop) scrollToBottom();
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                Log.i(TAG, "positionStart: " + positionStart + " itemCount: " + itemCount + " sumCount: " + adapter.getItemCount());
                if (positionStart + itemCount == adapter.getItemCount()) scrollToBottom();
            }
        });

        adapter.setImageClickListener((itemView, pickPath) ->
                ImageDisplayFragment.newInstance(pickPath)
                        .show(getChildFragmentManager(), ImageDisplayFragment.class.getSimpleName()));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (manager.findFirstCompletelyVisibleItemPosition() != 0) return;
                viewModel.getPreviousMessages();
            }
        });
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
        } else {
            openFileChooser();
        }
    }

    private void openFileChooser() {
        startActivityForResult(new Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PICK_IMAGE_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFileChooser();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            viewModel.sendImage(data.getData());
        }
    }

    private void scrollToBottom() {
        if(adapter.getItemCount() < 1) return;
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
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

// https://www.learningsomethingnew.com/how-to-use-a-recycler-view-to-show-images-from-storage
// https://coderwall.com/p/35xi3w/layout-change-animations-sliding-height
// https://github.com/chrisbanes/PhotoView