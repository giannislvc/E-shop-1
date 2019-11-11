package com.nativeboys.eshop.ui.conversation;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.MessageModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Locale;

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
        MessageModel model = getItem(position);

        // TODO: deserialization date format
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm", Locale.US);
        holder.timeStamp.setText(sdf.format(model.getTimestamp()));

        if (viewType == 1 || viewType == 3) {
            TextMessageViewHolder textHolder = (TextMessageViewHolder) holder;
            textHolder.textMessage.setText(model.getText());
            Integer width = calculateWidth(textHolder.textMessage.getContext());
            if (width != null) textHolder.textMessage.setMaxWidth(width);
        } else if(viewType == 2 || viewType == 4) {
            ImageMessageViewHolder imageHolder = (ImageMessageViewHolder) holder;
            /*Glide.with(imageHolder.imageMessage.getContext())
                    .load(model.getText())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new CenterCrop())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(imageHolder.imageMessage);*/
        }
    }

    // TODO: Remove it
    private Integer calculateWidth(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return null;
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return Math.round(displayMetrics.widthPixels / (float) 1.7);
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView timeStamp;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            timeStamp = itemView.findViewById(R.id.time_stamp);
        }

    }

    class TextMessageViewHolder extends MessageViewHolder {

        private TextView textMessage;

        TextMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_message);
        }
    }

    class ImageMessageViewHolder extends MessageViewHolder {

        private ImageView imageMessage;
        private AVLoadingIndicatorView progressWidget;

        ImageMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMessage = itemView.findViewById(R.id.image_message);
            progressWidget = itemView.findViewById(R.id.progress_widget);
            imageMessage.setOnClickListener(v -> {
                if (imageClickListener == null) return;
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                imageClickListener.onClick(v, getItem(position).getText());
            });
        }

    }

}


