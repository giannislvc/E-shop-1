package com.nativeboys.eshop.tools;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nativeboys.eshop.callbacks.CompletionHandler;
import com.nativeboys.eshop.http.Repository;
import com.nativeboys.eshop.models.adapter.SearchModel;
import com.nativeboys.eshop.models.firebase.MetaDataModel;
import com.nativeboys.eshop.models.node.Customer;
import com.nativeboys.eshop.models.node.DetailedCustomer;
import com.nativeboys.eshop.models.node.Product;
import com.nativeboys.eshop.models.query.Filter;
import com.nativeboys.eshop.models.query.Sort;
import com.nativeboys.eshop.models.query.StartLimit;
import com.nativeboys.eshop.paging.produts.ProductDataSourceFactory;
import com.nativeboys.eshop.paging.customerProducts.CustomerProductsDataSourceFactory;
import com.nativeboys.eshop.viewModels.FirebaseClientProvider;

import java.util.ArrayList;
import java.util.List;

public class GlobalViewModel extends AndroidViewModel {

    // TODO: offline - send message - another room chat_png - go online (check it ??)
    // TODO: new conversation offline, replace value events, DAO Firebase

    // private LiveData<PageKeyedDataSource<Integer, Product>> liveDataSource;

    final static String METADATA = "metadata";
    final static String CONVERSATIONS = "conversations";

    private final String clientId;
    private Repository repository;
    private DatabaseReference metadataRef;

    // Products Fragment
    private MutableLiveData<SearchModel> searchModel;
    private LiveData<PagedList<Product>> productPagedList;

    // Conversations Fragment
    private MutableLiveData<List<MetaDataModel>> metaData;

    // Search Fragment
    private MutableLiveData<List<Product>> popularProducts;
    private MutableLiveData<List<Product>> customerProductsHistory;
    private MutableLiveData<List<String>> customerSearchHistory;

    private MutableLiveData<String> textSearch;
    private LiveData<List<String>> searches;

    // Profile Fragment
    private MutableLiveData<DetailedCustomer> customerDetails;
    private MutableLiveData<String> customerId;
    private LiveData<PagedList<Product>> customerProducts;

    {
        searchModel = new MutableLiveData<>();
        productPagedList = Transformations.switchMap(searchModel, this::getLiveProducts);

        metaData = new MutableLiveData<>();

        popularProducts = new MutableLiveData<>();
        customerProductsHistory = new MutableLiveData<>();
        customerSearchHistory = new MutableLiveData<>();

        textSearch = new MutableLiveData<>();
        searches = Transformations.switchMap(textSearch, this::getSearch);

        customerDetails = new MutableLiveData<>();
        customerId = new MutableLiveData<>();
        customerProducts = Transformations.switchMap(customerId, this::getCustomerProducts);
    }

