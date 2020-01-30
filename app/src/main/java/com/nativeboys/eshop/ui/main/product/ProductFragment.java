package com.nativeboys.eshop.ui.main.product;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.nativeboys.eshop.models.node.DetailedProduct;

import net.cachapa.expandablelayout.ExpandableLayout;

public class ProductFragment extends FullDialogFragment {

    private DetailedProduct product;

    private ProductImageAdapter adapter;
    private EditText nameField, priceField, descriptionField, detailsField;

    private ScrollView scrollView;
    private ExpandableLayout detailsLayout, descriptionLayout;
    private TextView viewsField, picNumField, descriptionLabel, detailsLabel;
    private RatingBar ratingBar;
    private Button likeBtn;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product = DetailedProduct.GetDummy();
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

        adapter = new ProductImageAdapter();
        viewPager.setAdapter(adapter);
        setUpListeners();
        setUpView();
    }

    private void setUpView() {
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
