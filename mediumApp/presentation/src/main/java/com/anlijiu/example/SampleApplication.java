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
import okhttp3.OkHttpClient;
import timber.log.Timber;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;


public class SampleApplication extends DaggerApplication implements HasViewInjector {
    @Inject
    DispatchingAndroidInjector<View> viewInjector;
    @Inject
    ActivityLifecyclesServer activityLifecyclesServer;
    @Inject
    OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        registerActivityLifecycleCallbacks(activityLifecyclesServer);
        initRxGlobalErrorHandler();
        watchUncaughtException();

        initTimber();
        initImageLoader();
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

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    public static final int MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB;
    public static final int MAX_DISK_CACHE_SIZE_LOW = 20 * ByteConstants.MB;
    public static final int MAX_DISK_CACHE_SIZE_VERY_LOW = 5 * ByteConstants.MB;
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 8;

    private void initImageLoader() {

        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE,                     // Max entries in the cache
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE,                     // Max length of eviction queue
                Integer.MAX_VALUE);

        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, okHttpClient)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setBitmapMemoryCacheParamsSupplier(
                        new Supplier<MemoryCacheParams>() {
                            public MemoryCacheParams get() {
                                return bitmapCacheParams;
                            }
                        })

            .setMainDiskCacheConfig(
            DiskCacheConfig.newBuilder(this)
                                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_SIZE_LOW)
                                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_SIZE_VERY_LOW)
                                .build()).build();

        Fresco.initialize(this, config);

}


    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}
