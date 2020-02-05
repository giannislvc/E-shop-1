package com.nativeboys.eshop.ui.main.searchSettings;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.models.adapter.SortModel;
import com.nativeboys.eshop.models.node.Category;
import com.nativeboys.eshop.tools.GlobalViewModel;

public class SearchSettingsFragment extends DialogFragment {

    public interface OnUserApplyListener {
        void onUserApply(@NonNull String categoryId, int order);
    }

    private GlobalViewModel globalVM;
    private OnUserApplyListener onUserApplyListener;

    private Spinner sSpinner, cSpinner;
    private SortAdapter sAdapter;
    private CategoryAdapter cAdapter;

    private Button cancelBtn, applyBtn;

    public SearchSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVM = new ViewModelProvider(getActivity() != null ?
                getActivity() : this).get(GlobalViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sSpinner = view.findViewById(R.id.sort_by_spinner);
        cSpinner = view.findViewById(R.id.category_spinner);
        applyBtn = view.findViewById(R.id.apply_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);

        sAdapter = new SortAdapter(view.getContext());
        cAdapter = new CategoryAdapter(view.getContext());

        sSpinner.setAdapter(sAdapter);
        cSpinner.setAdapter(cAdapter);
        setUpListeners();
    }

    @Nullable
    private String getSelectedCategoryId() {
        Object selectedItem = cSpinner.getSelectedItem();
        if (selectedItem instanceof Category) {
            Category category = (Category) selectedItem;
            return category.getCategoryId();
        }
        return null;
    }

    @Nullable
    private Integer getSelectedOrder() {
        Object selectedItem = sSpinner.getSelectedItem();
        if (selectedItem instanceof SortModel) {
            SortModel sortByModel = (SortModel) selectedItem;
            return sortByModel.getNumericId();
        }
        return null;
    }


    private void setUpListeners() {
        globalVM.refreshGategories();
        sAdapter.setDataSet(SortModel.getTypes());
        globalVM.getCategories().observe(getViewLifecycleOwner(), categories ->
                cAdapter.setDataSet(categories));
        cancelBtn.setOnClickListener(v -> dismiss());
        applyBtn.setOnClickListener(v -> {
            if (onUserApplyListener != null) {
                Integer order = getSelectedOrder();
                String categoryId = getSelectedCategoryId();
                if (categoryId != null && order != null) {
                    onUserApplyListener.onUserApply(categoryId, order);
                    dismiss();
                }
            }
        });
    }

    public void setOnUserApplyListener(OnUserApplyListener onUserApplyListener) {
        this.onUserApplyListener = onUserApplyListener;
    }

    @Override
    public void onResume() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            Point size = new Point();
            if (window != null) {
                Display display = window.getWindowManager().getDefaultDisplay();
                display.getSize(size);
                window.setLayout((int) (size.x * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
            }
        }
        super.onResume();
    }

}
