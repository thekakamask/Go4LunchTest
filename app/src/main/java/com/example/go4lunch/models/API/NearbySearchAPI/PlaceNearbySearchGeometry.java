package com.example.go4lunch.models.API.NearbySearchAPI;

import com.google.gson.annotations.SerializedName;

public class PlaceNearbySearchGeometry {

    @SerializedName("location")
    private PlaceNearbySearchLocation mLocation;

    public PlaceNearbySearchLocation getLocation() {
        return mLocation;
    }

    public void setLocation(PlaceNearbySearchLocation location) {
        mLocation = location;
    }
}
