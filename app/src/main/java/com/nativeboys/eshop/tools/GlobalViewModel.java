package com.nativeboys.eshop.tools;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nativeboys.eshop.callbacks.AuthCompleteListener;
import com.nativeboys.eshop.callbacks.Completion;
import com.nativeboys.eshop.callbacks.CompletionHandler;
import com.nativeboys.eshop.http.Repository;
import com.nativeboys.eshop.models.adapter.SearchModel;
import com.nativeboys.eshop.models.firebase.MetaDataModel;
import com.nativeboys.eshop.models.firebase.UserModel;
import com.nativeboys.eshop.models.node.Category;
import com.nativeboys.eshop.models.node.Product;
import com.nativeboys.eshop.models.query.Filter;
import com.nativeboys.eshop.models.query.Sort;
import com.nativeboys.eshop.models.query.StartLimit;
import com.nativeboys.eshop.paging.ProductDataSourceFactory;

import java.util.ArrayList;
import java.util.List;

public class GlobalViewModel extends AndroidViewModel {

    // TODO: Offline overview, take photo fragment,
    // TODO: chat_png offline send image

    // TODO: offline - send message - another room chat_png - go online (check it ??)
    // TODO: new conversation offline, replace value events, DAO Firebase

    private final static String USERS = "users";
    final static String METADATA = "metadata";
    final static String CONVERSATIONS = "conversations";

    private Repository repository;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef, metadataRef, conversationsRef;

    private String userId;
    private MutableLiveData<FirebaseUser> user;
    private MutableLiveData<List<MetaDataModel>> metaData;

    private MutableLiveData<List<UserModel>> users;

    private LiveData<PagedList<Product>> productPagedList;
    //private LiveData<PageKeyedDataSource<Integer, Product>> liveDataSource;

    // Server

    private MutableLiveData<List<Product>> popularProducts;
    private LiveData<List<Product>> productHistory;
    private LiveData<List<String>> searchHistory;

    private MutableLiveData<String> textSearch;
    private LiveData<List<String>> searches;

    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<SearchModel> searchModel;

    {
        user = new MutableLiveData<>();
        metaData = new MutableLiveData<>();
        users = new MutableLiveData<>();

        searchModel = new MutableLiveData<>();
        productPagedList = Transformations.switchMap(searchModel, this::getLiveProducts);

        popularProducts = new MutableLiveData<>();
        productHistory = Transformations.switchMap(user, user ->
                getProductHistory(user.getUid()));
        searchHistory = Transformations.switchMap(user, user ->
                getSearchHistory(user.getUid()));
        textSearch = new MutableLiveData<>();
        searches = Transformations.switchMap(textSearch, this::getSearch);
        categories = new MutableLiveData<>();
    }

    private ValueEventListener usersListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<UserModel> users = new ArrayList<>();
            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                String id = userSnapshot.getKey();
                if (id != null && userId != null && !userId.equals(id)) {
                    UserModel user = userSnapshot.getValue(UserModel.class);
                    if (user != null) {
                        user.setId(id);
                        users.add(user);
                    }
                }
            }
            GlobalViewModel.this.users.setValue(users);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) { }
    };

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

    public GlobalViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
        repository = Repository.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference(USERS);
        metadataRef = FirebaseDatabase.getInstance().getReference(METADATA);
        conversationsRef = FirebaseDatabase.getInstance().getReference(CONVERSATIONS);

        mAuth.addAuthStateListener(firebaseAuth -> {
            setListeners(false);
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                this.user.setValue(user);
                userId = user.getUid();
                setListeners(true);
                searchModel.setValue(defaultSearch);
            }
        });

    }

    public MutableLiveData<String> getTextSearch() {
        return textSearch;
    }

    public MutableLiveData<SearchModel> getSearchModel() {
        return searchModel;
    }

    public final SearchModel defaultSearch = new SearchModel(
            new Filter(null, "ps"),
            new Sort(10, 0, 3, false));

    private LiveData<PagedList<Product>> getLiveProducts(@Nullable SearchModel model) {
        SearchModel search = model != null ? model : defaultSearch;
        Log.i("Hello", "getLiveProducts: " + search);
        ProductDataSourceFactory dataSourceFactory =
                new ProductDataSourceFactory(
                        userId,
                        search.getFilter(),
                        search.getSort()
        );
        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(search.getSort().getLimit())
                        .build();
        return (LiveData<PagedList<Product>>) (new LivePagedListBuilder(dataSourceFactory, config)).build();
    }

    public void updateSearch(@Nullable String categoryId, int order) {
        SearchModel search = searchModel.getValue();
        if (search != null) {
            searchModel.setValue(search.init(categoryId, order));
        }
    }


