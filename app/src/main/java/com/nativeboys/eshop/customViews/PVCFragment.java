package com.nativeboys.eshop.customViews;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

public class PVCFragment extends Fragment {

    private NavController parentNavController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentNavController = initParentNavController();
    }

    protected NavController getParentNavController() {
        return parentNavController;
    }

    @Nullable
    private NavController initParentNavController() {
        NavHostFragment navHostFragment = (NavHostFragment) getParentFragment();
        if (navHostFragment != null) {
            Fragment parent = navHostFragment.getParentFragment();
            if (parent != null && parent.getView() != null) {
                return Navigation.findNavController(parent.getView());
            }
        }
        return null;
    }

}
