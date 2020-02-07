package com.nativeboys.eshop.ui.main.conversation;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
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
import com.nativeboys.eshop.customViews.ImageProviderFragment;
import com.nativeboys.eshop.ui.main.conversation.imageDisplay.ImageDisplayFragment;
import com.nativeboys.eshop.tools.ConversationViewModel;
import com.nativeboys.eshop.tools.ConversationViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import static com.nativeboys.eshop.tools.ConversationViewModel.MESSAGES_PER_FETCH;

public class ConversationFragment extends ImageProviderFragment {

    private final String TAG = getClass().getSimpleName();

    private ConversationViewModel chatVM;

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
        String friendId;
        if (getArguments() != null) {
            ConversationFragmentArgs args = ConversationFragmentArgs.fromBundle(getArguments());
            friendId = args.getFriendId();
        } else {
            friendId = null;
        }
        ConversationViewModelFactory factory;
        if (getActivity() != null) {
            factory = new ConversationViewModelFactory(getActivity().getApplication(),
                    friendId != null ? friendId : "");
        } else {
            factory = null;
        }
        chatVM = (factory != null) ?
                new ViewModelProvider(this, factory).get(ConversationViewModel.class) :
                new ViewModelProvider(this).get(ConversationViewModel.class);
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
        backBtn = view.findViewById(R.id.back_btn);
        headline = view.findViewById(R.id.headline);
        recyclerView = view.findViewById(R.id.recycler_view);
        messageField = view.findViewById(R.id.message_field);
        sendBtn = view.findViewById(R.id.send_button);

        moreOptionsContainer = view.findViewById(R.id.more_options_holder);
        moreOptionsBtn = view.findViewById(R.id.more_options_btn);
        takePhotoBtn = view.findViewById(R.id.take_photo_btn);
        picturesBtn = view.findViewById(R.id.pictures_btn);

        adapter = new ConversationAdapter(chatVM.getUserId());
        manager = new LinearLayoutManager(recyclerView.getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        setUpListeners();
    }

    private void setUpListeners() {

        backBtn.setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        picturesBtn.setOnClickListener(view -> retrieveFileImage());

        takePhotoBtn.setOnClickListener(view -> retrieveCameraImage());

        moreOptionsBtn.setOnClickListener(view ->
                moreOptionsContainer.setVisibility(moreOptionsContainer.getVisibility() == View.VISIBLE ? View.GONE: View.VISIBLE));

        sendBtn.setOnClickListener(v-> {
            String message = messageField.getText() != null ? messageField.getText().toString() : "";
            if (message.isEmpty()) return;
            chatVM.sendMessage(message.trim());
            messageField.setText(null);
        });

        chatVM.getFriend().observe(getViewLifecycleOwner(), customer -> {
            if (customer != null) headline.setText(customer.getFirstName());
        });

        chatVM.getMessages().observe(getViewLifecycleOwner(), messageModels ->
                adapter.submitList(new ArrayList<>(messageModels)));

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
                chatVM.getPreviousMessages();
            }
        }), 1500);
    }

    private void scrollToBottom() {
        if(adapter.getItemCount() < 1) return;
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    protected void onImagesRetrieved(@NonNull List<Uri> uris) {
        if (uris.size() > 0) chatVM.sendImage(uris.get(0));
    }

    @Override
    protected void onPermissionDenied() { }

    @Override
    protected void onPermissionGranted() { }

}

// https://www.learningsomethingnew.com/how-to-use-a-recycler-view-to-show-images-from-storage
// https://coderwall.com/p/35xi3w/layout-change-animations-sliding-height
// https://github.com/chrisbanes/PhotoView