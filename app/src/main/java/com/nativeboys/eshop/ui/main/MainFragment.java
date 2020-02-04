package com.nativeboys.eshop.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.ui.main.conversations.ConversationsFragment;
import com.nativeboys.eshop.ui.main.products.ProductsFragment;
import com.nativeboys.eshop.ui.main.profile.ProfileFragment;

public class MainFragment extends Fragment {

    private ViewPager view_pager;
    private BottomNavigationView navigation_bar;
    private MainPagerAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
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
        view_pager = view.findViewById(R.id.view_pager);
        navigation_bar = view.findViewById(R.id.navigation_bar);
        adapter = new MainPagerAdapter(getChildFragmentManager());
        view_pager.setAdapter(adapter);
        setUpListeners();
    }

    private void setUpListeners() {
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) { }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0 : {
                        navigation_bar.setSelectedItemId(R.id.products);
                        break;
                    }
                    case 1 : {
                        navigation_bar.setSelectedItemId(R.id.chat);
                        break;
                    }
                    case 2 : {
                        navigation_bar.setSelectedItemId(R.id.profile);
                        break;
                    }
                    default: break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });

        navigation_bar.setOnNavigationItemSelectedListener(menuItem -> {
            int index = view_pager.getCurrentItem();
            switch (menuItem.getItemId()) {
                case R.id.products: {
                    if (index != 0 && adapter.getCount() > 0) {
                        view_pager.setCurrentItem(0);
                    }
                    break;
                }
                case R.id.chat: {
                    if (index != 1 && adapter.getCount() > 1) {
                        view_pager.setCurrentItem(1);
                    }
                    break;
                }
                case R.id.profile: {
                    if (index != 2 && adapter.getCount() > 2) {
                        view_pager.setCurrentItem(2);
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

}
