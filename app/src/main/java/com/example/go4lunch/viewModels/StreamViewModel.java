package com.example.go4lunch.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.models.API.NearbySearchAPI.PlaceNearbySearch;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetail;
import com.example.go4lunch.repository.StreamRepository;

import java.util.List;
import java.util.concurrent.Executor;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class StreamViewModel extends ViewModel {


    private final Executor executor;

    public final MutableLiveData<Observable<PlaceDetail>> fetchDetails = new MutableLiveData<>();
    public final MutableLiveData<Single<List<PlaceDetail>>> fetchRestaurantDetails = new MutableLiveData<>();
    public final MutableLiveData<Single<List<PlaceDetail>>> fetchAutoCompleteInfos = new MutableLiveData<>();

    public StreamViewModel(StreamRepository streamDataSource, Executor executor) {
        this.executor = executor;
    }

    public void init(String placeId, String input, int radius, String location, String type) {

        fetchDetails.setValue(StreamRepository.streamFetchDetails(placeId));
        fetchRestaurantDetails.setValue(StreamRepository.streamFetchRestaurantDetails(location,radius,type));
        fetchAutoCompleteInfos.setValue(StreamRepository.streamFetchAutocompleteInfos(input,radius,location,type));
    }

    public MutableLiveData<Observable<PlaceDetail>> getStreamFetchDetails() {
        return fetchDetails;
    }

    public MutableLiveData<Single<List<PlaceDetail>>> getStreamFetchRestaurantDetails() {
        return fetchRestaurantDetails;
    }

    public MutableLiveData<Single<List<PlaceDetail>>> getStreamFetchAutoCompleteInfos() {
        return fetchAutoCompleteInfos;
    }










}
