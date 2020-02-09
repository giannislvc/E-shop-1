package com.nativeboys.eshop.paging;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.nativeboys.eshop.models.node.Product;
import com.nativeboys.eshop.models.query.Filter;
import com.nativeboys.eshop.models.query.Sort;

public class ProductDataSourceFactory extends DataSource.Factory<Integer, Product> {

    //private MutableLiveData<PageKeyedDataSource<Integer, Product>> productLiveDataSource;

    private Sort sort;
    private Filter filter;
    private String customerId;

    public ProductDataSourceFactory(@NonNull String customerId, @NonNull Filter filter, @NonNull Sort sort) {
        this.filter = filter;
        this.customerId = customerId;
        this.sort = sort;
        //productLiveDataSource = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource<Integer, Product> create() {
        //ProductsDataSource productDataSource = new ProductsDataSource(customerId, filter, sort);
        //productLiveDataSource.postValue(productDataSource);
        return new ProductsDataSource(customerId, filter, sort);
    }


/*    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getProductLiveDataSource() {
        return productLiveDataSource;
    }*/
}
