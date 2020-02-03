package com.nativeboys.eshop.ui.main.product;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GalleryModel {

    private String url;
    private Uri uri;

    private GalleryModel(String url, Uri uri) {
        this.url = url;
        this.uri = uri;
    }

    public GalleryModel(String url) {
        this.url = url;
    }

    public GalleryModel(Uri uri) {
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

    private GalleryModel getClone() {
        return new GalleryModel(url, uri);
    }

    public static List<GalleryModel> getClones(@NonNull List<GalleryModel> list) {
        List<GalleryModel> clones = new ArrayList<>();
        for(GalleryModel model : list) {
            clones.add(model.getClone());
        }
        return clones;
    }

    @NonNull
    public static List<GalleryModel> transform(@Nullable List<Uri> uris, @Nullable List<String> urls) {
        List<GalleryModel> list = new ArrayList<>();
        if (uris != null) {
            for (Uri uri : uris) {
                list.add(new GalleryModel(uri));
            }
        }
        if (urls != null) {
            for (String url : urls) {
                list.add(new GalleryModel(url));
            }
        }
        return list;
    }

    @NonNull
    public static List<GalleryModel> transformUris(@Nullable List<Uri> uris) {
        return transform(uris, null);
    }

    @NonNull
    public static List<GalleryModel> transformUrls(@Nullable List<String> urls) {
        return transform(null, urls);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GalleryModel)) return false;
        GalleryModel that = (GalleryModel) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(uri, that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, uri);
    }

}
