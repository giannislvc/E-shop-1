package com.nativeboys.eshop.ui.main.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.customViews.AutoCompleteListener;
import com.nativeboys.eshop.customViews.AutoCompleteSearchView;
import com.nativeboys.eshop.customViews.NonScrollLayoutManager;
import com.nativeboys.eshop.tools.GlobalViewModel;

public class SearchFragment extends Fragment {

    private GlobalViewModel globalVM;
    private NavController navController;

    private SearchAdapter searchAdapter;
    private RecentSearchAdapter recentSearchAdapter;
    private SearchedProductsAdapter recentViewedAdapter, mostPopularAdapter;

    private ImageView clearText, backArrow;
    private TextView viewAllField;
    private RecyclerView searchesRecyclerView;
    private AutoCompleteSearchView searchField;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            globalVM = new ViewModelProvider(getActivity()).get(GlobalViewModel.class);
        }
    }

    private void navigateTo(int resource, @NonNull String argument) {
        if (resource == R.id.textSearchFragment) {
            navController.navigate(SearchFragmentDirections.actionSearchToTextSearch(argument));
        } else if (resource == R.id.productFragment) {
            // TODO: Implement
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        backArrow = view.findViewById(R.id.back_arrow);
        viewAllField = view.findViewById(R.id.view_all_field);
        RecyclerView viewsRecyclerView = view.findViewById(R.id.views_recycler_view);
        RecyclerView popularRecyclerView = view.findViewById(R.id.popular_recycler_view);
        searchField = view.findViewById(R.id.search_field);
        searchesRecyclerView = view.findViewById(R.id.searches_recycler_view);
        clearText = view.findViewById(R.id.clear_text);

        searchAdapter = new SearchAdapter(view.getContext());
        searchField.setAdapter(searchAdapter);

        searchesRecyclerView.setLayoutManager(new NonScrollLayoutManager(searchesRecyclerView.getContext()));
        recentSearchAdapter = new RecentSearchAdapter();
        searchesRecyclerView.setAdapter(recentSearchAdapter);

        viewsRecyclerView.setLayoutManager(new LinearLayoutManager(viewsRecyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        recentViewedAdapter = new SearchedProductsAdapter();
        viewsRecyclerView.setAdapter(recentViewedAdapter);

        popularRecyclerView.setLayoutManager(new LinearLayoutManager(viewsRecyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mostPopularAdapter = new SearchedProductsAdapter();
        popularRecyclerView.setAdapter(mostPopularAdapter);

        setUpListeners();
    }

    private void setUpListeners() {

        backArrow.setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        viewAllField.setOnClickListener(v -> {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) searchesRecyclerView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            searchesRecyclerView.setLayoutParams(params);
            v.setVisibility(View.INVISIBLE);
        });

        clearText.setOnClickListener(v -> searchField.setText(null));

        searchAdapter.setOnItemClickListener(searchField, model -> {
            if (model != null) navigateTo(R.id.textSearchFragment, model);
        });

        if (globalVM == null) return;

        globalVM.refreshMostPopular();

        globalVM.getSearches().observe(getViewLifecycleOwner(), strings -> searchAdapter.setDataSet(strings));

        searchField.setAutoCompleteListener(new AutoCompleteListener() {
            @Override
            public void onTextChanged(@NonNull EditText editText, String text) {
                globalVM.getTextSearch().setValue(text);
            }

            @Override
            public void onSearchClicked(@NonNull EditText editText, String text) {
                globalVM.getTextSearch().setValue(text);
                navigateTo(R.id.textSearchFragment, text);
            }
        });

        globalVM.getTextSearch().observe(getViewLifecycleOwner(), s -> {
            boolean enabled = (s != null && !s.isEmpty());
            clearText.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
        });

        globalVM.getPopularProducts().observe(getViewLifecycleOwner(),
                products -> mostPopularAdapter.submitList(products));

        globalVM.getSearchHistory().observe(getViewLifecycleOwner(),
                strings -> recentSearchAdapter.setDataSet(strings));

        globalVM.getProductHistory().observe(getViewLifecycleOwner(),
                products -> recentViewedAdapter.submitList(products));
    }

}
