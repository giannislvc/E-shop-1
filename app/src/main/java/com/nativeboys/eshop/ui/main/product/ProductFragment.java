package com.nativeboys.eshop.ui.main.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.customViews.FullDialogFragment;
import com.nativeboys.eshop.customViews.ScaleLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends FullDialogFragment {

    private ProductImageAdapter adapter;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new ProductImageAdapter();
        recyclerView.setLayoutManager(new ScaleLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        setDummyData();
    }

    private void setDummyData() {
        List<String> list = new ArrayList<>();
        list.add("https://m.media-amazon.com/images/I/51EQLDJSQwL.jpg");
        list.add("https://d.scdn.gr/images/sku_main_images/012190/12190723/20180208170447_assassin_s_creed_origins_pc.jpeg");
        list.add("https://b.scdn.gr/images/sku_main_images/007278/7278151/20150805172006_batman_arkham_asylum_game_of_the_year_edition_pc.jpeg");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcR5bhEvbwDSgxWR8lOOOTpMPrbAhlxHlT4B6pBRYk0yiYJ0L3G4");
        list.add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTFZIFAz0_T9x3qzeH4nKRTrOjl8ud88Y_JPKPmPDVrM-ZCXTyv");
        list.add("https://images-na.ssl-images-amazon.com/images/I/71CgLmxtR9L._AC_SX215_.jpg");
        list.add("https://b.scdn.gr/images/sku_main_images/003077/3077898/20161021164004_battlefield_4_pc.jpeg");
        adapter.submitList(list);
    }

}
