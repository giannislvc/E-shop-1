package com.nativeboys.eshop.ui.main.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class OptionsFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private SettingsViewModel settingsVM;
    private NavController parentNavController;

    private TextView sortByCell, categoryCell;

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
        sortByCell = view.findViewById(R.id.sort_by_cell);
        categoryCell = view.findViewById(R.id.category_cell);

        parentNavController = Navigation.findNavController(view);
        NavBackStackEntry backStackEntry = parentNavController.getBackStackEntry(R.id.settings_nav_graph);
        settingsVM = new ViewModelProvider(backStackEntry).get(SettingsViewModel.class);
        setUpListeners();
    }

    private void setUpListeners() {
        sortByCell.setOnClickListener(view ->
                parentNavController.navigate(OptionsFragmentDirections.actionOptionsToDetails(settingsVM.SORT)));

        categoryCell.setOnClickListener(view ->
                parentNavController.navigate(OptionsFragmentDirections.actionOptionsToDetails(settingsVM.CATEGORIES)));
    }

}
