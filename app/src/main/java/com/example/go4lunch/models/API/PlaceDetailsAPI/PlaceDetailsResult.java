package com.example.go4lunch.models.API.PlaceDetailsAPI;

import com.google.gson.annotations.SerializedName;

public class PlaceDetailsResult {

    @SerializedName("formatted_phone_number")
    private String mFormattedPhoneNumber;

    @SerializedName("geometry")
    private PlaceDetailsGeometry mGeometry;

    @SerializedName("name")
    private String mName;

    @SerializedName("opening_hours")
    private PlaceDetailsOpeningHours mPlaceDetailsOpeningHours;











}
