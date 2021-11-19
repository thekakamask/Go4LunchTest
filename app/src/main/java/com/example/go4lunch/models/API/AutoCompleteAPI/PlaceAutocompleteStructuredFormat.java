package com.example.go4lunch.models.API.AutoCompleteAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceAutocompleteStructuredFormat {

    @SerializedName("main_text")
    private String mMainText;

    @SerializedName("main_text_matched_substrings")
    private List<PlaceAutocompleteStructuredFormatSubstring> mMainTextMatchedSubStrings;

    @SerializedName("secondary_text")
    private String mSecondaryText;

    public String getMainText() {
        return mMainText;
    }

    public void setMainText(String mainText) {
        mMainText = mainText;
    }

    public List<PlaceAutocompleteStructuredFormatSubstring> getMainTextMatchedSubStrings() {
        return mMainTextMatchedSubStrings;
    }

    public void setMainTextMatchedSubStrings(List<PlaceAutocompleteStructuredFormatSubstring> mainTextMatchedSubStrings) {
        mMainTextMatchedSubStrings = mainTextMatchedSubStrings;
    }

    public String getSecondaryText() {
        return mSecondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        mSecondaryText = secondaryText;
    }
}
