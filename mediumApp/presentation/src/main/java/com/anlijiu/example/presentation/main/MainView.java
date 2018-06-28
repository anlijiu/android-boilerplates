package com.anlijiu.example.presentation.main;

import com.example.mvp.IMvpView;

import io.reactivex.Observable;

public interface MainView extends IMvpView {
    Observable<Object> increaseCount();
}
