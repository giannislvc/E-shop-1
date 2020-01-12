package com.nativeboys.eshop.ui.main.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.node.Product;
import com.nativeboys.eshop.tools.GlobalViewModel;

import java.util.List;

public class SearchFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private RecentSearchAdapter searchAdapter;
    private RecentViewAdapter viewAdapter;

    public SearchFragment() {
        // Required empty public constructor
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
        EditText searchField = view.findViewById(R.id.search_field);
        RecyclerView searchesRecyclerView = view.findViewById(R.id.searches_recycler_view);
        RecyclerView viewsRecyclerView = view.findViewById(R.id.views_recycler_view);

        searchesRecyclerView.setLayoutManager(new LinearLayoutManager(searchesRecyclerView.getContext()));
        searchAdapter = new RecentSearchAdapter();
        searchesRecyclerView.setAdapter(searchAdapter);

        viewsRecyclerView.setLayoutManager(new LinearLayoutManager(viewsRecyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        viewAdapter = new RecentViewAdapter();
        viewsRecyclerView.setAdapter(viewAdapter);


        if (getActivity() != null) {
            GlobalViewModel viewModel = ViewModelProviders.of(getActivity()).get(GlobalViewModel.class);

            viewModel.getSearchHistory().observe(this, strings ->
                    searchAdapter.setDataSet(strings));

            viewModel.getProductHistory().observe(this, products ->
                    viewAdapter.submitList(products));
        }
    }
}
