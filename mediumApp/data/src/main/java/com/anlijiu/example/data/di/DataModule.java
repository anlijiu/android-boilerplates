package com.anlijiu.example.data.di;

import android.Manifest;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;

import com.anlijiu.example.data.BuildConfig;
import com.anlijiu.example.data.cache.CardCache;
import com.anlijiu.example.data.cache.CardCacheImpl;
import com.anlijiu.example.data.compat.AppWidgetManagerCompat;
import com.anlijiu.example.data.compat.AppWidgetManagerCompatVO;
import com.anlijiu.example.data.compat.LauncherAppsCompat;
import com.anlijiu.example.data.compat.LauncherAppsCompatVO;
import com.anlijiu.example.data.compat.UserManagerCompat;
import com.anlijiu.example.data.compat.UserManagerCompatVNMr1;
import com.anlijiu.example.data.entity.MyObjectBox;
import com.anlijiu.example.data.repository.CardDataRepository;

import com.anlijiu.example.data.repository.ItemInfoDataRepository;
import com.anlijiu.example.data.repository.SimpleDataRepository;
import com.anlijiu.example.domain.repository.CardRepository;
import com.anlijiu.example.domain.repository.ItemInfoRepository;
import com.anlijiu.example.domain.repository.SimpleRepository;
import com.anlijiu.example.utils.PermissionUtils;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.objectbox.BoxStore;


import static android.content.Context.MODE_PRIVATE;


@Module
public class DataModule {

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return application.getSharedPreferences("sample", MODE_PRIVATE);
    }

    @Provides
    @Singleton
    CardCache provideCardCache(CardCacheImpl cardCache) {
        return cardCache;
    }

    @Provides
    @Singleton
    CardRepository provideCardRepository(CardDataRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    SimpleRepository provideSimpleRepository(SimpleDataRepository repository) {
        return repository;
    }


    @Provides
    @Singleton
    ItemInfoRepository provideItemInfoRepository(ItemInfoDataRepository repository) {
        return repository;
    }

    @Provides
    @Singleton
    RxSharedPreferences provideRxSharedPreferences(SharedPreferences preferences) {
        return RxSharedPreferences.create(preferences);
    }

    @Provides
    @Singleton
    @Named("test_key")
    Preference<String> provideVehicleIdPreference(RxSharedPreferences prefs) {
        return prefs.getString("test_key", "test key");
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
//                .setExclusionStrategies(new ExclusionUnderlineStrategy())
//                .registerTypeAdapterFactory(AutoValueAdapterFactory.create())
//                .registerTypeAdapterFactory(CloudGsonAdapterFactory.create())
                .setPrettyPrinting()
                .create();
    }

    @Provides
    @Singleton
    @Named("Root")
    File provideRootDir(Application context){
        File cacheDir;
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())
                && PermissionUtils.hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) ) {
            cacheDir = context.getExternalFilesDir("");
        } else {
            cacheDir = context.getFilesDir();
        }
        return cacheDir;
    }

    @Provides
    @Singleton
    @Named("Data")
    File provideDataCacheDir(@Named("Root") File dir){
        return new File(dir, "data");
    }

    @Provides
    @Singleton
    AppWidgetManagerCompat provideAppWidgetManagerCompat(AppWidgetManagerCompatVO appWidgetManagerCompat) {
        return appWidgetManagerCompat;
    }

    @Provides
    @Singleton
    LauncherAppsCompat provideLauncherAppCompat(LauncherAppsCompatVO launcherAppsCompatVO) {
        return launcherAppsCompatVO;
    }

    @Provides
    @Singleton
    UserManagerCompat provideUserManagerCompat(UserManagerCompatVNMr1 userManagerCompat) {
        return userManagerCompat;
    }
}
