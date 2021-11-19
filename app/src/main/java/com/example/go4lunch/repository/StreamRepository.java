package com.example.go4lunch.repository;


import androidx.annotation.NonNull;

import com.example.go4lunch.models.API.AutoCompleteAPI.AutoCompeteResult;
import com.example.go4lunch.models.API.AutoCompleteAPI.AutoCompeteResultPredictions;
import com.example.go4lunch.models.API.NearbySearchAPI.PlaceNearbySearch;
import com.example.go4lunch.models.API.NearbySearchAPI.PlaceNearbySearchPlace;
import com.example.go4lunch.models.API.PlaceDetailsAPI.PlaceDetail;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.operators.observable.ObservableError;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.http.Query;


public class StreamRepository {

    public static Observable<PlaceNearbySearch> streamFetchRestaurants(String location, int radius, String type) {
        Go4LunchService go4LunchService = RetrofitObject.retrofit.create(Go4LunchService.class);
        return go4LunchService.getRestaurants(location, radius, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10,TimeUnit.SECONDS);
    }

    public static Observable<PlaceDetail> streamFetchDetails(String placeId) {
        Go4LunchService go4LunchService = RetrofitObject.retrofit.create(Go4LunchService.class);
        return go4LunchService.getDetails(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10,TimeUnit.SECONDS);

    }

    public static Observable<AutoCompeteResult> streamFetchAutocomplete(String input, int radius, String location, String type) {
        Go4LunchService go4LunchService = RetrofitObject.retrofit.create(Go4LunchService.class);
        return go4LunchService.getAutocomplete(input, radius, location, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10,TimeUnit.SECONDS);
    }

    // 2 chained request
    public static Single<List<PlaceDetail>> streamFetchRestaurantDetails(String location, int radius, String type) {
        return streamFetchRestaurants(location, radius, type)
                .flatMapIterable(new Function<PlaceNearbySearch, List<PlaceNearbySearchPlace>>() {
                    @Override
                    public List<PlaceNearbySearchPlace> apply (PlaceNearbySearch placeNearbySearch) throws Exception {
                        return placeNearbySearch.getResultSearches();

                    }
                })
                .flatMap(new Function<PlaceNearbySearchPlace, Observable<PlaceDetail>>() {
                    @Override
                    public Observable<PlaceDetail> apply (PlaceNearbySearchPlace placeNearbySearchPlace) throws Exception {
                        return streamFetchDetails(placeNearbySearchPlace.getPlaceId());
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }


    // 2 chained request
    public static Single<List<PlaceDetail>> streamFetchAutocompleteInfos(String input, int radius, String location, String type) {
        return streamFetchAutocomplete(input, radius, location, type)
                .flatMapIterable(new Function<AutoCompeteResult, List<AutoCompeteResultPredictions>>() {
                    List<AutoCompeteResultPredictions> food = new ArrayList<>();

                    @Override
                    public List<AutoCompeteResultPredictions> apply (AutoCompeteResult autoCompeteResult) throws Exception {
                        for (AutoCompeteResultPredictions prediction : autoCompeteResult.getAutoCompetePredictions()) {
                            if (prediction.getTypes().contains("food")) {
                                food.add(prediction);
                            }
                        }
                        return food;
                    }
                })
                .flatMap(new Function<AutoCompeteResultPredictions, ObservableSource<PlaceDetail>>() {
                    @Override
                    public ObservableSource<PlaceDetail> apply (AutoCompeteResultPredictions prediction) throws Exception {
                        return streamFetchDetails(prediction.getPlaceId());
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }







}
