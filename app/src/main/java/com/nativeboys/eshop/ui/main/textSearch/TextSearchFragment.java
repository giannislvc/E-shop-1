package com.nativeboys.eshop.ui.main.textSearch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.ui.main.adapters.ProductsAdapter;
import com.nativeboys.eshop.ui.main.product.ProductFragment;

public class TextSearchFragment extends Fragment {

    private ImageView backArrow;
    private TextView headline;
    private RecyclerView recyclerView;
    private ProductsAdapter adapter;

    public TextSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backArrow = view.findViewById(R.id.back_arrow);
        headline = view.findViewById(R.id.headline);
        recyclerView = view.findViewById(R.id.recycler_view);

        adapter = new ProductsAdapter();
        recyclerView.setAdapter(adapter);
        FlexboxLayoutManager flexManager = new FlexboxLayoutManager(recyclerView.getContext());
        flexManager.setFlexDirection(FlexDirection.ROW);
        flexManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(flexManager);
        recyclerView.setHasFixedSize(true);

        setUpListeners();
    }

    private void setUpListeners() {
        adapter.setOnProductClickListener((itemView, product) -> {
            // TODO: Implement
            /*String userId = globalVM.getUserId();
            if (userId != null) {
                ProductFragment.newInstance(userId, product.getProductId())
                        .show(getChildFragmentManager(), ProductFragment.class.getSimpleName());
            }*/
        });
    }

}