    private ValueEventListener metaDataListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<MetaDataModel> metaData = new ArrayList<>();
            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                String id = userSnapshot.getKey();
                if (id == null) continue;
                MetaDataModel meta = userSnapshot.getValue(MetaDataModel.class);
                if (meta != null) {
                    meta.setId(id);
                    metaData.add(meta);
                }
            }
            GlobalViewModel.this.metaData.setValue(metaData);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

    private static final SearchModel defaultSearch = new SearchModel(
            new Filter(null, "ps"),
            new Sort(10, 0, 3, false));

    public GlobalViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
        metadataRef = FirebaseDatabase.getInstance().getReference(METADATA);
        clientId = FirebaseClientProvider.getInstance().getFirebaseUserId();
        setListeners(true);
        searchModel.setValue(defaultSearch);
        fetchCustomerDetails();
    }

    public void fetchCustomerDetails() {
        repository.getCustomerDetails(clientId, new CompletionHandler<DetailedCustomer>() {
            @Override
            public void onSuccess(@NonNull DetailedCustomer model) {
                customerDetails.setValue(model);
            }

            @Override
            public void onFailure(@Nullable String description) { }
        });
    }

    public void fetchClientProduts() {
        customerId.setValue(clientId);
    }

    public void updateCustomer(@NonNull Uri uri) {
        DetailedCustomer customer = getCustomerDetails().getValue();
        if (customer != null) {
            repository.updateCustomer(getApplication(), uri, customer, new CompletionHandler<Customer>() {
                @Override
                public void onSuccess(@NonNull Customer model) {
                    fetchCustomerDetails();
                }

                @Override
                public void onFailure(@Nullable String description) { }
            });
        }
    }

    private LiveData<PagedList<Product>> getLiveProducts(@Nullable SearchModel model) {
        SearchModel search = model != null ? model : defaultSearch;
        ProductDataSourceFactory dataSourceFactory =
                new ProductDataSourceFactory(
                        clientId,
                        search.getFilter(),
                        search.getSort()
        );
        // liveDataSource = dataSourceFactory.getProductLiveDataSource();
        PagedList.Config config =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(search.getSort().getLimit())
                        .build();
        return new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }

    private LiveData<PagedList<Product>> getCustomerProducts(@NonNull String customerId) {
        CustomerProductsDataSourceFactory dataSourceFactory =
                new CustomerProductsDataSourceFactory(
                        customerId,
                        clientId);
        PagedList.Config config =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(10)
                        .build();
        return new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }

    public void updateSearch(@Nullable String categoryId, int order) {
        searchModel.setValue(SearchModel.Create(categoryId, order));
    }

    public void refreshProducts() {
        SearchModel model = searchModel.getValue();
        if (model == null) return;
        model.getSort().setStart(0);
        searchModel.setValue(model);
    }

    private void setListeners(boolean enabled) {
        if (enabled) {
            metadataRef.child(clientId).addValueEventListener(metaDataListener);
        } else {
            metadataRef.child(clientId).removeEventListener(metaDataListener);
        }
    }

    public MutableLiveData<String> getTextSearch() {
        return textSearch;
    }

    public MutableLiveData<SearchModel> getSearchModel() {
        return searchModel;
    }

    public LiveData<PagedList<Product>> getProductPagedList() {
        return productPagedList;
    }

    public LiveData<List<MetaDataModel>> getMetaData() {
        return metaData;
    }

    public LiveData<List<Product>> getCustomerProductsHistory() {
        return customerProductsHistory;
    }

    public LiveData<List<String>> getCustomerSearchHistory() {
        return customerSearchHistory;
    }

    public LiveData<List<String>> getSearches() {
        return searches;
    }

    public MutableLiveData<List<Product>> getPopularProducts() {
        return popularProducts;
    }

    public MutableLiveData<DetailedCustomer> getCustomerDetails() {
        return customerDetails;
    }

    public LiveData<PagedList<Product>> getCustomerProducts() {
        return customerProducts;
    }

    public void fetchCustomerProductsHistory() {
        repository.getCustomerProductsHistory(clientId, new StartLimit(20, 0), new CompletionHandler<List<Product>>() {
            @Override
            public void onSuccess(@NonNull List<Product> model) {
                customerProductsHistory.setValue(model);
            }

            @Override
            public void onFailure(@Nullable String description) {
                customerProductsHistory.setValue(new ArrayList<>());
            }
        });
    }

    public void fetchCustomerSearchHistory() {
        repository.getCustomerSearchHistory(clientId, new CompletionHandler<List<String>>() {
            @Override
            public void onSuccess(@NonNull List<String> model) {
                customerSearchHistory.setValue(model);
            }

            @Override
            public void onFailure(@Nullable String description) {
                customerSearchHistory.setValue(new ArrayList<>());
            }
        });
    }

    public void fetchMostPopular() {
        Sort sort = new Sort(5, 0, 3, false);
        repository.getProducts(clientId, new Filter(), sort, new CompletionHandler<List<Product>>() {
            @Override
            public void onSuccess(@NonNull List<Product> model) {
                popularProducts.setValue(model);
            }

            @Override
            public void onFailure(@Nullable String description) { }
        });
    }

    private LiveData<List<String>> getSearch(String text) {
        MutableLiveData<List<String>> searches = new MutableLiveData<>();
        repository.getSearches(text, new CompletionHandler<List<String>>() {
            @Override
            public void onSuccess(@NonNull List<String> model) {
                searches.setValue(model);
            }

            @Override
            public void onFailure(@Nullable String description) {
                searches.setValue(new ArrayList<>());
            }
        });
        return searches;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        setListeners(false);
    }
}
