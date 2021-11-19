package com.example.go4lunch.models.API.PlaceDetailsAPI;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlaceDetail implements Serializable {

    @SerializedName("html_attributions")
    private List<String> mHtmlAttributions;

    @SerializedName("result")
    private PlaceDetailsResult mResult;

    @SerializedName("status")
    private String mStatus;

    public List<String> getHtmlAttributions() {
        return mHtmlAttributions;
    }

    public void setHtmlAttributions(List<String> htmlAttributions) {
        mHtmlAttributions = htmlAttributions;
    }

    public PlaceDetailsResult getResult() {
        return mResult;
    }

    public void setResult(PlaceDetailsResult result) {
        mResult = result;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }


}
