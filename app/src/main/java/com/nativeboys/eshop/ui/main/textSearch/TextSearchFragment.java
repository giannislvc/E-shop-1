package com.nativeboys.eshop.ui.main.textSearch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import com.nativeboys.eshop.viewModels.textSearch.TextSearchViewModel;
import com.nativeboys.eshop.viewModels.textSearch.TextSearchViewModelFactory;

public class TextSearchFragment extends Fragment {

    private TextSearchViewModel textSearchVM;
    private NavController navController;

    private ImageView backBtn;
    private TextView headline;
    private ProductsAdapter adapter;

    public TextSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String text = null;
        if (getArguments() != null) {
            TextSearchFragmentArgs args = TextSearchFragmentArgs.fromBundle(getArguments());
            text = args.getText();
        }
        TextSearchViewModelFactory factory = null;
        if (getActivity() != null) {
            factory = new TextSearchViewModelFactory(getActivity().getApplication(), text);
        }
        textSearchVM = (factory != null) ?
                new ViewModelProvider(this, factory).get(TextSearchViewModel.class) :
                new ViewModelProvider(this).get(TextSearchViewModel.class);
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
        navController = Navigation.findNavController(view);
        backBtn = view.findViewById(R.id.back_btn);
        headline = view.findViewById(R.id.headline);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
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
        backBtn.setOnClickListener(view -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        textSearchVM.getText().observe(getViewLifecycleOwner(), s -> headline.setText(s));

        textSearchVM.getProducts().observe(getViewLifecycleOwner(), products -> adapter.submitList(products));

        adapter.setOnProductClickListener((itemView, product) -> {
            String productId = product.getProductId();
            navController.navigate(TextSearchFragmentDirections.actionTextSearchToProduct(productId));
        });
    }

}
