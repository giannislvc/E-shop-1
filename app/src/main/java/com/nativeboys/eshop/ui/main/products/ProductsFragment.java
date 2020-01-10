package com.nativeboys.eshop.ui.main.products;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.UserModel;
import com.nativeboys.eshop.tools.GlobalViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    private FragmentActivity activity;
    private GlobalViewModel viewModel;

    public ProductsFragment() {
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
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        ProductsAdapter adapter = new ProductsAdapter();

        FlexboxLayoutManager flexManager = new FlexboxLayoutManager(activity);
        flexManager.setFlexDirection(FlexDirection.ROW);
        flexManager.setJustifyContent(JustifyContent.FLEX_START);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(flexManager);
        viewModel.getUsers().observe(this, users -> {
            List<UserModel> mock = new ArrayList<>();
            mock.add(new UserModel("1", "1", "1"));
            mock.add(new UserModel("2", "2", "2"));
            mock.add(new UserModel("3", "3", "3"));
            mock.add(new UserModel("4", "4", "4"));
            mock.add(new UserModel("5", "5", "5"));
            mock.add(new UserModel("6", "6", "6"));
            mock.addAll(users);
            adapter.submitList(new ArrayList<>(mock));
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