/*    private void setUpPaging(String userId) {
        ProductDataSourceFactory dataSourceFactory = new ProductDataSourceFactory(
                userId,
                new Filter(null, "ps"),
                new Sort(10, 0, 3, false)
        );
        //liveDataSource = dataSourceFactory.getProductLiveDataSource();
        PagedList.Config config =
                (new PagedList.Config.Builder())
                    .setEnablePlaceholders(false)
                    .setPageSize(10)
                    .build();
        productPagedList = (new LivePagedListBuilder(dataSourceFactory, config)).build();
    }*/

    public LiveData<PagedList<Product>> getProductPagedList() {
        return productPagedList;
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    @Nullable
    public String getUserId() {
        return userId;
    }

    public LiveData<List<MetaDataModel>> getMetaData() {
        return metaData;
    }

    public LiveData<List<UserModel>> getUsers() {
        return users;
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void registerUser(@NonNull String email, @NonNull String password, @NonNull String name, @NonNull AuthCompleteListener listener) {
        // TODO: Apply User inside Database
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> listener.onComplete(true, null))
                .addOnFailureListener(e -> listener.onComplete(false, e.getLocalizedMessage()));
    }

    public void loginUser(@NonNull String email, @NonNull String password, @NonNull AuthCompleteListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> listener.onComplete(true, null))
                .addOnFailureListener(e -> listener.onComplete(false, e.getLocalizedMessage()));
    }

    public void signOut() {
        mAuth.signOut();
    }

    private void setListeners(boolean enable) {
        FirebaseUser user = getUser().getValue();
        if (user == null) return;
        String userId = user.getUid();
        if (enable) {
            usersRef.addValueEventListener(usersListener);
            metadataRef.child(userId).addValueEventListener(metaDataListener);
        } else {
            usersRef.removeEventListener(usersListener);
            metadataRef.child(userId).removeEventListener(metaDataListener);
        }
    }

/*    public void addUser(String name, String lastName, String pickPath) {
        String id = usersRef.push().getKey();
        if (id == null) return;
        usersRef.child(id).setValue(new UserModel(name, lastName, pickPath));
    }*/

    public void getConversationIdWith(@NonNull String friendId, @NonNull Completion<String> completion) {
        getExistingConversationId(friendId, model ->
                completion.onResponse(model != null ? model : createConversation(friendId)));
    }

    private void getExistingConversationId(@NonNull String friendId, @NonNull Completion<String> completion) {
        if (userId != null) {
            metadataRef.child(userId).child(friendId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    MetaDataModel model = dataSnapshot.getValue(MetaDataModel.class);
                    completion.onResponse(model != null ? model.getConversationId() : null);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    completion.onResponse(null);
                }
            });
        } else {
            completion.onResponse(null);
        }
    }

    @Nullable
    private String createConversation(String friendId) {
        // String conId = metadataRef.child(USER_ID).child(friendId).getKey();
        if (userId == null) return null;
        String conId = conversationsRef.push().getKey();
        MetaDataModel sharedMeta = new MetaDataModel(conId, null);
        if (conId != null) {
            metadataRef.child(userId).child(friendId).setValue(sharedMeta);
            metadataRef.child(friendId).child(userId).setValue(sharedMeta);
        }
        return conId;
    }

    public LiveData<List<Product>> getProductHistory() {
        return productHistory;
    }

    public LiveData<List<String>> getSearchHistory() {
        return searchHistory;
    }

    public LiveData<List<String>> getSearches() {
        return searches;
    }

    public MutableLiveData<List<Product>> getPopularProducts() {
        return popularProducts;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    private LiveData<List<Product>> getProductHistory(String customerId) {
        MutableLiveData<List<Product>> products = new MutableLiveData<>();
        repository.getProductHistory(customerId, new StartLimit(20, 0), new CompletionHandler<List<Product>>() {
            @Override
            public void onSuccess(@NonNull List<Product> model) {
                products.setValue(model);
            }

            @Override
            public void onFailure(@Nullable String description) {
                products.setValue(new ArrayList<>());
            }
        });
        return products;
    }

    private LiveData<List<String>> getSearchHistory(String customerId) {
        MutableLiveData<List<String>> searches = new MutableLiveData<>();
        repository.getSearchHistory(customerId, new CompletionHandler<List<String>>() {
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

    public void refreshMostPopular() {
        Sort sort = new Sort(5, 0, 3, false);
        repository.getProducts(userId, new Filter(), sort, new CompletionHandler<List<Product>>() {
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

    public void refreshGategories() {
        repository.getCategories(new CompletionHandler<List<Category>>() {
            @Override
            public void onSuccess(@NonNull List<Category> model) {
                categories.setValue(model);
            }

            @Override
            public void onFailure(@Nullable String description) { }
        });
    }

    public void createProduct(@NonNull List<Uri> uri, @NonNull String text) {

        //repository.createProduct(getApplication(), uri, text);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        setListeners(false);
    }
}
