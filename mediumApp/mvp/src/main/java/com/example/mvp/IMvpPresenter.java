package com.example.mvp;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;


public interface IMvpPresenter<V extends IMvpView, VM extends ViewModel> {

  /**
   * Set or attach the view to this presenter
   */
  @UiThread
  void attachView(@NonNull V view, VM viewModel);

  @UiThread
  void onAttachView(@NonNull V view, VM viewModel);


  /**
   * Will be called if the view has been detached from the Presenter.
   * Usually this happens on screen orientation changes or view (like fragment) has been put on the backstack.
   */
  @UiThread
  void detachView();

  @UiThread
  void onDetachView();

  /**
   * Will be called if the presenter is no longer needed because the View has been destroyed permanently.
   * This is where you do clean up stuff.
   */
  @UiThread
  void destroy();
}
