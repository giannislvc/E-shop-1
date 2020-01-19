package com.nativeboys.eshop.ui.main.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.customViews.AutoCompleteInputView;
import com.nativeboys.eshop.customViews.NonScrollLayoutManager;
import com.nativeboys.eshop.tools.GlobalViewModel;

public class SearchFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private GlobalViewModel globalVM;

    private AutoCompleteInputView searchField;
    private SearchAdapter searchAdapter;

    private ImageView clearText;

    private RecyclerView searchesRecyclerView;
    private RecentSearchAdapter recentSearchAdapter;
    private RecentViewAdapter recentViewAdapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            globalVM = ViewModelProviders.of(getActivity()).get(GlobalViewModel.class);
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
        TextView viewAllField = view.findViewById(R.id.view_all_field);
        RecyclerView viewsRecyclerView = view.findViewById(R.id.views_recycler_view);
        searchField = view.findViewById(R.id.search_field);
        searchesRecyclerView = view.findViewById(R.id.searches_recycler_view);
        clearText = view.findViewById(R.id.clear_text);

        viewAllField.setOnClickListener(v -> {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) searchesRecyclerView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            searchesRecyclerView.setLayoutParams(params);
            v.setVisibility(View.INVISIBLE);
        });

        searchAdapter = new SearchAdapter(view.getContext());
        searchField.setAdapter(searchAdapter);
        searchAdapter.setOnItemSelectedListener(searchField, model -> {
            Log.i(TAG, "onViewCreated: " + model);
        });

        searchesRecyclerView.setLayoutManager(new NonScrollLayoutManager(searchesRecyclerView.getContext()));
        recentSearchAdapter = new RecentSearchAdapter();
        searchesRecyclerView.setAdapter(recentSearchAdapter);

        viewsRecyclerView.setLayoutManager(new LinearLayoutManager(viewsRecyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        recentViewAdapter = new RecentViewAdapter();
        viewsRecyclerView.setAdapter(recentViewAdapter);

        clearText.setOnClickListener(v -> searchField.setText(null));

        if (globalVM == null) return;

        globalVM.getSearches().observe(this, strings -> searchAdapter.setDataSet(strings));

        searchField.setOnTextChangedListener((editText, text) -> globalVM.getTextSearch().setValue(text));

        globalVM.getTextSearch().observe(this, s -> {
            boolean enabled = (s != null && !s.isEmpty());
            clearText.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
        });

        globalVM.getSearchHistory().observe(this, strings -> recentSearchAdapter.setDataSet(strings));

        globalVM.getProductHistory().observe(this, products -> recentViewAdapter.submitList(products));
    }
}
