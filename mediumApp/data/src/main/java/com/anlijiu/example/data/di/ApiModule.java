package com.anlijiu.example.data.di;

import com.anlijiu.example.data.cloud.user.UserApi;
import com.anlijiu.example.data.cloud.user.UserCacheProviders;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;


@Module
public final class ApiModule {
    @Provides
    @Singleton
    UserApi provideUserService(Retrofit retrofit) {
        return retrofit.create(UserApi.class);
    }

    @Provides
    @Singleton
    UserCacheProviders provideUserCache(RxCache rxCache) {
        return rxCache.using(UserCacheProviders.class);
    }

}
