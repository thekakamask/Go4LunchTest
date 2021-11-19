package com.example.go4lunch.models.API.PlaceDetailsAPI;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlaceDetailsViewport implements Serializable {

    @SerializedName("northeast")
    private PlaceDetailsNorthEast mNorthEast;

    @SerializedName("southwest")
    private PlaceDetailsSouthWest mSouthWest;

    public PlaceDetailsNorthEast getNorthEast() {
        return mNorthEast;
    }

    public void setNorthEast(PlaceDetailsNorthEast northEast) {
        mNorthEast = northEast;
    }

    public PlaceDetailsSouthWest getSouthWest() {
        return mSouthWest;
    }

    public void setSouthWest(PlaceDetailsSouthWest southWest) {
        mSouthWest = southWest;
    }
}
