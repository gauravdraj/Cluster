package com.example.cluster.ui.clusters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClustersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ClustersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is clusters fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}