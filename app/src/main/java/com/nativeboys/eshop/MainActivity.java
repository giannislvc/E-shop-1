package com.nativeboys.eshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.nativeboys.eshop.conversations.ConversationsFragment;
import com.nativeboys.eshop.products.ProductsFragment;
import com.nativeboys.eshop.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    /*Static fields are executed before the constructor (before an instance of this class)
    now we bypass the following exception -> "Calls to setPersistenceEnabled() must be made before any other usage of FirebaseDatabase instance"*/
    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private ViewPager view_pager;
    private BottomNavigationView navigation_bar;
    private MainPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_pager = findViewById(R.id.view_pager);
        navigation_bar = findViewById(R.id.navigation_bar);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
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
