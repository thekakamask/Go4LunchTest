package com.example.go4lunch.models.API.NearbySearchAPI;

import com.google.gson.annotations.SerializedName;

public class PlaceNearbySearchPhoto {

    @SerializedName("html_attributions")
    private Object mHtmlAttributions;

    @SerializedName("height")
    private Double mHeight;

    @SerializedName("photo_reference")
    private String mPhotoReference;

    @SerializedName("width")
    private Double mWidth;

    public Object getHtmlAttributions() {
        return mHtmlAttributions;
    }

    public void setHtmlAttributions(Object htmlAttributions) {
        mHtmlAttributions = htmlAttributions;
    }

    public Double getHeight() {
        return mHeight;
    }

    public void setHeight(Double height) {
        mHeight = height;
    }

    public String getPhotoReference() {
        return mPhotoReference;
    }

    public void setPhotoReference(String photoReference) {
        mPhotoReference = photoReference;
    }

    public Double getWidth() {
        return mWidth;
    }

    public void setWidth(Double width) {
        mWidth = width;
    }
}
