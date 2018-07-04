package com.anlijiu.example.data.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;

@Module(includes = ApiModule.class)
public class ReleaseApiModule {

    @Provides
    @Singleton
    HttpUrl provideHttpUrl() {
        return HttpUrl.parse("https://api.github.com");
    }


}
