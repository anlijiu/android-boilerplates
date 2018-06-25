package com.example.anlijiu.library;

import android.arch.lifecycle.MutableLiveData;
import android.view.View;

import com.example.mvp.BaseViewModel;

import timber.log.Timber;

public class MainViewModel extends BaseViewModel {
    public final MutableLiveData<String> textValue = new MutableLiveData<>();
    private int count = 0;

    public MainViewModel() {
        Timber.e("ssss MainViewModel constructor in !");
        textValue.setValue("hahahahah");
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int increase() {
        count++;
        return count;
    }

    public void onClickLoadData(View view) {
        Timber.e("ssss MainViewModel onClickLoadData");
        count ++;
        textValue.setValue("hahahahah" + count);
    }
}
