package com.anlijiu.example;

import android.app.Application;
import android.view.View;

import com.anlijiu.example.di.DaggerAppComponent;
import com.anlijiu.example.BuildConfig;
import com.anlijiu.example.di.HasViewInjector;
import com.anlijiu.example.ui.ActivityLifecyclesServer;
import com.jakewharton.threetenabp.AndroidThreeTen;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerApplication;
import io.reactivex.plugins.RxJavaPlugins;
import timber.log.Timber;

public class SampleApplication extends DaggerApplication implements HasViewInjector {
    @Inject
    DispatchingAndroidInjector<View> viewInjector;
    @Inject
    ActivityLifecyclesServer activityLifecyclesServer;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        registerActivityLifecycleCallbacks(activityLifecyclesServer);
        initRxGlobalErrorHandler();
        watchUncaughtException();

        initTimber();

    }

    @Override
    public AndroidInjector<View> viewInjector() {
        return viewInjector;
    }


    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            // TODO Crashlytics.start(this);
            // TODO Timber.plant(new CrashlyticsTree());
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void watchUncaughtException(){
//        Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);
    }

    private void initRxGlobalErrorHandler() {
        RxJavaPlugins.setErrorHandler(throwable -> Timber.e(throwable));
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}
