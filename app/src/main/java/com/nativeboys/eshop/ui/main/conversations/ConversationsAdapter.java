package com.nativeboys.eshop.ui.main.conversations;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.firebase.MessageModel;
import com.nativeboys.eshop.models.firebase.MetaDataModel;
import com.nativeboys.eshop.tools.CustomersCache;

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
        if (model != null) holder.bind(model);
    }

    class ConversationsViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private TextView userName, lastMessage, timeStamp;

        ConversationsViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            lastMessage = itemView.findViewById(R.id.last_message);
            timeStamp = itemView.findViewById(R.id.time_stamp);
            itemView.setOnClickListener(v -> {
                if (clickListener == null) return;
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                clickListener.onClick(v, getItem(position));
            });
        }

        private void loadLastMessageDrawable(@DrawableRes int resource) {
            Glide.with(lastMessage.getContext()).load(resource).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    lastMessage.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    lastMessage.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            });
        }

        private void bind(@NonNull MetaDataModel metaData) {
            userName.setText(null);
            CustomersCache.getCustomer(metaData.getId(), user -> {
                if (user == null) return;
                MessageModel message = metaData.getLastMessage();
                if (message == null) return;
                Context context = lastMessage.getContext();

                String text;
                boolean textMessage = message.getType() == 1;
                boolean friendMessage = metaData.getId().equals(message.getSenderId());

                if (friendMessage) { // Friend Message
                    text = textMessage ? message.getText() :
                            String.format(context.getResources().getString(R.string.friend_sent_photo), user.getFirstName());
                } else { // Client Message
                    text = textMessage ? String.format(context.getResources().getString(R.string.you_sent_text), message.getText()) :
                            context.getResources().getString(R.string.you_sent_photo);
                }

                lastMessage.setText(text);
                lastMessage.setGravity(textMessage ? Gravity.TOP : Gravity.CENTER_VERTICAL);
                timeStamp.setText(message.getFormattedTimestamp());

                int resource = textMessage ? R.drawable.ic_message_black_24dp : R.drawable.ic_image_black_24dp;
                loadLastMessageDrawable(resource);

                userName.setText(user.getFirstName());
                Glide.with(userImage.getContext())
                        .load(user.getProfileImageUrl())
                        .transform(new CenterCrop())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(userImage);
            });
        }

    }
}
