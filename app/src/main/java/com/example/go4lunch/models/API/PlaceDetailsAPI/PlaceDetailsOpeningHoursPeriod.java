package com.example.go4lunch.models.API.PlaceDetailsAPI;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlaceDetailsOpeningHoursPeriod implements Serializable {

    @SerializedName("close")
    private PlaceDetailsOpeningHoursPeriodClose mPeriodClose;

    @SerializedName("open")
    private PlaceDetailsOpeningHoursPeriodOpen mPeriodOpen;

    public PlaceDetailsOpeningHoursPeriodClose getPeriodClose() {
        return mPeriodClose;
    }

    public void setPeriodClose(PlaceDetailsOpeningHoursPeriodClose periodClose) {
        mPeriodClose = periodClose;
    }

    public PlaceDetailsOpeningHoursPeriodOpen getPeriodOpen() {
        return mPeriodOpen;
    }

    public void setPeriodOpen(PlaceDetailsOpeningHoursPeriodOpen periodOpen) {
        mPeriodOpen = periodOpen;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s %s", mPeriodOpen, mPeriodClose);
    }
}
