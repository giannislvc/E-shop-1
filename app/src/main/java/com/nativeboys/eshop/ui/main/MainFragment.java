package com.nativeboys.eshop.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.adapter.SortModel;
import com.nativeboys.eshop.models.node.Category;
import com.nativeboys.eshop.tools.GlobalViewModel;
import com.nativeboys.eshop.ui.main.conversations.ConversationsFragment;
import com.nativeboys.eshop.ui.main.products.ProductsFragment;
import com.nativeboys.eshop.ui.main.profile.ProfileFragment;
import com.nativeboys.eshop.ui.main.settings.SettingsFragment;

public class MainFragment extends Fragment implements SettingsFragment.OnUserInteractionListener {

    private GlobalViewModel globalVM;

    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView navigationBar;
    private MainPagerAdapter adapter;

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
        return inflater.inflate(R.layout.fragment_main_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        viewPager = view.findViewById(R.id.view_pager);
        navigationView = view.findViewById(R.id.navigation_view);
        navigationBar = view.findViewById(R.id.navigation_bar);
        adapter = new MainPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        setUpListeners();
    }

    private void setUpListeners() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0 : {
                        navigationBar.setSelectedItemId(R.id.products);
                        break;
                    }
                    case 1 : {
                        navigationBar.setSelectedItemId(R.id.chat);
                        break;
                    }
                    case 2 : {
                        navigationBar.setSelectedItemId(R.id.profile);
                        break;
                    }
                    default: break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });

        navigationBar.setOnNavigationItemSelectedListener(menuItem -> {
            int index = viewPager.getCurrentItem();
            switch (menuItem.getItemId()) {
                case R.id.products: {
                    if (index != 0 && adapter.getCount() > 0) {
                        viewPager.setCurrentItem(0);
                    }
                    break;
                }
                case R.id.chat: {
                    if (index != 1 && adapter.getCount() > 1) {
                        viewPager.setCurrentItem(1);
                    }
                    break;
                }
                case R.id.profile: {
                    if (index != 2 && adapter.getCount() > 2) {
                        viewPager.setCurrentItem(2);
                    }
                    break;
                }
                default: break;
            }
            return true;
        });
    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {

        MainPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1 : {
                    return new ConversationsFragment();
                }
                case 2 : {
                    return new ProfileFragment();
                }
                default: return new ProductsFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

    }

    @Override
    public void onSubmit(@Nullable Category category, @Nullable SortModel sort) {
        updateAndClose(category, sort);
    }

    @Override
    public void onClear() {
        updateAndClose(null, null);
    }

    private void updateAndClose(@Nullable Category category, @Nullable SortModel sort) {
        globalVM.updateSearch(
                category != null ? category.getCategoryId() : null,
                sort != null ? sort.getNumericId() : -1
        );
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof SettingsFragment) {
            SettingsFragment fragment = (SettingsFragment) childFragment;
            fragment.setOnUserInteraction(this);
        }
    }
}
