package com.nativeboys.eshop.ui.main.product;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.callbacks.CompletionHandler;
import com.nativeboys.eshop.customViews.ImageProviderFragment;
import com.nativeboys.eshop.models.node.Product;
import com.nativeboys.eshop.ui.main.adapters.CategoriesAdapter;
import com.nativeboys.eshop.viewModels.ProductViewModel;
import com.nativeboys.eshop.viewModels.ProductViewModelFactory;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends ImageProviderFragment {

    private ProductViewModel productVM;
    private NavController navController;

    private GalleryAdapter galleryAdapter;
    private EditText nameField, priceField, descriptionField, detailsField, hashTagsField;

    private ScrollView scrollView;
    private ExpandableLayout detailsLayout, descriptionLayout;
    private TextView viewsField, picNumField, descriptionLabel, detailsLabel,
            categoriesLabel, hashTagsLabel;
    private RatingBar ratingBar;
    private Button startBtn, endBtn;
    private ImageView backBtn;
    private CategoriesAdapter categoriesAdapter;
    private Button addImageButton;
    private RecyclerView categoriesRV;
    private ConstraintLayout addImageContainer;

    private Dialog successDialog, failureDialog, questionDialog;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String productId;
        if (getArguments() != null) {
            ProductFragmentArgs args = ProductFragmentArgs.fromBundle(getArguments());
            productId = args.getProductId();
        } else {
            productId = null;
        }
        ProductViewModelFactory factory;
        if (getActivity() != null) {
            factory = new ProductViewModelFactory(getActivity().getApplication(), productId);
        } else {
            factory = null;
        }
        productVM = (factory != null) ?
                new ViewModelProvider(this, factory).get(ProductViewModel.class) :
                new ViewModelProvider(this).get(ProductViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        scrollView = view.findViewById(R.id.scroll_view);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        picNumField = view.findViewById(R.id.pic_num_field);
        nameField = view.findViewById(R.id.name_field);
        ratingBar = view.findViewById(R.id.rating_bar);
        descriptionLayout = view.findViewById(R.id.exp_description_layout);
        detailsLayout = view.findViewById(R.id.details_exp_layout);
        descriptionLabel = view.findViewById(R.id.description_label);
        detailsLabel = view.findViewById(R.id.details_label);
        descriptionField = view.findViewById(R.id.description_field);
        detailsField = view.findViewById(R.id.details_field);
        viewsField = view.findViewById(R.id.views_field);
        startBtn = view.findViewById(R.id.start_btn);
        endBtn = view.findViewById(R.id.end_btn);
        priceField = view.findViewById(R.id.price_field);
        addImageButton = view.findViewById(R.id.add_image_button);
        categoriesLabel = view.findViewById(R.id.categories_label);
        categoriesRV = view.findViewById(R.id.categories_recycler_view);
        addImageContainer = view.findViewById(R.id.add_image_container);
        hashTagsLabel = view.findViewById(R.id.hash_tags_label);
        hashTagsField = view.findViewById(R.id.hash_tags_field);
        backBtn = view.findViewById(R.id.back_btn);

        categoriesAdapter = new CategoriesAdapter();
        categoriesRV.setAdapter(categoriesAdapter);
        categoriesRV.setLayoutManager(new LinearLayoutManager(
                categoriesRV.getContext(), LinearLayoutManager.HORIZONTAL , false));
        galleryAdapter = new GalleryAdapter();
        viewPager.setAdapter(galleryAdapter);
        addImageButton.setEnabled(!permissionsDenied()); // SOS
        setUpListeners();
        setUpDialogs(view);
    }

    @Override
    protected void onImagesRetrieved(@NonNull List<Uri> uris) {
        productVM.addUris(uris);
    }

    @Override
    protected void onPermissionDenied() {
        dismiss();
    }

    @Override
    protected void onPermissionGranted() {
        addImageButton.setEnabled(true);
    }

    private void setUpDialogs(@NonNull View view) {
        successDialog = new Dialog(view.getContext());
        successDialog.setContentView(R.layout.dialog_success);

        failureDialog = new Dialog(view.getContext());
        failureDialog.setContentView(R.layout.dialog_failure);

        questionDialog = new Dialog(view.getContext());
        questionDialog.setContentView(R.layout.dialog_question);
        Button cancelBtn = questionDialog.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(v -> questionDialog.dismiss());

        Button deleteBtn = questionDialog.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(this::deleteProduct);
    }

    private void applyResponse(@NonNull View view, String message, boolean success) {
        view.setEnabled(true);
        if (success) {
            TextView textView = successDialog.findViewById(R.id.text_view);
            textView.setText(message);
            successDialog.show();
        } else {
            TextView textView = failureDialog.findViewById(R.id.text_view);
            textView.setText(message);
            failureDialog.show();
        }
    }

    private void createProduct(@NonNull View view) {
        String name = nameField.getText().toString();
        String price = priceField.getText().toString();
        String description = descriptionField.getText().toString();
        String details = detailsField.getText().toString();
        String hashTags = hashTagsField.getText().toString();
        productVM.createProduct(name, price, description, details, hashTags, new CompletionHandler<String>() {
            @Override
            public void onSuccess(@NonNull String model) {
                applyResponse(view, model, true);
            }

            @Override
            public void onFailure(@Nullable String description) {
                applyResponse(view, description, false);
            }
        });
    }

    private void likeProduct(@NonNull View view) {
        view.setEnabled(false);
        productVM.likeProduct(new CompletionHandler<Boolean>() {
            @Override
            public void onSuccess(@NonNull Boolean liked) {
                view.setEnabled(true);
                startBtn.setText(liked ? R.string.liked : R.string.like);
            }

            @Override
            public void onFailure(@Nullable String description) {
                applyResponse(view, description, false);
            }
        });
    }

    private void deleteProduct(@NonNull View view) {
        view.setEnabled(false);
        productVM.deleteProduct(new CompletionHandler<String>() {
            @Override
            public void onSuccess(@NonNull String model) {
                view.setEnabled(true);
                questionDialog.dismiss();
                dismiss();
            }

            @Override
            public void onFailure(@Nullable String description) {
                applyResponse(view, description, false);
            }
        });
    }

    private void setButtonsListeners(boolean isClientProduct) {
        if (isClientProduct) {
            // Delete
            startBtn.setOnClickListener(v -> questionDialog.show());
            // Submit
            endBtn.setOnClickListener(this::createProduct);
        } else {
            // Like
            startBtn.setOnClickListener(this::likeProduct);
            // Message
            endBtn.setOnClickListener(v -> {
                Product product = productVM.getProduct().getValue();
                if (product != null) {
                    navController.navigate(ProductFragmentDirections
                            .actionProductToConversation(product.getUploaderId()));
                }
            });
        }
    }

    private void dismiss() {
        if(getActivity() != null) getActivity().onBackPressed();
    }

    private void setUpListeners() {

        productVM.getGalleryNo().observe(getViewLifecycleOwner(), no ->
                picNumField.setText(String.valueOf(no != null ? no : 0)));

        productVM.isClientProduct().observe(getViewLifecycleOwner(), aBoolean -> {
            boolean isClientProduct = aBoolean != null && aBoolean;
            nameField.setEnabled(isClientProduct);
            priceField.setEnabled(isClientProduct);
            descriptionField.setEnabled(isClientProduct);
            detailsField.setEnabled(isClientProduct);
            startBtn.setText(isClientProduct ? R.string.delete : R.string.like);
            endBtn.setText(isClientProduct ? R.string.submit : R.string.message);
            setButtonsListeners(isClientProduct);
            galleryAdapter.setIsClientProduct(isClientProduct);

            int visibility = isClientProduct ? View.VISIBLE : View.GONE;
            hashTagsLabel.setVisibility(visibility);
            categoriesLabel.setVisibility(visibility);
            categoriesRV.setVisibility(visibility);
            hashTagsField.setVisibility(visibility);
            addImageContainer.setVisibility(visibility);
        });

        productVM.getProduct().observe(getViewLifecycleOwner(), product -> {
            if (product == null) return;
            nameField.setText(product.getName());
            descriptionField.setText(product.getDescription());
            detailsField.setText(product.getDetails());
            ratingBar.setRating(product.getRating());
            viewsField.setText(String.format(getResources().getString(R.string.views_format), product.getViewsQty()));
            String price = String.format(priceField.getResources().getString(R.string.price), product.getPrice());
            priceField.setText(price);
        });

        productVM.getCategories().observe(getViewLifecycleOwner(), categories ->
                categoriesAdapter.submitList(categories != null ? categories : new ArrayList<>()));

        productVM.getGallery().observe(getViewLifecycleOwner(), list ->
                galleryAdapter.submitList(list != null ? list : new ArrayList<>()));

        categoriesAdapter.setOnCategoryClickListener(category ->
                productVM.setSelectedCategory(category));

        backBtn.setOnClickListener(v -> dismiss());

        galleryAdapter.setOnRemoveListener(position -> productVM.removeImage(position));

        addImageButton.setOnClickListener(v -> retrieveFileImages());

        descriptionLayout.setOnExpansionUpdateListener((expansionFraction, state) -> {
            if (state == ExpandableLayout.State.EXPANDED) {
                scrollView.post(() -> scrollView.smoothScrollTo(0, descriptionLabel.getTop()));
            }
        });

        detailsLayout.setOnExpansionUpdateListener((expansionFraction, state) -> {
            if (state == ExpandableLayout.State.EXPANDED) {
                scrollView.post(() -> scrollView.smoothScrollTo(0, detailsLabel.getTop()));
            }
        });

        descriptionLabel.setOnClickListener(v -> toggle(descriptionLayout, descriptionLabel));
        detailsLabel.setOnClickListener(v -> toggle(detailsLayout, detailsLabel));
    }

    private void toggle(@NonNull ExpandableLayout exLayout, @NonNull TextView tv) {
        int drawable = exLayout.isExpanded() ? R.drawable.ic_expand_more_black_24dp : R.drawable.ic_expand_less_black_24dp;
        Glide.with(tv.getContext()).load(drawable).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                tv.setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                tv.setCompoundDrawablesWithIntrinsicBounds(null, null, placeholder, null);
            }
        });
        exLayout.toggle();
    }

}
