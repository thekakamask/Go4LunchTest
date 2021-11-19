package com.example.go4lunch.models.API.AutoCompleteAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutoCompeteResultPredictions {

    @SerializedName("description")
    private String mDescription;

    @SerializedName("matched_substrings")
    private List<PlaceAutocompleteMatchedSubstring> mPlaceAutocompleteMatchedSubstrings;

    @SerializedName("structured_formatting")
    private PlaceAutocompleteStructuredFormat mPlaceAutocompleteStructuredFormat;

    @SerializedName("terms")
    private List<PlaceAutocompleteTerm> mPlaceAutocompleteTerms;

    @SerializedName("place_id")
    private String mPlaceId;

    @SerializedName("reference")
    private String mReference;

    @SerializedName("types")
    private List<String> mTypes;

    @SerializedName("id")
    private String mId;

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public List<PlaceAutocompleteMatchedSubstring> getPlaceAutocompleteMatchedSubstrings() {
        return mPlaceAutocompleteMatchedSubstrings;
    }

    public void setPlaceAutocompleteMatchedSubstrings(List<PlaceAutocompleteMatchedSubstring> placeAutocompleteMatchedSubstrings) {
        mPlaceAutocompleteMatchedSubstrings = placeAutocompleteMatchedSubstrings;
    }

    public PlaceAutocompleteStructuredFormat getPlaceAutocompleteStructuredFormat() {
        return mPlaceAutocompleteStructuredFormat;
    }

    public void setPlaceAutocompleteStructuredFormat(PlaceAutocompleteStructuredFormat placeAutocompleteStructuredFormat) {
        mPlaceAutocompleteStructuredFormat = placeAutocompleteStructuredFormat;
    }

    public List<PlaceAutocompleteTerm> getPlaceAutocompleteTerms() {
        return mPlaceAutocompleteTerms;
    }

    public void setPlaceAutocompleteTerms(List<PlaceAutocompleteTerm> placeAutocompleteTerms) {
        mPlaceAutocompleteTerms = placeAutocompleteTerms;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public String getReference() {
        return mReference;
    }

    public void setReference(String reference) {
        mReference = reference;
    }

    public List<String> getTypes() {
        return mTypes;
    }

    public void setTypes(List<String> types) {
        mTypes = types;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }
}
