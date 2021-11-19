package com.example.go4lunch.models.API.PlaceDetailsAPI;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlaceDetailsOpeningHours implements Serializable {

    @SerializedName("open_now")
    private Boolean mOpenNow;

    @SerializedName("periods")
    private List<PlaceDetailsOpeningHoursPeriod> mPlaceDetailsOpeningHoursPeriods;

    @SerializedName("weekday_text")
    private List<String> mWeekDayText;

    public Boolean getOpenNow() {
        return mOpenNow;
    }

    public void setOpenNow(Boolean openNow) {
        mOpenNow = openNow;
    }

    public List<PlaceDetailsOpeningHoursPeriod> getPlaceDetailsOpeningHoursPeriods() {
        return mPlaceDetailsOpeningHoursPeriods;
    }

    public void setPlaceDetailsOpeningHoursPeriods(List<PlaceDetailsOpeningHoursPeriod> placeDetailsOpeningHoursPeriods) {
        mPlaceDetailsOpeningHoursPeriods = placeDetailsOpeningHoursPeriods;
    }

    public List<String> getWeekDayText() {
        return mWeekDayText;
    }

    public void setWeekDayText(List<String> weekDayText) {
        mWeekDayText = weekDayText;
    }
}
