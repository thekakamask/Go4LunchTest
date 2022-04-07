package com.example.go4lunch.repository;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.go4lunch.models.API.NearbySearchAPI.PlaceNearbySearch;
import com.example.go4lunch.models.API.NearbySearchAPI.PlaceNearbySearchPlace;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/*public class RestoRepository {

    private static final String TAG = "RestoRepository";
    private final String ESTABLISHMENT_TYPE="restaurant";
    private int radius = 1500;
    private String location = "48.854685336064264, 2.3467574997969596"; // chatelet
    private final CompositeDisposable disposable = new CompositeDisposable();
    private String mNextPageToken = null;
    List<PlaceNearbySearchPlace> restos;
    private int THREAD_SLEEP=2000;


    public void getResto() {
        Log.d(TAG, "getResto: enter");
        StreamRepository.streamFetchRestaurants(location, radius, ESTABLISHMENT_TYPE)
                .subscribe(new Observer<PlaceNearbySearch>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(@NonNull PlaceNearbySearch placeNearbySearch) {
                        mNextPageToken=placeNearbySearch.getNextPageToken();
                        Log.d(TAG, "getResto#onNext: mNextPageToken: " + mNextPageToken);
                        Log.d(TAG, "getResto#onNext: nb of resto: " + placeNearbySearch.getResultSearches().size());

                        placeNearbySearch.getResultSearches().forEach(place -> {
                            Log.d(TAG, "getResto#onNext: place: " + place.getName());
                            restos.add(place);
                        });
                        Log.d(TAG, "getResto#onNext: restos.size(): " + restos.size());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        try {
                            // There is a short delay between when a next_page_token is issued, and when it will become valid
                            if (mNextPageToken != null) {
                                Thread.sleep(THREAD_SLEEP);
                                getRestoSecondPage();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    public void getRestoSecondPage() {
        Log.d(TAG, "getSecondPage: mNextPageToken: " + mNextPageToken);
        StreamRepository.streamRestaurantsNextPage(location, radius, ESTABLISHMENT_TYPE, mNextPageToken)
                .subscribe(new Observer<PlaceNearbySearch>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(@NonNull PlaceNearbySearch placeNearbySearch) {
                        mNextPageToken = placeNearbySearch.getNextPageToken();
                        Log.d(TAG, "getSecondPage#onNext: mNextPageToken: " + mNextPageToken);
                        Log.d(TAG, "getSecondPage#onNext: nb of resto: " + placeNearbySearch.getResultSearches().size());
                        placeNearbySearch.getResultSearches().forEach(place -> {
                            Log.d(TAG, "getSecondPage#onNext: place: " + place.getName());
                            restos.add(place);
                        });
                        Log.d(TAG, "getSecondPage#onNext: restos.size(): " + restos.size());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "getSecondPage#onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        try {
                            // There is a short delay between when a next_page_token is issued, and when it will become valid
                            if (mNextPageToken != null) {
                                Thread.sleep(THREAD_SLEEP);
                                getRestoThirdPage();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                });

    }

    public void getRestoThirdPage() {
        Log.d(TAG, "getThirdPage: mNextPageToken: " + mNextPageToken);
        StreamRepository.streamRestaurantsNextPage(location, radius, ESTABLISHMENT_TYPE, mNextPageToken)
                .subscribe(new Observer<PlaceNearbySearch>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(@NonNull PlaceNearbySearch placeNearbySearch) {
                        mNextPageToken = placeNearbySearch.getNextPageToken();
                        Log.d(TAG, "getThirdPage#onNext: mNextPageToken: " + mNextPageToken);
                        Log.d(TAG, "getThirdPage#onNext: nb of resto: " + placeNearbySearch.getResultSearches().size());
                        placeNearbySearch.getResultSearches().forEach(place -> {
                            Log.d(TAG, "getThirdPage#onNext: place: " + place.getName());
                            restos.add(place);
                        });
                        Log.d(TAG, "getThirdPage#onNext: restos.size(): " + restos.size());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "getThirdPage#onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getThirdPage#onComplete: completed");
                    }

                });
    }

}*/
