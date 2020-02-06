package com.nativeboys.eshop.ui.login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.tools.GlobalViewModel;

public class LoginFragment extends Fragment implements OnFragmentTransaction {

    private FragmentActivity activity;
    private NavController navController;
    private GlobalViewModel globalVM;

    private ViewPager viewPager;
    private LoginPagerAdapter adapter;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVM = new ViewModelProvider(activity).get(GlobalViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        viewPager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        adapter = new LoginPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        if (globalVM.isUserLoggedIn()) toMainMenu();
    }

    private void toMainMenu() {
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build();
        navController.navigate(R.id.mainFragment, null, navOptions);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof SignInFragment) {
            SignInFragment signInFragment = (SignInFragment) childFragment;
            signInFragment.setOnFragmentTransactionListener(this);
        } else if (childFragment instanceof SignUpFragment) {
            SignUpFragment signUpFragment = (SignUpFragment) childFragment;
            signUpFragment.setOnFragmentTransactionListener(this);
        }
    }

    @Override
    public void moveToRegister() {
        if (adapter.getCount() > 0) viewPager.setCurrentItem(1);
    }

    @Override
    public void moveToLogin() {
        viewPager.setCurrentItem(0);
    }

    @Override
    public void moveToMainMenu() {
        toMainMenu();
    }

    private class LoginPagerAdapter extends FragmentStatePagerAdapter {

        LoginPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new SignInFragment();
            } else {
                return new SignUpFragment();
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(position == 0 ? R.string.login : R.string.register);
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

}
