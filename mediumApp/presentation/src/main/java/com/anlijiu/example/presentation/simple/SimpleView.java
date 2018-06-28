package com.anlijiu.example.presentation.simple;

import com.example.mvp.IMvpView;

import io.reactivex.Observable;

public interface SimpleView extends IMvpView {
    Observable<Object> increaseCount();
}
