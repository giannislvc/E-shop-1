package com.nativeboys.eshop.ui.main.conversations;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.customViews.PVCFragment;
import com.nativeboys.eshop.tools.GlobalViewModel;
import com.nativeboys.eshop.ui.main.MainFragmentDirections;

import java.util.ArrayList;

public class ConversationsFragment extends PVCFragment {

    private ConversationsAdapter adapter;
    private GlobalViewModel globalVM;

    public ConversationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVM = new ViewModelProvider(getActivity() != null ? getActivity() : this)
                .get(GlobalViewModel.class);
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
        NavController navController = Navigation.findNavController(view);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new ConversationsAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        adapter.setConversationClickListener((itemView, meta) -> {
            String userId = globalVM.getUserId();
            String friendId = meta.getId();
            if (userId != null && friendId != null) {
                NavController parentController = getParentNavController();
                if (parentController == null) return;
                parentController.navigate(MainFragmentDirections.actionMainToConversation(userId, friendId));
            }
        });

        globalVM.getMetaData().observe(getViewLifecycleOwner(), metaData -> {
            if (metaData == null) return;
            adapter.submitList(new ArrayList<>(metaData));
        });
    }
}
