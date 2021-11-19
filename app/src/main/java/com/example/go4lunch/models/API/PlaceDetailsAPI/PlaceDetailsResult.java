package com.example.go4lunch.models.API.PlaceDetailsAPI;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlaceDetailsResult implements Serializable {

    @SerializedName("formatted_phone_number")
    private String mFormattedPhoneNumber;

    @SerializedName("geometry")
    private PlaceDetailsGeometry mGeometry;

    @SerializedName("name")
    private String mName;

    @SerializedName("opening_hours")
    private PlaceDetailsOpeningHours mPlaceDetailsOpeningHours;

    @SerializedName("photos")
    private List<PlaceDetailsPhoto> mPhotos;

    @SerializedName("place_id")
    private String mPlaceId;

    @SerializedName("rating")
    private Double mRating;

    @SerializedName("reference")
    private String mReference;

    @SerializedName("vicinity")
    private String mVicinity;

    @SerializedName ("website")
    private String mWebsite;

    @SerializedName("id")
    private String mId;

    public String getFormattedPhoneNumber() {
        return mFormattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        mFormattedPhoneNumber = formattedPhoneNumber;
    }

    public PlaceDetailsGeometry getGeometry() {
        return mGeometry;
    }

    public void setGeometry(PlaceDetailsGeometry geometry) {
        mGeometry = geometry;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public PlaceDetailsOpeningHours getPlaceDetailsOpeningHours() {
        return mPlaceDetailsOpeningHours;
    }

    public void setPlaceDetailsOpeningHours(PlaceDetailsOpeningHours placeDetailsOpeningHours) {
        mPlaceDetailsOpeningHours = placeDetailsOpeningHours;
    }

    public List<PlaceDetailsPhoto> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<PlaceDetailsPhoto> photos) {
        mPhotos = photos;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public Double getRating() {
        return mRating;
    }

    public void setRating(Double rating) {
        mRating = rating;
    }

    public String getReference() {
        return mReference;
    }

    public void setReference(String reference) {
        mReference = reference;
    }

    public String getVicinity() {
        return mVicinity;
    }

    public void setVicinity(String vicinity) {
        mVicinity = vicinity;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }


}
