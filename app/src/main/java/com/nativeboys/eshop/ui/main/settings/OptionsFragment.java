package com.nativeboys.eshop.ui.main.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.viewModels.SettingsViewModel;

import static com.nativeboys.eshop.viewModels.SettingsViewModel.CATEGORIES;
import static com.nativeboys.eshop.viewModels.SettingsViewModel.SORT;

public class OptionsFragment extends Fragment {

    private SettingsViewModel settingsVM;
    private NavController parentNavController;

    private ConstraintLayout sbContainer, cContainer;
    private TextView selectedSbField, selectedCfield;


    public OptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_options, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sbContainer = view.findViewById(R.id.sb_container);
        cContainer = view.findViewById(R.id.c_container);
        selectedSbField = view.findViewById(R.id.selected_sb_field);
        selectedCfield = view.findViewById(R.id.selected_c_field);

        parentNavController = Navigation.findNavController(view);
        NavBackStackEntry backStackEntry = parentNavController.getBackStackEntry(R.id.settings_nav_graph);
        settingsVM = new ViewModelProvider(backStackEntry).get(SettingsViewModel.class);
        setUpListeners();
    }

    private void setUpListeners() {
        sbContainer.setOnClickListener(view ->
                parentNavController.navigate(OptionsFragmentDirections.actionOptionsToDetails(SORT)));

        cContainer.setOnClickListener(view ->
                parentNavController.navigate(OptionsFragmentDirections.actionOptionsToDetails(CATEGORIES)));

        settingsVM.getSelectedCategory().observe(getViewLifecycleOwner(), category -> {
            String description = category != null ? category.getName() : null;
            selectedCfield.setText(description);
        });

        settingsVM.getSelectedSort().observe(getViewLifecycleOwner(), sort -> {
            String description = sort != null ? sort.getDescription() : null;
            selectedSbField.setText(description);
        });
    }

}
