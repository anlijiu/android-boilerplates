package com.anlijiu.example.data.di;

import android.Manifest;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;

import com.anlijiu.example.data.cache.CardCache;
import com.anlijiu.example.data.cache.CardCacheImpl;
import com.anlijiu.example.data.compat.AppWidgetManagerCompat;
import com.anlijiu.example.data.compat.AppWidgetManagerCompatVO;
import com.anlijiu.example.data.compat.LauncherAppsCompat;
import com.anlijiu.example.data.compat.LauncherAppsCompatVO;
import com.anlijiu.example.data.compat.UserManagerCompat;
import com.anlijiu.example.data.compat.UserManagerCompatVNMr1;
import com.anlijiu.example.data.exception.RxErrorHandlingCallAdapterFactory;
import com.anlijiu.example.data.repository.CardDataRepository;

import com.anlijiu.example.data.repository.CloudDataRepository;
import com.anlijiu.example.data.repository.ItemInfoDataRepository;
import com.anlijiu.example.data.repository.SimpleDataRepository;
import com.anlijiu.example.domain.objects.AutoValueGsonTypeAdapterFactory;
import com.anlijiu.example.domain.repository.CardRepository;
import com.anlijiu.example.domain.repository.CloudRepository;
import com.anlijiu.example.domain.repository.ItemInfoRepository;
import com.anlijiu.example.domain.repository.SimpleRepository;
import com.anlijiu.example.utils.PermissionUtils;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import static android.content.Context.MODE_PRIVATE;


@Module(includes = ApiModule.class)
public class DataModule {
    static final int OKHTTP_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

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
    CloudRepository provideCloudRepository(CloudDataRepository repository) {
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
                .registerTypeAdapterFactory(AutoValueGsonTypeAdapterFactory.create())
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
            cacheDir = context.getExternalCacheDir();
        } else {
            cacheDir = context.getCacheDir();
        }
        return cacheDir;
    }

    @Provides
    @Singleton
    @Named("Data")
    File provideDataCacheDir(@Named("Root") File dir){
            File dataCacheDir = new File(dir, "data");
            if(!dataCacheDir.exists()) {
                dataCacheDir.mkdirs();
            }
            return dataCacheDir;
    }


    @Provides
    @Singleton
    @Named("Http")
    File provideHttpCacheDir(@Named("Root") File dir){
        File httpCacheDir = new File(dir, "http");
        if(!httpCacheDir.exists()) {
            httpCacheDir.mkdirs();
        }
        return httpCacheDir;
    }


    @Provides
    @Singleton
    RxCache provideRxCache(@Named("Http") File cacheDir) {
        return new RxCache.Builder().persistence(cacheDir, new GsonSpeaker());
    }

    @Provides
    @Singleton
    OkHttpClient.Builder provideOkhttpBuilder(Application application, @Named("Http") File cacheDir) {
        Cache cache = new Cache(cacheDir, OKHTTP_DISK_CACHE_SIZE);
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .cache(cache);

    }


    @Provides
    @Singleton
    Retrofit provideRetrofit(HttpUrl baseUrl, OkHttpClient client, Gson gson) {

        return new Retrofit.Builder() //
                .client(client) //
                .baseUrl(baseUrl) //
                .addConverterFactory(GsonConverterFactory.create(gson)) //
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                //.addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //
                .build();
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
