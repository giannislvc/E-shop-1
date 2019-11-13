package com.nativeboys.eshop.ui.main.conversations;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.ui.main.MainFragmentDirections;
import com.nativeboys.eshop.viewModels.GlobalViewModel;

import java.util.ArrayList;

public class ConversationsFragment extends Fragment {

    private FragmentActivity activity;
    private GlobalViewModel viewModel;

    private NavController navController;
    private ConversationsAdapter adapter;

    public ConversationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(activity).get(GlobalViewModel.class);
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
        RecyclerView recycler_view = view.findViewById(R.id.recycler_view);

        adapter = new ConversationsAdapter(activity.getApplicationContext());
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(activity));
        adapter.setConversationClickListener((itemView, meta) -> openConversation(meta.getConversationId(), meta.getId()));
        viewModel.getMetaData().observe(this, metaData -> {
            if (metaData == null) return;
            adapter.submitList(new ArrayList<>(metaData));
        });
    }

    private void openConversation(String conversationId, String friendId) {
        String userId = viewModel.USER_ID;
        if (conversationId != null && friendId != null) {
            navController.navigate(MainFragmentDirections.actionMainToConversation(conversationId, userId, friendId));
        }
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
