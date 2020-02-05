package com.nativeboys.eshop.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.tools.GlobalViewModel;

import java.util.HashMap;
import java.util.Map;

public class MainFragment extends Fragment {

    private GlobalViewModel globalVM;
    private NavController nestedNavController, parentNavController;
    private BottomNavigationView navigationBar;

    private Map<Integer, Integer> map = new HashMap<Integer, Integer>() {{
        put(R.id.productsFragment, R.id.products);
        put(R.id.conversationsFragment, R.id.chat);
        put(R.id.profileFragment, R.id.profile);
    }};

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVM = new ViewModelProvider(getActivity() != null ? getActivity() : this)
                .get(GlobalViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nestedNavController = Navigation.findNavController(view.findViewById(R.id.nested_nav_host));
        parentNavController = Navigation.findNavController(view);
        navigationBar = view.findViewById(R.id.navigation_bar);
        setUpListeners();
    }

    private int getFragmentIdByMenu(int menuId) {
        Integer index = null;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == menuId) {
                index = entry.getKey();
                break;
            }
        }
        return index != null ? index : -1;
    }

    private void navigate(int currentFragId, int destinationFragId) {
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(currentFragId, true).build();
        nestedNavController.navigate(destinationFragId, null, navOptions);
    }

    private void setUpListeners() {
        navigationBar.setOnNavigationItemSelectedListener(menuItem -> {
            NavDestination currentFragment = nestedNavController.getCurrentDestination();
            if (currentFragment == null) return false;
            int currentFragId = currentFragment.getId();
            int destinationFragId = getFragmentIdByMenu(menuItem.getItemId());
            if (destinationFragId == -1 || destinationFragId == currentFragId) return false;
            navigate(currentFragId, destinationFragId);
            return true;
        });
    }

}
