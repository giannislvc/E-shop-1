package com.nativeboys.eshop.products;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.SharedViewModel;

import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private FragmentActivity activity;
    private SharedViewModel viewModel;

    private RecyclerView recycler_view;
    private ProductsAdapter adapter;
    private Button add_button;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(activity).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler_view = view.findViewById(R.id.recycler_view);
        add_button = view.findViewById(R.id.add_button);
        adapter = new ProductsAdapter(activity.getApplicationContext());
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(activity));
        setUpListeners();
    }

    private void setUpListeners() {
        adapter.setOnUserClickListener((itemView, user) -> {
            viewModel.getConversationIdWith(user.getId(), model -> Log.i(TAG, "onResponse: " + model));
        });
        add_button.setOnClickListener(v-> viewModel.addUser("John", "Doe", "https://cdn0.iconfinder.com/data/icons/emojis-colored-outlined-pixel-perfect/64/emoji-67-512.png"));
        viewModel.getUsers().observe(this, users -> {
            if (users == null) { return; }
            adapter.submitList(new ArrayList<>(users));
        });
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
