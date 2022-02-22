package com.example.go4lunch.models.API.NearbySearchAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceNearbySearchPlace {

    @SerializedName("next_page_token")
    private String mNextPageToken;

    @SerializedName("geometry")
    private PlaceNearbySearchGeometry mGeometry;

    @SerializedName("opening_hours")
    private PlaceNearbySearchOpeningHours mOpeningHours;

    @SerializedName("icon")
    private String mIcon;

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("photos")
    private List<PlaceNearbySearchPhoto> mPhotos;

    @SerializedName("place_id")
    private String mPlaceId;

    @SerializedName("reference")
    private String mReference;

    @SerializedName("vicinity")
    private String mVicinity;

    @SerializedName("types")
    private List<String> mTypes;

    public String getNextPageToken() {
        return mNextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        mNextPageToken= nextPageToken;
    }

    public PlaceNearbySearchGeometry getGeometry() {
        return mGeometry;
    }

    public void setGeometry(PlaceNearbySearchGeometry geometry) {
        mGeometry = geometry;
    }

    public PlaceNearbySearchOpeningHours getOpeningHours() {
        return mOpeningHours;
    }

    public void setOpeningHours(PlaceNearbySearchOpeningHours openingHours) {
        mOpeningHours = openingHours;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<PlaceNearbySearchPhoto> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<PlaceNearbySearchPhoto> photos) {
        mPhotos = photos;
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

    public String getVicinity() {
        return mVicinity;
    }

    public void setVicinity(String vicinity) {
        mVicinity = vicinity;
    }

    public List<String> getTypes() {
        return mTypes;
    }

    public void setTypes(List<String> types) {
        mTypes = types;
    }

    @Override
    public String toString() {
        return "PlaceNearbySearchPlace{" +
                "mGeometry=" + mGeometry +
                ", mIcon=" + mIcon +
                ", mId=" + mId +
                ", mName=" + mName +
                ", mOpeningHours" + mOpeningHours +
                ", mPhotos" + mPhotos +
                ", mPlaceId" + mPlaceId +
                ", mReference" + mReference +
                ", mTypes" + mTypes +
                ", mVicinity" + mVicinity + '\'' + '}';

    }
}
