package com.example.go4lunch.models.API.AutoCompleteAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutoCompeteResult {

    @SerializedName("status")
    private String mStatus;
    @SerializedName("preditions")
    private List<AutoCompeteResultPredictions> mAutoCompetePredictions;

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public List<AutoCompeteResultPredictions> getAutoCompetePredictions() {
        return mAutoCompetePredictions;
    }

    public void setAutoCompetePredictions(List<AutoCompeteResultPredictions> autoCompetePredictions) {
        mAutoCompetePredictions = autoCompetePredictions;
    }
}
