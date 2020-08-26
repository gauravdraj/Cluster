package com.example.cluster.ui.join;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JoinViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public JoinViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is join fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}