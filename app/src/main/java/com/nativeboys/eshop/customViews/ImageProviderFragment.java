package com.nativeboys.eshop.customViews;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private static final int PICK_IMAGE_FROM_GALLERY_REQUEST = 1;
    private static final int IMAGE_FROM_CAMERA = 1001;

    private static final String[] permissions =
            new String[] {
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

    private Uri imgUri; // Workaround for Camera Intent

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
        if (resultCode != RESULT_OK) return;
        List<Uri> fileUris = new ArrayList<>();
        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST && data != null) {
            ClipData clipData = data.getClipData();
            Uri uri = data.getData();
            if (clipData != null) { // Multiple Local Images
                for(int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    fileUris.add(item.getUri());
                }
            } else if (uri != null) { // Single Local Image
                fileUris.add(uri);
            }
        } else if (requestCode == IMAGE_FROM_CAMERA) { // Camera Image
            if (imgUri != null) fileUris.add(imgUri);
        }
        onImagesRetrieved(fileUris);
    }

    protected boolean permissionsDenied() {
        if (getActivity() == null) return true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission)
                    == PackageManager.PERMISSION_DENIED) {
                return true;
            }
        }
        return false;
    }

    private void requestPermissions() {
        if (permissionsDenied()) {
            requestPermissions(permissions, MY_PERMISSIONS_REQUEST);
        }
    }

    /**Single Local Image*/
    protected void retrieveFileImage() {
        retrieveData(true);
    }

    /**Multiple Local Images*/
    protected void retrieveFileImages() {
        retrieveData(false);
    }

    /**Camera's Image*/
    protected void retrieveCameraImage() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        if (getActivity() == null) return;
        imgUri = getActivity().getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(
                intent
                , IMAGE_FROM_CAMERA);
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

// https://android.jlelse.eu/androids-new-image-capture-from-a-camera-using-file-provider-dd178519a954
