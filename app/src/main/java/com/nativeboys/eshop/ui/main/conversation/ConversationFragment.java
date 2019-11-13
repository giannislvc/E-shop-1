package com.nativeboys.eshop.ui.main.conversation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
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
import com.nativeboys.eshop.ui.main.conversation.imageDisplay.ImageDisplayFragment;
import com.nativeboys.eshop.viewModels.ConversationViewModel;
import com.nativeboys.eshop.viewModels.ConversationViewModelFactory;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.nativeboys.eshop.viewModels.ConversationViewModel.MESSAGES_PER_FETCH;

public class ConversationFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private static final int PICK_IMAGE_REQUEST = 1;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ConversationFragmentArgs args = ConversationFragmentArgs.fromBundle(getArguments());
            userId = args.getUserId();
            friendId = args.getFriendId();
            conversationId = args.getConversationId();
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

        backBtn.setOnClickListener(v -> activity.onBackPressed());

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
                if (positionStart != 0 || adapter.getItemCount() <= MESSAGES_PER_FETCH) scrollToBottom();
            }
        });

        adapter.setImageClickListener((itemView, pickPath) ->
                ImageDisplayFragment.newInstance(pickPath)
                        .show(getChildFragmentManager(), ImageDisplayFragment.class.getSimpleName()));

        new Handler(Looper.getMainLooper()).postDelayed(() -> recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (manager.findFirstCompletelyVisibleItemPosition() != 0) return;
                viewModel.getPreviousMessages();
            }
        }), 1500);
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