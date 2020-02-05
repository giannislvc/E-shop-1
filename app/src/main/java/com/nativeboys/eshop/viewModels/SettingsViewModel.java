package com.nativeboys.eshop.viewModels;

import android.app.Application;
import android.util.Log;

import com.nativeboys.eshop.callbacks.CompletionHandler;
import com.nativeboys.eshop.http.Repository;
import com.nativeboys.eshop.models.adapter.SortModel;
import com.nativeboys.eshop.models.node.Category;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SettingsViewModel extends AndroidViewModel {

    private final String TAG = getClass().getSimpleName();
    public final int CATEGORIES = 0, SORT = 1;

    private final Repository repository;
    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<List<SortModel>> sort;

    //private MutableLiveData<DetailModel> categoryDetails;

    {
        categories = new MutableLiveData<>();
        sort = new MutableLiveData<>();
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

    public MutableLiveData<List<Category>> getCategories() {
        return categories;
    }

    public MutableLiveData<List<SortModel>> getSort() {
        return sort;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "onCleared: ");
    }
}
