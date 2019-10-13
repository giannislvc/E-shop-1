package com.nativeboys.eshop.conversations;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.conversation.ConversationFragment;
import com.nativeboys.eshop.models.MessageModel;
import com.nativeboys.eshop.models.MetaDataModel;

import java.util.ArrayList;
import java.util.List;

public class ConversationsFragment extends Fragment {

    private FragmentActivity activity;

    private RecyclerView recycler_view;
    private ConversationsAdapter adapter;

    public ConversationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler_view = view.findViewById(R.id.recycler_view);
        adapter = new ConversationsAdapter(activity.getApplicationContext());
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(activity));
        adapter.setConversationClickListener((itemView, meta) -> {
            // TODO: Pass arguments
            new ConversationFragment().show(getChildFragmentManager(), ConversationFragment.class.getSimpleName());
        });
        adapter.submitList(new ArrayList<>(mockData()));
    }

    private List<MetaDataModel> mockData() {
        List<MetaDataModel> list = new ArrayList<>();
        list.add(new MetaDataModel("1", "2", new MessageModel("5", "200", "Hello World", "123", 1)));
        list.add(new MetaDataModel("3", "4", new MessageModel("10", "200", "Hello Baby!!", "123", 1)));
        return list;
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
