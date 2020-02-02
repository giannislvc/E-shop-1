package com.nativeboys.eshop.ui.main.product;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageSliderModel {

    private String url;
    private Uri uri;

    private ImageSliderModel(String url, Uri uri) {
        this.url = url;
        this.uri = uri;
    }

    public ImageSliderModel(String url) {
        this.url = url;
    }

    public ImageSliderModel(Uri uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    private ImageSliderModel getClone() {
        return new ImageSliderModel(url, uri);
    }

    public static List<ImageSliderModel> getClones(@NonNull List<ImageSliderModel> list) {
        List<ImageSliderModel> clones = new ArrayList<>();
        for(ImageSliderModel model : list) {
            clones.add(model.getClone());
        }
        return clones;
    }

    @NonNull
    public static List<ImageSliderModel> transform(@Nullable List<Uri> uris, @Nullable List<String> urls) {
        List<ImageSliderModel> list = new ArrayList<>();
        if (uris != null) {
            for (Uri uri : uris) {
                list.add(new ImageSliderModel(uri));
            }
        }
        if (urls != null) {
            for (String url : urls) {
                list.add(new ImageSliderModel(url));
            }
        }
        return list;
    }

    @NonNull
    public static List<ImageSliderModel> transformUris(@Nullable List<Uri> uris) {
        return transform(uris, null);
    }

    @NonNull
    public static List<ImageSliderModel> transformUrls(@Nullable List<String> urls) {
        return transform(null, urls);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageSliderModel)) return false;
        ImageSliderModel that = (ImageSliderModel) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(uri, that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, uri);
    }

}
