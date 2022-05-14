package com.example.go4lunch.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.core.Observable;

public class StreamViewModel extends ViewModel {

    public final MutableLiveData<Observable> streamFetchDetails = new MutableLiveData<>();
    public final MutableLiveData<Observable> streamFetchRestaurantsDetails = new MutableLiveData<>();
    public final MutableLiveData<Observable> streamFetchAutoCompleteInfos = new MutableLiveData<>();

    public
}
