package com.nativeboys.eshop.products;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private FragmentActivity activity;

    private RecyclerView recycler_view;
    private ProductsAdapter adapter;

    public ProductsFragment() {
        // Required empty public constructor
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
        adapter = new ProductsAdapter(activity.getApplicationContext());
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(activity));
        adapter.setOnUserClickListener((itemView, user) -> Log.i(TAG, "onUserClicked: " + user.toString()));
        adapter.submitList(new ArrayList<>(mockData()));
    }

    private List<UserModel> mockData() {
        List<UserModel> list = new ArrayList<>();
        list.add(new UserModel("1", "Giannis", "Dougos", "https://cdn0.iconfinder.com/data/icons/emojis-colored-outlined-pixel-perfect/64/emoji-67-512.png"));
        list.add(new UserModel("2", "Vaggelis", "Boudis", "https://cdn0.iconfinder.com/data/icons/emojis-flat-pixel-perfect-w-skin-tone-2/64/c3_emoji-emoticon-face-bored-tired-angry-drop-512.png"));
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
