package com.nativeboys.eshop.ui.main.conversations;

import android.content.Context;
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
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.firebase.MessageModel;
import com.nativeboys.eshop.models.firebase.MetaDataModel;
import com.nativeboys.eshop.tools.UsersCache;

public class ConversationsAdapter extends ListAdapter<MetaDataModel, ConversationsAdapter.ConversationsViewHolder> {

    private OnConversationClickListener clickListener;

    public interface OnConversationClickListener {
        void onClick(View itemView, MetaDataModel meta);
    }

    void setConversationClickListener(OnConversationClickListener listener) {
        this.clickListener = listener;
    }

    private static final DiffUtil.ItemCallback<MetaDataModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<MetaDataModel>() {

        @Override
        public boolean areItemsTheSame(@NonNull MetaDataModel model, @NonNull MetaDataModel t1) {
            return model.getId().equals(t1.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MetaDataModel model, @NonNull MetaDataModel t1) {
            MessageModel m1 = model.getLastMessage();
            MessageModel m2 = t1.getLastMessage();
            if (m1 != null && m2 != null) return m1.getText().equals(m2.getText());
            return false;
        }

    };

    ConversationsAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ConversationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_cell, parent, false);
        return new ConversationsAdapter.ConversationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationsViewHolder holder, int position) {
        MetaDataModel model = getItem(position);
        holder.userName.setText(null);
        UsersCache.getUser(model.getId(), user -> {
            if (user == null) return;
            MessageModel message = model.getLastMessage();
            if (message == null) return;
            Context context = holder.lastMessage.getContext();
            String text;
            if (model.getId().equals(message.getSenderId())) { // Friend
                text = message.getType() == 1 ? message.getText() :
                        String.format(context.getResources().getString(R.string.friend_sent_photo), user.getName());
            } else { // Client
                text = message.getType() == 1 ? String.format(context.getResources().getString(R.string.you_sent_text), message.getText()) :
                        context.getResources().getString(R.string.you_sent_photo);
            }
            holder.lastMessage.setText(text);
            holder.userName.setText(user.getName());
            Glide.with(holder.userImage.getContext()).load(user.getPickPath()).into(holder.userImage);
        });
    }

    class ConversationsViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private TextView userName, lastMessage;

        ConversationsViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            lastMessage = itemView.findViewById(R.id.last_message);
            itemView.setOnClickListener(v -> {
                if (clickListener == null) return;
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                clickListener.onClick(v, getItem(position));
            });
        }
    }
}
