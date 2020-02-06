package com.nativeboys.eshop.ui.main.conversation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.firebase.MessageModel;
import com.nativeboys.eshop.customViews.ScreenManager;

public class ConversationAdapter extends ListAdapter<MessageModel, ConversationAdapter.MessageViewHolder> {

    public interface OnImageClickListener {
        void onClick(@NonNull View itemView, @NonNull String pickPath);
    }

    private static final DiffUtil.ItemCallback<MessageModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<MessageModel>() {

        @Override
        public boolean areItemsTheSame(@NonNull MessageModel model, @NonNull MessageModel t1) {
            return model.getId().equals(t1.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MessageModel model, @NonNull MessageModel t1) {
            return model.getText().equals(t1.getText());
        }

    };

    private final String clientId;
    private OnImageClickListener imageClickListener;

    ConversationAdapter(@NonNull String clientId) {
        super(DIFF_CALLBACK);
        this.clientId = clientId;
    }

    void setImageClickListener(@NonNull OnImageClickListener listener) {
        imageClickListener = listener;
    }

    /*
    Type 1: Text Sender
    Type 2: Image Sender
    Type 3: Text Receiver
    Type 4: Image Receiver
    */

    @Override
    public int getItemViewType(int position) {
        MessageModel message = getItem(position);
        boolean sender = message.getSenderId().equals(clientId);
        if (sender) { // Sender
            return message.getType() == 2 ? 2 : 1;
        } else { // Receiver
            return message.getType() == 2 ? 4 : 3;
        }
    }

    @NonNull
    @Override
    public ConversationAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case 2: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_sender_cell, parent, false);
                return new ImageMessageViewHolder(view);
            }
            case 3: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_receiver_cell, parent, false);
                return new TextMessageViewHolder(view);
            }
            case 4: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_receiver_cell, parent, false);
                return new ImageMessageViewHolder(view);
            }
            default: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sender_cell, parent, false);
                return new TextMessageViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.MessageViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        MessageModel message = getItem(position);
        if (message != null) {
            if (viewType == 1 || viewType == 3) {
                TextMessageViewHolder textHolder = (TextMessageViewHolder) holder;
                textHolder.bind(message);
            } else if(viewType == 2 || viewType == 4) {
                ImageMessageViewHolder imageHolder = (ImageMessageViewHolder) holder;
                imageHolder.bind(message);
            }
        }
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView timeStamp;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            timeStamp = itemView.findViewById(R.id.time_stamp);
        }

        public void bind(@NonNull MessageModel message) {
            timeStamp.setText(message.getFormattedTimestamp());
        }

    }

    class TextMessageViewHolder extends MessageViewHolder {

        private TextView textMessage;

        TextMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_message);
            textMessage.setMaxWidth(Math.round(ScreenManager.getWidth() / (float) 1.7));
        }

        @Override
        public void bind(@NonNull MessageModel message) {
            super.bind(message);
            textMessage.setText(message.getText());
        }

    }

    class ImageMessageViewHolder extends MessageViewHolder {

        private ImageView imageMessage;

        ImageMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMessage = itemView.findViewById(R.id.image_message);
            // AVLoadingIndicatorView progressWidget = itemView.findViewById(R.id.progress_widget);
            imageMessage.setOnClickListener(v -> {
                if (imageClickListener == null) return;
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                imageClickListener.onClick(v, getItem(position).getText());
            });
        }

        @Override
        public void bind(@NonNull MessageModel message) {
            super.bind(message);
            Glide.with(imageMessage.getContext())
                    .load(message.getText())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new CenterCrop())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(imageMessage);
        }

    }

}


