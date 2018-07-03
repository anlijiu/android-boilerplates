package com.anlijiu.example.di;

import android.app.Application;
import android.appwidget.AppWidgetHost;
import android.content.Context;
import android.os.Handler;


import com.anlijiu.example.SampleApplication;
import com.anlijiu.example.data.executor.JobExecutor;
import com.anlijiu.example.domain.executor.PostExecutionThread;
import com.anlijiu.example.domain.executor.ThreadExecutor;
import com.anlijiu.example.presentation.appwidget.LauncherAppWidgetHost;
import com.anlijiu.example.ui.UIThread;

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

    @Provides
    @Singleton
    Application provideApplication(SampleApplication application) {
        return application;
    }

    @Provides @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    AppWidgetHost provideAppWidgetHost(LauncherAppWidgetHost launcherAppWidgetHost) {
        return launcherAppWidgetHost;
    }

}
