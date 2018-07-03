package com.anlijiu.example.presentation.cloud;

import com.example.mvp.IMvpView;

import io.reactivex.Observable;

public interface UserListView extends IMvpView {
    Observable<Object> increaseCount();
}
