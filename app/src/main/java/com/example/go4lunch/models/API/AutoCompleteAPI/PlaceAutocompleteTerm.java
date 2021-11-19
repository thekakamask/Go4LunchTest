package com.example.go4lunch.models.API.AutoCompleteAPI;

import com.google.gson.annotations.SerializedName;

public class PlaceAutocompleteTerm {

    @SerializedName("offset")
    private Long mOffset;

    @SerializedName("value")
    private String mValue;

    public Long getOffset() {
        return mOffset;
    }

    public void setOffset(Long offset) {
        mOffset = offset;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }
}
