package com.nativeboys.eshop.ui.main.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.customViews.PVCFragment;
import com.nativeboys.eshop.models.adapter.SortModel;
import com.nativeboys.eshop.models.node.Category;
import com.nativeboys.eshop.tools.GlobalViewModel;
import com.nativeboys.eshop.ui.main.MainFragmentDirections;
import com.nativeboys.eshop.ui.main.adapters.ProductsAdapter;
import com.nativeboys.eshop.ui.main.settings.SettingsFragment;

public class ProductsFragment extends PVCFragment implements SettingsFragment.OnUserInteractionListener {

    private GlobalViewModel globalVM;

    private SwipeRefreshLayout swipeRefresh;
    private DrawerLayout drawerLayout;
    private ProductsAdapter adapter;
    private LinearLayout searchBar;
    private ImageView settingsButton;
    private FloatingActionButton addProductButton;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVM = new ViewModelProvider(getActivity() != null ? getActivity() : this)
                .get(GlobalViewModel.class);
        globalVM.refreshProducts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefresh = view.findViewById(R.id.swipe_refresh_layout);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        addProductButton = view.findViewById(R.id.add_product_button);
        settingsButton = view.findViewById(R.id.settings_button);
        searchBar = view.findViewById(R.id.search_bar);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        swipeRefresh.setColorSchemeColors(view.getResources().getColor(R.color.colorPrimary));

        adapter = new ProductsAdapter();
        recyclerView.setAdapter(adapter);
        FlexboxLayoutManager flexManager = new FlexboxLayoutManager(recyclerView.getContext());
        flexManager.setFlexDirection(FlexDirection.ROW);
        flexManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(flexManager);
        recyclerView.setHasFixedSize(true);

        setUpListeners();
    }

    private void navigateTo(int resource, String productId) {
        NavController parentController = getParentNavController();
        if (parentController == null) return;
        if (resource == R.id.productFragment) {
            parentController.navigate(MainFragmentDirections.actionMainToProduct(productId));
        } else {
            parentController.navigate(resource);
        }
    }

    private void navigateTo(int resource) {
        navigateTo(resource, null);
    }

    private void setUpListeners() {
        swipeRefresh.setOnRefreshListener(() -> globalVM.refreshProducts());
        settingsButton.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        searchBar.setOnClickListener(v -> navigateTo(R.id.action_main_to_search));
        addProductButton.setOnClickListener(v -> navigateTo(R.id.productFragment));
        globalVM.getProductPagedList().observe(getViewLifecycleOwner(), products -> {
            if (swipeRefresh.isRefreshing()) swipeRefresh.setRefreshing(false);
            adapter.submitList(products);
        });

        adapter.setOnProductClickListener((itemView, product) ->
                navigateTo(R.id.productFragment, product.getProductId()));
    }

    private void updateAndClose(@Nullable Category category, @Nullable SortModel sort) {
        drawerLayout.closeDrawer(GravityCompat.START);
        globalVM.updateSearch(
                category != null ? category.getCategoryId() : null,
                sort != null ? sort.getNumericId() : -1
        );
    }

    @Override
    public void onSubmit(@Nullable Category category, @Nullable SortModel sort) {
        updateAndClose(category, sort);
    }

    @Override
    public void onClear() {
        updateAndClose(null, null);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof SettingsFragment) {
            SettingsFragment fragment = (SettingsFragment) childFragment;
            fragment.setOnUserInteraction(this);
        }
    }
}
