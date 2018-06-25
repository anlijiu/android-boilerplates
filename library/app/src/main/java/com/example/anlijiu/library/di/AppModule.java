package com.example.anlijiu.library.di;

import android.app.Application;
import android.appwidget.AppWidgetHost;
import android.content.Context;

import com.example.anlijiu.library.SampleApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(SampleApplication application) {
        return application.getApplicationContext();
    }


}
