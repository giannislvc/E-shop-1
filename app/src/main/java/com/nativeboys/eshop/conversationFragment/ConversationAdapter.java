package com.nativeboys.eshop.conversationFragment;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.MessageModel;

public class ConversationAdapter extends ListAdapter<MessageModel, ConversationAdapter.TextMessageViewHolder> {

    // TODO: implement image type

    private final Context context;
    private final String clientId;

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

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        return getItem(position).getId().equals(clientId) ? 1 : 2;
    }

    ConversationAdapter(@NonNull Context context, @NonNull String clientId) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.clientId = clientId;
    }

    @NonNull
    @Override
    public ConversationAdapter.TextMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sender_cell, parent, false);
            return new ConversationAdapter.SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_receiver_cell, parent, false);
            return new ConversationAdapter.ReceiverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.TextMessageViewHolder holder, int position) {
        Integer width = calculateWidth();
        if (width != null) holder.text_message.setMaxWidth(width);
        MessageModel model = getItem(position);
        holder.text_message.setText(model.getText());
        holder.time_stamp.setText(model.getTimestamp()); // TODO: deserialization date format
    }

    // TODO: Remove it
    private Integer calculateWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) { return null; }
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return Math.round(displayMetrics.widthPixels / (float) 1.7);
    }

    class TextMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView text_message, time_stamp;

        TextMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            text_message = itemView.findViewById(R.id.text_message);
            time_stamp = itemView.findViewById(R.id.time_stamp);
        }
    }

    class SenderViewHolder extends ConversationAdapter.TextMessageViewHolder {

        SenderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class ReceiverViewHolder extends ConversationAdapter.TextMessageViewHolder {

        ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}


