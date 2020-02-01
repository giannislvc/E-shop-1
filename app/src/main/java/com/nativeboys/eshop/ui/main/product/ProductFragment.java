package com.nativeboys.eshop.ui.main.product;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.customViews.FullDialogFragment;
import com.nativeboys.eshop.models.node.Category;
import com.nativeboys.eshop.models.node.DetailedProduct;
import com.nativeboys.eshop.tools.GlobalViewModel;
import com.nativeboys.eshop.ui.main.adapters.CategoriesAdapter;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProductFragment extends FullDialogFragment {

    private final static String testProduct = "{\n" +
            "\t\"name\" : \"Xiaomi Mi A2 - 64GB - Global Version - Black (Unlocked) 10\",\n" +
            "\t\"price\" : \"104.86\",\n" +
            "\t\"description\" : \"A phone that works smarter for you Android One phones have the latest AI-powered innovations from Google built in. That means they can auto-adjust to your needs and help get things done more easily throughout your day\",\n" +
            "\t\"details\" : \"Unlocked\\n4G Ready for ultra fast network speeds\\nUltra-sharp 12 Megapixel + 20 Megapixel dual rear camera & 20 Megapixel selfie camera\\n5.99” FHD+ Corning® Gorilla® Glass 5 Display\\nSuper fast Snapdragon™ 660 Octa Core Processor + 6GB of RAM\\nAndroid One OS\\n64GB Storage\",\n" +
            "\t\"hash_tags\" : [\n" +
            "\t\t\"phone\", \"Xiaomi Mi A2\", \"Global Version\", \"Octa Core Processor\", \"64GB Storage\", \"Android One\", \"Gorilla® Glass 5\"\n" +
            "\t],\n" +
            "\t\"uploader_id\" : \"VAOyj0DW5wYK2D0lEis1tKcRpSj2\",\n" +
            "\t\"category_id\" : \"4ac5f84d-aa5d-42bc-a8e2-4cbf55acb03b\"\n" +
            "}";

    private static final int MY_PERMISSIONS_REQUEST = 100;
    private int PICK_IMAGE_FROM_GALLERY_REQUEST = 1;

    private GlobalViewModel globalVM;
    private DetailedProduct product;

    private ProductImageAdapter adapter;
    private EditText nameField, priceField, descriptionField, detailsField;

    private ScrollView scrollView;
    private ExpandableLayout detailsLayout, descriptionLayout;
    private TextView viewsField, picNumField, descriptionLabel, detailsLabel;
    private RatingBar ratingBar;
    private Button likeBtn;
    private CategoriesAdapter categoriesAdapter;
    private Button addImageButton;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVM = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(GlobalViewModel.class);
        product = DetailedProduct.GetDummy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST && resultCode == RESULT_OK
                && data != null) {
            ClipData clipData = data.getClipData();
            Uri uri = data.getData();
            List<Uri> fileUris = new ArrayList<>();
            if (clipData != null) {
                for(int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    fileUris.add(item.getUri());
                }
            } else if (uri != null) {
                fileUris.add(uri);
            }
            uploadAlbum(fileUris);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // permission was granted, Do the file
                    // related task you need to do.
                } else {
                    // permission denied Disable the
                    // functionality that depends on this permission
                }
            }
        }
    }

    private void test() {
        if (getActivity() == null) return;
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
        addImageButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            // Show only images, no videos or anything else
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            // Always show chooser (if there are multiple options available)
            startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    PICK_IMAGE_FROM_GALLERY_REQUEST);
        });
    }

    private void uploadAlbum(List<Uri> fileUri) {
        globalVM.createProduct(fileUri, testProduct);
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
        likeBtn = view.findViewById(R.id.like_btn);
        priceField = view.findViewById(R.id.price_field);
        addImageButton = view.findViewById(R.id.add_image_button);

        RecyclerView categoriesRV = view.findViewById(R.id.categories_recycler_view);
        categoriesAdapter = new CategoriesAdapter();
        categoriesRV.setAdapter(categoriesAdapter);
        categoriesRV.setLayoutManager(new LinearLayoutManager(
                categoriesRV.getContext(), LinearLayoutManager.HORIZONTAL , false));

        adapter = new ProductImageAdapter();
        viewPager.setAdapter(adapter);
        setUpListeners();
        setUpView();
        test();
    }

    private void setUpView() {
        globalVM.refreshGategories();
        nameField.setEnabled(false);
        priceField.setEnabled(false);
        descriptionField.setEnabled(false);
        detailsField.setEnabled(false);
        adapter.submitList(product.getGalleryUrls());
        picNumField.setText(String.valueOf(product.getGalleryUrls().size()));
        nameField.setText(product.getName());
        descriptionField.setText(product.getDescription());
        detailsField.setText(product.getDetails());
        ratingBar.setRating(product.getRating());
        viewsField.setText(String.format(getResources().getString(R.string.views_format), product.getViewsQty()));
        String price = String.format(priceField.getResources().getString(R.string.price), product.getPrice());
        priceField.setText(price);
    }

    private void setUpListeners() {

        globalVM.getCategories().observe(getViewLifecycleOwner(), categories ->
                categoriesAdapter.submitList(categories));

        likeBtn.setOnClickListener(view -> view.setActivated(!view.isActivated()));

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

        descriptionLabel.setOnClickListener(v -> {
            int drawable = descriptionLayout.isExpanded() ? R.drawable.ic_expand_more_black_24dp : R.drawable.ic_expand_less_black_24dp;
            Glide.with(v.getContext()).load(drawable).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    descriptionLabel.setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    descriptionLabel.setCompoundDrawablesWithIntrinsicBounds(null, null, placeholder, null);
                }
            });
            descriptionLayout.toggle();
        });

        detailsLabel.setOnClickListener(v -> {
            int drawable = detailsLayout.isExpanded() ? R.drawable.ic_expand_more_black_24dp : R.drawable.ic_expand_less_black_24dp;
            Glide.with(v.getContext()).load(drawable).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    detailsLabel.setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    detailsLabel.setCompoundDrawablesWithIntrinsicBounds(null, null, placeholder, null);
                }
            });
            detailsLayout.toggle();
        });
    }

}
