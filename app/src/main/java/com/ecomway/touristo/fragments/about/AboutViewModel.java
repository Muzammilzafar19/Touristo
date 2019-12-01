package com.ecomway.touristo.fragments.about;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private String description="The state of Punjab is renowned for its cuisine, culture and history. Punjab has a vast public transportation and communication network. Some of the main cities in Punjab are Amritsar, Jalandhar, Patiala, Pathankot and Ludhiana. Patiala is known for its historical forts. Punjab also has a rich Sikh religious history."
    +"Punjab receives many religious tourists, as the state is home to some of the holiest places in Sikhism, including the Harmandir Sahib and three of the five Panj Takht.";
    public AboutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(description);
    }

    public LiveData<String> getText() {
        return mText;
    }
}