package com.anlijiu.example.di;

import android.content.Context;
import android.content.SharedPreferences;


import com.anlijiu.example.ui.ActivityLifecyclesServer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by anlijiu on 17-9-13.
 */

@Module
public class UiModule {

    @Provides
    @Singleton
    ActivityLifecyclesServer provideActivityScreenSwitcherServer() {
        final ActivityLifecyclesServer.Proxy proxy = new ActivityLifecyclesServer.Proxy();
        return proxy;
    }

}
