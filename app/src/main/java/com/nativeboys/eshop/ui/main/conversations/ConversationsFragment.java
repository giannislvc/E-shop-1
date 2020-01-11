package com.nativeboys.eshop.ui.main.conversations;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.tools.GlobalViewModel;
import com.nativeboys.eshop.ui.main.MainFragmentDirections;

import java.util.ArrayList;

public class ConversationsFragment extends Fragment {

    private NavController navController;
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
        navController = Navigation.findNavController(view);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new ConversationsAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        if (getActivity() == null) return;
        GlobalViewModel globalVM = ViewModelProviders.of(getActivity()).get(GlobalViewModel.class);

        adapter.setConversationClickListener((itemView, meta) -> {
            String userId = globalVM.getUserId();
            String conId = meta.getConversationId();
            String friendId = meta.getId();
            if (userId != null && conId != null && friendId != null) {
                navController.navigate(MainFragmentDirections.actionMainToConversation(conId, userId, friendId));
            }
        });

        globalVM.getMetaData().observe(this, metaData -> {
            if (metaData == null) return;
            adapter.submitList(new ArrayList<>(metaData));
        });
    }
}
