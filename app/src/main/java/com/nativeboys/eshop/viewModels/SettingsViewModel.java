package com.nativeboys.eshop.viewModels;

import android.app.Application;
import android.util.Log;

import com.nativeboys.eshop.callbacks.CompletionHandler;
import com.nativeboys.eshop.http.Repository;
import com.nativeboys.eshop.models.SettingsModel;
import com.nativeboys.eshop.models.adapter.SortModel;
import com.nativeboys.eshop.models.node.Category;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class SettingsViewModel extends AndroidViewModel {

    private final String TAG = getClass().getSimpleName();
    public final static int CATEGORIES = 0, SORT = 1;

    private final Repository repository;

    private MutableLiveData<List<Category>> categories;
    private LiveData<Category> selectedCategory;

    private MutableLiveData<List<SortModel>> sort;
    private LiveData<SortModel> selectedSort;

    {
        categories = new MutableLiveData<>();
        selectedCategory = Transformations.map(categories, this::getSelectedCategory);
        sort = new MutableLiveData<>();
        selectedSort = Transformations.map(sort, this::getSelectedSort);
    }

    private Category getSelectedCategory(@Nullable List<Category> list) {
        return list != null ? SettingsModel.getSelectedItem(list) : null;
    }

    private SortModel getSelectedSort(@Nullable List<SortModel> list) {
        return list != null ? SettingsModel.getSelectedItem(list) : null;
    }

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
        fetchCategories();
        sort.setValue(SortModel.getTypes());
    }

    private void fetchCategories() {
        repository.getCategories(new CompletionHandler<List<Category>>() {
            @Override
            public void onSuccess(@NonNull List<Category> model) {
                categories.setValue(model);
            }

            @Override
            public void onFailure(@Nullable String description) {
                categories.setValue(new ArrayList<>());
            }
        });
    }

    public <T extends SettingsModel> void setSelectedSetting(int type, @Nullable T model) {
        String id = model != null ? model.getId() : null;
        if (type == CATEGORIES) {
            List<Category> categories = getCategories().getValue();
            if (categories != null) {
                this.categories.setValue(SettingsModel.setSelectedItem(categories, id));
            }
        } else if (type == SORT) {
            List<SortModel> sort = getSort().getValue();
            if (sort != null) {
                this.sort.setValue(SettingsModel.setSelectedItem(sort, id));
            }
        }
    }

    public void clearSelectedSettings() {
        setSelectedSetting(CATEGORIES, null);
        setSelectedSetting(SORT, null);
    }

    public MutableLiveData<List<Category>> getCategories() {
        return categories;
    }

    public MutableLiveData<List<SortModel>> getSort() {
        return sort;
    }

    public LiveData<Category> getSelectedCategory() {
        return selectedCategory;
    }

    public LiveData<SortModel> getSelectedSort() {
        return selectedSort;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "onCleared: ");
    }
}
