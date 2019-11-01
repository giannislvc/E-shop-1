package com.nativeboys.eshop.conversation;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.MessageModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ConversationAdapter extends ListAdapter<MessageModel, ConversationAdapter.MessageViewHolder> {

    private final static String TEST_PICK_PATH = "https://demolay.org/wp-content/uploads/2018/12/Vacation.jpg";

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

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getSenderId().equals(clientId) ? 1 : 2;
    }

    @NonNull
    @Override
    public ConversationAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sender_cell, parent, false);
            return new ConversationAdapter.TextMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_receiver_cell, parent, false);
            return new ConversationAdapter.ImageMessageViewHolder(view);
            /*View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_receiver_cell, parent, false);
            return new ConversationAdapter.TextMessageViewHolder(view);*/
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.MessageViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        MessageModel model = getItem(position);

        // TODO: deserialization date format
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm", Locale.US);
        holder.timeStamp.setText(sdf.format(model.getTimestamp()));

        if (viewType == 2) {
            TextMessageViewHolder textHolder = (TextMessageViewHolder) holder;
            textHolder.textMessage.setText(model.getText());

            Integer width = calculateWidth(textHolder.textMessage.getContext());
            if (width != null) textHolder.textMessage.setMaxWidth(width);
        } else {
            ImageMessageViewHolder imageHolder = (ImageMessageViewHolder) holder;
            Glide.with(imageHolder.imageMessage.getContext())
                    .load(TEST_PICK_PATH)
                    .transform(new CenterCrop(), new RoundedCorners(30))
                    .into(imageHolder.imageMessage);
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

        ImageMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMessage = itemView.findViewById(R.id.image_message);
            imageMessage.setOnClickListener(v -> {
                if (imageClickListener == null) return;
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                imageClickListener.onClick(v, TEST_PICK_PATH);
            });
        }

    }

}


