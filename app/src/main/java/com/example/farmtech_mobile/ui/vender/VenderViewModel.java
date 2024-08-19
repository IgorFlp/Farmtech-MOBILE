package com.example.farmtech_mobile.ui.vender;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VenderViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public VenderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is vender fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}