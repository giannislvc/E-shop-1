package com.nativeboys.eshop.viewModels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.nativeboys.eshop.R;
import com.nativeboys.eshop.callbacks.CompletionHandler;
import com.nativeboys.eshop.http.Repository;
import com.nativeboys.eshop.models.node.Category;
import com.nativeboys.eshop.models.node.DeleteResponse;
import com.nativeboys.eshop.models.node.DetailedProduct;
import com.nativeboys.eshop.models.node.LikeResponse;
import com.nativeboys.eshop.models.node.NewProduct;
import com.nativeboys.eshop.ui.main.product.GalleryModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductViewModel extends AndroidViewModel {

    private final Repository repository;
    private final String clientId;

    private MutableLiveData<String> productId;
    private LiveData<DetailedProduct> product;
    private LiveData<Boolean> clientProduct;

    private MutableLiveData<List<Category>> categories;
    private MutableLiveData<List<GalleryModel>> gallery;
    private LiveData<Integer> galleryNo;

    {
        productId = new MutableLiveData<>();
        product = Transformations.switchMap(productId, this::getProduct);
        clientProduct = Transformations.map(product, this::isClientProduct);
        categories = new MutableLiveData<>();
        gallery = new MutableLiveData<>();
        galleryNo = Transformations.map(gallery, gallery -> gallery != null ? gallery.size() : 0);
    }

    private boolean isClientProduct(@Nullable DetailedProduct product) {
        return product == null || Objects.equals(product.getUploaderId(), this.clientId);
    }

    ProductViewModel(@NonNull Application application, @NonNull String clientId, @Nullable String productId) {
        super(application);
        repository = Repository.getInstance();
        this.productId.setValue(productId);
        this.clientId = clientId;
        fetchCategories();
    }

    public LiveData<DetailedProduct> getProduct() {
        return product;
    }

    public LiveData<List<GalleryModel>> getGallery() {
        return gallery;
    }

    public LiveData<Integer> getGalleryNo() {
        return galleryNo;
    }

    public LiveData<Boolean> isClientProduct() {
        return clientProduct;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    private LiveData<DetailedProduct> getProduct(@Nullable String productId) {
        MutableLiveData<DetailedProduct> product = new MutableLiveData<>();
        if (productId != null) {
            repository.getProduct(productId, clientId, new CompletionHandler<DetailedProduct>() {
                @Override
                public void onSuccess(@NonNull DetailedProduct model) {
                    addUrls(model.getGalleryUrls());
                    setSelectedCategory(model.getCategoryId());
                    product.setValue(model);
                }

                @Override
                public void onFailure(@Nullable String description) {
                    product.setValue(null);
                }
            });
        } else {
            product.setValue(null);
        }
        return product;
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

    private List<GalleryModel> getCurrentImages() {
        return gallery.getValue() != null
                ? gallery.getValue() : new ArrayList<>();
    }

    private void addImages(@NonNull List<GalleryModel> newList) {
        List<GalleryModel> currentImages = getCurrentImages();
        List<GalleryModel> clones = GalleryModel.getClones(currentImages);
        clones.addAll(newList);
        gallery.setValue(clones);
    }

    public void addUris(@NonNull List<Uri> uris) {
        addImages(GalleryModel.transformUris(uris));
    }

    private void addUrls(@NonNull List<String> urls) {
        addImages(GalleryModel.transformUrls(urls));
    }

    public void removeImage(int position) {
        List<GalleryModel> currentImages = getCurrentImages();
        if (position < currentImages.size() && position >= 0) {
            List<GalleryModel> clones = GalleryModel.getClones(currentImages);
            clones.remove(position);
            gallery.setValue(clones);
        }
    }

    @Nullable
    private String getSelectedCategoryId() {
        List<Category> list = categories.getValue() != null ? categories.getValue() : new ArrayList<>();
        for (Category category : list) {
            if (category.isSelected()) return category.getCategoryId();
        }
        return null;
    }

    private void setSelectedCategory(@NonNull String categoryId) {
        List<Category> list = categories.getValue() != null ? categories.getValue() : new ArrayList<>();
        List<Category> clones = Category.getClones(list);
        for (Category clone : clones) {
            if (clone.getCategoryId().equals(categoryId)) {
                if (!clone.isSelected()) {
                    clone.setSelected(true);
                }
            } else if (clone.isSelected()) {
                clone.setSelected(false);
            }
        }
        categories.setValue(clones);
    }

    public void setSelectedCategory(@NonNull Category category) {
        setSelectedCategory(category.getCategoryId());
    }

    public void likeProduct(@NonNull CompletionHandler<Boolean> completion) {
        DetailedProduct product = this.getProduct().getValue();
        if (product != null) {
            repository.likeProduct(clientId, product.getProductId(), new CompletionHandler<LikeResponse>() {
                @Override
                public void onSuccess(@NonNull LikeResponse model) {
                    completion.onSuccess(model.isLiked());
                }

                @Override
                public void onFailure(@Nullable String description) {
                    completion.onFailure(description);
                }
            });
        } else {
            completion.onFailure(null);
        }
    }

    public void deleteProduct(@NonNull CompletionHandler<String> completion) {
        DetailedProduct product = this.getProduct().getValue();
        if (product != null) {
            repository.deleteProduct(product.getProductId(), new CompletionHandler<DeleteResponse>() {
                @Override
                public void onSuccess(@NonNull DeleteResponse model) {
                    completion.onSuccess(model.getMessage());
                }

                @Override
                public void onFailure(@Nullable String description) {
                    completion.onFailure(description);
                }
            });
        } else {
            completion.onFailure(null);
        }
    }

    public void createProduct(@NonNull String name,
                              @NonNull String price,
                              @NonNull String description,
                              @NonNull String details,
                              @NonNull List<String> hashTags,
                              @NonNull CompletionHandler<String> completion) {
        // TODO: Here implement Update Product logic
        String categoryId = getSelectedCategoryId();
        if (categoryId != null) {
            NewProduct product = new NewProduct(name, price, description, details, hashTags, clientId, categoryId);
            List<GalleryModel> list = getCurrentImages();
            List<Uri> uris = new ArrayList<>();
            for(GalleryModel model : list) {
                Uri uri = model.getUri();
                if (uri == null) continue;
                uris.add(uri);
            }
            repository.createProduct(getApplication(), product, uris, new CompletionHandler<DetailedProduct>() {
                @Override
                public void onSuccess(@NonNull DetailedProduct model) {
                    ProductViewModel.this.productId.setValue(model.getProductId());
                    completion.onSuccess(getApplication().getString(R.string.product_uploaded_successfully));
                }

                @Override
                public void onFailure(@Nullable String description) {
                    completion.onFailure(description);
                }
            });
        } else {
            completion.onFailure(getApplication().getString(R.string.select_category));
        }
    }

    public void updateProduct() {
        // + productId,
        // From GalleryModel list find uris and urls and send them back
    }

}
