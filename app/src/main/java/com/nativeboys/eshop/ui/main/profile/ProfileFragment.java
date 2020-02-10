package com.nativeboys.eshop.ui.main.profile;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.nativeboys.eshop.R;
import com.nativeboys.eshop.customViews.ImageBottomSheetDialog;
import com.nativeboys.eshop.customViews.ImageProviderFragment;
import com.nativeboys.eshop.tools.GlobalViewModel;
import com.nativeboys.eshop.ui.main.MainFragmentDirections;
import com.nativeboys.eshop.ui.main.adapters.ProductsAdapter;
import com.nativeboys.eshop.viewModels.FirebaseClientProvider;

import java.util.List;

public class ProfileFragment extends ImageProviderFragment implements ImageBottomSheetDialog.OnUserInteractionListener {

    private GlobalViewModel globalVM;
    private ProductsAdapter adapter;

    private ShapeableImageView userImage;
    private ImageButton changeImageBtn;
    private ImageView logoutBtn;
    private TextView userNameField, likesField, productsField;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVM = new ViewModelProvider(getActivity() != null ? getActivity() : this).get(GlobalViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userImage = view.findViewById(R.id.user_image);
        logoutBtn = view.findViewById(R.id.logout_btn);
        userNameField = view.findViewById(R.id.user_name_field);
        likesField = view.findViewById(R.id.likes_field);
        productsField = view.findViewById(R.id.products_field);
        changeImageBtn = view.findViewById(R.id.change_image_btn);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        adapter = new ProductsAdapter();
        recyclerView.setAdapter(adapter);

        globalVM.fetchCustomerDetails();
        globalVM.fetchClientProducts();
        setUpListeners();
    }

    private void setUpListeners() {

        globalVM.getCustomerDetails().observe(getViewLifecycleOwner(), dCustomer -> {
            userNameField.setText(dCustomer.getFullName());
            likesField.setText(dCustomer.getLikes());
            likesField.setText(String.format(getString(R.string.likes_), dCustomer.getLikes()));
            productsField.setText(String.format(getString(R.string.products_), dCustomer.getProducts()));
            if (dCustomer.getProfileImageUrl() != null) {
                Glide.with(userImage.getContext())
                        .load(dCustomer.getProfileImageUrl())
                        .transform(new CenterCrop())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(userImage);
            }
        });

        changeImageBtn.setOnClickListener(view -> {
            ImageBottomSheetDialog dialog = new ImageBottomSheetDialog();
            dialog.show(getChildFragmentManager(), BottomSheetDialogFragment.class.getSimpleName());
            dialog.setListener(this);
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseClientProvider.getInstance().logout();
            NavController navController = getParentNavController();
            if (navController != null) {
                navController.navigate(R.id.action_main_to_login);
            }
        });

        globalVM.getCustomerProducts().observe(getViewLifecycleOwner(), products -> adapter.submitList(products));

        adapter.setOnProductClickListener((itemView, product) -> {
            NavController navController = getParentNavController();
            navController.navigate(MainFragmentDirections.actionMainToProduct(product.getProductId()));
        });
    }

    @Override
    protected void onImagesRetrieved(@NonNull List<Uri> uris) {
        if (uris.size() > 0) {
            globalVM.updateCustomer(uris.get(0));
        }
    }

    @Override
    protected void onPermissionDenied() { }

    @Override
    protected void onPermissionGranted() { }

    @Override
    public void onOptionClicked(@NonNull ImageBottomSheetDialog.Option option) {
        if (option == ImageBottomSheetDialog.Option.FILE) {
            retrieveFileImage();
        } else {
            retrieveCameraImage();
        }
    }
}
