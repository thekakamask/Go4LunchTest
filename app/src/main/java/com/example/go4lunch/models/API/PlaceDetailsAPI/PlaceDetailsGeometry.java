package com.example.go4lunch.models.API.PlaceDetailsAPI;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlaceDetailsGeometry implements Serializable {

    @SerializedName("location")
    private PlaceDetailsLocation mLocation;

    @SerializedName("viewport")
    private PlaceDetailsViewport mViewport;

    public PlaceDetailsLocation getLocation() {
        return mLocation;
    }

    public void setLocation(PlaceDetailsLocation location) {
        mLocation = location;
    }

    public PlaceDetailsViewport getViewport() {
        return mViewport;
    }

    public void setViewport(PlaceDetailsViewport viewport) {
        mViewport = viewport;
    }
}
