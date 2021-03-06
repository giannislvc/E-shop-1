package com.nativeboys.eshop.ui.main.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.adapter.SortModel;
import com.nativeboys.eshop.models.node.Category;
import com.nativeboys.eshop.viewModels.SettingsViewModel;

public class SettingsFragment extends Fragment implements NavController.OnDestinationChangedListener {

    public interface OnUserInteractionListener {
        void onSubmit(@Nullable Category category, @Nullable SortModel sort);
        void onClear();
    }

    private SettingsViewModel settingsVM;
    private OnUserInteractionListener onUserInteraction;
    private NavController nestedNavController;

    private ImageView backBtn;
    private Button clearBtn, submitBtn;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearBtn = view.findViewById(R.id.clear_btn);
        submitBtn = view.findViewById(R.id.submit_btn);
        backBtn = view.findViewById(R.id.back_btn);
        nestedNavController = Navigation.findNavController(view.findViewById(R.id.settings_nav_host));
        NavBackStackEntry backStackEntry = nestedNavController.getBackStackEntry(R.id.settings_nav_graph);
        settingsVM = new ViewModelProvider(backStackEntry).get(SettingsViewModel.class);
        setUpListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        nestedNavController.addOnDestinationChangedListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        nestedNavController.removeOnDestinationChangedListener(this);
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        boolean visible = R.id.detailsFragment == destination.getId();
        backBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setOnUserInteraction(OnUserInteractionListener onUserInteraction) {
        this.onUserInteraction = onUserInteraction;
    }

    private void setUpListeners() {

        settingsVM.getSelectedCategory().observe(getViewLifecycleOwner(), category -> {
            // TODO: Replace workaround
        });

        settingsVM.getSelectedSort().observe(getViewLifecycleOwner(), sort -> {
            // TODO: Replace workaround
        });

        clearBtn.setOnClickListener(view -> {
            if (onUserInteraction != null) {
                settingsVM.clearSelectedSettings();
                onUserInteraction.onClear();
            }
        });
        submitBtn.setOnClickListener(view -> {
            if (onUserInteraction != null) {
                onUserInteraction.onSubmit(
                        settingsVM.getSelectedCategory().getValue(),
                        settingsVM.getSelectedSort().getValue()
                );
            }
        });
        backBtn.setOnClickListener(view ->
                nestedNavController.navigate(R.id.action_details_to_options));
    }

}
