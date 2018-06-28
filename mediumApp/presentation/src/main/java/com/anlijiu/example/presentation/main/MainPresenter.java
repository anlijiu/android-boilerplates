package com.anlijiu.example.presentation.main;

import android.support.annotation.NonNull;

import com.anlijiu.example.di.scope.ActivityScope;
import com.example.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

@ActivityScope
public class MainPresenter extends MvpPresenter<MainView, MainViewModel> {
    @Inject
    public MainPresenter() {}

    @Override
    public void onAttachView(@NonNull MainView view, MainViewModel viewModel) {
        view.increaseCount().subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                viewModel.textValue.setValue("laaaaa" + viewModel.increase());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        Timber.e("MainPresenter onAttachView");
    }

    @Override
    public void onDetachView() {
        Timber.e("MainPresenter onDetachView");
    }

    @Override
    public void destroy() {
        super.destroy();
        Timber.e("MainPresenter destroy");
    }
}
