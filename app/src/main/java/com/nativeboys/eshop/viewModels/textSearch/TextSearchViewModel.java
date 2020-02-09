package com.nativeboys.eshop.viewModels.textSearch;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.nativeboys.eshop.models.node.Product;
import com.nativeboys.eshop.models.query.Filter;
import com.nativeboys.eshop.models.query.Sort;
import com.nativeboys.eshop.paging.produts.ProductDataSourceFactory;
import com.nativeboys.eshop.viewModels.FirebaseClientProvider;

public class TextSearchViewModel extends AndroidViewModel {

    private final String clientId;

    private MutableLiveData<String> text;
    private LiveData<PagedList<Product>> productsPagedList;

    {
        text = new MutableLiveData<>();
        productsPagedList = Transformations.switchMap(text, this::getLiveProducts);
    }

    TextSearchViewModel(@NonNull Application application, @NonNull String text) {
        super(application);
        clientId = FirebaseClientProvider.getInstance().getFirebaseUserId();
        this.text.setValue(text);
    }

    public LiveData<String> getText() {
        return text;
    }

    public LiveData<PagedList<Product>> getProducts() {
        return productsPagedList;
    }

    private LiveData<PagedList<Product>> getLiveProducts(@NonNull String text) {
        int defaultLimit = 10;
        ProductDataSourceFactory dataSourceFactory =
                new ProductDataSourceFactory(
                        clientId,
                        new Filter(null, text),
                        new Sort(defaultLimit, 0, 3, false));

        PagedList.Config config =
                new PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setPageSize(defaultLimit)
                    .build();
        return new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }
}
