package com.nativeboys.eshop.ui.main.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.viewModels.SettingsViewModel;

import static com.nativeboys.eshop.viewModels.SettingsViewModel.CATEGORIES;
import static com.nativeboys.eshop.viewModels.SettingsViewModel.SORT;

public class DetailsFragment extends Fragment {

    private int type = -1;
    private SettingsViewModel settingsVM;
    private DetailsAdapter adapter;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            DetailsFragmentArgs args = DetailsFragmentArgs.fromBundle(getArguments());
            type = args.getType();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController parentNavController = Navigation.findNavController(view);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        NavBackStackEntry backStackEntry = parentNavController.getBackStackEntry(R.id.settings_nav_graph);
        settingsVM = new ViewModelProvider(backStackEntry).get(SettingsViewModel.class);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new DetailsAdapter();
        recyclerView.setAdapter(adapter);
        setUpListeners();
    }

    private void setUpListeners() {
        if (type == CATEGORIES) {
            settingsVM.getCategories().observe(getViewLifecycleOwner(), categories ->
                    adapter.setDataSet(categories));
        } else if (type == SORT) {
            settingsVM.getSort().observe(getViewLifecycleOwner(), sort ->
                    adapter.setDataSet(sort));
        }
        adapter.setLister(model -> {
            settingsVM.setSelectedSetting(type, model);
            adapter.notifyDataSetChanged();
        });
    }


}
