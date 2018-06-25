package com.example.anlijiu.library;

import android.app.Application;

import com.example.anlijiu.library.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import timber.log.Timber;

public class SampleApplication extends DaggerApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initTimber();

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


    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}
