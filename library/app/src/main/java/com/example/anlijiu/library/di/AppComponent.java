package com.example.anlijiu.library.di;


import com.example.anlijiu.library.SampleApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AppModule.class,
        ActivityBuildersModule.class,
        AndroidSupportInjectionModule.class,
})
interface AppComponent extends AndroidInjector<SampleApplication>{

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<SampleApplication> {}
}
