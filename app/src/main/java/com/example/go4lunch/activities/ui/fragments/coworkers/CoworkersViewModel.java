package com.example.go4lunch.activities.ui.fragments.coworkers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CoworkersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CoworkersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}