package com.nativeboys.eshop.conversations;

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
import com.nativeboys.eshop.models.MessageModel;
import com.nativeboys.eshop.models.MetaDataModel;

public class ConversationsAdapter extends ListAdapter<MetaDataModel, ConversationsAdapter.ConversationsViewHolder> {

    private final Context context;
    private OnConversationClickListener clickListener;

    public interface OnConversationClickListener {
        void onClick(View itemView, MetaDataModel meta);
    }

    void setConversationClickListener(OnConversationClickListener listener) {
        this.clickListener = listener;
    }

    // TODO: Replace it with user image url
    private final static String test_image = "https://cdn1.iconfinder.com/data/icons/business-users/512/circle-512.png";

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

    ConversationsAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
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
        holder.user_name.setText(model.getId());
        if (model.getLastMessage() != null) holder.last_message.setText(model.getLastMessage().getText());
        Glide.with(context).load(test_image).into(holder.user_image);
    }

    class ConversationsViewHolder extends RecyclerView.ViewHolder {

        private ImageView user_image;
        private TextView user_name, last_message;

        ConversationsViewHolder(@NonNull View itemView) {
            super(itemView);
            user_image = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            last_message = itemView.findViewById(R.id.last_message);
            itemView.setOnClickListener(v -> {
                if (clickListener == null) return;
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;
                clickListener.onClick(v, getItem(position));
            });
        }
    }
}
