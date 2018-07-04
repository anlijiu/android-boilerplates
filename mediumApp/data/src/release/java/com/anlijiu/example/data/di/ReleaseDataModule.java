package com.anlijiu.example.data.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.anlijiu.example.data.BuildConfig;
import com.anlijiu.example.data.entity.MyObjectBox;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.objectbox.BoxStore;
import okhttp3.OkHttpClient;

@Module(includes = {DataModule.class})
public final class ReleaseDataModule {

    @Provides
    @Singleton
    BoxStore provideMyObjectBox(Application application) {
        BoxStore boxStore = MyObjectBox.builder().androidContext(application).build();
        return boxStore;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(OkHttpClient.Builder builder) {
        return builder.build();
    }
}
