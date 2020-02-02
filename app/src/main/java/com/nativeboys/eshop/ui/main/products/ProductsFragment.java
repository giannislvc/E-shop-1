package com.nativeboys.eshop.ui.main.products;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.tools.GlobalViewModel;
import com.nativeboys.eshop.ui.main.product.ProductFragment;

public class ProductsFragment extends Fragment {

    private NavController navController;
    private GlobalViewModel viewModel;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(GlobalViewModel.class);
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
        navController = Navigation.findNavController(view);
        FloatingActionButton addProductButton = view.findViewById(R.id.add_product_button);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayout searchBar = view.findViewById(R.id.search_bar);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#2fb7ec"));

        ProductsAdapter adapter = new ProductsAdapter();
        recyclerView.setAdapter(adapter);
        FlexboxLayoutManager flexManager = new FlexboxLayoutManager(recyclerView.getContext());
        flexManager.setFlexDirection(FlexDirection.ROW);
        flexManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(flexManager);
        recyclerView.setHasFixedSize(true);
        viewModel.getProductPagedList().observe(getViewLifecycleOwner(), adapter::submitList);

        searchBar.setOnClickListener(v ->
                navController.navigate(R.id.action_main_to_search));

        addProductButton.setOnClickListener(v -> {
            String userId = viewModel.getUserId();
            if (userId != null) {
                ProductFragment.newInstance(userId, null)
                        .show(getChildFragmentManager(), ProductFragment.class.getSimpleName());
            }
        });

        adapter.setOnProductClickListener((itemView, product) -> {
            String userId = viewModel.getUserId();
            if (userId != null) {
                ProductFragment.newInstance(userId, product.getProductId())
                        .show(getChildFragmentManager(), ProductFragment.class.getSimpleName());
            }
        });
    }
}
