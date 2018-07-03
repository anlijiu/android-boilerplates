package com.anlijiu.example.data.di;


import com.anlijiu.example.data.di.qualifier.ApiEndpoint;
import com.f2prateek.rx.preferences2.Preference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;

@Module(includes = ApiModule.class)
public class DebugApiModule {

    @Provides
    @Singleton
    HttpUrl provideHttpUrl(@ApiEndpoint Preference<String> apiEndpoint) {
        return HttpUrl.parse(apiEndpoint.get());
    }


}
