package com.nativeboys.eshop.customViews;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.nativeboys.eshop.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public abstract class ImageProviderFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST = 100;
    private final static int PICK_IMAGE_FROM_GALLERY_REQUEST = 1;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted();
            } else {
                onPermissionDenied();
            }
        }
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
            onImagesRetrieved(fileUris);
        }
    }

    private void requestPermissions() {
        if (getActivity() == null) return;
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                    MY_PERMISSIONS_REQUEST);
        }
    }

    protected void retrieveImage() {
        retrieveData(true);
    }

    protected void retrieveImages() {
        retrieveData(false);
    }

    private void retrieveData(boolean singleImage) {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (!singleImage) intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        // Always show chooser (if there are multiple options available)
        startActivityForResult(
                Intent.createChooser(intent, getResources().getString(R.string.select_picture)),
                PICK_IMAGE_FROM_GALLERY_REQUEST);
    }

    protected abstract void onImagesRetrieved(@NonNull List<Uri> uris);

    protected abstract void onPermissionDenied();

    protected abstract void onPermissionGranted();

}
