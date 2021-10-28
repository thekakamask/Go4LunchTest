package com.example.go4lunch.models.API.NearbySearchAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceNearbySearch {

    @SerializedName("html_attributions")
    private List<Object> mHtmlAttributions;

    @SerializedName("results")
    private List<PlaceNearbySearchPlace> mResultSearches;

    @SerializedName("status")
    private String mStatus;

    public List<Object> getHtmlAttributions() {
        return mHtmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        mHtmlAttributions = htmlAttributions;
    }

    public List<PlaceNearbySearchPlace> getResultSearches() {
        return mResultSearches;
    }

    public void setResultSearches(List<PlaceNearbySearchPlace> resultSearches) {
        mResultSearches = resultSearches;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    @Override
    public String toString() {
        return "GoogleApi{" +
                "mHtmlAttributions=" + mHtmlAttributions +
                ", mResultSearch=" + mResultSearches +
                ", mStatus=" + mStatus + '\'' + '}';
    }
}
