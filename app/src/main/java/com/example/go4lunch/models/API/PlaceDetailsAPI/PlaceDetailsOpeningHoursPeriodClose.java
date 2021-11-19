package com.example.go4lunch.models.API.PlaceDetailsAPI;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlaceDetailsOpeningHoursPeriodClose implements Serializable {

    @SerializedName("day")
    private Long mDay;

    @SerializedName("time")
    private String mTime;

    public Long getDay() {
        return mDay;
    }

    public void setDay(Long day) {
        mDay = day;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    @Override
    public String toString() {
        return String.format("%s %s", mTime, mDay);
    }
}
